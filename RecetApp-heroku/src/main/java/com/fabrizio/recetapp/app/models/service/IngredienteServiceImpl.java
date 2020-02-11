package com.fabrizio.recetapp.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fabrizio.recetapp.app.models.dao.IIngredienteDao;
import com.fabrizio.recetapp.app.models.entity.Ingrediente;

@Service
public class IngredienteServiceImpl implements IIngredienteService {
	
	@Autowired
	private IIngredienteDao ingredienteDao;

	@Override
	@Transactional(readOnly = true)
	public List<Ingrediente> findAll() {
		return (List<Ingrediente>) ingredienteDao.findAll();
	}

	@Override
	@Transactional
	public void save(Ingrediente ingrediente) {
		ingredienteDao.save(ingrediente);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Ingrediente> findByNombre(String term) {
		return ingredienteDao.findByNombre(term);
	}

	@Override
	@Transactional(readOnly = true)
	public Ingrediente findByNombreContaining(String term) {
		return ingredienteDao.findByNombreContaining(term);
	}
	
	@Override
	@Transactional
	public void delete(Long id) {
		ingredienteDao.deleteById(id);
	}

	@Override
	public Ingrediente findOne(Long id) {
		return ingredienteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteAll() {
		ingredienteDao.deleteAll();
	}

}
