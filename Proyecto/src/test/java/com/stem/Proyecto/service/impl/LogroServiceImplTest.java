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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.stem.Proyecto.entity.Logro;
import com.stem.Proyecto.repository.LogroRepository;

/**
 * Clase de pruebas unitarias para {@link LogroServiceImpl}.
 * Utiliza Mockito para simular el comportamiento de {@link LogroRepository}.
 */
@ExtendWith(MockitoExtension.class)
class LogroServiceImplTest {

    @Mock // Crea un mock del repositorio de Logro
    private LogroRepository logroRepository;

    @InjectMocks // Inyecta el mock creado (logroRepository) en esta instancia
    private LogroServiceImpl logroService; // La implementación real del servicio que queremos probar

    private Logro logro1;
    private Logro logro2;

    /**
     * Configuración inicial para cada test.
     * Se ejecuta antes de cada método de prueba.
     */
    @BeforeEach
    void setUp() {
        // Inicializa objetos Logro de prueba
        logro1 = new Logro("MVP Temporada", "Jugador más valioso de la temporada", 2023);
        logro1.setId(1L); // Simula que ya tiene un ID asignado por la BD

        logro2 = new Logro("Campeón de Liga", "Ganador de la liga nacional", 2024);
        logro2.setId(2L); // Simula que ya tiene un ID asignado por la BD
    }

    @Test
    @DisplayName("Test para obtener todos los logros")
    void testFindAll() {
        // Configura el comportamiento del mock:
        // Cuando se llame a logroRepository.findAll(), devolverá una lista de logro1 y logro2
        when(logroRepository.findAll()).thenReturn(Arrays.asList(logro1, logro2));

        // Llama al método del servicio que estamos probando
        List<Logro> logros = logroService.findAll();

        // Afirmaciones para verificar el resultado
        assertNotNull(logros, "La lista de logros no debería ser nula");
        assertEquals(2, logros.size(), "Debería devolver 2 logros");
        assertTrue(logros.contains(logro1), "La lista debería contener logro1");
        assertTrue(logros.contains(logro2), "La lista debería contener logro2");

        // Verifica que el método findAll() del repositorio fue llamado exactamente una vez
        verify(logroRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test para obtener un logro por ID existente")
    void testFindByIdExisting() {
        // Configura el mock para devolver un Optional con logro1 cuando se busca por ID 1L
        when(logroRepository.findById(1L)).thenReturn(Optional.of(logro1));

        // Llama al método del servicio
        Optional<Logro> foundLogro = logroService.findById(1L);

        // Afirmaciones
        assertTrue(foundLogro.isPresent(), "El logro debería estar presente");
        assertEquals(logro1.getNombre(), foundLogro.get().getNombre(), "El nombre del logro debería coincidir");

        // Verifica que findById() del repositorio fue llamado con el ID correcto
        verify(logroRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test para obtener un logro por ID no existente")
    void testFindByIdNotFound() {
        // Configura el mock para devolver un Optional vacío cuando se busca por un ID no existente
        when(logroRepository.findById(99L)).thenReturn(Optional.empty());

        // Llama al método del servicio
        Optional<Logro> foundLogro = logroService.findById(99L);

        // Afirmaciones
        assertFalse(foundLogro.isPresent(), "El logro no debería estar presente");

        // Verifica la llamada al repositorio
        verify(logroRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Test para guardar un nuevo logro")
    void testSave() {
        // Crea un nuevo logro (sin ID, como si fuera nuevo)
        Logro newLogro = new Logro("Mejor Anotador", "Líder en puntos de la temporada", 2025);

        // Configura el mock para devolver el mismo logro que se pasa al save,
        // simulando el comportamiento de un repositorio que guarda y devuelve la entidad con ID.
        when(logroRepository.save(any(Logro.class))).thenReturn(logro1); // Usamos logro1 para simular el retorno con ID

        // Llama al método del servicio
        Logro savedLogro = logroService.save(newLogro);

        // Afirmaciones
        assertNotNull(savedLogro, "El logro guardado no debería ser nulo");
        assertEquals(logro1.getNombre(), savedLogro.getNombre(), "El nombre debería coincidir");
        assertNotNull(savedLogro.getId(), "El logro guardado debería tener un ID asignado"); // Asumiendo que el ID se asigna al guardar

        // Verifica que el método save() del repositorio fue llamado exactamente una vez con cualquier objeto Logro
        verify(logroRepository, times(1)).save(any(Logro.class));
    }

    @Test
    @DisplayName("Test para actualizar un logro existente")
    void testUpdateExisting() {
        // Simula la existencia del logro antes de la actualización
        when(logroRepository.findById(1L)).thenReturn(Optional.of(logro1));

        // Configura el mock para devolver el logro actualizado
        Logro updatedInfo = new Logro("MVP Finales", "Jugador más valioso de las Finales", 2023);
        updatedInfo.setId(1L); // Asegura que el ID coincida para la actualización
        when(logroRepository.save(any(Logro.class))).thenReturn(updatedInfo); // Simula el guardado/actualización

        // Llama al método del servicio
        Logro result = logroService.update(1L, updatedInfo);

        // Afirmaciones
        assertNotNull(result, "El logro actualizado no debería ser nulo");
        assertEquals("MVP Finales", result.getNombre(), "El nombre del logro debería haberse actualizado");
        assertEquals("Jugador más valioso de las Finales", result.getDescripcion(), "La descripción debería haberse actualizado");
        assertEquals(2023, result.getAnio(), "El año debería haberse mantenido");

        // Verifica que findById fue llamado para verificar la existencia
        verify(logroRepository, times(1)).findById(1L);
        // Verifica que save fue llamado para persistir los cambios
        verify(logroRepository, times(1)).save(any(Logro.class));
    }

    @Test
    @DisplayName("Test para actualizar un logro no existente")
    void testUpdateNonExisting() {
        // Simula que el logro no se encuentra
        when(logroRepository.findById(99L)).thenReturn(Optional.empty());

        Logro updatedInfo = new Logro("Logro Falso", "Descripción Falsa", 2000);
        // No le asignamos ID ya que el método update lo haría si existiera

        // Llama al método del servicio
        Logro result = logroService.update(99L, updatedInfo);

        // Afirmaciones
        assertNull(result, "El resultado debería ser nulo si el logro no existe");

        // Verifica que findById fue llamado, pero save no
        verify(logroRepository, times(1)).findById(99L);
        verify(logroRepository, never()).save(any(Logro.class));
    }

    @Test
    @DisplayName("Test para eliminar un logro existente por ID")
    void testDeleteByIdExisting() {
        // En tu servicio, deleteById se llama directamente sin una comprobación de existencia.
        // Mockito verificará que el método fue invocado.
        doNothing().when(logroRepository).deleteById(1L);

        // Llama al método del servicio
        logroService.deleteById(1L);

        // Afirmaciones (en este caso, verificar las interacciones con el mock)
        // Verifica que deleteById() del repositorio fue llamado exactamente una vez con el ID correcto
        verify(logroRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Test para intentar eliminar un logro no existente por ID")
    void testDeleteByIdNonExisting() {
        // De nuevo, como el servicio llama directamente a deleteById,
        // el test verifica que el intento de eliminación se realiza,
        // incluso si el ID no existe "realmente" en la base de datos mockeada.
        doNothing().when(logroRepository).deleteById(99L);

        logroService.deleteById(99L);

        verify(logroRepository, times(1)).deleteById(99L);
    }
}