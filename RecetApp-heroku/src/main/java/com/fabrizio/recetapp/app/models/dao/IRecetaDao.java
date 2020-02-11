package com.fabrizio.recetapp.app.models.dao;

import java.util.List;

import com.fabrizio.recetapp.app.models.entity.Ingrediente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fabrizio.recetapp.app.models.entity.Receta;

@Repository
public interface IRecetaDao extends CrudRepository<Receta, Long> {

	public List<Receta>findByNombreLikeIgnoreCase(String term);

}
