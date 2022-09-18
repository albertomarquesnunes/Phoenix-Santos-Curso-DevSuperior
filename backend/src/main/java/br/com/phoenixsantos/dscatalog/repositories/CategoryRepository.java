package br.com.phoenixsantos.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.phoenixsantos.dscatalog.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long>{

}
