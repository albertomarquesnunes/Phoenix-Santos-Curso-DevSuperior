package br.com.phoenixsantos.dscatalog.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.phoenixsantos.dscatalog.dto.ProductDTO;
import br.com.phoenixsantos.dscatalog.entities.Category;
import br.com.phoenixsantos.dscatalog.entities.Product;
import br.com.phoenixsantos.dscatalog.repositories.CategoryRepository;
import br.com.phoenixsantos.dscatalog.repositories.ProductRepository;
import br.com.phoenixsantos.dscatalog.services.exceptions.DatabaseException;
import br.com.phoenixsantos.dscatalog.services.exceptions.ResourceNotFoundException;
import br.com.phoenixsantos.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	@Mock
	private CategoryRepository categoryRepository;
	
	private Category category;
	private long existingId;
	private long nonExistindId;
	private long dependentId;
	private PageImpl<Product> page;
	private Product product;

	
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistindId = 1000L;
		dependentId=4L;
		product = Factory.createProduct();
		page = new PageImpl<>(List.of(product));
		category = Factory.createCategory();
	
		
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		Mockito.when(repository.getOne(existingId)).thenReturn(product);
		Mockito.when(repository.getOne(nonExistindId)).thenThrow(EntityNotFoundException.class);
		Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);
	    Mockito.when(categoryRepository.getOne(nonExistindId)).thenThrow(EntityNotFoundException.class);
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistindId)).thenReturn(Optional.empty());
		Mockito.doNothing().when(repository).deleteById(existingId);	
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistindId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {
		ProductDTO dto = Factory.createProductDTO();
		ProductDTO result = service.update(existingId,dto);
		Assertions.assertNotNull(result);
		
			
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptiontWhenIdDoesNotExists() {
		ProductDTO dto  = Factory.createProductDTO();
		 Assertions.assertThrows(ResourceNotFoundException.class,()->{
			 service.update(nonExistindId,dto);
				
		});
			
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptiontWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class,()->{
			service.findById(nonExistindId);
		});
			
	}
	
	@Test
	public void findByIdShouldReturnProductDTPWhenIdExists() {
		ProductDTO result = service.findById(existingId);
		Assertions.assertNotNull(result);
			
	}
	
	@Test
	public void findAllPagedShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<ProductDTO> result = service.findAllPaged(pageable);
		Assertions.assertNotNull(result);
		Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(()->{
			service.delete(existingId);
		});
		
		Mockito.verify(repository,Mockito.times(1)).deleteById(existingId);;
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class,()->{
			service.delete(nonExistindId);
		});
		
		Mockito.verify(repository,Mockito.times(1)).deleteById(nonExistindId);
	}
	
	@Test
	public void deleteShouldThrowDataIntegrityViolationExceptionWhenDependent() {
		
		Assertions.assertThrows(DatabaseException.class,()->{
			service.delete(dependentId);
		});
		
		Mockito.verify(repository,Mockito.times(1)).deleteById(dependentId);;
	}
}

