package com.tobeto.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tobeto.dto.role.response.RoleResponseDTO;
import com.tobeto.entities.user.Role;
import com.tobeto.entities.user.User;
import com.tobeto.repository.user.RoleRepository;

import jakarta.transaction.Transactional;

@Service
public class LoginService {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	public String login(String email, String password) {
		Optional<User> optionalUser = userService.getUserByEmail(email);
		if (optionalUser.isPresent() && passwordEncoder.matches(password, optionalUser.get().getPassword()))

		{
			String token = tokenService.createToken(optionalUser.get());
			return token;
		} else {
			throw new RuntimeException("Login Error");
		}
	}

	public String userSignUp(String email, String password, List<RoleResponseDTO> roleDTOs) {
		User user = new User();

		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));

		List<Role> roles = roleDTOs.stream()
				.map(roleDto -> roleRepository.findByName(roleDto.getName())
						.orElseThrow(() -> new RuntimeException("Role not found: " + roleDto.getName())))
				.collect(Collectors.toList());
		user.setRoles(roles);
		userService.createUser(user);
		return tokenService.createToken(user);
	}
}
