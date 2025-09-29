package com.stem.Proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stem.Proyecto.entity.Equipo;
import com.stem.Proyecto.entity.Jugador;

@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Long> {

    List<Jugador> findByApellido(String apellido);
    List<Jugador> findByNombre(String nombre);
    List<Jugador> findByEquipoId(Long equipoId);
    List<Jugador> findByNumeroCamisetaAndEquipo(Integer numeroCamiseta, Equipo equipo);

    List<Jugador> findByNombreContainingIgnoreCase(String nombre);

}