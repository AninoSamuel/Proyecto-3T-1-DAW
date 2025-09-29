  package com.stem.Proyecto.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.stem.Proyecto.entity.Equipo;
import com.stem.Proyecto.entity.Partido;
import com.stem.Proyecto.repository.EquipoRepository;
import com.stem.Proyecto.repository.PartidoRepository;

/**
 * Clase de pruebas unitarias para {@link PartidoServiceImpl}.
 * Utiliza Mockito para simular el comportamiento de {@link PartidoRepository} y {@link EquipoRepository}.
 */
@ExtendWith(MockitoExtension.class)
class PartidoServiceImplTest {

    @Mock
    private PartidoRepository partidoRepository;

    @Mock
    private EquipoRepository equipoRepository;

    @InjectMocks
    private PartidoServiceImpl partidoService;

    private Partido partido1;
    private Partido partido2;
    private Equipo equipoLocal1;
    private Equipo equipoVisitante1;
    private Equipo equipoLocal2;
    private Equipo equipoVisitante2;

    @BeforeEach
    void setUp() {
        equipoLocal1 = new Equipo("Real Madrid", "Madrid", "RMA", 1902, "WiZink Center", "Chus Mateo");
        equipoLocal1.setId(10L);

        equipoVisitante1 = new Equipo("FC Barcelona", "Barcelona", "FCB", 1899, "Palau Blaugrana", "Roger Grimau");
        equipoVisitante1.setId(20L);

        equipoLocal2 = new Equipo("Valencia Basket", "Valencia", "VAL", 1986, "Pabellón Fuente de San Luis", "Álex Mumbrú");
        equipoLocal2.setId(30L);

        equipoVisitante2 = new Equipo("Cazoo Baskonia", "Vitoria-Gasteiz", "BAS", 1959, "Fernando Buesa Arena", "Joan Peñarroya");
        equipoVisitante2.setId(40L);

        partido1 = new Partido(
                LocalDate.of(2025, 1, 15),
                LocalTime.of(20, 30),
                85, 78,
                "2024-2025", "WiZink Center",
                equipoLocal1, equipoVisitante1
        );
        partido1.setId(1L);

        partido2 = new Partido(
                LocalDate.of(2025, 1, 20),
                LocalTime.of(18, 0),
                92, 95,
                "2024-2025", "Pabellón Fuente de San Luis",
                equipoLocal2, equipoVisitante2
        );
        partido2.setId(2L);
    }

    @Test
    @DisplayName("Test para obtener todos los partidos")
    void testFindAll() {
        when(partidoRepository.findAll()).thenReturn(Arrays.asList(partido1, partido2));

        List<Partido> partidos = partidoService.findAll();

        assertNotNull(partidos, "La lista de partidos no debería ser nula");
        assertEquals(2, partidos.size(), "Debería devolver 2 partidos");
        assertTrue(partidos.contains(partido1), "La lista debería contener partido1");
        assertTrue(partidos.contains(partido2), "La lista debería contener partido2");
        verify(partidoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test para obtener un partido por ID existente")
    void testFindByIdExisting() {
        when(partidoRepository.findById(1L)).thenReturn(Optional.of(partido1));

        Optional<Partido> foundPartido = partidoService.findById(1L);

        assertTrue(foundPartido.isPresent(), "El partido debería estar presente");
        assertEquals(partido1.getLugar(), foundPartido.get().getLugar(), "El lugar del partido debería coincidir");
        verify(partidoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test para obtener un partido por ID no existente")
    void testFindByIdNotFound() {
        when(partidoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Partido> foundPartido = partidoService.findById(99L);

        assertFalse(foundPartido.isPresent(), "El partido no debería estar presente");
        verify(partidoRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Test para guardar un nuevo partido con equipos existentes")
    void testSaveNewPartidoWithExistingTeams() {
        // Un nuevo partido sin ID
        Partido newPartido = new Partido(
                LocalDate.of(2025, 2, 1),
                LocalTime.of(21, 0),
                70, 65,
                "2024-2025", "Otro Pabellón",
                equipoLocal1, equipoVisitante2 // Usamos equipos existentes
        );

        // Mockear la búsqueda de los equipos
        when(equipoRepository.findById(equipoLocal1.getId())).thenReturn(Optional.of(equipoLocal1));
        when(equipoRepository.findById(equipoVisitante2.getId())).thenReturn(Optional.of(equipoVisitante2));
        // Mockear el guardado del partido
        when(partidoRepository.save(any(Partido.class))).thenReturn(partido1); // Simula que devuelve partido1 con ID asignado

        Partido savedPartido = partidoService.save(newPartido);

        assertNotNull(savedPartido, "El partido guardado no debería ser nulo");
        assertEquals(partido1.getLugar(), savedPartido.getLugar());
        assertEquals(equipoLocal1.getId(), savedPartido.getEquipoLocal().getId(), "El equipo local debería coincidir");
        assertEquals(equipoVisitante2.getId(), savedPartido.getEquipoVisitante().getId(), "El equipo visitante debería coincidir");

        verify(equipoRepository, times(1)).findById(equipoLocal1.getId());
        verify(equipoRepository, times(1)).findById(equipoVisitante2.getId());
        verify(partidoRepository, times(1)).save(any(Partido.class));
    }

    @Test
    @DisplayName("Test para guardar un nuevo partido con equipo local no existente")
    void testSaveNewPartidoWithNonExistingLocalTeam() {
        Equipo nonExistingTeam = new Equipo("Equipo Falso", "Ciudad Falsa", "FFF", 2000, "Estadio Falso", "Entrenador Falso");
        nonExistingTeam.setId(99L); // ID que no existirá

        Partido newPartido = new Partido(
                LocalDate.of(2025, 2, 2),
                LocalTime.of(19, 0),
                80, 75,
                "2024-2025", "Pabellón Dummy",
                nonExistingTeam, equipoVisitante1
        );

        // Mockear que el equipo local no se encuentra
        when(equipoRepository.findById(nonExistingTeam.getId())).thenReturn(Optional.empty());
        // Mockear que el equipo visitante SÍ se encuentra
        when(equipoRepository.findById(equipoVisitante1.getId())).thenReturn(Optional.of(equipoVisitante1));

        // Cuando se guarda, el servicio asignará null al equipo local si no lo encuentra.
        // Simulamos que el repositorio devuelve un partido con el equipo local como null.
        Partido expectedSavedPartido = new Partido(
                LocalDate.of(2025, 2, 2),
                LocalTime.of(19, 0),
                80, 75,
                "2024-2025", "Pabellón Dummy",
                null, equipoVisitante1
        );
        expectedSavedPartido.setId(3L); // Simula un ID asignado
        when(partidoRepository.save(any(Partido.class))).thenReturn(expectedSavedPartido);

        Partido savedPartido = partidoService.save(newPartido);

        assertNotNull(savedPartido);
        assertNull(savedPartido.getEquipoLocal(), "El equipo local debería ser nulo si no se encuentra");
        assertNotNull(savedPartido.getEquipoVisitante(), "El equipo visitante no debería ser nulo");

        verify(equipoRepository, times(1)).findById(nonExistingTeam.getId());
        verify(equipoRepository, times(1)).findById(equipoVisitante1.getId());
        verify(partidoRepository, times(1)).save(any(Partido.class));
    }

    @Test
    @DisplayName("Test para actualizar un partido existente y cambiarle ambos equipos")
    void testUpdatePartidoChangeTeams() {
        when(partidoRepository.findById(1L)).thenReturn(Optional.of(partido1));

        // Nuevos equipos para la actualización
        Equipo nuevoLocal = equipoLocal2; // ID 30L
        Equipo nuevoVisitante = equipoVisitante2; // ID 40L

        Partido updatedData = new Partido(
                LocalDate.of(2025, 1, 15),
                LocalTime.of(22, 0), // Hora actualizada
                88, 80,
                "2024-2025", "WiZink Center",
                nuevoLocal, nuevoVisitante
        );

        // Simular que se encuentran los nuevos equipos
        when(equipoRepository.findById(nuevoLocal.getId())).thenReturn(Optional.of(nuevoLocal));
        when(equipoRepository.findById(nuevoVisitante.getId())).thenReturn(Optional.of(nuevoVisitante));

        // Simular el guardado del partido actualizado
        Partido savedUpdatedPartido = new Partido(
                LocalDate.of(2025, 1, 15),
                LocalTime.of(22, 0),
                88, 80,
                "2024-2025", "WiZink Center",
                nuevoLocal, nuevoVisitante
        );
        savedUpdatedPartido.setId(1L);
        when(partidoRepository.save(any(Partido.class))).thenReturn(savedUpdatedPartido);

        Partido result = partidoService.update(1L, updatedData);

        assertNotNull(result);
        assertEquals(LocalTime.of(22, 0), result.getHora(), "La hora debería haberse actualizado");
        assertEquals(nuevoLocal.getId(), result.getEquipoLocal().getId(), "El equipo local debería haberse actualizado");
        assertEquals(nuevoVisitante.getId(), result.getEquipoVisitante().getId(), "El equipo visitante debería haberse actualizado");

        verify(partidoRepository, times(1)).findById(1L);
        verify(equipoRepository, times(1)).findById(nuevoLocal.getId());
        verify(equipoRepository, times(1)).findById(nuevoVisitante.getId());
        verify(partidoRepository, times(1)).save(any(Partido.class));
    }

    @Test
    @DisplayName("Test para actualizar un partido existente y dejar un equipo como nulo")
    void testUpdatePartidoRemoveTeam() {
        when(partidoRepository.findById(1L)).thenReturn(Optional.of(partido1)); // Partido1 tiene ambos equipos

        Partido updatedData = new Partido(
                LocalDate.of(2025, 1, 15),
                LocalTime.of(20, 30),
                85, 78,
                "2024-2025", "WiZink Center",
                equipoLocal1, null // Quitar equipo visitante
        );

        // Simular el guardado del partido actualizado
        Partido savedUpdatedPartido = new Partido(
                LocalDate.of(2025, 1, 15),
                LocalTime.of(20, 30),
                85, 78,
                "2024-2025", "WiZink Center",
                equipoLocal1, null
        );
        savedUpdatedPartido.setId(1L);
        when(partidoRepository.save(any(Partido.class))).thenReturn(savedUpdatedPartido);

        Partido result = partidoService.update(1L, updatedData);

        assertNotNull(result);
        assertNotNull(result.getEquipoLocal(), "El equipo local no debería ser nulo");
        assertNull(result.getEquipoVisitante(), "El equipo visitante debería ser nulo");

        verify(partidoRepository, times(1)).findById(1L);
        verify(equipoRepository, times(1)).findById(equipoLocal1.getId()); // Se intenta buscar el local
        verify(equipoRepository, never()).findById(eq(equipoVisitante1.getId())); // No se intenta buscar el visitante
        verify(partidoRepository, times(1)).save(any(Partido.class));
    }


    @Test
    @DisplayName("Test para actualizar un partido no existente")
    void testUpdateNonExistingPartido() {
        when(partidoRepository.findById(99L)).thenReturn(Optional.empty());

        Partido updatedData = new Partido(
                LocalDate.of(2025, 3, 1),
                LocalTime.of(10, 0),
                100, 90,
                "2024-2025", "Lugar Falso",
                equipoLocal1, equipoVisitante1
        );

        Partido result = partidoService.update(99L, updatedData);

        assertNull(result, "El resultado debería ser nulo si el partido no existe");
        verify(partidoRepository, times(1)).findById(99L);
        verify(partidoRepository, never()).save(any(Partido.class));
        verify(equipoRepository, never()).findById(anyLong()); // No se deberían buscar equipos si el partido no existe
    }

    @Test
    @DisplayName("Test para eliminar un partido existente por ID")
    void testDeleteByIdExisting() {
        // En tu servicio, deleteById se llama directamente sin una comprobación de existencia.
        // Mockito verificará que el método fue invocado.
        doNothing().when(partidoRepository).deleteById(1L);

        partidoService.deleteById(1L);

        verify(partidoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Test para intentar eliminar un partido no existente por ID")
    void testDeleteByIdNonExisting() {
        // De nuevo, como el servicio llama directamente a deleteById,
        // el test verifica que el intento de eliminación se realiza,
        // incluso si el ID no existe "realmente" en la base de datos mockeada.
        doNothing().when(partidoRepository).deleteById(99L);

        partidoService.deleteById(99L);

        verify(partidoRepository, times(1)).deleteById(99L);
    }
}