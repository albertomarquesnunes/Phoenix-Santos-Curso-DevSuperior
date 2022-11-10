package br.com.phoenixsantos.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import br.com.phoenixsantos.dscatalog.dto.ProductDTO;
import br.com.phoenixsantos.dscatalog.repositories.ProductRepository;
import br.com.phoenixsantos.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class ProductServiceIT {
	
	@Autowired
	private ProductService service;
	@Autowired
	private ProductRepository repository;
	private long existingId;
	private long nonExistindId;
	private long totalCountProducts;
	
	
	@BeforeEach
	void setup() throws Exception{
		 existingId = 1L;
		 nonExistindId = 1000L;
		 totalCountProducts = 25L;
		
	}
	
	@Test
	public void deleteShouldDeleteResourceWhenIdExists() {
		service.delete(existingId);
		Assertions.assertEquals(totalCountProducts-1, repository.count());
		
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, 
					()-> service.delete(nonExistindId)
				);
	}
	
	@Test
	public void findAllPagedShouldReturnPageWhenPage0Size10() {
		PageRequest pageRequest = PageRequest.of(0,10);
		Page<ProductDTO> result = service.findAllPaged(pageRequest);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(10, result.getSize());
		Assertions.assertEquals(totalCountProducts, result.getTotalElements());
	}
	
	@Test
	public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExist() {
		PageRequest pageRequest = PageRequest.of(50,10);
		Page<ProductDTO> result = service.findAllPaged(pageRequest);
		Assertions.assertTrue(result.isEmpty());
	
	}
	
	@Test
	public void findAllPagedShouldReturnSortedPageWhenSortByName() {
		PageRequest pageRequest = PageRequest.of(0,10,Sort.by("name"));
		Page<ProductDTO> result = service.findAllPaged(pageRequest);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
		Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
	
	}

}
