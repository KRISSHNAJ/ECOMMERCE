package com.prototype.ecommerce.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.prototype.ecommerce.entity.ProductEntity;
import com.prototype.ecommerce.exception.ProductNotFoundException;
import com.prototype.ecommerce.exception.ProductServiceException;
import com.prototype.ecommerce.service.ProductService;

public class ProductControllerTest {

    private ProductController productController;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        productController = new ProductController();
    }

    @Test
    void testCreateProduct_Success() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductName("Test Product");
        productEntity.setProductDescription("Test Description");
        productEntity.setProductPrice(10.0);
        productEntity.setProductQuantityAvailable(100);

        when(productService.createProduct(any())).thenReturn(productEntity);
    }

    @Test
    void testCreateProduct_InvalidInput() {
        ProductEntity productEntity = new ProductEntity(); // Empty product

        doThrow(new ProductServiceException("Invalid input")).when(productService).createProduct(productEntity);

        ResponseEntity<?> response = productController.createProduct(productEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Failed to create product"));
    }

    @Test
    void testGetProductById_Success() {
        Long productId = 1L;
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductId(productId);
        productEntity.setProductName("Test Product");
        productEntity.setProductDescription("Test Description");
        productEntity.setProductPrice(10.0);
        productEntity.setProductQuantityAvailable(100);

        when(productService.getProductById(productId)).thenReturn(Optional.of(productEntity));
    }

    @Test
    void testGetProductById_ProductNotFound() {
        Long productId = 1L;

        doThrow(new ProductNotFoundException("Product not found")).when(productService).getProductById(productId);
    }

    @Test
    void testUpdateProduct_Success() {
        Long productId = 1L;
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductName("Updated Product");
        productEntity.setProductDescription("Updated Description");
        productEntity.setProductPrice(20.0);
        productEntity.setProductQuantityAvailable(200);

        doNothing().when(productService).updateProduct(productId, productEntity);
    }

    @Test
    void testUpdateProduct_ProductNotFound() {
        Long productId = 1L;
        ProductEntity productEntity = new ProductEntity();

        doThrow(new ProductNotFoundException("Product not found")).when(productService).updateProduct(productId, productEntity);

        ResponseEntity<String> response = productController.updateProduct(productId, productEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Failed to update product"));
    }

    @Test
    void testDeleteProductById_Success() {
        Long productId = 1L;

        when(productService.deleteProductById(productId)).thenReturn(true);
    }

    @Test
    void testDeleteProductById_ProductNotFound() {
        Long productId = 1L;

        when(productService.deleteProductById(productId)).thenReturn(false);
    }

    @Test
    void testApplyDiscountOrTax_Success() {
        Long productId = 1L;
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductDiscountPercentage(10.0);

        when(productService.applyDiscountOrTax(productId, productEntity)).thenReturn(productEntity);
    }

    @Test
    void testApplyDiscountOrTax_ProductNotFound() {
        Long productId = 1L;
        ProductEntity productEntity = new ProductEntity();

        doThrow(new ProductNotFoundException("Product not found")).when(productService).applyDiscountOrTax(productId, productEntity);

        ResponseEntity<?> response = productController.applyDiscountOrTax(productId, productEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Failed to apply discount or tax"));
    }
}