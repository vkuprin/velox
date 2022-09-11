package com.velox.server.controllers;

import com.velox.server.models.User;
import com.velox.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	private UserService userService;


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
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails)principal).getUsername();
		} else {
			username = principal.toString();
		}
		return userService.findByUsername(username);
	}

	@PutMapping
	//@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public User update(User user) {
		//TODO list of fields
		return null;
	}

}
