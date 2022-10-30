package com.devsuperior.dscatalog.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;

@RestController
@RequestMapping(value= "/products") //rota products
public class ProductResource {
	
	@Autowired
	private ProductService service;
	
	@GetMapping//vai responder todas as categorias que estão no banco de dados
	public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable)
			{
		
		// PARAMETROS: page, size, sort
		
		//vou intanciar aqui o PageRequest utilizando um metodo do builder dele que é o PageRequest.of
		//PageRequest pageRequest = PageRequest.of(page,linesPerPage, Direction.valueOf(direction),orderBy); 
		
		
		Page<ProductDTO> list = service.findAllPaged(pageable);
		
		return ResponseEntity.ok().body(list); // objectos guardados nesta lista, e agora retorno esta lista na minha requesição
			
	}
	@GetMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id){
		ProductDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto); 
		
	}
	@PostMapping
	public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO dto){
		dto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
		
	}
	
	
	@PutMapping(value= "/{id}")
	public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO dto){
		dto = service.update(id,dto);
		return ResponseEntity.ok().body(dto);
		
	}
	
	@DeleteMapping(value= "/{id}")
	public ResponseEntity<ProductDTO> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
		
	}

}