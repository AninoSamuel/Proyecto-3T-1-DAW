package com.stem.Proyecto.service.impl;

import java.util.ArrayList; 
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stem.Proyecto.entity.Jugador;
import com.stem.Proyecto.entity.Logro; 
import com.stem.Proyecto.repository.LogroRepository;
import com.stem.Proyecto.service.LogroService;

/**
 * Esta clase maneja las operaciones de negocio para los Logros.
 * Actúa como un puente entre el controlador (que recibe peticiones) y el repositorio (que habla con la base de datos).
 */
@Service
public class LogroServiceImpl implements LogroService {

    private final LogroRepository logroRepository;

    @Autowired
    public LogroServiceImpl(LogroRepository logroRepository) {
        this.logroRepository = logroRepository;
    }

    /**
     * Encuentra y devuelve todos los logros que existen en la base de datos.
     * Este método utiliza una consulta personalizada para asegurar que la relación
     * con los jugadores ({@code jugadoresConEsteLogro}) sea cargada de forma
     * 'eagerly' (ansiosa) para evitar problemas de N+1 o LazyInitializationException
     * al serializar los datos para el frontend.
     *
     * @return Una lista de todos los logros. Si no hay ninguno, la lista estará vacía.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Logro> findAll() {
        // Este método debe llamar a la consulta del repositorio que carga los jugadores
        // (es decir, el método que usa LEFT JOIN FETCH)
        return logroRepository.findAllWithJugadores(); 
    }

    /**
     * Busca un logro específico usando su ID.
     * Si la relación con los jugadores es LAZY (por defecto en ManyToMany),
     * este método asegura que la colección de jugadores sea inicializada
     * dentro de la transacción antes de que la entidad sea devuelta,
     * permitiendo su correcta serialización en el controlador.
     *
     * @param id El número de identificación del logro que se quiere buscar.
     * @return Un 'Optional' que contendrá el logro si lo encuentra, o estará vacío si no existe.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Logro> findById(Long id) {
        Optional<Logro> logroOptional = logroRepository.findById(id);
        
        // Si el logro está presente y la relación 'jugadoresConEsteLogro' es LAZY,
        // esta línea fuerza la carga de la colección desde la base de datos.
        // Es necesaria para que el método getJugadoresJson() de la entidad Logro
        // pueda acceder a los datos reales de los jugadores fuera de esta transacción.
        logroOptional.ifPresent(logro -> {
            logro.getJugadoresConEsteLogro().size(); // Accede a la colección para inicializarla
        });
        
        return logroOptional;
    }

    /**
     * Guarda un logro nuevo en la base de datos o actualiza uno que ya existe.
     *
     * @param logro El logro que se quiere guardar o actualizar.
     * @return El logro que ha sido guardado, con su ID (si es nuevo) o con sus datos actualizados.
     */
    @Override
    @Transactional
    public Logro save(Logro logro) {
        return logroRepository.save(logro);
    }

    /**
     * Actualiza la información de un logro que ya existe.
     *
     * @param id El ID del logro que se va a actualizar.
     * @param logroActualizado El logro con los datos nuevos (nombre, descripción, año).
     * @return El logro con su información ya cambiada, o 'null' si no se encontró un logro con ese ID.
     */
    @Override
    @Transactional
    public Logro update(Long id, Logro logroActualizado) {
        return logroRepository.findById(id)
                .map(logroExistente -> {
                    logroExistente.setNombre(logroActualizado.getNombre());
                    logroExistente.setDescripcion(logroActualizado.getDescripcion());
                    logroExistente.setAnio(logroActualizado.getAnio());
                    // Nota: Si 'logroActualizado' también contiene una lista de jugadores
                    // y quieres actualizar las asociaciones aquí, necesitarías una lógica
                    // más compleja para añadir/eliminar jugadores de la relación.
                    // Por simplicidad, este update solo maneja los campos básicos del logro.
                    return logroRepository.save(logroExistente);
                })
                .orElse(null);
    }

    /**
     * Elimina un logro de la base de datos usando su ID.
     * Antes de eliminar el logro, desvincula todas sus relaciones con los jugadores
     * para evitar errores de integridad de clave foránea en la tabla de unión.
     *
     * @param id El ID del logro que se quiere borrar.
     */
    @Override
    @Transactional // Es crucial para la eliminación de relaciones ManyToMany
    public void deleteById(Long id) {
        Optional<Logro> logroOptional = logroRepository.findById(id);

        if (logroOptional.isPresent()) {
            Logro logroToDelete = logroOptional.get();

            // Desvincular este logro de todos los jugadores asociados.
            // Es CRÍTICO iterar sobre una COPIA de la lista para evitar ConcurrentModificationException
            // mientras se modifica la lista original dentro del bucle.
            if (logroToDelete.getJugadoresConEsteLogro() != null && !logroToDelete.getJugadoresConEsteLogro().isEmpty()) {
                List<Jugador> jugadoresAsociados = new ArrayList<>(logroToDelete.getJugadoresConEsteLogro());

                for (Jugador jugador : jugadoresAsociados) {
                    // Llama al método helper en la entidad Jugador para eliminar la asociación bidireccional.
                    // Esto modificará la colección 'logros' del jugador y, por consiguiente, la tabla de unión.
                    jugador.removeLogro(logroToDelete);

                    // Dado que el método está @Transactional, los cambios en las entidades gestionadas
                    // (como la lista 'logros' del jugador) se persistirán automáticamente
                    // al finalizar la transacción del método.
                }
            }

            logroRepository.delete(logroToDelete); 
        } else {
            System.out.println("Intento de eliminar logro con ID " + id + " que no existe.");
        }
    }
}