package br.com.phoenixsantos.dscatalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.phoenixsantos.dscatalog.entities.Category;
import br.com.phoenixsantos.dscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;

	public List<Category> findAll(){
		return repository.findAll();
	}

}