package com.fabrizio.recetapp.app.models.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "recetas")
public class Receta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String nombre;
    @NotEmpty
    @Lob
    private String descripcion;
    @NotEmpty
    private String tiempo;

    private String foto;

    private boolean vegetariano;

    private boolean vegano;

    private boolean celiaco;

    @ManyToMany
    private List<Ingrediente> items;

    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;

    public Receta() {
        this.items = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public boolean isVegetariano() {
        return vegetariano;
    }

    public void setVegetariano(boolean vegetariano) {
        this.vegetariano = vegetariano;
    }

    public boolean isVegano() {
        return vegano;
    }

    public void setVegano(boolean vegano) {
        this.vegano = vegano;
    }

    public boolean isCeliaco() {
        return celiaco;
    }

    public void setCeliaco(boolean celiaco) {
        this.celiaco = celiaco;
    }

    public List<Ingrediente> getItems() {
        return items;
    }

    public void setItems(List<Ingrediente> items) {
        this.items = items;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void addIngrediente(Ingrediente i) {
        this.items.add(i);
    }

    @Override
    public String toString() {
        return "Receta [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", tiempo=" + tiempo
                + ", foto=" + foto + ", vegetariano=" + vegetariano + ", vegano=" + vegano + ", celiaco=" + celiaco
                + ", items=" + items + ", usuario=" + usuario + "]";
    }

    private static final long serialVersionUID = 1L;


}
