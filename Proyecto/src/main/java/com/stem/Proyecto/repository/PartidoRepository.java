package com.stem.Proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stem.Proyecto.entity.Equipo;
import com.stem.Proyecto.entity.Partido;

@Repository
public interface PartidoRepository extends JpaRepository<Partido, Long> {

    List<Partido> findByEquipoLocal(Equipo equipoLocal);
    List<Partido> findByEquipoVisitante(Equipo equipoVisitante);
    List<Partido> findByTemporada(String temporada);
    List<Partido> findByEquipoLocalIdAndEquipoVisitanteId(Long equipoLocalId, Long equipoVisitanteId);
}