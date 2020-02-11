package com.fabrizio.recetapp.app.models.service;

import com.fabrizio.recetapp.app.models.dao.IIngredienteDao;
import com.fabrizio.recetapp.app.models.dao.IRecetaDao;
import com.fabrizio.recetapp.app.models.entity.Ingrediente;
import com.fabrizio.recetapp.app.models.entity.Receta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecetaServiceImpl implements IRecetaService {

    @Autowired
    private IRecetaDao recetaDao;

    @Autowired
    IngredienteServiceImpl ingredienteService;

    @Autowired
    private IIngredienteDao ingredienteDao;

    @Override
    @Transactional(readOnly = true)
    public List<Receta> findAll() {
        return (List<Receta>) recetaDao.findAll();
    }

    @Override
    @Transactional
    public void save(Receta receta) {
        recetaDao.save(receta);
    }

    @Override
    @Transactional(readOnly = true)
    public Receta findOne(Long id) {
        return recetaDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        recetaDao.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAll() {
        recetaDao.deleteAll();
    }

    @Override
    public Receta findRecetaById(Long id) {
        return recetaDao.findById(id).orElse(null);
    }

    @Override
    public Ingrediente findIngredienteById(Long id) {
        return ingredienteDao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ingrediente> findIngredienteByNombre(String term) {
        return ingredienteDao.findByNombre(term);
    }

    @Override
    public List<Ingrediente> findByNombre(String term) {
        return ingredienteDao.findByNombreLikeIgnoreCase(term);
    }

    public List<Receta> findByIngrediente(List<Ingrediente> ingredientes) {
        List<Receta> r = new ArrayList<>();
        int z = 0;
        for (Receta re :
                recetaDao.findAll()) {
            z = 0;
            for (Ingrediente i :
                    ingredientes) {
                if (re.getItems().contains(i)) {
                    z++;
                }
            }
            if (z == ingredientes.size())
                r.add(re);
        }
        return r;
    }

    
//    Este metodo  devuelve todas las recetas que contengan al menos uno de los ingredientes que ingresa el usuario
    public List<Receta> findByIngrediente2(List<Ingrediente> ingredientes) {
        List<Receta> r = new ArrayList<>();
        for (Receta re :
                recetaDao.findAll()) {
            for (Ingrediente i :
                    ingredientes) {
                if (re.getItems().contains(i)) {
                    r.add(re);
                    break;
                }
            }
        }
        return r;
    }

//	@Override
//	public List<Receta> findRecetaByIngredienteNombre(List<Ingrediente> ingredientes) {
//		List<Receta> recetasEncontradas = new ArrayList<Receta>();
//		List<Ingrediente> ingredientesTraidos = ingredientes;
//		List<Receta> recetasTraidas = new ArrayList
//		Ingrediente ingrediente;
//		
//		//FORMA 1
//		for (Receta receta : recetas) {
//			if (receta.getItems().contains(ingredientes)) {
//				recetasEncontradas.add(receta);
//			}
//		}
//		
//		//FORMA 2
//		for( Receta receta : recetas) {
//			if (ingrediente.getReceta().getItems() == ingredientes) {
//				recetasEncontradas.add(receta);
//			}
//		}
//			
//		
//		return recetasEncontradas;
//	}


}
