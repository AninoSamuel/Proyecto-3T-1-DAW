package com.stem.Proyecto.service.impl;

import java.util.HashSet;   
import java.util.List;
import java.util.Optional;
import java.util.Set;     

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stem.Proyecto.entity.Jugador;
import com.stem.Proyecto.entity.Logro;
import com.stem.Proyecto.repository.EquipoRepository;
import com.stem.Proyecto.repository.JugadorRepository;
import com.stem.Proyecto.repository.LogroRepository;
import com.stem.Proyecto.service.JugadorService;

/**
 * Esta clase maneja las operaciones de negocio para los Jugadores.
 * Permite gestionar jugadores, asignarles equipos y asignarles logros.
 */
@Service
public class JugadorServiceImpl implements JugadorService {

    private final JugadorRepository jugadorRepository;
    private final EquipoRepository equipoRepository;
    private final LogroRepository logroRepository;

    /**
     * Constructor que Spring usa para inyectar los repositorios necesarios.
     *
     * @param jugadorRepository Objeto para guardar y buscar jugadores.
     * @param equipoRepository Objeto para buscar equipos y asignarlos a jugadores.
     * @param logroRepository Objeto para buscar logros y asignarlos a jugadores.
     */
    @Autowired
    public JugadorServiceImpl(JugadorRepository jugadorRepository, EquipoRepository equipoRepository, LogroRepository logroRepository) {
        this.jugadorRepository = jugadorRepository;
        this.equipoRepository = equipoRepository;
        this.logroRepository = logroRepository;
    }

    /**
     * Encuentra y devuelve todos los jugadores que existen en la base de datos.
     *
     * @return Una lista de todos los jugadores. Si no hay ninguno, la lista estará vacía.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Jugador> findAll() {
        return jugadorRepository.findAll();
    }

    /**
     * Busca un jugador específico usando su ID.
     *
     * @param id El número de identificación del jugador que se quiere buscar.
     * @return Un 'Optional' que contendrá el jugador si lo encuentra, o estará vacío si no existe.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Jugador> findById(Long id) {
        return jugadorRepository.findById(id);
    }

    /**
     * Guarda un jugador nuevo en la base de datos o actualiza uno que ya existe.
     * Si el jugador tiene un equipo asignado, se asegura de que el equipo exista y lo vincula.
     *
     * @param jugador El jugador que se quiere guardar o actualizar.
     * @return El jugador que ha sido guardado, con su ID (si es nuevo) o con sus datos actualizados.
     */
    @Override
    @Transactional
    public Jugador save(Jugador jugador) {
        if (jugador.getEquipo() != null && jugador.getEquipo().getId() != null) {
            // Si el jugador tiene un ID de equipo, busca el equipo y lo asigna
            equipoRepository.findById(jugador.getEquipo().getId())
                    .ifPresent(jugador::setEquipo);
        } else if (jugador.getEquipo() == null) {
            // Si no se proporciona un equipo, asegura que el campo de equipo sea nulo
            jugador.setEquipo(null);
        }
        return jugadorRepository.save(jugador);
    }

    /**
     * Actualiza la información de un jugador que ya existe.
     * Permite cambiar datos como nombre, altura, peso, posición, número de camiseta, y también el equipo al que pertenece.
     *
     * @param id El ID del jugador que se va a actualizar.
     * @param jugadorActualizado El jugador con los datos nuevos.
     * @return El jugador con su información ya cambiada, o 'null' si no se encontró un jugador con ese ID.
     */
    @Override
    @Transactional
    public Jugador update(Long id, Jugador jugadorActualizado) {
        return jugadorRepository.findById(id)
                .map(jugadorExistente -> {
                    // Actualiza los datos básicos del jugador
                    jugadorExistente.setNombre(jugadorActualizado.getNombre());
                    jugadorExistente.setApellido(jugadorActualizado.getApellido());
                    jugadorExistente.setFechaNacimiento(jugadorActualizado.getFechaNacimiento());
                    jugadorExistente.setAlturaCm(jugadorActualizado.getAlturaCm());
                    jugadorExistente.setPesoKg(jugadorActualizado.getPesoKg());
                    jugadorExistente.setPosicion(jugadorActualizado.getPosicion());
                    jugadorExistente.setNumeroCamiseta(jugadorActualizado.getNumeroCamiseta());
                    jugadorExistente.setActivo(jugadorActualizado.getActivo());

                    // Actualiza el equipo del jugador, si se proporciona
                    if (jugadorActualizado.getEquipo() != null && jugadorActualizado.getEquipo().getId() != null) {
                        equipoRepository.findById(jugadorActualizado.getEquipo().getId())
                                .ifPresent(jugadorExistente::setEquipo);
                    } else if (jugadorActualizado.getEquipo() == null) {
                        jugadorExistente.setEquipo(null); // Desasocia el equipo si se envía nulo
                    }
                    return jugadorRepository.save(jugadorExistente);
                })
                .orElse(null); // Retorna null si el jugador no se encuentra
    }

    /**
     * Elimina un jugador de la base de datos usando su ID.
     *
     * @param id El ID del jugador que se quiere borrar.
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        jugadorRepository.deleteById(id);
    }

    /**
     * Busca jugadores por nombre (parcial o completo), ignorando mayúsculas y minúsculas.
     *
     * @param nombre El nombre o parte del nombre a buscar.
     * @return Una lista de jugadores que coinciden con el criterio de búsqueda.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Jugador> findByNombreContainingIgnoreCase(String nombre) {
        return jugadorRepository.findByNombreContainingIgnoreCase(nombre);
    }


    /**
     * Asigna un logro a un jugador específico.
     *
     * @param jugadorId El ID del jugador al que se le quiere añadir el logro.
     * @param logroId El ID del logro que se quiere añadir.
     * @return El jugador con el logro añadido.
     * @throws RuntimeException si no se encuentra al jugador o al logro.
     */
    @Override 
    @Transactional
    public Jugador addLogroToJugador(Long jugadorId, Long logroId) {
        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado con ID: " + jugadorId));

        Logro logro = logroRepository.findById(logroId)
                .orElseThrow(() -> new RuntimeException("Logro no encontrado con ID: " + logroId));

        // Usa el método helper addLogro() definido en la entidad Jugador
        jugador.addLogro(logro);
        return jugadorRepository.save(jugador);
    }

    /**
     * Quita un logro de un jugador específico.
     *
     * @param jugadorId El ID del jugador al que se le quiere quitar el logro.
     * @param logroId El ID del logro que se quiere quitar.
     * @throws RuntimeException si no se encuentra al jugador o al logro.
     */
    @Override 
    @Transactional
    public void removeLogroFromJugador(Long jugadorId, Long logroId) {
        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado con ID: " + jugadorId));

        Logro logro = logroRepository.findById(logroId)
                .orElseThrow(() -> new RuntimeException("Logro no encontrado con ID: " + logroId));

        // Usa el método helper removeLogro() definido en la entidad Jugador
        jugador.removeLogro(logro);
        jugadorRepository.save(jugador);
    }

    /**
     * Actualiza (reemplaza) la lista completa de logros de un jugador con una nueva lista de IDs de logros.
     *
     * @param jugadorId El ID del jugador cuyos logros se actualizarán.
     * @param logroIds Una lista de IDs de logros que se asignarán al jugador.
     * @return El {@link Jugador} actualizado con la nueva lista de logros.
     * @throws RuntimeException si no se encuentra al jugador o si algún logro en la lista no se encuentra.
     */
    @Override 
    @Transactional
    public Jugador updateJugadorLogros(Long jugadorId, List<Long> logroIds) {
        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado con ID: " + jugadorId));

        // Crear un Set de los nuevos logros a partir de sus IDs
        Set<Logro> nuevosLogros = new HashSet<>();
        if (logroIds != null) {
            for (Long id : logroIds) {
                Logro logro = logroRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Logro no encontrado con ID: " + id));
                nuevosLogros.add(logro);
            }
        }

        // 1. Desvincular logros antiguos que ya no están en la nueva lista
        // (Iterar sobre una copia para evitar ConcurrentModificationException)
        Set<Logro> logrosActuales = new HashSet<>(jugador.getLogros()); // Copia del set actual
        for (Logro logroExistente : logrosActuales) {
            if (!nuevosLogros.contains(logroExistente)) {
                // Si el logro existente no está en la nueva lista, elimínalo
                jugador.removeLogro(logroExistente); // Usa el método helper
            }
        }

        // 2. Vincular nuevos logros que no estaban antes
        for (Logro nuevoLogro : nuevosLogros) {
            if (!jugador.getLogros().contains(nuevoLogro)) {
                // Si el nuevo logro no está en la lista actual del jugador, añádelo
                jugador.addLogro(nuevoLogro); // Usa el método helper
            }
        }

        return jugadorRepository.save(jugador);
    }
}