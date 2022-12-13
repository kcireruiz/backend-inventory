package com.company.inventory.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.company.inventory.model.Product;
import com.company.inventory.response.CategoryResponseRest;
import com.company.inventory.response.ProductResponseRest;
import com.company.inventory.service.IProductService;
import com.company.inventory.util.CategoryExcelExporter;
import com.company.inventory.util.Util;
import com.company.inventory.util.productExcelExporter;

@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("/api/v1")
@RestController
public class ProductRestController {
	
	private IProductService productService;
	
	public ProductRestController(IProductService productService) {
		super();
		this.productService = productService;
	}

	/**
	 * 
	 * @param picture
	 * @param name
	 * @param price
	 * @param account
	 * @param categoryId
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/products")
	public ResponseEntity<ProductResponseRest> save(
			@RequestParam("picture") MultipartFile picture,
			@RequestParam("name") String name,
			@RequestParam("price") int price,
			@RequestParam("account") int account,
			@RequestParam("categoryId") Long categoryId 
			)throws IOException{
		Product product = new Product();
		product.setName(name);
		product.setAccount(account);
		product.setPrice(price);
		product.setPicture(Util.compressZLib(picture.getBytes()));
		
		ResponseEntity<ProductResponseRest> response = productService.save(product, categoryId);
		return  response;
	}
	
	
	/**
	 * Search by id
	 * @param id
	 * @return
	 */
	@GetMapping("/products/{id}")
	public ResponseEntity<ProductResponseRest> searchProductById(@PathVariable Long id){
		ResponseEntity<ProductResponseRest> response = productService.searchById(id);
		return response;
		
	}
	
	/**
	 * Search by name
	 * @param name
	 * @return
	 */
	@GetMapping("/products/filter/{name}")
	public ResponseEntity<ProductResponseRest> searchProductByName(@PathVariable String name){
		ResponseEntity<ProductResponseRest> response = productService.searchByName(name);
		return response;
		
	}
	
	/**
	 * delete by id
	 * @param id
	 * @return
	 */
	@DeleteMapping("/products/{id}")
	public ResponseEntity<ProductResponseRest> deleteProductById(@PathVariable Long id){
		ResponseEntity<ProductResponseRest> response = productService.deleteById(id);
		return response;
	}
	
	/**
	 * Search products
	 * @param 
	 * @return
	 */
	@GetMapping("/products")
	public ResponseEntity<ProductResponseRest> searchProducts(){
		ResponseEntity<ProductResponseRest> response = productService.searchAll();
		return response;
		
	}
	
	/**
	 * Update product
	 * @param 
	 * @return
	 */
	@PutMapping("/products/{id}")
	public ResponseEntity<ProductResponseRest> updateProduct(
			@RequestParam("picture") MultipartFile picture,
			@RequestParam("name") String name,
			@RequestParam("price") int price,
			@RequestParam("account") int account,
			@RequestParam("categoryId") Long categoryId,
			@PathVariable Long id
			)throws IOException{
		Product product = new Product();
		product.setName(name);
		product.setAccount(account);
		product.setPrice(price);
		product.setPicture(Util.compressZLib(picture.getBytes()));
		
		ResponseEntity<ProductResponseRest> response = productService.update(product, categoryId, id);
		return  response;
	}
	
	/**
	 * export to excel file
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/products/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException{
		response.setContentType("application/octet-stream");
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=\"resultproduct.XLSX\"";
		response.setHeader(headerKey, headerValue);
		ResponseEntity<ProductResponseRest> ProductResponse = productService.searchAll(); 
		
		productExcelExporter excelExporter = new productExcelExporter(
				ProductResponse.getBody().getProductResponse().getProducts());
		
		excelExporter.export(response);
	}
}
