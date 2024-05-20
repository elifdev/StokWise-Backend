package com.tobeto.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tobeto.repository.user.UserRepository;
import com.tobeto.repository.warehouse.ProductRepository;

@Service
public class ScheduledTasks {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Scheduled(cron = "0 0 0 1 * ?")
	@Transactional
	public void deleteOldSoftDeletedUsers() {
		LocalDateTime cutoffTime = LocalDateTime.now().minusYears(1);
		userRepository.deleteOldSoftDeletedUsers(cutoffTime);
	}

	@Scheduled(cron = "0 0 0 1 * ?")
	@Transactional
	public void deleteOldSoftDeletedProducts() {
		LocalDateTime cutoffTime = LocalDateTime.now().minusYears(1);
		productRepository.deleteOldSoftDeletedProducts(cutoffTime);
	}
}
