package com.devsuperior.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service // Todo o framework moderno tem algum mecanismo de injecção de dependencia automatizado 
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Pageable pageable){
		Page<Product> list =repository.findAll(pageable);//Chamamos a busca paginada lá do repository,agora vamos ter que converter a lista de Product para a colecção equivalente a ProductDTO   // Fui lá no repository e busquei tds as categorias no banco de dados e guardei nessa lista de categorias, agora vou ter que converter essa Lista de Categorias para uma lista de CategoriaDTO
		
		return list.map(x->new ProductDTO(x));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product>obj= repository.findById(id);// o retorno dessa busca nunca vai ser um objecto nulo, ai sim, dentro desse optional eu vou poder ter ou não a Categoria lá dentro.
		Product entity = obj.orElseThrow(()-> new ResourceNotFoundException("Entity not found "));// O metodo do get do Optional ele obtem aquele objecto que estava dentro do optional, entao obtendo essa entidade que eu busquei dentro do banco de dados, irei mandar retornar dentro do meu metodo
		return new ProductDTO(entity, entity.getCategories());
	}
	
	@Transactional
	public ProductDTO insert(ProductDTO dto) { 
		Product entity = new Product();
		copyDtoToEntity(dto,entity);
		entity=repository.save(entity);
		return new ProductDTO(entity,entity.getCategories());
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
		
		Product entity = repository.getOne(id); //Neste caso aqui, em versoes mais recentes do Spring Boot, o nome da função mudou para: getReferenceById
		//entity.setName(dto.getName());
		copyDtoToEntity(dto,entity);
		entity= repository.save(entity);
		return new ProductDTO(entity);
		}
		catch(EntityNotFoundException e) { // isto é no caso de eu tentar chamar o getOne e dar um ID que não existe e depois tentar salvar, ele vai estourar para mim essa (EntityNotFoundException e) e caso ela ocorra, ai sim, eu vou lançar a minha excepção personalizada que agora é ResourceNotFoundException
			throw new ResourceNotFoundException("Id not found " + id);
		}
		
	}

	
	public void delete(Long id) {
		try { //caso eu tente deletar um ID que não existe, acontece uma excepção(EmptyResultDataAccessException)
			repository.deleteById(id);		
		
	}
	catch(EmptyResultDataAccessException e) {
		throw new ResourceNotFoundException("Id not found " + id);
		
	}
		catch(DataIntegrityViolationException e) { //CASO OCORRA ESSA EXCEPÇÃO, EU VOU LANÇAR UMA EXCEPÇÃO DE SERVIÇO PERSONALIZADA, SÓ QUE AGORA NESTE CASO NAO FAZ SENTIDO EU LANÇAR UMA "RESOURCENOTFOUNDEXCEPTION", VOU TER QUE LANÇAR UMA EXCEPÇÃO DIFERENTE
			throw new DatabaseException("Integrity violation");
		}
	}
	
	/* Explicação acerca do metodo infra: nos apontamentos -> 1º Capitulo CRUD */
	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());
		
		entity.getCategories().clear();
		for (CategoryDTO catDto : dto.getCategories()) {
			Category category = categoryRepository.getOne(catDto.getId());
			entity.getCategories().add(category);
		}
	}

}
