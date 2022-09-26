package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;

@Service // Todo o framework moderno tem algum mecanismo de injecção de dependencia automatizado 
public class CategoryService {

	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){
		List<Category> list =repository.findAll(); // Fui lá no repository e busquei tds as categorias no banco de dados e guardei nessa lista de categorias, agora vou ter que converter essa Lista de Categorias para uma lista de CategoriaDTO
		
		return list.stream().map(x->new CategoryDTO(x)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category>obj= repository.findById(id);// o retorno dessa busca nunca vai ser um objecto nulo, ai sim, dentro desse optional eu vou poder ter ou não a Categoria lá dentro.
		Category entity = obj.orElseThrow(()-> new EntityNotFoundException("Entity not found "));// O metodo do get do Optional ele obtem aquele objecto que estava dentro do optional, entao obtendo essa entidade que eu busquei dentro do banco de dados, irei mandar retornar dentro do meu metodo
		return new CategoryDTO(entity);
	}
	
	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category();
		entity.setName(dto.getName());
		entity=repository.save(entity);
		return new CategoryDTO(entity);
	}
}
