package com.stem.Proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.stem.Proyecto.entity.Logro;

@Repository
public interface LogroRepository extends JpaRepository<Logro, Long> {

    List<Logro> findByNombre(String nombre);
    List<Logro> findByAnio(Integer anio);
    List<Logro> findByDescripcionContainingIgnoreCase(String palabra);
    @Query("SELECT l FROM Logro l LEFT JOIN FETCH l.jugadoresConEsteLogro")
    List<Logro> findAllWithJugadores();
}