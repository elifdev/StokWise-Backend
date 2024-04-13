package com.tobeto.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tobeto.entities.user.Role;
import com.tobeto.entities.user.User;
import com.tobeto.exception.ServiceException;
import com.tobeto.exception.ServiceException.ERROR_CODES;
import com.tobeto.repository.user.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<User> getAllUser() {
		return userRepository.findAll();
	}

	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User createUser(User user) {
		return userRepository.save(user);
	}

	public User updateUser(User user) {
		Optional<User> oUser = userRepository.findByEmail(user.getEmail());
		if (oUser.isPresent()) {
			User dbUser = oUser.get();
			dbUser.setEmail(user.getEmail());
			dbUser.setPassword(user.getPassword());
			List<Role> existingRoles = dbUser.getRoles();
			user.setRoles(existingRoles);

			return userRepository.save(dbUser);
		} else {
			throw new ServiceException(ERROR_CODES.USER_NOT_FOUND);
		}
	}

	public void deleteUser(User user) {
		Optional<User> dbUser = userRepository.findByEmail(user.getEmail());
		if (dbUser.isPresent()) {
			userRepository.delete(dbUser.get());
		}
	}

	public boolean changePassword(String oldPassword, String newPassword,
			String email) {
		Optional<User> oUser = userRepository.findByEmail(email);
		if (oUser.isPresent()) {
			// kullanıcı, adına göre veritabanında bulundu.
			// şifresini kontrol edelim.
			User dbUser = oUser.get();
			if (passwordEncoder.matches(oldPassword, dbUser.getPassword())) {
				// şifresi doğru. Şifresini yeni şifre ile güncelleyelim.
				// kullanici.setSifre(passwordEncoder.encode(yeniSifre));
				dbUser.setPassword(newPassword);
				userRepository.save(dbUser);
				return true;
			}
		}
		return false;
	}

}
