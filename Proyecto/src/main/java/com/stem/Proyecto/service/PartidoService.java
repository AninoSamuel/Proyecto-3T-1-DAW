package com.stem.Proyecto.service;

import java.util.List;
import java.util.Optional;

import com.stem.Proyecto.entity.Partido;

/**
 * Esta interfaz define las operaciones básicas (crear, leer, actualizar, borrar)
 * que se pueden realizar sobre los partidos.
 * La implementación de esta interfaz contendrá la lógica de negocio real para los partidos.
 */
public interface PartidoService {
    /**
     * Devuelve una lista de todos los partidos.
     *
     * @return Una lista de objetos {@link Partido}. Puede estar vacía si no hay partidos.
     */
    List<Partido> findAll();

    /**
     * Busca un partido por su número de identificación (ID).
     *
     * @param id El ID del partido a buscar.
     * @return Un 'Optional' que contendrá el {@link Partido} si se encuentra, o estará vacío si no existe.
     */
    Optional<Partido> findById(Long id);

    /**
     * Guarda un partido nuevo o actualiza uno existente en la base de datos.
     *
     * @param partido El objeto {@link Partido} que se va a guardar.
     * @return El partido guardado o actualizado.
     */
    Partido save(Partido partido);

    /**
     * Actualiza los datos de un partido existente.
     *
     * @param id El ID del partido a actualizar.
     * @param partidoActualizado El objeto {@link Partido} con la nueva información.
     * @return El partido con los datos actualizados, o 'null' si el partido no se encontró.
     */
    Partido update(Long id, Partido partidoActualizado);

    /**
     * Elimina un partido de la base de datos usando su ID.
     *
     * @param id El ID del partido a eliminar.
     */
    void deleteById(Long id);
}