package com.fabrizio.recetapp.app.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fabrizio.recetapp.app.models.entity.Usuario;

public interface IUsuarioDao extends JpaRepository<Usuario, Long>{

}
