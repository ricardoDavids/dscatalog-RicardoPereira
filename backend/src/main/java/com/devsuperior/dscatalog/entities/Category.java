package com.devsuperior.dscatalog.entities;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name= "tb_category")
public class Category implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Aqui vai ser um ID auto incrementado, ou seja,quando vc insere a 1ª categoria, automaticamente o banco de dados vai inserir o codigo nº1 
	private Long id;
	private String name;
	
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE") // Quando vc fala que está armazenando no banco de dados sem especificar o TIME ZONE , se é -3, -5, +3, +6, então vai ser o fuso horario UTC
	//Qual é o instante que o registro foi criado pela 1ª vez
	private Instant createdAt; // Aqui é um atributo que serve para armazenar um instante(tem dia e hora)
	
	
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Instant updatedAt;
	
	public Category() {
	}

	public Category(Long id, String name) {
		//super();
		this.id = id;
		this.name = name;
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

	
	
	
	public Instant getCreatedAt() {
		return createdAt;
	}
	
	/* O metodos set não vão existir porque eu não vou querer alterar à mão, porque esses atributos irão ser alterados automaticamente apenas quando voçe criar e atualizar os objectos. 
	 
	public void setCreatedAt(Instant createdAt) {  
		this.createdAt = createdAt;
	}
	*/
	
	public Instant getUpdatedAt() {
		return updatedAt;
	}
	
	/*
	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
	*/
	
	@PrePersist
	// este é um metodo que quer dizer antes de salvar, vou fazer com que o atributo createdAt receba o Instant now, mas para que isso funcione preciso de colocar um Anotation
	public void prePersist() {
		createdAt= Instant.now();
	}
	
	
	// este é um metodo que quer dizer antes de atualizar, vou fazer com que o atributo updatedAt receba o Instant now
	@PreUpdate
	public void preUpdate() {
		updatedAt = Instant.now();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
