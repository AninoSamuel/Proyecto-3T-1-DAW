package com.stem.Proyecto.service;

import java.util.List;
import java.util.Optional;

import com.stem.Proyecto.entity.Logro;

/**
 * Esta interfaz define las operaciones básicas (crear, leer, actualizar, borrar)
 * que se pueden realizar sobre los logros.
 * La implementación de esta interfaz contendrá la lógica de negocio real para los logros.
 */
public interface LogroService {
    

    /**
     * Devuelve una lista de todos los logros.
     *
     * @return Una lista de objetos {@link Logro}. Puede estar vacía si no hay logros.
     */
    List<Logro> findAll();

    /**
     * Busca un logro por su número de identificación (ID).
     *
     * @param id El ID del logro a buscar.
     * @return Un 'Optional' que contendrá el {@link Logro} si se encuentra, o estará vacío si no existe.
     */
    Optional<Logro> findById(Long id);

    /**
     * Guarda un logro nuevo o actualiza uno existente en la base de datos.
     *
     * @param logro El objeto {@link Logro} que se va a guardar.
     * @return El logro guardado o actualizado.
     */
    Logro save(Logro logro);

    /**
     * Actualiza los datos de un logro existente.
     *
     * @param id El ID del logro a actualizar.
     * @param logroActualizado El objeto {@link Logro} con la nueva información.
     * @return El logro con los datos actualizados, o 'null' si el logro no se encontró.
     */
    Logro update(Long id, Logro logroActualizado);

    /**
     * Elimina un logro de la base de datos usando su ID.
     *
     * @param id El ID del logro a eliminar.
     */
    void deleteById(Long id);
}