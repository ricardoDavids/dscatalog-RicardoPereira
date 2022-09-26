package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

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
		Category entity = obj.orElseThrow(()-> new ResourceNotFoundException("Entity not found "));// O metodo do get do Optional ele obtem aquele objecto que estava dentro do optional, entao obtendo essa entidade que eu busquei dentro do banco de dados, irei mandar retornar dentro do meu metodo
		return new CategoryDTO(entity);
	}
	
	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category();
		entity.setName(dto.getName());
		entity=repository.save(entity);
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
		/*Qual a diferença do findById para o getOne(id)? O findById, ele efectiva o acesso ao bd, ele vai lá no banco de dados(bd), trás os dados daquele objecto que vc procurou. O getOne ele nao toca no bd, ele vai instanciar um objecto provisorio com os dados ali e com esse Id naquele objecto
		 * Só quando vc mandar salvar, ai sim é que ele vai no banco de dados, essa é a diferença, entao quando vc quiser atualizar um dado, vc usa o getOne para não ter que ir ate ao banco de dados sem necessidade  */
		//Agora que aqui já estou com a entidade Category instanciada na memoria, eu irei atualizar os dados dela.
		
		// Quais são os dados aqui de atualização que vem no dto? No caso da categoria é somente o nome.
		Category entity = repository.getOne(id); //Neste caso aqui, em versoes mais recentes do Spring Boot, o nome da função mudou para: getReferenceById
		entity.setName(dto.getName());
		entity= repository.save(entity);
		return new CategoryDTO(entity);
		}
		catch(EntityNotFoundException e) { // isto é no caso de eu tentar chamar o getOne e dar um ID que não existe e depois tentar salvar, ele vai estourar para mim essa (EntityNotFoundException e) e caso ela ocorra, ai sim, eu vou lançar a minha excepção personalizada que agora é ResourceNotFoundException
			throw new ResourceNotFoundException("Id not found " + id);
		}
		
	}
}
