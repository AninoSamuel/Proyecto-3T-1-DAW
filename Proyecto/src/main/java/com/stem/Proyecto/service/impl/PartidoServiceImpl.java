package com.stem.Proyecto.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stem.Proyecto.entity.Partido;
import com.stem.Proyecto.repository.EquipoRepository;
import com.stem.Proyecto.repository.PartidoRepository;
import com.stem.Proyecto.service.PartidoService;

/**
 * Esta clase maneja las operaciones de negocio para los Partidos.
 * Se encarga de gestionar la información de los partidos, incluyendo los equipos que participan.
 */
@Service
public class PartidoServiceImpl implements PartidoService {

    private final PartidoRepository partidoRepository;
    private final EquipoRepository equipoRepository;

    /**
     * Constructor que Spring usa para inyectar los repositorios necesarios.
     *
     * @param partidoRepository Objeto para guardar y buscar partidos.
     * @param equipoRepository Objeto para buscar equipos y asignarlos a los partidos (local y visitante).
     */
    @Autowired
    public PartidoServiceImpl(PartidoRepository partidoRepository, EquipoRepository equipoRepository) {
        this.partidoRepository = partidoRepository;
        this.equipoRepository = equipoRepository;
    }

    /**
     * Encuentra y devuelve todos los partidos que existen en la base de datos.
     *
     * @return Una lista de todos los partidos. Si no hay ninguno, la lista estará vacía.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Partido> findAll() {
        return partidoRepository.findAll();
    }

    /**
     * Busca un partido específico usando su ID.
     *
     * @param id El número de identificación del partido que se quiere buscar.
     * @return Un 'Optional' que contendrá el partido si lo encuentra, o estará vacío si no existe.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Partido> findById(Long id) {
        return partidoRepository.findById(id);
    }

    /**
     * Guarda un partido nuevo en la base de datos o actualiza uno que ya existe.
     * Se asegura de que los equipos local y visitante existan antes de vincularlos al partido.
     *
     * @param partido El partido que se quiere guardar o actualizar.
     * @return El partido que ha sido guardado, con su ID (si es nuevo) o con sus datos actualizados.
     */
    @Override
    @Transactional
    public Partido save(Partido partido) {
        // Si el partido tiene un equipo local con ID, busca el equipo y lo asigna
        if (partido.getEquipoLocal() != null && partido.getEquipoLocal().getId() != null) {
            equipoRepository.findById(partido.getEquipoLocal().getId())
                    .ifPresent(partido::setEquipoLocal);
        }
        // Si el partido tiene un equipo visitante con ID, busca el equipo y lo asigna
        if (partido.getEquipoVisitante() != null && partido.getEquipoVisitante().getId() != null) {
            equipoRepository.findById(partido.getEquipoVisitante().getId())
                    .ifPresent(partido::setEquipoVisitante);
        }
        return partidoRepository.save(partido);
    }

    /**
     * Actualiza la información de un partido que ya existe.
     * Permite cambiar la fecha, hora, puntuaciones, temporada, lugar, y también los equipos local y visitante.
     *
     * @param id El ID del partido que se va a actualizar.
     * @param partidoActualizado El partido con los datos nuevos.
     * @return El partido con su información ya cambiada, o 'null' si no se encontró un partido con ese ID.
     */
    @Override
    @Transactional
    public Partido update(Long id, Partido partidoActualizado) {
        return partidoRepository.findById(id)
                .map(partidoExistente -> {
                    // Actualiza los datos básicos del partido
                    partidoExistente.setFecha(partidoActualizado.getFecha());
                    partidoExistente.setHora(partidoActualizado.getHora());
                    partidoExistente.setPuntuacionLocal(partidoActualizado.getPuntuacionLocal());
                    partidoExistente.setPuntuacionVisitante(partidoActualizado.getPuntuacionVisitante());
                    partidoExistente.setTemporada(partidoActualizado.getTemporada());
                    partidoExistente.setLugar(partidoActualizado.getLugar());

                    // Actualiza el equipo local del partido
                    if (partidoActualizado.getEquipoLocal() != null && partidoActualizado.getEquipoLocal().getId() != null) {
                        equipoRepository.findById(partidoActualizado.getEquipoLocal().getId())
                                .ifPresent(partidoExistente::setEquipoLocal);
                    } else { // Si el equipo local actualizado es nulo, desasocia el existente
                        partidoExistente.setEquipoLocal(null);
                    }

                    // Actualiza el equipo visitante del partido
                    if (partidoActualizado.getEquipoVisitante() != null && partidoActualizado.getEquipoVisitante().getId() != null) {
                        equipoRepository.findById(partidoActualizado.getEquipoVisitante().getId())
                                .ifPresent(partidoExistente::setEquipoVisitante);
                    } else { // Si el equipo visitante actualizado es nulo, desasocia el existente
                        partidoExistente.setEquipoVisitante(null);
                    }
                    return partidoRepository.save(partidoExistente);
                })
                .orElse(null);
    }

    /**
     * Elimina un partido de la base de datos usando su ID.
     *
     * @param id El ID del partido que se quiere borrar.
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        partidoRepository.deleteById(id);
    }
}