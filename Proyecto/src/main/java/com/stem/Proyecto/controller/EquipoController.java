package com.stem.Proyecto.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus; 
import org.springframework.web.bind.annotation.RestController;

import com.stem.Proyecto.entity.Equipo;
import com.stem.Proyecto.entity.Jugador;
import com.stem.Proyecto.service.EquipoService;

/**
 * Este controlador REST maneja las peticiones relacionadas con los equipos.
 * Permite realizar operaciones CRUD (Crear, Leer, Actualizar, Borrar) sobre los equipos
 * y obtener los jugadores asociados a un equipo.
 * Todas las rutas de esta API comienzan con "/api/v1/equipos".
 */
@RestController
@RequestMapping("/api/v1/equipos")
public class EquipoController {

    @Autowired
    private EquipoService equipoService;

    /**
     * Obtiene una lista de todos los equipos.
     *
     * @return Una lista de todos los objetos {@link Equipo} en la base de datos.
     * Ejemplo de uso: GET /api/v1/equipos
     */
    @GetMapping
    public List<Equipo> getAllEquipos() {
        return equipoService.findAll();
    }

    /**
     * Obtiene un equipo específico por su ID.
     *
     * @param id El ID del equipo a buscar.
     * @return El objeto {@link Equipo} si se encuentra, o 'null' si no existe.
     * Ejemplo de uso: GET /api/v1/equipos/1
     */
    @GetMapping("/{id}")
    public Equipo getEquipoById(@PathVariable Long id) {
        return equipoService.findById(id).orElse(null);
    }

    /**
     * Crea un nuevo equipo en la base de datos.
     *
     * @param equipo El objeto {@link Equipo} a crear (enviado en el cuerpo de la petición).
     * @return El equipo recién creado.
     * Ejemplo de uso: POST /api/v1/equipos
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Indica que se devolverá un estado HTTP 201 (Created)
    public Equipo createEquipo(@RequestBody Equipo equipo) {
        return equipoService.save(equipo);
    }

    /**
     * Actualiza la información de un equipo existente (método PUT).
     * @param id El ID del equipo a actualizar.
     * @param equipo El objeto {@link Equipo} con la información actualizada (enviado en el cuerpo de la petición).
     * @return El equipo actualizado, o 'null' si el equipo con el ID proporcionado no existe.
     * Ejemplo de uso: PUT /api/v1/equipos/1
     */
    @PutMapping("/{id}")
    public Equipo updateEquipo(@PathVariable Long id, @RequestBody Equipo equipo) {
        Equipo result = null;
        // Solo actualiza si el equipo existe
        if (equipoService.findById(id).isPresent()) {
            equipo.setId(id); // Asegura que el ID del objeto coincida con el ID de la ruta
            result = equipoService.save(equipo); // save() actúa como update si el ID ya existe
        }
        return result;
    }

    /**
     * Actualiza parcialmente la información de un equipo existente (método PATCH).
     * @param id El ID del equipo a actualizar.
     * @param equipo El objeto {@link Equipo} con los datos parciales (los no nulos se actualizarán).
     * @return El equipo actualizado, o un estado 404 Not Found si el equipo no existe.
     * Ejemplo de uso: PATCH /api/v1/equipos/1
     */
    @PatchMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"})
        public ResponseEntity<Equipo> updateEquipoPatch(@PathVariable Long id, @RequestBody Equipo equipo) {
            Equipo updatedEquipo = equipoService.update(id, equipo); 
            if (updatedEquipo != null) {
                return ResponseEntity.ok(updatedEquipo);
            }
            return ResponseEntity.notFound().build();
        }

    /**
     * Elimina un equipo de la base de datos por su ID.
     *
     * @param id El ID del equipo a eliminar.
     * Ejemplo de uso: DELETE /api/v1/equipos/1
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Indica que se devolverá un estado HTTP 204 (No Content)
    public void deleteEquipo(@PathVariable Long id) {
        // Solo intenta eliminar si el equipo existe
        if (equipoService.findById(id).isPresent()) {
            equipoService.deleteById(id);
        }
    }

    /**
     * Obtiene la lista de jugadores que pertenecen a un equipo específico.
     *
     * @param id El ID del equipo del que se quieren obtener los jugadores.
     * @return Una lista de objetos {@link Jugador} asociados al equipo, o 'null' si el equipo no se encuentra.
     * Ejemplo de uso: GET /api/v1/equipos/1/jugadores
     */
    @GetMapping("/{id}/jugadores")
    public List<Jugador> getJugadoresByEquipoId(@PathVariable Long id) {
        Optional<Equipo> equipoOpt = equipoService.findById(id);
        // Si el equipo existe, devuelve su lista de jugadores; de lo contrario, devuelve null.
        return equipoOpt.map(Equipo::getJugadores).orElse(null);
    }
}