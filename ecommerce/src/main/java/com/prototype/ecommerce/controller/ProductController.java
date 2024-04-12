package com.prototype.ecommerce.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prototype.ecommerce.entity.ProductEntity;
import com.prototype.ecommerce.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/prototype/ecommerce/products")
public class ProductController {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductService productService;

    /**
     * Endpoint to create a new product.
     * 
     * @param productEntity The product information to be created.
     * @return ResponseEntity containing the created product.
     */
	@PostMapping("/create")
	public ResponseEntity<?> createProduct(@RequestBody @Valid ProductEntity productEntity) {
		try {
			logger.info("Creating product: {}", productEntity);
			ProductEntity createdProduct = productService.createProduct(productEntity);
			logger.info("Product created successfully: {}", createdProduct);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
		} catch (Exception e) {
			logger.error("Failed to create product: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create product: " + e.getMessage());
		}
	}

    /**
     * Endpoint to retrieve a product by its ID.
     * 
     * @param productId The ID of the product to retrieve.
     * @return ResponseEntity containing the retrieved product, if found.
     */
	@GetMapping("/getProduct/{productId}")
	public ResponseEntity<?> getProductById(@PathVariable Long productId) {
		try {
			logger.info("Fetching product with ID: {}", productId);
			Optional<ProductEntity> product = productService.getProductById(productId);
			if (product.isPresent()) {
				logger.info("Product found: {}", product.get());
				return ResponseEntity.ok(product.get());
			} else {
				logger.warn("Product not found with ID: {}", productId);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found with ID: " + productId);
			}
		} catch (Exception e) {
			logger.error("Failed to fetch product: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch product: " + e.getMessage());
		}
	}
	
    /**
     * Endpoint to update a product.
     * 
     * @param productId    The ID of the product to update.
     * @param productEntity The updated product information.
     * @return ResponseEntity indicating the status of the update operation.
     */
    @PutMapping("/update/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable Long productId,@RequestBody ProductEntity productEntity) {
        try {
        	logger.info("Updating product with ID: {}", productId);
            productService.updateProduct(productId, productEntity);
            logger.info("Product updated successfully");
            return ResponseEntity.ok("Product updated successfully");
        } catch (Exception e) {
        	logger.error("Failed to update product: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update product: " + e.getMessage());
        }
    }
	
    /**
     * Endpoint to delete a product by its ID.
     * 
     * @param productId The ID of the product to delete.
     * @return ResponseEntity indicating the status of the deletion operation.
     */
	@DeleteMapping("/delete/{productId}")
	public ResponseEntity<String> deleteProductById(@PathVariable Long productId) {
		try {
			logger.info("Deleting product with ID: {}", productId);
			boolean deleted = productService.deleteProductById(productId);
			if (deleted) {
				logger.info("Product deleted successfully");
				return ResponseEntity.ok("Product deleted successfully");
			} else {
				logger.warn("Product not found with ID: {}", productId);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
			}
		} catch (Exception e) {
			logger.error("Failed to delete product: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete product: " + e.getMessage());
		}
	}
	
    /**
     * Endpoint to apply discount or tax to a product.
     * 
     * @param productId The ID of the product to update.
     * @param productDiscountPercentage The discount percentage to apply.
     * @param productTaxRate The tax rate to apply.
     * @param productEntity The updated product information.
     * @return ResponseEntity containing the updated product.
     */
	@PutMapping("/applyDiscountOrTax/{productId}")
	public ResponseEntity<?> applyDiscountOrTax(@PathVariable Long productId,@RequestBody ProductEntity productEntity) {
		try {
			logger.info("Applying discount or tax to product with ID: {}", productId);
			ProductEntity updatedProduct = productService.applyDiscountOrTax(productId, productEntity);
			logger.info("Discount or tax applied successfully: {}", updatedProduct);
			return ResponseEntity.ok(updatedProduct);
		} catch (Exception e) {
			logger.error("Failed to apply discount or tax: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to apply discount or tax: " + e.getMessage());
		}
	}
    
}
