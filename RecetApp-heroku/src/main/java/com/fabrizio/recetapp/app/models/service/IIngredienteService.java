package com.fabrizio.recetapp.app.models.service;

import java.util.List;

import com.fabrizio.recetapp.app.models.entity.Ingrediente;

public interface IIngredienteService {
	
	List<Ingrediente> findAll();
	
	public void save(Ingrediente ingrediente);
	
	public List<Ingrediente> findByNombre(String term);

	public Ingrediente findByNombreContaining(String term);
	
	public void delete(Long id);

	public Ingrediente findOne(Long id);

	public void deleteAll();


}
