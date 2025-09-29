package com.stem.Proyecto.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.stem.Proyecto.entity.Equipo;
import com.stem.Proyecto.entity.Partido;
import com.stem.Proyecto.service.EquipoService;
import com.stem.Proyecto.service.PartidoService;

/**
 * Este controlador REST maneja las peticiones relacionadas con los partidos.
 * Permite realizar operaciones CRUD (Crear, Leer, Actualizar, Borrar) sobre los partidos
 * y obtener información sobre los equipos participantes.
 * Todas las rutas de esta API comienzan con "/api/v1/partidos".
 */
@RestController
@RequestMapping("/api/v1/partidos")
public class PartidoController {

    private final PartidoService partidoService;
    private final EquipoService equipoService;

    /**
     * Constructor que Spring usa para inyectar los servicios necesarios.
     *
     * @param partidoService El objeto que contiene la lógica de negocio para los partidos.
     * @param equipoService El objeto que contiene la lógica de negocio para los equipos.
     */
    @Autowired
    public PartidoController(PartidoService partidoService, EquipoService equipoService) {
        this.partidoService = partidoService;
        this.equipoService = equipoService;
    }

    /**
     * Obtiene una lista de todos los partidos.
     *
     * @return Una lista de todos los objetos {@link Partido} en la base de datos.
     * Ejemplo de uso: GET /api/v1/partidos
     */
    @GetMapping
    public List<Partido> getAllPartidos() {
        return partidoService.findAll();
    }

    /**
     * Obtiene un partido específico por su ID.
     *
     * @param id El ID del partido a buscar.
     * @return El objeto {@link Partido} si se encuentra, o 'null' si no existe.
     * Ejemplo de uso: GET /api/v1/partidos/1
     */
    @GetMapping("/{id}")
    public Partido getPartidoById(@PathVariable Long id) {
        return partidoService.findById(id).orElse(null);
    }

    /**
     * Obtiene el equipo local de un partido específico.
     *
     * @param id El ID del partido del que se quiere obtener el equipo local.
     * @return El objeto {@link Equipo} del equipo local, o 'null' si el partido no se encuentra o no tiene equipo local.
     * Ejemplo de uso: GET /api/v1/partidos/1/equipoLocal
     */
    @GetMapping("/{id}/equipoLocal")
    public Equipo getEquipoLocalByPartidoId(@PathVariable Long id) {
        return partidoService.findById(id)
                             .map(Partido::getEquipoLocal) // Si encuentra el partido, obtiene su equipo local
                             .orElse(null); // Si no encuentra el partido, devuelve null
    }

    /**
     * Obtiene el equipo visitante de un partido específico.
     *
     * @param id El ID del partido del que se quiere obtener el equipo visitante.
     * @return El objeto {@link Equipo} del equipo visitante, o 'null' si el partido no se encuentra o no tiene equipo visitante.
     * Ejemplo de uso: GET /api/v1/partidos/1/equipoVisitante
     */
    @GetMapping("/{id}/equipoVisitante")
    public Equipo getEquipoVisitanteByPartidoId(@PathVariable Long id) {
        return partidoService.findById(id)
                             .map(Partido::getEquipoVisitante) // Si encuentra el partido, obtiene su equipo visitante
                             .orElse(null); // Si no encuentra el partido, devuelve null
    }

    /**
     * Crea un nuevo partido en la base de datos.
     * Antes de guardar, intenta vincular los equipos local y visitante existentes por sus IDs.
     *
     * @param partido El objeto {@link Partido} a crear (enviado en el cuerpo de la petición).
     * @return El partido recién creado.
     * Ejemplo de uso: POST /api/v1/partidos
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Indica que se devolverá un estado HTTP 201 (Created)
    public Partido createPartido(@RequestBody Partido partido) {
        // Intenta asignar el equipo local si se proporciona un ID válido
        if (partido.getEquipoLocal() != null && partido.getEquipoLocal().getId() != null) {
            equipoService.findById(partido.getEquipoLocal().getId())
                         .ifPresent(partido::setEquipoLocal);
        }
        // Intenta asignar el equipo visitante si se proporciona un ID válido
        if (partido.getEquipoVisitante() != null && partido.getEquipoVisitante().getId() != null) {
            equipoService.findById(partido.getEquipoVisitante().getId())
                         .ifPresent(partido::setEquipoVisitante);
        }
        return partidoService.save(partido);
    }

    /**
     * Actualiza la información de un partido existente.
     * Permite cambiar la fecha, hora, puntuaciones, temporada, lugar, y también los equipos local y visitante.
     *
     * @param id El ID del partido a actualizar.
     * @param partido El objeto {@link Partido} con la información actualizada (enviado en el cuerpo de la petición).
     * @return El partido actualizado, o 'null' si el partido con el ID proporcionado no existe.
     * Ejemplo de uso: PUT /api/v1/partidos/1
     */
    @PutMapping("/{id}")
    public Partido updatePartido(@PathVariable Long id, @RequestBody Partido partido) {
        Optional<Partido> existingPartido = partidoService.findById(id);
        if (existingPartido.isPresent()) {
            partido.setId(id); // Asegura que el ID del objeto coincida con el ID de la ruta

            // Actualiza el equipo local, buscando el equipo real si se proporciona un ID, o desasociándolo si es nulo
            if (partido.getEquipoLocal() != null && partido.getEquipoLocal().getId() != null) {
                equipoService.findById(partido.getEquipoLocal().getId())
                             .ifPresent(partido::setEquipoLocal);
            } else if (partido.getEquipoLocal() == null) {
                partido.setEquipoLocal(null);
            }

            // Actualiza el equipo visitante, buscando el equipo real si se proporciona un ID, o desasociándolo si es nulo
            if (partido.getEquipoVisitante() != null && partido.getEquipoVisitante().getId() != null) {
                equipoService.findById(partido.getEquipoVisitante().getId())
                             .ifPresent(partido::setEquipoVisitante);
            } else if (partido.getEquipoVisitante() == null) {
                partido.setEquipoVisitante(null);
            }

            return partidoService.save(partido); // save() actúa como update si el ID ya existe
        }
        return null; // Si el partido no existe, devuelve null
    }

    /**
     * Elimina un partido de la base de datos por su ID.
     *
     * @param id El ID del partido a eliminar.
     * Ejemplo de uso: DELETE /api/v1/partidos/1
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Indica que se devolverá un estado HTTP 204 (No Content)
    public void deletePartido(@PathVariable Long id) {
        // Solo intenta eliminar si el partido existe
        if (partidoService.findById(id).isPresent()) {
            partidoService.deleteById(id);
        }
    }
}