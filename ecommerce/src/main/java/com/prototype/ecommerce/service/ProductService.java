package com.prototype.ecommerce.service;

import java.util.Optional;

import com.prototype.ecommerce.entity.ProductEntity;

public interface ProductService {

	/**
	 * Creates a new product.
	 * 
	 * @param productEntity The product entity to be created
	 * @return The created product entity
	 */
	ProductEntity createProduct(ProductEntity productEntity);

	/**
	 * Retrieves a product by its ID.
	 * 
	 * @param productId The ID of the product to retrieve
	 * @return An optional containing the product entity if found, otherwise empty
	 */
	Optional<ProductEntity> getProductById(Long productId);
	
	/**
	 * Updates an existing product.
	 * 
	 * @param productId    The ID of the product to update
	 * @param productEntity The updated product entity
	 */
	void updateProduct(Long productId, ProductEntity productEntity);
	
	/**
	 * Deletes a product by its ID.
	 * 
	 * @param productId The ID of the product to delete
	 * @return True if the product was successfully deleted, otherwise false
	 */
	boolean deleteProductById(Long productId);
	
	/**
	 * Applies discount or tax to a product.
	 * 
	 * @param productId                The ID of the product to apply discount or tax
	 * @param productDiscountPercentage The discount percentage to apply (optional)
	 * @param productTaxRate           The tax rate to apply (optional)
	 * @param productEntity            The updated product entity
	 * @return The updated product entity
	 */
	 ProductEntity applyDiscountOrTax(Long productId, ProductEntity productEntity);

}
