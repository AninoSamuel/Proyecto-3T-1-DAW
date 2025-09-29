package com.stem.Proyecto.service;

import java.util.List;
import java.util.Optional;

import com.stem.Proyecto.entity.Jugador;

/**
 * Esta interfaz define las operaciones básicas (crear, leer, actualizar, borrar)
 * que se pueden realizar sobre los jugadores.
 * La implementación de esta interfaz contendrá la lógica de negocio real para los jugadores.
 */
public interface JugadorService {
    /**
     * Devuelve una lista de todos los jugadores.
     *
     * @return Una lista de objetos {@link Jugador}. Puede estar vacía si no hay jugadores.
     */
    List<Jugador> findAll();

    /**
     * Busca un jugador por su número de identificación (ID).
     *
     * @param id El ID del jugador a buscar.
     * @return Un 'Optional' que contendrá el {@link Jugador} si se encuentra, o estará vacío si no existe.
     */
    Optional<Jugador> findById(Long id);

    /**
     * Guarda un jugador nuevo o actualiza uno existente en la base de datos.
     *
     * @param jugador El objeto {@link Jugador} que se va a guardar.
     * @return El jugador guardado o actualizado.
     */
    Jugador save(Jugador jugador);

    /**
     * Actualiza los datos de un jugador existente.
     *
     * @param id El ID del jugador a actualizar.
     * @param jugadorActualizado El objeto {@link Jugador} con la nueva información.
     * @return El jugador con los datos actualizados, o 'null' si el jugador no se encontró.
     */
    Jugador update(Long id, Jugador jugadorActualizado);

    /**
     * Elimina un jugador de la base de datos usando su ID.
     *
     * @param id El ID del jugador a eliminar.
     */
    void deleteById(Long id);

    List<Jugador> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Añade un logro específico a un jugador.
     *
     * @param jugadorId El ID del jugador al que se le añadirá el logro.
     * @param logroId El ID del logro a añadir.
     * @return El {@link Jugador} actualizado con el nuevo logro.
     */
    Jugador addLogroToJugador(Long jugadorId, Long logroId); // <-- ¡AÑADIR ESTE MÉTODO!

    /**
     * Elimina un logro específico de un jugador.
     *
     * @param jugadorId El ID del jugador del que se eliminará el logro.
     * @param logroId El ID del logro a eliminar.
     */
    void removeLogroFromJugador(Long jugadorId, Long logroId); // <-- ¡AÑADIR ESTE MÉTODO!

    /**
     * Actualiza (reemplaza) la lista completa de logros de un jugador con una nueva lista de IDs de logros.
     *
     * @param jugadorId El ID del jugador cuyos logros se actualizarán.
     * @param logroIds Una lista de IDs de logros que se asignarán al jugador.
     * @return El {@link Jugador} actualizado con la nueva lista de logros.
     */
    Jugador updateJugadorLogros(Long jugadorId, List<Long> logroIds); // <-- ¡AÑADIR ESTE MÉTODO!

    // Opcional: Si quieres un método para obtener los logros de un jugador directamente desde el servicio
    // public Set<Logro> getLogrosByJugadorId(Long jugadorId);
}