package com.stem.Proyecto.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;   // <-- ¡AÑADIR ESTA IMPORTACIÓN!
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;     // <-- ¡AÑADIR ESTA IMPORTACIÓN!
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach; // <-- ¡AÑADIR ESTA IMPORTACIÓN para probar excepciones!
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.stem.Proyecto.entity.Equipo;
import com.stem.Proyecto.entity.Jugador;
import com.stem.Proyecto.entity.Logro;
import com.stem.Proyecto.repository.EquipoRepository;
import com.stem.Proyecto.repository.JugadorRepository;
import com.stem.Proyecto.repository.LogroRepository;

/**
 * Clase de pruebas unitarias para {@link JugadorServiceImpl}.
 * Utiliza Mockito para simular el comportamiento de {@link JugadorRepository},
 * {@link EquipoRepository} y {@link LogroRepository}.
 */
@ExtendWith(MockitoExtension.class)
class JugadorServiceImplTest {

    @Mock
    private JugadorRepository jugadorRepository;

    @Mock
    private EquipoRepository equipoRepository;

    @Mock
    private LogroRepository logroRepository;

    @InjectMocks
    private JugadorServiceImpl jugadorService;

    private Jugador jugador1;
    private Jugador jugador2;
    private Equipo equipo1;
    private Logro logro1;
    private Logro logro2;
    private Logro logro3; // Para pruebas de actualización

    @BeforeEach
    void setUp() {
        equipo1 = new Equipo("Real Madrid", "Madrid", "RMA", 1902, "WiZink Center", "Chus Mateo");
        equipo1.setId(1L);

        logro1 = new Logro("MVP Temporada", "Jugador más valioso de la temporada", 2023);
        logro1.setId(10L);
        // ¡CAMBIO CLAVE AQUÍ! Ahora es un Set.
        logro1.setJugadoresConEsteLogro(new HashSet<>()); // Inicializa el Set en el mock

        logro2 = new Logro("Campeón de Liga", "Ganador de la liga nacional", 2023);
        logro2.setId(11L);
        // ¡CAMBIO CLAVE AQUÍ! Ahora es un Set.
        logro2.setJugadoresConEsteLogro(new HashSet<>()); // Inicializa el Set en el mock

        logro3 = new Logro("Máximo Anotador", "Líder de anotación de la competición", 2024);
        logro3.setId(12L);
        logro3.setJugadoresConEsteLogro(new HashSet<>());


        jugador1 = new Jugador("Luka", "Doncic", LocalDate.of(1999, 2, 28), 201, 104, "Base", 77, true);
        jugador1.setId(1L);
        jugador1.setEquipo(equipo1);
        // ¡CAMBIO CLAVE AQUÍ! Ahora es un Set.
        jugador1.setLogros(new HashSet<>()); // Inicializa el Set de logros del jugador
        jugador1.addLogro(logro1); // Añadir un logro inicial para algunas pruebas
        // Asegúrate de que el logro también tenga al jugador si estás probando la bidireccionalidad de forma estricta.
        logro1.addJugador(jugador1); // Asegurar bidireccionalidad para el mock

        jugador2 = new Jugador("LeBron", "James", LocalDate.of(1984, 12, 30), 206, 113, "Alero", 23, true);
        jugador2.setId(2L);
        jugador2.setEquipo(null);
        // ¡CAMBIO CLAVE AQUÍ! Ahora es un Set.
        jugador2.setLogros(new HashSet<>());
    }

    @Test
    @DisplayName("Test para obtener todos los jugadores")
    void testFindAll() {
        when(jugadorRepository.findAll()).thenReturn(Arrays.asList(jugador1, jugador2));

        List<Jugador> jugadores = jugadorService.findAll();

        assertNotNull(jugadores);
        assertEquals(2, jugadores.size());
        assertTrue(jugadores.contains(jugador1));
        assertTrue(jugadores.contains(jugador2));
        verify(jugadorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test para obtener un jugador por ID existente")
    void testFindByIdExisting() {
        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador1));

        Optional<Jugador> foundJugador = jugadorService.findById(1L);

        assertTrue(foundJugador.isPresent());
        assertEquals(jugador1.getNombre(), foundJugador.get().getNombre());
        verify(jugadorRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test para obtener un jugador por ID no existente")
    void testFindByIdNotFound() {
        when(jugadorRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Jugador> foundJugador = jugadorService.findById(99L);

        assertFalse(foundJugador.isPresent());
        verify(jugadorRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Test para guardar un nuevo jugador con equipo existente")
    void testSaveNewJugadorWithExistingEquipo() {
        Jugador newJugador = new Jugador("Facundo", "Campazzo", LocalDate.of(1991, 3, 23), 178, 88, "Base", 3, true);
        newJugador.setEquipo(equipo1); // Intenta asociarlo con equipo1
        newJugador.setLogros(new HashSet<>()); // Asegura que los logros estén inicializados como Set

        when(equipoRepository.findById(equipo1.getId())).thenReturn(Optional.of(equipo1));
        // Mockear que save devuelve el newJugador con un ID simulado si es un jugador nuevo
        when(jugadorRepository.save(any(Jugador.class))).thenAnswer(invocation -> {
            Jugador j = invocation.getArgument(0);
            if (j.getId() == null) {
                j.setId(3L); // Simular asignación de ID para un nuevo jugador
            }
            return j;
        });

        Jugador savedJugador = jugadorService.save(newJugador);

        assertNotNull(savedJugador);
        assertEquals(newJugador.getNombre(), savedJugador.getNombre()); // Comparar con newJugador, no jugador1
        assertEquals(equipo1.getId(), savedJugador.getEquipo().getId(), "El jugador debería tener el equipo asignado");
        verify(equipoRepository, times(1)).findById(equipo1.getId());
        verify(jugadorRepository, times(1)).save(any(Jugador.class));
    }

    @Test
    @DisplayName("Test para guardar un nuevo jugador sin equipo")
    void testSaveNewJugadorWithoutEquipo() {
        Jugador newJugador = new Jugador("Nikola", "Jokic", LocalDate.of(1995, 2, 19), 211, 129, "Pívot", 15, true);
        newJugador.setEquipo(null);
        newJugador.setLogros(new HashSet<>()); // Asegura que los logros estén inicializados como Set

        when(jugadorRepository.save(any(Jugador.class))).thenAnswer(invocation -> {
            Jugador j = invocation.getArgument(0);
            if (j.getId() == null) {
                j.setId(4L);
            }
            return j;
        });

        Jugador savedJugador = jugadorService.save(newJugador);

        assertNotNull(savedJugador);
        assertEquals(newJugador.getNombre(), savedJugador.getNombre());
        assertNull(savedJugador.getEquipo(), "El jugador no debería tener equipo asignado");
        verify(equipoRepository, never()).findById(anyLong());
        verify(jugadorRepository, times(1)).save(any(Jugador.class));
    }

    @Test
    @DisplayName("Test para actualizar un jugador existente y cambiarle el equipo")
    void testUpdateJugadorChangeEquipo() {
        // Asegúrate de que jugador1 original tenga sus logros inicializados como Set
        jugador1.setLogros(new HashSet<>(Arrays.asList(logro1)));
        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador1));

        Equipo nuevoEquipo = new Equipo("Los Angeles Lakers", "Los Angeles", "LAL", 1947, "Crypto.com Arena", "Darvin Ham");
        nuevoEquipo.setId(3L);

        Jugador jugadorUpdatedData = new Jugador("Luka", "Doncic", LocalDate.of(1999, 2, 28), 201, 104, "Base", 77, true);
        jugadorUpdatedData.setEquipo(nuevoEquipo); // El objeto entrante con el nuevo equipo
        jugadorUpdatedData.setLogros(new HashSet<>(jugador1.getLogros())); // Mantener los logros existentes para el update

        // Simula la asignación del nuevo equipo al jugador existente
        // Para simplificar, hacemos que el save devuelva el jugador1 con el nuevo equipo ya asignado
        Jugador jugador1WithNewTeam = new Jugador("Luka", "Doncic", LocalDate.of(1999, 2, 28), 201, 104, "Base", 77, true);
        jugador1WithNewTeam.setId(1L);
        jugador1WithNewTeam.setEquipo(nuevoEquipo); // Asignado en la lógica del mock
        jugador1WithNewTeam.setLogros(new HashSet<>(jugador1.getLogros())); // Mantener logros existentes

        when(equipoRepository.findById(nuevoEquipo.getId())).thenReturn(Optional.of(nuevoEquipo));
        when(jugadorRepository.save(any(Jugador.class))).thenReturn(jugador1WithNewTeam);

        Jugador result = jugadorService.update(1L, jugadorUpdatedData);

        assertNotNull(result);
        assertEquals(nuevoEquipo.getId(), result.getEquipo().getId(), "El equipo debería haberse actualizado");
        assertEquals(jugador1.getLogros().size(), result.getLogros().size(), "Los logros no deberían cambiar en el update básico"); // Verificar que los logros se mantienen
        verify(jugadorRepository, times(1)).findById(1L);
        verify(equipoRepository, times(1)).findById(nuevoEquipo.getId());
        verify(jugadorRepository, times(1)).save(any(Jugador.class));
    }

    @Test
    @DisplayName("Test para actualizar un jugador existente y desasociarlo de su equipo")
    void testUpdateJugadorRemoveEquipo() {
        // Asegúrate de que jugador1 original tenga sus logros inicializados como Set
        jugador1.setLogros(new HashSet<>(Arrays.asList(logro1)));
        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador1)); // jugador1 tiene equipo1

        Jugador jugadorUpdatedData = new Jugador("Luka", "Doncic", LocalDate.of(1999, 2, 28), 201, 104, "Base", 77, true);
        jugadorUpdatedData.setEquipo(null); // Se le quita el equipo
        jugadorUpdatedData.setLogros(new HashSet<>(jugador1.getLogros())); // Mantener los logros existentes para el update


        // Simula que el save devuelve el jugador sin equipo
        Jugador jugador1WithoutTeam = new Jugador("Luka", "Doncic", LocalDate.of(1999, 2, 28), 201, 104, "Base", 77, true);
        jugador1WithoutTeam.setId(1L);
        jugador1WithoutTeam.setEquipo(null); // Sin equipo
        jugador1WithoutTeam.setLogros(new HashSet<>(jugador1.getLogros()));

        when(jugadorRepository.save(any(Jugador.class))).thenReturn(jugador1WithoutTeam);

        Jugador result = jugadorService.update(1L, jugadorUpdatedData);

        assertNotNull(result);
        assertNull(result.getEquipo(), "El equipo del jugador debería ser nulo");
        verify(jugadorRepository, times(1)).findById(1L);
        verify(equipoRepository, never()).findById(anyLong());
        verify(jugadorRepository, times(1)).save(any(Jugador.class));
    }

    @Test
    @DisplayName("Test para actualizar un jugador no existente")
    void testUpdateNonExistingJugador() {
        when(jugadorRepository.findById(99L)).thenReturn(Optional.empty());

        Jugador jugadorUpdated = new Jugador("Jugador Falso", "Apellido Falso", LocalDate.now(), 180, 80, "Base", 10, false);
        jugadorUpdated.setLogros(new HashSet<>()); // Asegura que los logros estén inicializados como Set

        Jugador result = jugadorService.update(99L, jugadorUpdated);

        assertNull(result, "El resultado debería ser nulo si el jugador no existe");
        verify(jugadorRepository, times(1)).findById(99L);
        verify(jugadorRepository, never()).save(any(Jugador.class));
        verify(equipoRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("Test para eliminar un jugador existente por ID")
    void testDeleteByIdExisting() {
        // En tu servicio, solo llamas a deleteById directamente, sin existsById.
        // Mockito verificará que se llamó.
        doNothing().when(jugadorRepository).deleteById(1L); // Opcional, pero explícito para void methods

        jugadorService.deleteById(1L);

        // Verifica que deleteById fue llamado con el ID correcto
        verify(jugadorRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Test para intentar eliminar un jugador no existente por ID")
    void testDeleteByIdNonExisting() {
        // Para simular que no existe sin un existsById, simplemente no mockeamos findById
        // y verificamos que deleteById no se llama si no hay una pre-condición de "existencia" en el servicio.
        // En tu servicio actual, deleteById se llama directamente sin una comprobación previa.
        // Por lo tanto, este test verificaría que deleteById *siempre* se llama si no hay Optional o existsById.
        // Si tu repository.deleteById lanza una excepción para no existentes, ese es un test de integración.
        // Para un test unitario aquí, simplemente verificamos que se intenta la eliminación.
        doNothing().when(jugadorRepository).deleteById(99L);

        jugadorService.deleteById(99L);

        verify(jugadorRepository, times(1)).deleteById(99L);
    }


    // --- Tests para los nuevos métodos de gestión de Logros ---

    @Test
    @DisplayName("Test para añadir un logro a un jugador existente")
    void testAddLogroToJugadorExisting() {
        // Asegúrate de que jugador1 tenga una copia modificable de logros para la simulación
        Jugador jugadorParaMock = new Jugador(jugador1.getNombre(), jugador1.getApellido(), jugador1.getFechaNacimiento(), jugador1.getAlturaCm(), jugador1.getPesoKg(), jugador1.getPosicion(), jugador1.getNumeroCamiseta(), jugador1.getActivo());
        jugadorParaMock.setId(jugador1.getId());
        jugadorParaMock.setEquipo(jugador1.getEquipo());
        jugadorParaMock.setLogros(new HashSet<>(jugador1.getLogros())); // Copia modificable
        
        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugadorParaMock));
        when(logroRepository.findById(logro2.getId())).thenReturn(Optional.of(logro2));

        // Simular el guardado: el jugador devuelto por save tendrá el nuevo logro
        when(jugadorRepository.save(any(Jugador.class))).thenAnswer(invocation -> {
            Jugador savedJ = invocation.getArgument(0);
            return savedJ; // Devuelve el jugador modificado tal como lo guarda el servicio
        });

        Jugador result = jugadorService.addLogroToJugador(1L, logro2.getId());

        assertNotNull(result, "El jugador no debería ser nulo");
        assertEquals(2, result.getLogros().size(), "El jugador debería tener 2 logros");
        assertTrue(result.getLogros().contains(logro1), "Debería contener el logro original");
        assertTrue(result.getLogros().contains(logro2), "Debería contener el logro añadido");

        verify(jugadorRepository, times(1)).findById(1L);
        verify(logroRepository, times(1)).findById(logro2.getId());
        verify(jugadorRepository, times(1)).save(any(Jugador.class));
    }

    @Test
    @DisplayName("Test para añadir un logro a un jugador no existente debería lanzar RuntimeException")
    void testAddLogroToJugadorNonExistingJugadorThrowsException() {
        when(jugadorRepository.findById(99L)).thenReturn(Optional.empty());
        // No necesitamos mockear logroRepository.findById si el jugador no se encuentra primero.

        assertThrows(RuntimeException.class, () -> {
            jugadorService.addLogroToJugador(99L, logro1.getId());
        }, "Debería lanzar RuntimeException si el jugador no existe");

        verify(jugadorRepository, times(1)).findById(99L);
        verify(logroRepository, never()).findById(anyLong()); // No debería llegar a buscar el logro
        verify(jugadorRepository, never()).save(any(Jugador.class));
    }

    @Test
    @DisplayName("Test para añadir un logro no existente a un jugador existente debería lanzar RuntimeException")
    void testAddLogroToJugadorNonExistingLogroThrowsException() {
        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador1));
        when(logroRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            jugadorService.addLogroToJugador(1L, 99L);
        }, "Debería lanzar RuntimeException si el logro no existe");

        verify(jugadorRepository, times(1)).findById(1L);
        verify(logroRepository, times(1)).findById(99L);
        verify(jugadorRepository, never()).save(any(Jugador.class));
    }

    @Test
    @DisplayName("Test para quitar un logro de un jugador existente")
    void testRemoveLogroFromJugadorExisting() {
        // Jugador1 ya tiene logro1 (configurado en setUp). Queremos quitarlo.
        // Asegúrate de que jugador1 tenga una copia modificable de logros para la simulación
        Jugador jugadorParaMock = new Jugador(jugador1.getNombre(), jugador1.getApellido(), jugador1.getFechaNacimiento(), jugador1.getAlturaCm(), jugador1.getPesoKg(), jugador1.getPosicion(), jugador1.getNumeroCamiseta(), jugador1.getActivo());
        jugadorParaMock.setId(jugador1.getId());
        jugadorParaMock.setEquipo(jugador1.getEquipo());
        jugadorParaMock.setLogros(new HashSet<>(jugador1.getLogros())); // Copia modificable

        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugadorParaMock));
        when(logroRepository.findById(logro1.getId())).thenReturn(Optional.of(logro1));

        // Simular el guardado: el jugador devuelto por save tendrá el logro quitado
        when(jugadorRepository.save(any(Jugador.class))).thenAnswer(invocation -> {
            Jugador savedJ = invocation.getArgument(0);
            return savedJ;
        });

        jugadorService.removeLogroFromJugador(1L, logro1.getId());

        // Después de la remoción, el jugadorParaMock (que fue el que se modificó) debería no tener el logro1
        assertFalse(jugadorParaMock.getLogros().contains(logro1), "El logro1 debería haber sido removido del jugador");
        assertEquals(0, jugadorParaMock.getLogros().size(), "El jugador debería tener 0 logros");

        verify(jugadorRepository, times(1)).findById(1L);
        verify(logroRepository, times(1)).findById(logro1.getId());
        verify(jugadorRepository, times(1)).save(any(Jugador.class)); // Verifica que save fue llamado
    }

    @Test
    @DisplayName("Test para quitar un logro de un jugador no existente debería lanzar RuntimeException")
    void testRemoveLogroFromJugadorNonExistingJugadorThrowsException() {
        when(jugadorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            jugadorService.removeLogroFromJugador(99L, logro1.getId());
        }, "Debería lanzar RuntimeException si el jugador no existe");

        verify(jugadorRepository, times(1)).findById(99L);
        verify(logroRepository, never()).findById(anyLong());
        verify(jugadorRepository, never()).save(any(Jugador.class));
    }

    @Test
    @DisplayName("Test para quitar un logro no existente de un jugador existente debería lanzar RuntimeException")
    void testRemoveLogroFromJugadorNonExistingLogroThrowsException() {
        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador1));
        when(logroRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            jugadorService.removeLogroFromJugador(1L, 99L);
        }, "Debería lanzar RuntimeException si el logro no existe");

        verify(jugadorRepository, times(1)).findById(1L);
        verify(logroRepository, times(1)).findById(99L);
        verify(jugadorRepository, never()).save(any(Jugador.class));
    }

    @Test
    @DisplayName("Test para actualizar la lista de logros: añadir nuevos y quitar existentes")
    void testUpdateJugadorLogrosAddAndRemove() {
        // Jugador1 tiene solo logro1 al inicio. Queremos que termine con logro2 y logro3.
        // Simulamos que el jugador existe
        Jugador jugadorParaMock = new Jugador(jugador1.getNombre(), jugador1.getApellido(), jugador1.getFechaNacimiento(), jugador1.getAlturaCm(), jugador1.getPesoKg(), jugador1.getPosicion(), jugador1.getNumeroCamiseta(), jugador1.getActivo());
        jugadorParaMock.setId(jugador1.getId());
        jugadorParaMock.setEquipo(jugador1.getEquipo());
        jugadorParaMock.setLogros(new HashSet<>(Arrays.asList(logro1))); // Logros iniciales
        logro1.addJugador(jugadorParaMock); // Asegurar bidireccionalidad

        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugadorParaMock));
        when(logroRepository.findById(logro1.getId())).thenReturn(Optional.of(logro1)); // Necesario si se verifica su existencia
        when(logroRepository.findById(logro2.getId())).thenReturn(Optional.of(logro2));
        when(logroRepository.findById(logro3.getId())).thenReturn(Optional.of(logro3));

        // Simular el guardado
        when(jugadorRepository.save(any(Jugador.class))).thenAnswer(invocation -> {
            Jugador savedJ = invocation.getArgument(0);
            // No es necesario simular la bidireccionalidad en el save mock,
            // ya que se asume que la entidad ya lo maneja al añadir/quitar.
            return savedJ;
        });

        List<Long> nuevosLogroIds = Arrays.asList(logro2.getId(), logro3.getId());
        Jugador result = jugadorService.updateJugadorLogros(1L, nuevosLogroIds);

        assertNotNull(result);
        assertEquals(2, result.getLogros().size(), "El jugador debería tener 2 logros al final");
        assertTrue(result.getLogros().contains(logro2), "Debería contener logro2");
        assertTrue(result.getLogros().contains(logro3), "Debería contener logro3");
        assertFalse(result.getLogros().contains(logro1), "No debería contener logro1");

        verify(jugadorRepository, times(1)).findById(1L);
        verify(logroRepository, times(1)).findById(logro2.getId());
        verify(logroRepository, times(1)).findById(logro3.getId());
        // verify(logroRepository, times(1)).findById(logro1.getId()); // Podría ser llamado si la implementación lo verifica
        verify(jugadorRepository, times(1)).save(any(Jugador.class));
    }

    @Test
    @DisplayName("Test para actualizar la lista de logros: solo añadir nuevos")
    void testUpdateJugadorLogrosOnlyAdd() {
        // Jugador1 tiene solo logro1 al inicio. Queremos que termine con logro1 y logro2.
        Jugador jugadorParaMock = new Jugador(jugador1.getNombre(), jugador1.getApellido(), jugador1.getFechaNacimiento(), jugador1.getAlturaCm(), jugador1.getPesoKg(), jugador1.getPosicion(), jugador1.getNumeroCamiseta(), jugador1.getActivo());
        jugadorParaMock.setId(jugador1.getId());
        jugadorParaMock.setEquipo(jugador1.getEquipo());
        jugadorParaMock.setLogros(new HashSet<>(Arrays.asList(logro1)));
        logro1.addJugador(jugadorParaMock);

        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugadorParaMock));
        when(logroRepository.findById(logro1.getId())).thenReturn(Optional.of(logro1));
        when(logroRepository.findById(logro2.getId())).thenReturn(Optional.of(logro2));

        when(jugadorRepository.save(any(Jugador.class))).thenAnswer(invocation -> {
            Jugador savedJ = invocation.getArgument(0);
            return savedJ;
        });

        List<Long> nuevosLogroIds = Arrays.asList(logro1.getId(), logro2.getId());
        Jugador result = jugadorService.updateJugadorLogros(1L, nuevosLogroIds);

        assertNotNull(result);
        assertEquals(2, result.getLogros().size(), "El jugador debería tener 2 logros al final");
        assertTrue(result.getLogros().contains(logro1), "Debería contener logro1");
        assertTrue(result.getLogros().contains(logro2), "Debería contener logro2");

        verify(jugadorRepository, times(1)).findById(1L);
        verify(logroRepository, times(1)).findById(logro1.getId());
        verify(logroRepository, times(1)).findById(logro2.getId());
        verify(jugadorRepository, times(1)).save(any(Jugador.class));
    }

    @Test
    @DisplayName("Test para actualizar la lista de logros: solo quitar existentes")
    void testUpdateJugadorLogrosOnlyRemove() {
        // Jugador1 tiene logro1 y logro2 al inicio. Queremos que termine con solo logro2.
        Jugador jugadorParaMock = new Jugador(jugador1.getNombre(), jugador1.getApellido(), jugador1.getFechaNacimiento(), jugador1.getAlturaCm(), jugador1.getPesoKg(), jugador1.getPosicion(), jugador1.getNumeroCamiseta(), jugador1.getActivo());
        jugadorParaMock.setId(jugador1.getId());
        jugadorParaMock.setEquipo(jugador1.getEquipo());
        jugadorParaMock.setLogros(new HashSet<>(Arrays.asList(logro1, logro2)));
        logro1.addJugador(jugadorParaMock);
        logro2.addJugador(jugadorParaMock);

        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugadorParaMock));
        when(logroRepository.findById(logro2.getId())).thenReturn(Optional.of(logro2)); // El que se mantiene

        when(jugadorRepository.save(any(Jugador.class))).thenAnswer(invocation -> {
            Jugador savedJ = invocation.getArgument(0);
            return savedJ;
        });

        List<Long> nuevosLogroIds = Arrays.asList(logro2.getId()); // Solo queremos logro2
        Jugador result = jugadorService.updateJugadorLogros(1L, nuevosLogroIds);

        assertNotNull(result);
        assertEquals(1, result.getLogros().size(), "El jugador debería tener 1 logro al final");
        assertTrue(result.getLogros().contains(logro2), "Debería contener logro2");
        assertFalse(result.getLogros().contains(logro1), "No debería contener logro1");

        verify(jugadorRepository, times(1)).findById(1L);
        verify(logroRepository, times(1)).findById(logro2.getId()); // Solo se busca el que se mantiene
        verify(jugadorRepository, times(1)).save(any(Jugador.class));
    }

    @Test
    @DisplayName("Test para actualizar la lista de logros: sin cambios")
    void testUpdateJugadorLogrosNoChange() {
        // Jugador1 tiene solo logro1 al inicio. La nueva lista también es logro1.
        Jugador jugadorParaMock = new Jugador(jugador1.getNombre(), jugador1.getApellido(), jugador1.getFechaNacimiento(), jugador1.getAlturaCm(), jugador1.getPesoKg(), jugador1.getPosicion(), jugador1.getNumeroCamiseta(), jugador1.getActivo());
        jugadorParaMock.setId(jugador1.getId());
        jugadorParaMock.setEquipo(jugador1.getEquipo());
        jugadorParaMock.setLogros(new HashSet<>(Arrays.asList(logro1)));
        logro1.addJugador(jugadorParaMock);

        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugadorParaMock));
        when(logroRepository.findById(logro1.getId())).thenReturn(Optional.of(logro1));

        when(jugadorRepository.save(any(Jugador.class))).thenAnswer(invocation -> {
            Jugador savedJ = invocation.getArgument(0);
            return savedJ;
        });

        List<Long> nuevosLogroIds = Arrays.asList(logro1.getId());
        Jugador result = jugadorService.updateJugadorLogros(1L, nuevosLogroIds);

        assertNotNull(result);
        assertEquals(1, result.getLogros().size(), "El jugador debería tener 1 logro al final");
        assertTrue(result.getLogros().contains(logro1), "Debería contener logro1");

        verify(jugadorRepository, times(1)).findById(1L);
        verify(logroRepository, times(1)).findById(logro1.getId());
        verify(jugadorRepository, times(1)).save(any(Jugador.class)); // Save siempre se llama por ahora, incluso sin cambios
    }

    @Test
    @DisplayName("Test para actualizar la lista de logros: con lista vacía (quitar todos)")
    void testUpdateJugadorLogrosEmptyList() {
        // Jugador1 tiene logro1 al inicio. Queremos que no tenga ninguno.
        Jugador jugadorParaMock = new Jugador(jugador1.getNombre(), jugador1.getApellido(), jugador1.getFechaNacimiento(), jugador1.getAlturaCm(), jugador1.getPesoKg(), jugador1.getPosicion(), jugador1.getNumeroCamiseta(), jugador1.getActivo());
        jugadorParaMock.setId(jugador1.getId());
        jugadorParaMock.setEquipo(jugador1.getEquipo());
        jugadorParaMock.setLogros(new HashSet<>(Arrays.asList(logro1)));
        logro1.addJugador(jugadorParaMock);

        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugadorParaMock));

        when(jugadorRepository.save(any(Jugador.class))).thenAnswer(invocation -> {
            Jugador savedJ = invocation.getArgument(0);
            return savedJ;
        });

        List<Long> nuevosLogroIds = new ArrayList<>(); // Lista vacía
        Jugador result = jugadorService.updateJugadorLogros(1L, nuevosLogroIds);

        assertNotNull(result);
        assertTrue(result.getLogros().isEmpty(), "El jugador no debería tener logros");

        verify(jugadorRepository, times(1)).findById(1L);
        verify(logroRepository, never()).findById(anyLong()); // No se busca ningún logro
        verify(jugadorRepository, times(1)).save(any(Jugador.class));
    }

    @Test
    @DisplayName("Test para actualizar logros de un jugador no existente debería lanzar RuntimeException")
    void testUpdateJugadorLogrosNonExistingJugadorThrowsException() {
        when(jugadorRepository.findById(99L)).thenReturn(Optional.empty());

        List<Long> logroIds = Arrays.asList(logro1.getId());

        assertThrows(RuntimeException.class, () -> {
            jugadorService.updateJugadorLogros(99L, logroIds);
        }, "Debería lanzar RuntimeException si el jugador no existe");

        verify(jugadorRepository, times(1)).findById(99L);
        verify(logroRepository, never()).findById(anyLong());
        verify(jugadorRepository, never()).save(any(Jugador.class));
    }

    @Test
    @DisplayName("Test para actualizar logros con un ID de logro no existente en la lista debería lanzar RuntimeException")
    void testUpdateJugadorLogrosNonExistingLogroInListThrowsException() {
        // Jugador1 existe
        Jugador jugadorParaMock = new Jugador(jugador1.getNombre(), jugador1.getApellido(), jugador1.getFechaNacimiento(), jugador1.getAlturaCm(), jugador1.getPesoKg(), jugador1.getPosicion(), jugador1.getNumeroCamiseta(), jugador1.getActivo());
        jugadorParaMock.setId(jugador1.getId());
        jugadorParaMock.setEquipo(jugador1.getEquipo());
        jugadorParaMock.setLogros(new HashSet<>(Arrays.asList(logro1)));
        logro1.addJugador(jugadorParaMock);

        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugadorParaMock));
        when(logroRepository.findById(logro1.getId())).thenReturn(Optional.of(logro1));
        when(logroRepository.findById(99L)).thenReturn(Optional.empty()); // Logro inexistente

        List<Long> logroIds = Arrays.asList(logro1.getId(), 99L); // Uno existente y uno no existente

        assertThrows(RuntimeException.class, () -> {
            jugadorService.updateJugadorLogros(1L, logroIds);
        }, "Debería lanzar RuntimeException si un logro en la lista no existe");

        verify(jugadorRepository, times(1)).findById(1L);
        verify(logroRepository, times(1)).findById(logro1.getId());
        verify(logroRepository, times(1)).findById(99L); // Intenta buscar el logro inexistente
        verify(jugadorRepository, never()).save(any(Jugador.class));
    }
}