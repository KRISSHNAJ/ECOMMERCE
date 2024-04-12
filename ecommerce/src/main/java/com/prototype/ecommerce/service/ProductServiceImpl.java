package com.prototype.ecommerce.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prototype.ecommerce.entity.ProductEntity;
import com.prototype.ecommerce.exception.ProductNotFoundException;
import com.prototype.ecommerce.exception.ProductServiceException;
import com.prototype.ecommerce.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Autowired
	private ProductRepository productRepository;

	/**
	 * Creates a new product.
	 * 
	 * @param productEntity The product information to be created.
	 * @return The created product entity.
	 */
	@Override
	public ProductEntity createProduct(ProductEntity productEntity) {
		try {
			if (productEntity == null ) {
				throw new IllegalArgumentException("ProductEntity cannot be null or empty");
			} else {
				logger.info("Creating product: {}", productEntity);
				resetDefaultValues(productEntity);
				ProductEntity createdProduct = productRepository.save(productEntity);
				logger.info("Product created successfully: {}", createdProduct);
				return createdProduct;
			}
		} catch (Exception e) {
			logger.error("Failed to create product: {}", e.getMessage());
			throw new ProductServiceException("Failed to create product: " + e.getMessage());
		}
	}

	/**
	 * Retrieves a product by its ID.
	 * 
	 * @param productId The ID of the product to retrieve.
	 * @return Optional containing the retrieved product, if found.
	 */
	@Override
	@Transactional(readOnly = true)
	public Optional<ProductEntity> getProductById(Long productId) {
		try {
			logger.info("Fetching product with ID: {}", productId);
			Optional<ProductEntity> product = productRepository.findById(productId);
			if (product.isPresent()) {
				logger.info("Product found: {}", product.get());
				return product;
			} else {
				logger.warn("Product not found with ID: {}", productId);
				throw new ProductNotFoundException("Product not found with ID: " + productId);
			}
		} catch (ProductNotFoundException e) {
			logger.warn("Product not found with ID: {}", productId);
			throw new ProductNotFoundException("Product not found with ID: " + productId);
		} catch (Exception e) {
			logger.error("Failed to fetch product: {}", e.getMessage());
			throw new ProductServiceException("Failed to fetch product: " + e.getMessage());
		}
	}

	/**
	 * Updates an existing product.
	 * 
	 * @param productId     The ID of the product to update.
	 * @param productEntity The updated product information.
	 * @throws ProductServiceException If an error occurs during the update process.
	 */
	@Override
	public void updateProduct(Long productId, ProductEntity productEntity) {
		resetDefaultValues(productEntity);
		try {
			logger.info("Updating product with ID: {}", productId);
			ProductEntity existingProduct = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));
			existingProduct.setProductName(productEntity.getProductName());
			existingProduct.setProductDescription(productEntity.getProductDescription());
			existingProduct.setProductPrice(productEntity.getProductPrice());
			existingProduct.setProductQuantityAvailable(productEntity.getProductQuantityAvailable());
			productRepository.save(existingProduct);
			logger.info("Product updated successfully");
		} catch (Exception e) {
			logger.error("Failed to update product: {}", e.getMessage());
			throw new ProductServiceException("Failed to update product: " + e.getMessage());
		}
	}

	/**
	 * Deletes a product by its ID.
	 * 
	 * @param productId The ID of the product to delete.
	 * @return True if the product was successfully deleted, otherwise false.
	 * @throws ProductServiceException If an error occurs during the deletion
	 *                                 process.
	 */
	@Override
	public boolean deleteProductById(Long productId) {
		try {
			logger.info("Deleting product with ID: {}", productId);
			if (productRepository.existsById(productId)) {
				productRepository.deleteById(productId);
				logger.info("Product deleted successfully");
				return true; // Product found
			} else {
				logger.warn("Product not found with ID: {}", productId);
				return false; // Product not found
			}
		} catch (Exception e) {
			logger.error("Failed to delete product: {}", e.getMessage());
			throw new ProductServiceException("Failed to delete product: " + e.getMessage());
		}
	}
	
	/**
	 * Applies discount or tax to a product.
	 * 
	 * @param productId                 The ID of the product to update.
	 * @param productDiscountPercentage The discount percentage to apply.
	 * @param productTaxRate            The tax rate to apply.
	 * @param productEntity             The updated product information.
	 * @return The updated product entity.
	 * @throws ProductServiceException If an error occurs during the update process.
	 */
	@Override
	public ProductEntity applyDiscountOrTax(Long productId, ProductEntity productEntity) {
		resetDefaultValues(productEntity);
		try {
			logger.info("Applying discount or tax to product with ID: {}", productId);
			ProductEntity existingProduct = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));
			// updated product details
			existingProduct.setProductName(productEntity.getProductName());
			existingProduct.setProductDescription(productEntity.getProductDescription());
			existingProduct.setProductPrice(productEntity.getProductPrice());
			existingProduct.setProductQuantityAvailable(productEntity.getProductQuantityAvailable());

			// updated product discount or tax
			if (!(productEntity.getProductDiscountPercentage() <= 0.0)) {
				double discount = existingProduct.getProductPrice()* (productEntity.getProductDiscountPercentage() / 100);
				existingProduct.setProductPrice(existingProduct.getProductPrice() - discount);
				existingProduct.setProductDiscountPercentage(productEntity.getProductDiscountPercentage());
			} else if (!(productEntity.getProductTaxRate() <= 0.0)) {
				double tax = existingProduct.getProductPrice() * (productEntity.getProductTaxRate() / 100);
				existingProduct.setProductPrice(existingProduct.getProductPrice() + tax);
				existingProduct.setProductTaxRate(productEntity.getProductTaxRate());
			}
			ProductEntity updatedProduct = productRepository.save(existingProduct);
			logger.info("Discount or tax applied successfully: {}", updatedProduct);
			return updatedProduct;
		} catch (Exception e) {
			logger.error("Failed to apply discount or tax: {}", e.getMessage());
			throw new ProductServiceException("Failed to update product: " + e.getMessage());
		}
	}

	public void resetDefaultValues(ProductEntity productEntity) {
		try {
		if (productEntity.getProductName() == null || productEntity.getProductName() == " " || productEntity.getProductName().trim().isEmpty() || productEntity.getProductName().trim().isBlank()) {
			productEntity.setProductName(" ");
		}
		if (productEntity.getProductDescription() == null || productEntity.getProductDescription() == " " || productEntity.getProductDescription().trim().isEmpty() || productEntity.getProductDescription().trim().isBlank()) {
			productEntity.setProductDescription(" ");
		}
		if (productEntity.getProductPrice() == null || productEntity.getProductPrice() <= 0.0) {
			productEntity.setProductPrice(0.0);
		}
		if (productEntity.getProductQuantityAvailable() == null || productEntity.getProductQuantityAvailable() <= 0) {
			productEntity.setProductQuantityAvailable(0);
		}
		if (productEntity.getProductDiscountPercentage() == null || productEntity.getProductDiscountPercentage() <= 0.0) {
			productEntity.setProductDiscountPercentage(0.0);
		}
		if (productEntity.getProductTaxRate() == null || productEntity.getProductTaxRate() <= 0.0) {
			productEntity.setProductTaxRate(0.0);
		}
	} catch (Exception e) {
		logger.error("Failed to update resetDefaultValues: {}", e.getMessage());
		throw new ProductServiceException("Failed to update resetDefaultValues: " + e.getMessage());
	}
}

}
