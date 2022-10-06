package com.devsuperior.dscatalog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

public class ProductDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String description;
	private Double price;
	private String imgUrl;
	private Instant date;
	
	//Meu DTO tambem vai aceitar uma lista de categorias
	private List<CategoryDTO> categories = new ArrayList<>();
	
	
	public ProductDTO() {
		
	}


	public ProductDTO(Long id, String name, String description, Double price, String imgUrl, Instant date) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imgUrl = imgUrl;
		this.date = date;
	}
	
	public ProductDTO(Product entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.description =entity.getDescription();
		this.price = entity.getPrice();
		this.imgUrl = entity.getImgUrl();
		this.date = entity.getDate();
	}
	
	//vou agora criar um dto que recebe as categorias, entao preciso de um constructor para receber as categorias
	
	/*Porque estou fazendo outro constructor que recebe a entidade e mais as categorias? é para eu saber que quando eu chamar esse constructor aqui em baixo, eu quero instanciar o DTO colocando os elementos na lista de Categoria = new ArrayList */
	public ProductDTO(Product entity, Set<Category>categories) {
		
		//Este this infra(entity) vai querer dizer o seguinte: ele chama este constructor que recebe só entidade, ele irá executar tudo relacionado com entity.getId e por adiante...
		//Depois disso eu irei percorrer esse conj de categorias e inserindo cada categoria que está nele como um novo categoryDTO QUE ESTÁ NA MINHA LISTA DE CATEGORIES
		
		this(entity);
		/*Para cada categoria cat dessa minha lista de categories, eu vou fazer o seguinte:
		  vou lá na minha lista de categorias da classe que é o this.categories(é este aqui - > private List<CategoryDTO> categories = new ArrayList<>();   
		  e depois adiciono um new CategoryDTO passando o meu cat como argumento
		  
		  Então para cada categoria entidade que chegou aqui nesse conjunto, para cada uma delas eu irei executar essa funçao lambda, pega cada elemento e insere ele transformado para DTO na listinha de categories aqui da minha classe      */
		
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


	public String getImgUrl() {
		return imgUrl;
	}


	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
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
