package com.velox.server.controllers;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.velox.server.models.ERole;
import com.velox.server.payload.request.*;
import com.velox.server.security.SecureCommands;
import com.velox.server.security.jwt.JwtUtils;
import com.velox.server.service.RefreshTokenService;
import com.velox.server.service.UserDetailsImpl;
import com.velox.server.service.UserService;
import com.velox.server.service.events.EmailEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.velox.server.exception.TokenRefreshException;
import com.velox.server.models.RefreshToken;
import com.velox.server.models.Role;
import com.velox.server.models.User;
import com.velox.server.payload.response.JwtResponse;
import com.velox.server.payload.response.MessageResponse;
import com.velox.server.payload.response.TokenRefreshResponse;
import com.velox.server.repository.RoleRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserService userService;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  private ApplicationEventPublisher publisher;

  @Autowired
  private PasswordEncoder encoder;

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private SecureCommands secureCommands;

  @Autowired
  private RefreshTokenService refreshTokenService;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    String jwt = jwtUtils.generateJwtToken(userDetails);

    List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
        .collect(Collectors.toList());

    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

    return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
        userDetails.getUsername(), userDetails.getEmail(), roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userService.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userService.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
        encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();
    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    roles.add(userRole);

    /*if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);

          break;
        case "mod":
          Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(modRole);

          break;
        default:
          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
        }
      });
    }*/

    user.setRoles(roles);
    user.setCreatedAt(new Timestamp(new Date().getTime()));
    userService.save(user);
    String command = secureCommands.generateTokenFromCommand("verify " + user.getEmail());
    publisher.publishEvent(new EmailEvents("send_verify" , user.getEmail(), command));
    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenService.findByToken(requestRefreshToken)
        .map(refreshTokenService::verifyExpiration)
        .map(RefreshToken::getUser)
        .map(user -> {
          String token = jwtUtils.generateTokenFromEmail(user.getEmail());
          return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
        })
        .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
            "Refresh token is not in database!"));
  }
  
  @PostMapping("/logout")
  public ResponseEntity<?> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
    refreshTokenService.deleteByUserId(logOutRequest.getUserId());
    return ResponseEntity.ok(new MessageResponse("Log out successful!"));
  }

  @GetMapping("/email/verify")
  public ResponseEntity verifyEmail(@RequestParam(name = "token") String token) {
    try {
      String command = secureCommands.getCommandFromJwtToken(token);
      if (StringUtils.isEmpty(command) || !command.startsWith("verify ")) {
        return ResponseEntity.badRequest().build();
      }
      String email = command.split(" ")[1];
      userService.verifyEmail(email);
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/email/restore_pass")
  public ResponseEntity sendRestoreLink(@RequestParam(name = "email") String email) {
    if (!userService.existsByEmail(email)) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email does not exist"));
    }
    String command = secureCommands.generateTokenFromCommand("restore_pass " + email);
    publisher.publishEvent(new EmailEvents("send_restore_pass" , email, command));
    return ResponseEntity.ok().build();
  }

  @PostMapping("/restore_pass")
  public ResponseEntity restorePassword(@Valid @RequestBody RestorePassword restorePassword) {
    try {
      String command = secureCommands.getCommandFromJwtToken(restorePassword.getToken());
      if (StringUtils.isEmpty(command) || !command.startsWith("restore_pass ")) {
        return ResponseEntity.badRequest().build();
      }
      String email = command.split(" ")[1];
      userService.changePassword(email, encoder.encode(restorePassword.getPassword()));
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }

  }

  @PutMapping("/email")
  public ResponseEntity updateEmail(@RequestBody Request request) {
    User user = userService.updateEmail(request.getId(), request.getData());

    String token = jwtUtils.generateTokenFromEmail(user.getEmail());
    return ResponseEntity.ok(token);
  }

  //TODO send temp password
  /*@GetMapping("/email/temp_pass")
  public ResponseEntity sendTempPass(@RequestParam(name = "email") String email) {

    return ResponseEntity.ok().build();
  }*/
}
