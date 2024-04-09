package com.tobeto.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tobeto.entities.user.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String email);
}
