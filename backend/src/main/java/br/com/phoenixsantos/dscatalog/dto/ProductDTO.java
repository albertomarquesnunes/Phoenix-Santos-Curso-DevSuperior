package br.com.phoenixsantos.dscatalog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import br.com.phoenixsantos.dscatalog.entities.Category;
import br.com.phoenixsantos.dscatalog.entities.Product;

public class ProductDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	private Long id;
	
	@Size(min=4, max = 60, message = "Deve ter entre 4 e 60 caracteres")
	@NotBlank(message = "Campo Obrigatório")
	private String name;
	private String description;
	
	@Positive(message="O Preço deve ser positivo")
	private Double price;
	private String img_Url;
	
	@PastOrPresent(message = "A data do produto não pode ser futura")
	private Instant date;
	
	private List<CategoryDTO> categories = new ArrayList<>();
	
	public ProductDTO() {
		
	}

	public ProductDTO(Long id, String name, String description, Double price, String img_Url, Instant date) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.img_Url = img_Url;
		this.date = date;
	}

	public ProductDTO(Product entity) {
		super();
		this.id = entity.getId();
		this.name = entity.getName();
		this.description = entity.getDescription();
		this.price = entity.getPrice();
		this.img_Url = entity.getImg_Url();
		this.date = entity.getDate();
	}
	
	public ProductDTO(Product entity, Set<Category> categories) {
		this(entity);
		categories.forEach(cat -> this.categories.add(new CategoryDTO(cat)));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getImg_Url() {
		return img_Url;
	}

	public void setImg_Url(String img_Url) {
		this.img_Url = img_Url;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public List<CategoryDTO> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryDTO> categories) {
		this.categories = categories;
	}

	

}
