package com.stem.Proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stem.Proyecto.entity.Equipo;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {
    List<Equipo> findByNombre(String nombre);
    List<Equipo> findByCiudad(String ciudad);
}
