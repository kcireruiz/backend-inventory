package com.company.inventory.service;

import org.springframework.http.ResponseEntity;

import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;

public interface IProductService {

	public ResponseEntity<ProductResponseRest> save(Product product, Long categoryId);
	
	public ResponseEntity<ProductResponseRest> searchById(Long id);
	
}
