package com.fabrizio.recetapp.app.models.service;

import java.util.List;

import com.fabrizio.recetapp.app.models.entity.Ingrediente;
import com.fabrizio.recetapp.app.models.entity.Receta;

public interface IRecetaService {
	
	public List<Receta> findAll();
	
	public void save(Receta receta);
	
	public Receta findOne(Long id);
	
	public void delete(Long id);
	
	public List<Ingrediente> findByNombre(String term);
	
	public List<Ingrediente> findIngredienteByNombre(String term);
	
	public void deleteAll();
	
	public Receta findRecetaById(Long id);
	
	public Ingrediente findIngredienteById(Long id);
	
//	public List<Receta> findRecetaByIngredienteNombre(List<Ingrediente> ingredientes);

}
