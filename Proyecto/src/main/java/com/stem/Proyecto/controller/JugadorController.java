package com.stem.Proyecto.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set; 

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stem.Proyecto.entity.Jugador;
import com.stem.Proyecto.entity.Logro;
import com.stem.Proyecto.service.EquipoService;
import com.stem.Proyecto.service.JugadorService;

/**
 * Este controlador REST maneja las peticiones relacionadas con los jugadores.
 * Permite realizar operaciones CRUD (Crear, Leer, Actualizar, Borrar) sobre los jugadores,
 * así como gestionar sus equipos y logros asociados.
 * Todas las rutas de esta API comienzan con "/api/v1/jugadores".
 */
@RestController
@RequestMapping("/api/v1/jugadores")
public class JugadorController {

    private final JugadorService jugadorService;
    private final EquipoService equipoService;
    // Si no vas a usar logroService directamente en el controlador (solo a través de jugadorService), puedes eliminarlo.
    // private final LogroService logroService;

    // Usar inyección por constructor es preferible a @Autowired en los campos
    @Autowired
    public JugadorController(JugadorService jugadorService, EquipoService equipoService /*, LogroService logroService*/) {
        this.jugadorService = jugadorService;
        this.equipoService = equipoService;
        // this.logroService = logroService;
    }

    /**
     * Obtiene una lista de todos los jugadores.
     *
     * @return Una lista de todos los objetos {@link Jugador} en la base de datos.
     * Ejemplo de uso: GET /api/v1/jugadores
     */
    @GetMapping
    public ResponseEntity<List<Jugador>> getAllJugadores() {
        List<Jugador> jugadores = jugadorService.findAll();
        return ResponseEntity.ok(jugadores);
    }

    /**
     * Obtiene un jugador específico por su ID.
     *
     * @param id El ID del jugador a buscar.
     * @return El objeto {@link Jugador} si se encuentra, o 'null' si no existe.
     * Ejemplo de uso: GET /api/v1/jugadores/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<Jugador> getJugadorById(@PathVariable Long id) {
        return jugadorService.findById(id)
                .map(ResponseEntity::ok) 
                .orElse(ResponseEntity.notFound().build()); 
    }

    /**
     * Crea un nuevo jugador en la base de datos.
     * Antes de guardar, intenta vincular al jugador con un equipo existente si se proporciona un ID de equipo.
     *
     * @param jugador El objeto {@link Jugador} a crear (enviado en el cuerpo de la petición).
     * @return El jugador recién creado.
     * Ejemplo de uso: POST /api/v1/jugadores
     */
    @PostMapping
    public ResponseEntity<Jugador> createJugador(@RequestBody Jugador jugador) {
        // La lógica de asignación de equipo ahora se maneja en el servicio save()
        Jugador createdJugador = jugadorService.save(jugador);
        return new ResponseEntity<>(createdJugador, HttpStatus.CREATED); 
    }

    /**
     * Actualiza la información de un jugador existente.
     * También permite actualizar el equipo asociado al jugador.
     *
     * @param id El ID del jugador a actualizar.
     * @param jugador El objeto {@link Jugador} con la información actualizada (enviado en el cuerpo de la petición).
     * @return El jugador actualizado, o 'null' si el jugador con el ID proporcionado no existe.
     * Ejemplo de uso: PUT /api/v1/jugadores/1
     */
    @PutMapping("/{id}")
    public ResponseEntity<Jugador> updateJugador(@PathVariable Long id, @RequestBody Jugador jugador) {
        // La lógica de actualización del equipo y los datos básicos está en el servicio update()
        Jugador updatedJugador = jugadorService.update(id, jugador);
        if (updatedJugador != null) {
            return ResponseEntity.ok(updatedJugador);
        } else {
            return ResponseEntity.notFound().build(); 
        }
    }

    /**
     * Elimina un jugador de la base de datos por su ID.
     *
     * @param id El ID del jugador a eliminar.
     * Ejemplo de uso: DELETE /api/v1/jugadores/1
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJugador(@PathVariable Long id) {
        // Comprobar si existe antes de eliminar es una buena práctica para devolver 404
        if (jugadorService.findById(id).isPresent()) {
            jugadorService.deleteById(id);
            return ResponseEntity.noContent().build(); 
        } else {
            return ResponseEntity.notFound().build(); 
        }
    }

    /**
     * Busca jugadores por nombre (parcial o completo).
     *
     * @param nombre El nombre o parte del nombre a buscar.
     * @return Una lista de jugadores que coinciden con el criterio de búsqueda.
     * Ejemplo de uso: GET /api/v1/jugadores/search?nombre=juan
     */
    @GetMapping("/search")
    public ResponseEntity<List<Jugador>> searchJugadores(@RequestParam String nombre) {
        List<Jugador> jugadores = jugadorService.findByNombreContainingIgnoreCase(nombre);
        if (jugadores.isEmpty()) {
            return ResponseEntity.noContent().build(); 
        }
        return ResponseEntity.ok(jugadores); 
    }

    /**
     * Obtiene la lista de logros asociados a un jugador específico.
     *
     * @param jugadorId El ID del jugador del que se quieren obtener los logros.
     * @return Un Set de objetos {@link Logro} asociados al jugador.
     * Ejemplo de uso: GET /api/v1/jugadores/1/logros
     */
    @GetMapping("/{jugadorId}/logros")
    public ResponseEntity<Set<Logro>> getJugadorLogros(@PathVariable Long jugadorId) { 
        Optional<Jugador> jugadorOpt = jugadorService.findById(jugadorId);
        if (jugadorOpt.isPresent()) {
            // Devuelve el Set de logros del jugador.
            // Spring Boot (Jackson) lo serializará como un array JSON.
            return ResponseEntity.ok(jugadorOpt.get().getLogros());
        } else {
            return ResponseEntity.notFound().build(); 
        }
    }

    /**
     * Añade un logro específico a un jugador.
     *
     * @param jugadorId El ID del jugador al que se le asignará el logro.
     * @param logroId El ID del logro a asignar.
     * @return El objeto {@link Jugador} actualizado con el logro asignado.
     * Ejemplo de uso: POST /api/v1/jugadores/1/logros/1
     */
    @PostMapping("/{jugadorId}/logros/{logroId}")
    public ResponseEntity<Jugador> addLogroToJugador(@PathVariable Long jugadorId, @PathVariable Long logroId) {
        try {
            // Llama directamente al método del servicio que maneja la lógica
            Jugador updatedJugador = jugadorService.addLogroToJugador(jugadorId, logroId);
            return ResponseEntity.ok(updatedJugador);
        } catch (RuntimeException e) {
            // El servicio lanza RuntimeException si jugador o logro no se encuentran
            return ResponseEntity.notFound().build(); 
        } catch (Exception e) {
            // Captura cualquier otra excepción inesperada
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Actualiza (reemplaza) la lista completa de logros de un jugador.
     * Recibe una lista de IDs de logros en el cuerpo de la petición.
     *
     * @param jugadorId El ID del jugador cuyos logros se actualizarán.
     * @param logroIds Una lista de IDs de logros que se asignarán al jugador.
     * @return El objeto {@link Jugador} actualizado con la nueva lista de logros.
     * Ejemplo de uso: PUT /api/v1/jugadores/1/logros (con body: [1, 2, 5])
     */
    @PutMapping("/{jugadorId}/logros") 
    public ResponseEntity<Jugador> updateJugadorLogros(
            @PathVariable Long jugadorId,
            @RequestBody List<Long> logroIds) { 
        try {
            // Llama directamente al método del servicio que maneja la lógica
            Jugador updatedJugador = jugadorService.updateJugadorLogros(jugadorId, logroIds);
            return ResponseEntity.ok(updatedJugador); // 200 OK y el jugador actualizado
        } catch (RuntimeException e) {
            // El servicio lanza RuntimeException si jugador o alguno de los logros no se encuentran
            // Puedes refinar esto para mensajes de error más específicos si la RuntimeException tiene detalles.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); 
            // Si la excepción del servicio contiene un mensaje útil, podrías devolver:
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); 
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Elimina un logro específico de un jugador.
     *
     * @param jugadorId El ID del jugador del que se removerá el logro.
     * @param logroId El ID del logro a remover.
     * @return ResponseEntity con 204 No Content si la eliminación es exitosa, o 404 Not Found si no se encuentra.
     * Ejemplo de uso: DELETE /api/v1/jugadores/1/logros/1
     */
    @DeleteMapping("/{jugadorId}/logros/{logroId}")
    public ResponseEntity<Void> removeLogroFromJugador(@PathVariable Long jugadorId, @PathVariable Long logroId) {
        try {
            // Llama directamente al método del servicio que maneja la lógica
            jugadorService.removeLogroFromJugador(jugadorId, logroId);
            return ResponseEntity.noContent().build(); 
        } catch (RuntimeException e) {
            // El servicio lanza RuntimeException si jugador o logro no se encuentran
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); 
        }
    }
}