package br.com.phoenixsantos.dscatalog.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.phoenixsantos.dscatalog.entities.Category;
import br.com.phoenixsantos.dscatalog.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>{

	
	@Query("Select DISTINCT obj from Product obj INNER JOIN obj.categories cats Where (COALESCE(:categories) IS NULL OR cats IN :categories) AND (LOWER(obj.name) LIKE LOWER(CONCAT('%',:name,'%')))")
	Page<Product> find(List<Category> categories, String name, Pageable pageable);

}
