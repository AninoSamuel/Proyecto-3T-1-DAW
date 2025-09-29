package com.stem.Proyecto.service;

import java.util.List;
import java.util.Optional;

import com.stem.Proyecto.entity.Equipo;

/**
 * Esta interfaz define las operaciones básicas (crear, leer, actualizar, borrar)
 * que se pueden realizar sobre los equipos.
 * La implementación de esta interfaz contendrá la lógica de negocio real.
 */
public interface EquipoService {
    /**
     * Devuelve una lista de todos los equipos.
     *
     * @return Una lista de objetos {@link Equipo}. Puede estar vacía si no hay equipos.
     */
    List<Equipo> findAll();

    /**
     * Busca un equipo por su número de identificación (ID).
     *
     * @param id El ID del equipo a buscar.
     * @return Un 'Optional' que contendrá el {@link Equipo} si se encuentra, o estará vacío si no existe.
     */
    Optional<Equipo> findById(Long id);

    /**
     * Guarda un equipo nuevo o actualiza uno existente en la base de datos.
     *
     * @param equipo El objeto {@link Equipo} que se va a guardar.
     * @return El equipo guardado o actualizado.
     */
    Equipo save(Equipo equipo);

    /**
     * Actualiza los datos de un equipo existente.
     *
     * @param id El ID del equipo a actualizar.
     * @param equipoActualizado El objeto {@link Equipo} con la nueva información.
     * @return El equipo con los datos actualizados, o 'null' si el equipo no se encontró.
     */
    Equipo update(Long id, Equipo equipoActualizado);

    /**
     * Elimina un equipo de la base de datos usando su ID.
     *
     * @param id El ID del equipo a eliminar.
     */
    void deleteById(Long id);
}