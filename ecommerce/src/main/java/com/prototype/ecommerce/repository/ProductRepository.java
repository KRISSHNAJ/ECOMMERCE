package com.prototype.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prototype.ecommerce.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>{
	
}