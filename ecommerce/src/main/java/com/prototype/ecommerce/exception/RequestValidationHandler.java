package com.prototype.ecommerce.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.prototype.ecommerce.controller.ProductController;
import com.prototype.ecommerce.entity.ProductEntity;

@Component
public class RequestValidationHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	public ResponseEntity<?> handleValidation(ProductEntity productEntity, BindingResult bindingResult) {
		try {

			Map<String, String> response = new HashMap<>();

			// Scenario 1: Empty Body
			if (productEntity == null) {
				response.put("ERROR: ", "PLEASE CHECK YOUR REQUEST, It was EMPTY BODY");
				return ResponseEntity.badRequest().body(response);
			}

			// Scenario 2: Only 4 Fields
			if (!hasAllRequiredFields(productEntity)) {
				response.put("ERROR: ", "All required fields must be provided.");
				return ResponseEntity.badRequest().body(response);
			}

			// Scenario 3: Only Key (with null/0/""/empty/blank space)
			if (hasEmptyKeys(productEntity)) {
				response.put("ERROR: ", "Invalid values for some keys.");
				return ResponseEntity.badRequest().body(response);
			}

			// Scenario 4: Only Key (without null/0/""/empty/blank space)
			if (hasKeysWithoutValues(productEntity)) {
				response.put("ERROR: ", "Values for some keys are missing.");
				return ResponseEntity.badRequest().body(response);
			}

			// Scenario 6: Mismatch Data Types
			if (bindingResult.hasErrors()) {
				return ResponseEntity.badRequest().body("Data type mismatch: " + bindingResult.getFieldError().getField());
			}

			// No validation issues found
			return null;
		} catch (Exception e) {
			logger.error("Failed to validate the request: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to validate the request: " + e.getMessage());
		}
	}

	private boolean hasAllRequiredFields(ProductEntity productEntity) {
		return productEntity.getProductName() != null && productEntity.getProductDescription() != null && productEntity.getProductPrice() != null && productEntity.getProductQuantityAvailable() != null;
	}

	private boolean hasEmptyKeys(ProductEntity productEntity) {
		return productEntity.getProductName() == null || productEntity.getProductName().trim().isEmpty() || 
			productEntity.getProductDescription() == null || productEntity.getProductDescription().trim().isEmpty();
	}

	private boolean hasKeysWithoutValues(ProductEntity productEntity) {
		return productEntity.getProductName() != null && productEntity.getProductName().trim().isEmpty() || productEntity.getProductDescription() != null && productEntity.getProductDescription().trim().isEmpty();
	}

}
