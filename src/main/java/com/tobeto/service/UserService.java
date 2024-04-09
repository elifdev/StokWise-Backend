package com.tobeto.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tobeto.entities.user.User;
import com.tobeto.repository.user.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User createUser(User user) {
		return userRepository.save(user);
	}

}
