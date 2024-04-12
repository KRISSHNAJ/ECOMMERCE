package com.prototype.ecommerce.exception;

public class ProductServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ProductServiceException() {
		super();
	}

	public ProductServiceException(String message) {
		super(message);
	}

	public ProductServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProductServiceException(Throwable cause) {
		super(cause);
	}

}
