package com.company.inventory.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.dao.IProductDao;
import com.company.inventory.model.Category;
import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;
import com.company.inventory.util.Util;

@Service
public class ProductServiceImpl implements IProductService {

	private IProductDao productDao;
	private ICategoryDao categoryDao;
	
	public ProductServiceImpl(IProductDao productDao,ICategoryDao categoryDao) {
		super();
		this.productDao = productDao;
		this.categoryDao = categoryDao;
	}

	@Override
	public ResponseEntity<ProductResponseRest> save(Product product, Long categoryId) {
		ProductResponseRest response = new  ProductResponseRest();
		List<Product> list = new ArrayList<>();
		
		try {
			//search category to set in the product object
			Optional<Category> category = categoryDao.findById(categoryId);
			
			if (category.isPresent()) {
				product.setCategory(category.get());
				
			}else {
				response.setMetadata("respuesta nok","-1", "categoria no encontrada asociada al producto");
				return new ResponseEntity<ProductResponseRest>(response,HttpStatus.NOT_FOUND);
			}
			
			//save the product
			Product productSaved = productDao.save(product);
			
			if (productSaved != null) {
				list.add(productSaved);
				response.getProductResponse().setProducts(list);
				response.setMetadata("respuesta ok", "200", "Producto guardado");
			}else {
				response.setMetadata("respuesta nok","-1", "Producto no guardado");
				return new ResponseEntity<ProductResponseRest>(response,HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata("respuesta nok","-1", "Error al guardar producto");
			return new ResponseEntity<ProductResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ProductResponseRest>(response,HttpStatus.OK);
	}

	@Transactional(readOnly = true)
	@Override 
	public ResponseEntity<ProductResponseRest> searchById(Long id) {
		ProductResponseRest response = new  ProductResponseRest();
		List<Product> list = new ArrayList<>();
		
		try {
			//search product by id
			Optional<Product> product = productDao.findById(id);
			
			if (product.isPresent()) {
				byte[] imageDescompresed = Util.decompressZLib(product.get().getPicture());
				product.get().setPicture(imageDescompresed);
				list.add(product.get());
				response.getProductResponse().setProducts(list);
				response.setMetadata("Respuesta ok", "200", "Producto encontrado");
			}else {
				response.setMetadata("respuesta nok","-1", "producto no encontrado");
				return new ResponseEntity<ProductResponseRest>(response,HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata("respuesta nok","-1", "Error al guardar producto");
			return new ResponseEntity<ProductResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ProductResponseRest>(response,HttpStatus.OK);
	}

	@Transactional(readOnly = true)
	@Override
	public ResponseEntity<ProductResponseRest> searchByName(String name) {
		ProductResponseRest response = new  ProductResponseRest();
		List<Product> list = new ArrayList<>();
		List<Product> listAux = new ArrayList<>();
		
		try {
			//search product by name
			listAux = productDao.findByNameContainingIgnoreCase(name);
			
			
			if (listAux.size() > 0) {
				listAux.stream().forEach((p)->{
					byte[] imageDescompresed = Util.decompressZLib(p.getPicture());
					p.setPicture(imageDescompresed);
					list.add(p);
				});
			
				response.getProductResponse().setProducts(list);
				response.setMetadata("Respuesta ok", "200", "Productos encontrado");
			}else {
				response.setMetadata("respuesta nok","-1", "productos no encontrado");
				return new ResponseEntity<ProductResponseRest>(response,HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata("respuesta nok","-1", "Error al buscar productos por nombre");
			return new ResponseEntity<ProductResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ProductResponseRest>(response,HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> deleteById(Long id) {
		ProductResponseRest response = new  ProductResponseRest();
		
		try {
			//delete product by id
			productDao.deleteById(id);
			response.setMetadata("Respuesta ok", "200", "Producto Elimando");
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata("respuesta nok","-1", "Error al eliminar el producto");
			return new ResponseEntity<ProductResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ProductResponseRest>(response,HttpStatus.OK);
	}

	@Transactional(readOnly = true)
	@Override
	public ResponseEntity<ProductResponseRest> searchAll() {
		ProductResponseRest response = new  ProductResponseRest();
		List<Product> list = new ArrayList<>();
		List<Product> listAux = new ArrayList<>();
		
		try {
			//search products
			listAux = (List<Product>) productDao.findAll();
			
			
			if (listAux.size() > 0) {
				listAux.stream().forEach((p)->{
					byte[] imageDescompresed = Util.decompressZLib(p.getPicture());
					p.setPicture(imageDescompresed);
					list.add(p);
				});
			
				response.getProductResponse().setProducts(list);
				response.setMetadata("Respuesta ok", "200", "Productos encontrado");
			}else {
				response.setMetadata("respuesta nok","-1", "productos no encontrado");
				return new ResponseEntity<ProductResponseRest>(response,HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata("respuesta nok","-1", "Error al buscar productos");
			return new ResponseEntity<ProductResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ProductResponseRest>(response,HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> update(Product product, Long categoryId, Long id) {
		ProductResponseRest response = new  ProductResponseRest();
		List<Product> list = new ArrayList<>();
		
		try {
			//update product
			Optional<Category> category = categoryDao.findById(categoryId);
			
			if (category.isPresent()) {
				product.setCategory(category.get());
				
			}else {
				response.setMetadata("respuesta nok","-1", "categoria no encontrada asociada al producto");
				return new ResponseEntity<ProductResponseRest>(response,HttpStatus.NOT_FOUND);
			}
			
			//search the product to update
			Optional<Product>  productSearch = productDao.findById(id);
			
			if (productSearch.isPresent()) {
				//update the product
				productSearch.get().setAccount(product.getAccount());
				productSearch.get().setCategory(product.getCategory());
				productSearch.get().setName(product.getName());
				productSearch.get().setPicture(product.getPicture());
				productSearch.get().setPrice(product.getPrice());
				
				//update the product in  db
				Product productUpdate = productDao.save(productSearch.get());
				
				if (productUpdate != null) {
					list.add(productUpdate);
					response.getProductResponse().setProducts(list);
					response.setMetadata("respuesta ok", "200", "Producto actualizado");
				}else {
					response.setMetadata("respuesta nok","-1", "Producto no actualizado");
					return new ResponseEntity<ProductResponseRest>(response,HttpStatus.BAD_REQUEST);
				}
				
			}else {
				response.setMetadata("respuesta nok","-1", "Producto no actualizado");
				return new ResponseEntity<ProductResponseRest>(response,HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata("respuesta nok","-1", "Error al actualizar producto");
			return new ResponseEntity<ProductResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ProductResponseRest>(response,HttpStatus.OK);

	}

}
