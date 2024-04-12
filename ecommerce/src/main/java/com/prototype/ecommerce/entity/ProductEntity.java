package com.prototype.ecommerce.entity;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "products")
public class ProductEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Long productId;

	@NotBlank(message = "Product name is required")
	@Column(name = "product_name")
	private String productName;

	@NotBlank(message = "Product description is required")
	@Column(name = "product_description")
	private String productDescription;

	@NotNull(message = "Product price is required")
	@Column(name = "product_price")
	private Double productPrice;

	@NotNull(message = "Quantity available is required")
	@Column(name = "product_quantity_available")
	private Integer productQuantityAvailable;

	@JsonIgnore
	private Double productDiscountPercentage;

	@JsonIgnore
	private Double productTaxRate;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public Double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(Double productPrice) {
		this.productPrice = productPrice;
	}

	public Integer getProductQuantityAvailable() {
		return productQuantityAvailable;
	}

	public void setProductQuantityAvailable(Integer productQuantityAvailable) {
		this.productQuantityAvailable = productQuantityAvailable;
	}

	@JsonIgnore
	public Double getProductDiscountPercentage() {
		return productDiscountPercentage;
	}

	@JsonProperty
	public void setProductDiscountPercentage(Double productDiscountPercentage) {
		this.productDiscountPercentage = productDiscountPercentage;
	}

	@JsonIgnore
	public Double getProductTaxRate() {
		return productTaxRate;
	}

	@JsonProperty
	public void setProductTaxRate(Double productTaxRate) {
		this.productTaxRate = productTaxRate;
	}

}
