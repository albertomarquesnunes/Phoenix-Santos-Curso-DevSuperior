package br.com.phoenixsantos.dscatalog.tests;

import java.time.Instant;

import br.com.phoenixsantos.dscatalog.dto.ProductDTO;
import br.com.phoenixsantos.dscatalog.entities.Category;
import br.com.phoenixsantos.dscatalog.entities.Product;

public class Factory {
	
	public static Product createProduct() {
		Product product = new Product(1L,"Phone","Good Phone",800.0,"https://img.com/img.png",Instant.parse("2020-11-11T03:00:00Z"));
		product.getCategories().add(createCategory());
		return product;
		
	}
	
	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product,product.getCategories());
	}
	
	
	public static Category createCategory()	{
		return new Category(1L,"Eletrnicos");
	}	
}
