package com.stem.Proyecto.service.impl;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.stem.Proyecto.entity.Equipo;
import com.stem.Proyecto.repository.EquipoRepository;

/**
 * Clase de pruebas unitarias para {@link EquipoServiceImpl}.
 * Utiliza Mockito para simular el comportamiento de {@link EquipoRepository}.
 */
@ExtendWith(MockitoExtension.class) // Habilita la integración de Mockito con JUnit 5
class EquipoServiceImplTest {

    @Mock // Crea un mock del repositorio de Equipo
    private EquipoRepository equipoRepository;

    @InjectMocks // Inyecta los mocks creados (en este caso, equipoRepository) en esta instancia
    private EquipoServiceImpl equipoService; // La implementación real del servicio que queremos probar

    private Equipo equipo1;
    private Equipo equipo2;

    /**
     * Configuración inicial para cada test.
     * Se ejecuta antes de cada método de prueba.
     */
    @BeforeEach
    void setUp() {
        // Inicializa objetos Equipo de prueba
        equipo1 = new Equipo("Real Madrid", "Madrid", "RMA", 1902, "WiZink Center", "Chus Mateo");
        equipo1.setId(1L); // Simula que ya tiene un ID asignado por la BD

        equipo2 = new Equipo("FC Barcelona", "Barcelona", "FCB", 1899, "Palau Blaugrana", "Roger Grimau");
        equipo2.setId(2L); // Simula que ya tiene un ID asignado por la BD
    }

    @Test
    @DisplayName("Test para obtener todos los equipos")
    void testFindAll() {
        // Configura el comportamiento del mock:
        // Cuando se llame a equipoRepository.findAll(), devolverá una lista de equipo1 y equipo2
        when(equipoRepository.findAll()).thenReturn(Arrays.asList(equipo1, equipo2));

        // Llama al método del servicio que estamos probando
        List<Equipo> equipos = equipoService.findAll();

        // Afirmaciones para verificar el resultado
        assertNotNull(equipos, "La lista de equipos no debería ser nula");
        assertEquals(2, equipos.size(), "Debería devolver 2 equipos");
        assertTrue(equipos.contains(equipo1), "La lista debería contener equipo1");
        assertTrue(equipos.contains(equipo2), "La lista debería contener equipo2");

        // Verifica que el método findAll() del repositorio fue llamado exactamente una vez
        verify(equipoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test para obtener un equipo por ID existente")
    void testFindByIdExisting() {
        // Configura el mock para devolver un Optional con equipo1 cuando se busca por ID 1L
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipo1));

        // Llama al método del servicio
        Optional<Equipo> foundEquipo = equipoService.findById(1L);

        // Afirmaciones
        assertTrue(foundEquipo.isPresent(), "El equipo debería estar presente");
        assertEquals(equipo1.getNombre(), foundEquipo.get().getNombre(), "El nombre del equipo debería coincidir");

        // Verifica que findById() del repositorio fue llamado con el ID correcto
        verify(equipoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test para obtener un equipo por ID no existente")
    void testFindByIdNotFound() {
        // Configura el mock para devolver un Optional vacío cuando se busca por un ID no existente
        when(equipoRepository.findById(99L)).thenReturn(Optional.empty());

        // Llama al método del servicio
        Optional<Equipo> foundEquipo = equipoService.findById(99L);

        // Afirmaciones
        assertFalse(foundEquipo.isPresent(), "El equipo no debería estar presente");

        // Verifica la llamada al repositorio
        verify(equipoRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Test para guardar un nuevo equipo")
    void testSave() {
        // Configura el mock para devolver el mismo equipo que se pasa al save,
        // simulando el comportamiento de un repositorio que guarda y devuelve la entidad.
        when(equipoRepository.save(any(Equipo.class))).thenReturn(equipo1);

        // Crea un nuevo equipo (sin ID, como si fuera nuevo)
        Equipo newEquipo = new Equipo("Real Madrid", "Madrid", "RMA", 1902, "WiZink Center", "Chus Mateo");

        // Llama al método del servicio
        Equipo savedEquipo = equipoService.save(newEquipo);

        // Afirmaciones
        assertNotNull(savedEquipo, "El equipo guardado no debería ser nulo");
        assertEquals(equipo1.getNombre(), savedEquipo.getNombre(), "El nombre debería coincidir");
        assertNotNull(savedEquipo.getId(), "El equipo guardado debería tener un ID asignado"); // Asumiendo que el ID se asigna al guardar

        // Verifica que el método save() del repositorio fue llamado exactamente una vez con cualquier objeto Equipo
        verify(equipoRepository, times(1)).save(any(Equipo.class));
    }

    @Test
    @DisplayName("Test para actualizar un equipo existente")
    void testUpdateExisting() {
        // Simula la existencia del equipo antes de la actualización
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipo1));

        // Configura el mock para devolver el equipo actualizado
        Equipo updatedInfo = new Equipo("Real Madrid C.F.", "Madrid", "RMC", 1902, "Estadio Santiago Bernabéu", "Carlo Ancelotti");
        updatedInfo.setId(1L); // Asegura que el ID coincida para la actualización
        when(equipoRepository.save(any(Equipo.class))).thenReturn(updatedInfo); // Simula el guardado/actualización

        // Llama al método del servicio
        Equipo result = equipoService.update(1L, updatedInfo);

        // Afirmaciones
        assertNotNull(result, "El equipo actualizado no debería ser nulo");
        assertEquals("Real Madrid C.F.", result.getNombre(), "El nombre del equipo debería haberse actualizado");
        assertEquals("Estadio Santiago Bernabéu", result.getPabellon(), "El pabellón debería haberse actualizado");

        // Verifica que findById fue llamado para verificar la existencia
        verify(equipoRepository, times(1)).findById(1L);
        // Verifica que save fue llamado para persistir los cambios
        verify(equipoRepository, times(1)).save(any(Equipo.class));
    }

    @Test
    @DisplayName("Test para actualizar un equipo no existente")
    void testUpdateNonExisting() {
        // Simula que el equipo no se encuentra
        when(equipoRepository.findById(99L)).thenReturn(Optional.empty());

        Equipo updatedInfo = new Equipo("Equipo Falso", "Ciudad Falsa", "FFF", 2000, "Estadio Falso", "Entrenador Falso");
        // No le asignamos ID ya que el método update lo haría si existiera

        // Llama al método del servicio
        Equipo result = equipoService.update(99L, updatedInfo);

        // Afirmaciones
        assertNull(result, "El resultado debería ser nulo si el equipo no existe");

        // Verifica que findById fue llamado, pero save no
        verify(equipoRepository, times(1)).findById(99L);
        verify(equipoRepository, never()).save(any(Equipo.class));
    }

    @Test
    @DisplayName("Test para eliminar un equipo existente por ID")
    void testDeleteByIdExisting() {
        // Configura el mock para simular que el equipo existe antes de eliminarlo
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipo1));
        // No necesitamos configurar un thenReturn para deleteById ya que es un método void

        // Llama al método del servicio
        equipoService.deleteById(1L);

        // Afirmaciones (en este caso, verificar las interacciones con el mock)
        // Verifica que findById() fue llamado para confirmar la existencia
        verify(equipoRepository, times(1)).findById(1L);
        // Verifica que deleteById() del repositorio fue llamado exactamente una vez con el ID correcto
        verify(equipoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Test para intentar eliminar un equipo no existente por ID")
    void testDeleteByIdNonExisting() {
        // Configura el mock para simular que el equipo no existe
        when(equipoRepository.findById(99L)).thenReturn(Optional.empty());

        // Llama al método del servicio
        equipoService.deleteById(99L);

        // Afirmaciones
        // Verifica que findById() fue llamado, pero deleteById() NO fue llamado
        verify(equipoRepository, times(1)).findById(99L);
        verify(equipoRepository, never()).deleteById(anyLong()); // Asegura que deleteById no fue llamado
    }
}