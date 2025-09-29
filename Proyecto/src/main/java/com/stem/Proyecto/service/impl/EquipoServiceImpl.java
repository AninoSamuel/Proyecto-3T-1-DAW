package com.stem.Proyecto.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stem.Proyecto.entity.Equipo;
import com.stem.Proyecto.repository.EquipoRepository;
import com.stem.Proyecto.service.EquipoService;

/**
 * Esta clase maneja las operaciones de negocio para los Equipos.
 * Actúa como un puente entre el controlador (que recibe peticiones) y el repositorio (que habla con la base de datos).
 */
@Service
public class EquipoServiceImpl implements EquipoService {

    private final EquipoRepository equipoRepository;

    /**
     * Constructor que Spring usa para inyectar el repositorio de equipos.
     *
     * @param equipoRepository El objeto que permite guardar y buscar equipos en la base de datos.
     */
    @Autowired
    public EquipoServiceImpl(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }

    /**
     * Encuentra y devuelve todos los equipos que existen en la base de datos.
     *
     * @return Una lista de todos los equipos. Si no hay ninguno, la lista estará vacía.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Equipo> findAll() {
        return equipoRepository.findAll();
    }

    /**
     * Busca un equipo específico usando su ID.
     *
     * @param id El número de identificación del equipo que se quiere buscar.
     * @return Un 'Optional' que contendrá el equipo si lo encuentra, o estará vacío si no existe.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Equipo> findById(Long id) {
        return equipoRepository.findById(id);
    }

    /**
     * Guarda un equipo nuevo en la base de datos o actualiza uno que ya existe.
     *
     * @param equipo El equipo que se quiere guardar o actualizar.
     * @return El equipo que ha sido guardado, con su ID (si es nuevo) o con sus datos actualizados.
     */
    @Override
    @Transactional
    public Equipo save(Equipo equipo) {
        return equipoRepository.save(equipo);
    }

    /**
     * Actualiza la información de un equipo que ya existe.
     *
     * @param id El ID del equipo que se va a actualizar.
     * @param equipoActualizado El equipo con los datos nuevos (nombre, ciudad, abreviatura, año de fundación, pabellón, entrenador).
     * @return El equipo con su información ya cambiada, o 'null' si no se encontró un equipo con ese ID.
     */
    @Override
    @Transactional
    public Equipo update(Long id, Equipo equipoActualizado) {
        return equipoRepository.findById(id)
                .map(equipoExistente -> {
                    equipoExistente.setNombre(equipoActualizado.getNombre());
                    equipoExistente.setCiudad(equipoActualizado.getCiudad());
                    equipoExistente.setAbreviatura(equipoActualizado.getAbreviatura());
                    equipoExistente.setAnioFundacion(equipoActualizado.getAnioFundacion());
                    equipoExistente.setPabellon(equipoActualizado.getPabellon());
                    equipoExistente.setEntrenador(equipoActualizado.getEntrenador());
                    return equipoRepository.save(equipoExistente);
                })
                .orElse(null);
    }

    /**
     * Elimina un equipo de la base de datos usando su ID.
     *
     * @param id El ID del equipo que se quiere borrar.
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        equipoRepository.deleteById(id);
    }
}