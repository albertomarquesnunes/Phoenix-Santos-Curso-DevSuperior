package br.com.phoenixsantos.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.phoenixsantos.dscatalog.dto.CategoryDTO;
import br.com.phoenixsantos.dscatalog.dto.ProductDTO;
import br.com.phoenixsantos.dscatalog.entities.Category;
import br.com.phoenixsantos.dscatalog.entities.Product;
import br.com.phoenixsantos.dscatalog.repositories.CategoryRepository;
import br.com.phoenixsantos.dscatalog.repositories.ProductRepository;
import br.com.phoenixsantos.dscatalog.services.exceptions.DatabaseException;
import br.com.phoenixsantos.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Pageable pageable){
		Page<Product> list = repository.findAll(pageable);
		return  list.map(x -> new ProductDTO(x));
		
	}


	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(()-> new ResourceNotFoundException("Não Encontrado"));
		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		copyDtoToEntity(dto,entity);
		entity = repository.save(entity);
		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
				Product entity = repository.getOne(id);
				copyDtoToEntity(dto,entity);
				entity = repository.save(entity);
				return new ProductDTO(entity);
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not Found " + id);
		}
	
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e)
		{
			throw new ResourceNotFoundException("Id not Fount " + id);
		
		}
		catch(DataIntegrityViolationException e)
		{
			throw new DatabaseException("Integrity violation");
		}
	}
	

	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setImg_Url(dto.getImg_Url());
		entity.setPrice(dto.getPrice());
		
		entity.getCategories().clear();
		for(CategoryDTO catDto : dto.getCategories()) {
		Category category = categoryRepository.getOne(catDto.getId());
		entity.getCategories().add(category);
		}
	}

}