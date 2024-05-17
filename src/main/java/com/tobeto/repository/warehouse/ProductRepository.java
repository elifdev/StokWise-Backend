package com.tobeto.repository.warehouse;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tobeto.entities.warehouse.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {

	@Query("SELECT p FROM Product p ORDER BY p.name")
	List<Product> findAll();

	@Modifying
	@Query("UPDATE Product p SET p.isDeleted = true WHERE p.id = :id")
	void softDeleteById(@Param("id") UUID id);

	@Query("SELECT p FROM Product p WHERE p.isDeleted = false")
	List<Product> findAllActive();

}
