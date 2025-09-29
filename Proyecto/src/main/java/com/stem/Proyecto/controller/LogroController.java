package com.stem.Proyecto.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stem.Proyecto.entity.Logro;
import com.stem.Proyecto.service.LogroService;

/**
 * Este controlador REST maneja las peticiones relacionadas con los logros.
 * Permite realizar operaciones CRUD (Crear, Leer, Actualizar, Borrar) sobre los logros.
 * Todas las rutas de esta API comienzan con "/api/v1/logros".
 */
@RestController
@RequestMapping("/api/v1/logros")
public class LogroController {

    private final LogroService logroService;

    /**
     * Constructor que Spring usa para inyectar el servicio de logros.
     *
     * @param logroService El objeto que contiene la lógica de negocio para los logros.
     */
    @Autowired // La inyección por constructor es la forma recomendada y asegura que el servicio esté disponible
    public LogroController(LogroService logroService) {
        this.logroService = logroService;
    }

    /**
     * Obtiene una lista de todos los logros.
     * Este método es el punto de entrada para la ruta GET /api/v1/logros.
     * Llama al servicio para obtener todos los logros, los cuales, gracias a la
     * configuración en la entidad Logro (el método getJugadoresJson() con @JsonProperty),
     * serán serializados a JSON incluyendo una lista de los jugadores asociados.
     *
     * @return Un {@link ResponseEntity} que contiene una lista de todos los objetos {@link Logro}
     * en la base de datos con un estado HTTP 200 OK.
     * Ejemplo de uso: GET /api/v1/logros
     */
    @GetMapping // Mapea las peticiones GET a la ruta base "/api/v1/logros"
    public ResponseEntity<List<Logro>> findAll() { // Nombre de método claro para la operación de listar todos
        List<Logro> logros = logroService.findAll();
        return ResponseEntity.ok(logros); // Devuelve las entidades Logro directamente para que Jackson las serialice
    }

    /**
     * Busca un logro específico por su ID.
     *
     * @param id El ID del logro a buscar.
     * @return Un {@link ResponseEntity} que contiene el {@link Logro} si se encuentra (código 200 OK),
     * o un estado 404 Not Found si no existe.
     * Ejemplo de uso: GET /api/v1/logros/1
     */
    @GetMapping("/{id}") // Mapea las peticiones GET a rutas como "/api/v1/logros/1"
    public ResponseEntity<Logro> findById(@PathVariable Long id) {
        Optional<Logro> logroOpt = logroService.findById(id);
        // Si el logro existe, devuelve 200 OK con el logro; si no, devuelve 404 Not Found.
        return logroOpt.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo logro en la base de datos.
     *
     * @param logro El objeto {@link Logro} a crear (enviado en el cuerpo de la petición).
     * @return Un {@link ResponseEntity} con el logro recién creado y un estado 201 Created.
     * Ejemplo de uso: POST /api/v1/logros
     */
    @PostMapping // Mapea las peticiones POST a la ruta base "/api/v1/logros"
    public ResponseEntity<Logro> save(@RequestBody Logro logro) {
        Logro savedLogro = logroService.save(logro);
        return new ResponseEntity<>(savedLogro, HttpStatus.CREATED); 
    }

    /**
     * Actualiza la información de un logro existente.
     *
     * @param id El ID del logro a actualizar.
     * @param logro El objeto {@link Logro} con la información actualizada (enviado en el cuerpo de la petición).
     * @return Un {@link ResponseEntity} con el logro actualizado y un estado 200 OK si se actualiza,
     * o un estado 404 Not Found si el logro con el ID proporcionado no existe.
     * Ejemplo de uso: PUT /api/v1/logros/1
     */
    @PutMapping("/{id}") 
    public ResponseEntity<Logro> update(@PathVariable Long id, @RequestBody Logro logro) {
        Logro updatedLogro = logroService.update(id, logro);
        if (updatedLogro != null) {
            return ResponseEntity.ok(updatedLogro); 
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Elimina un logro de la base de datos por su ID.
     *
     * @param id El ID del logro a eliminar.
     * @return Un {@link ResponseEntity} sin contenido y un estado 204 No Content para indicar que la operación fue exitosa.
     * Ejemplo de uso: DELETE /api/v1/logros/1
     */
    @DeleteMapping("/{id}") 
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        logroService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}