package com.velox.server.controllers;

import com.velox.server.models.ERole;
import com.velox.server.models.Role;
import com.velox.server.models.User;
import com.velox.server.payload.request.Request;
import com.velox.server.payload.request.SignupRequest;
import com.velox.server.payload.request.UpdatePassword;
import com.velox.server.payload.response.MessageResponse;
import com.velox.server.repository.RoleRepository;
import com.velox.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController extends AbstractController{
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	RoleRepository roleRepository;

	@GetMapping("/all")
	public List<?> getAllUsers() {
		return userService.getAllUsers();
	}
	
	@GetMapping("/{id}")
	//@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public User getUserById(@PathVariable(name = "id") long id) {
		return userService.getUser(id);
	}

	@DeleteMapping("/{id}")
	//@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> deleteUser(@PathVariable(name = "id") long id) {
		 userService.deleteUser(id);
		 return ResponseEntity.ok().build();
	}

	@GetMapping
	//@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public User getUserProfile() {
		return getUser();
	}

	@PutMapping
	//@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public User update(User newUser) {
		User oldUser =  newUser.getId() == null ? getUser() : newUser;
		return userService.updateUser(oldUser, newUser);
	}


	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
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

		if (strRoles == null) {
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
		}

		user.setRoles(roles);
		user.setCreatedAt(new Timestamp(new Date().getTime()));
		userService.save(user);
		/*String command = secureCommands.generateTokenFromCommand("verify " + user.getEmail());
		publisher.publishEvent(new EmailEvents("send_verify" , user.getEmail(), command));*/
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PutMapping("/pass")
	public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePassword updatePassword) {
		userService.updatePassword(getUser(), updatePassword.getOldPassword(), updatePassword.getOldPassword());

		return ResponseEntity.ok().build();
	}

	@PutMapping("/suspend/{id}")
	public ResponseEntity suspend(@RequestBody Request request) {
		userService.suspend(request.getId(), request.getFlag());

		return ResponseEntity.ok().build();
	}


}
