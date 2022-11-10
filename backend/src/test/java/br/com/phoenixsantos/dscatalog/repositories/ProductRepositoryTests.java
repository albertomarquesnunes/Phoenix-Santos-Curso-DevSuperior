package br.com.phoenixsantos.dscatalog.repositories;


import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import br.com.phoenixsantos.dscatalog.entities.Product;
import br.com.phoenixsantos.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {
	
	private long existingId;
	private long nonExistindId;
	private long countTotalProducts;
	
	@Autowired
	private ProductRepository repository;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistindId = 1000L;
		countTotalProducts=25L;
	}
	
	@Test
	public void findByIdShouldReturnNotEmptOptionalWhenIdExists() {
		
		
		Optional<Product> result = repository.findById(existingId);
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnEmptyOptionaltWhenIdDoesNotExists() {
		
	
		Optional<Product> result = repository.findById(nonExistindId);
		Assertions.assertTrue(result.isEmpty());
	}
	
	
	
	
	@Test
	public void saveSloundPersistWithAutoincrementWhenIdIsNull() {
		Product product = Factory.createProduct();
		product.setId(null);
		
		product=repository.save(product);
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts+1,product.getId());
		
	}
	
	
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		
		repository.deleteById(existingId);
		Optional<Product> result =repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, ()->{
			repository.deleteById(nonExistindId);
		});
	}
}
