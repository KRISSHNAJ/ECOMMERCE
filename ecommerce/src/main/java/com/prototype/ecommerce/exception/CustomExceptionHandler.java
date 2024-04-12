package com.prototype.ecommerce.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
		String errorMessage = "Validation error: repquest boody wont be blank";
		errorMessage += ex.getBindingResult().getAllErrors().stream().map(error -> error.getDefaultMessage()).reduce("",
				(accumulator, message) -> accumulator + message + "; ");
		return ResponseEntity.badRequest().body(errorMessage);
	}

}
