package com.company.inventory.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.inventory.model.Category;
import com.company.inventory.response.CategoryResponseRest;
import com.company.inventory.service.ICategoryService;
import com.company.inventory.util.CategoryExcelExporter;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1")
public class CategoryRestController {
	
	@Autowired
	private ICategoryService service;
	
	@GetMapping("/ejemplo")
	public String ejemplo(){
		
		return "ejemplo";
	}
	
	@GetMapping("/categories")
	public ResponseEntity<CategoryResponseRest> searchCategories(){
		ResponseEntity<CategoryResponseRest> response = service.search(); 
		return response;
		
	}
	
	@GetMapping("/categories/{id}")
	public ResponseEntity<CategoryResponseRest> searchCategoriesById(@PathVariable Long id){
		ResponseEntity<CategoryResponseRest> response = service.searchId(id); 
		return response;
		
	}
	
	@PostMapping("/categories")
	public ResponseEntity<CategoryResponseRest> saveCategories(@RequestBody Category category){
		ResponseEntity<CategoryResponseRest> response = service.save(category); 
		return response;
		
	}
	/**
	 * update categories
	 * @param categories
	 * @param Category,id
	 * @return
	 */
	@PutMapping("/categories/{id}")
	public ResponseEntity<CategoryResponseRest> updateCategories(@RequestBody Category category,@PathVariable Long id){
		ResponseEntity<CategoryResponseRest> response = service.update(category, id);
		return response;
		
	}
	
	/**
	 * delete categories
	 * @param categories
	 * @param id
	 * @return
	 */
	@DeleteMapping("/categories/{id}")
	public ResponseEntity<CategoryResponseRest> deleteCategories(@PathVariable Long id){
		ResponseEntity<CategoryResponseRest> response = service.delete(id);
		return response;
		
	}
	
	/**
	 * export to excel file
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/categories/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException{
		response.setContentType("application/octet-stream");
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=\"resultcategory.XLSX\"";
		response.setHeader(headerKey, headerValue);
		ResponseEntity<CategoryResponseRest> CategoryResponse = service.search(); 
		
		CategoryExcelExporter excelExporter = new CategoryExcelExporter(
				CategoryResponse.getBody().getCategoryResponse().getCategory());
		
		excelExporter.export(response);
	}
}
