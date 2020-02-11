package com.fabrizio.recetapp.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fabrizio.recetapp.app.models.entity.Ingrediente;

@Repository
public interface IIngredienteDao  extends JpaRepository<Ingrediente, Long> {

	@Query("select i from Ingrediente i where i.nombre like %?1%")
	public List<Ingrediente> findByNombre(String term);

	public Ingrediente findByNombreContaining(String term);
	
	public List<Ingrediente> findByNombreLikeIgnoreCase(String term);

}
