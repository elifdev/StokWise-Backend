package com.tobeto.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tobeto.entities.warehouse.Category;
import com.tobeto.repository.warehouse.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	public Category addCategory(Category category) {
		return categoryRepository.save(category);
	}

	public void deleteCategory(Category category) {
		Optional<Category> dbCategory = categoryRepository.findById(category.getId());
		if (dbCategory.isPresent()) {
			categoryRepository.delete(dbCategory.get());
		}

	}

}