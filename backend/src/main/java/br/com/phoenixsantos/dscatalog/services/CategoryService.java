package br.com.phoenixsantos.dscatalog.services;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.phoenixsantos.dscatalog.dto.CategoryDTO;
import br.com.phoenixsantos.dscatalog.entities.Category;
import br.com.phoenixsantos.dscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){
		List<Category> list = repository.findAll();
		return  list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
		
	}

}