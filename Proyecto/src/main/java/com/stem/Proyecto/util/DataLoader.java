package com.stem.Proyecto.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.stem.Proyecto.config.InitializationConfig;
import com.stem.Proyecto.entity.Equipo;
import com.stem.Proyecto.entity.Jugador;
import com.stem.Proyecto.entity.Logro;
import com.stem.Proyecto.entity.Partido;
import com.stem.Proyecto.repository.EquipoRepository;
import com.stem.Proyecto.repository.JugadorRepository;
import com.stem.Proyecto.repository.LogroRepository;
import com.stem.Proyecto.repository.PartidoRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final EquipoRepository equipoRepository;
    private final JugadorRepository jugadorRepository;
    private final LogroRepository logroRepository;
    private final PartidoRepository partidoRepository;

    private final InitializationConfig config;

    private final Faker faker = new Faker(new Locale("es"));
    private final Random random = new Random();
    private final LocalDate hoy = LocalDate.now();

    private final List<String> ciudadesNBA = List.of("Los Angeles", "Boston", "Chicago", "Miami", "Golden State", "Cleveland", "Houston", "Dallas", "Denver", "Milwaukee");
    private final List<String> nombresEquiposNBA = List.of("Lakers", "Celtics", "Bulls", "Heat", "Warriors", "Cavaliers", "Rockets", "Mavericks", "Nuggets", "Bucks");
    private final List<String> abreviaturasNBA = List.of("LAL", "BOS", "CHI", "MIA", "GSW", "CLE", "HOU", "DAL", "DEN", "MIL");
    private final List<String> pabellonesNBA = List.of("Crypto.com Arena", "TD Garden", "United Center", "Kaseya Center", "Chase Center", "Rocket Mortgage FieldHouse", "Toyota Center", "American Airlines Center", "Ball Arena", "Fiserv Forum");
    private final List<String> nombresLogros = List.of(
            "MVP de la Temporada", "Jugador All-Star", "Mejor Entrenador/a",
            "MVP de las Finales", "Defensor del Año", "Sexto Hombre del Año",
            "Jugador Más Mejorado", "Novato del Año", "Máximo Anotador",
            "Líder en Asistencias", "Líder en Rebotes"
    );
    private final String[] posicionesJugador = {"Base", "Escolta", "Alero", "Ala-Pívot", "Pívot"};


    public DataLoader(EquipoRepository equipoRepository, JugadorRepository jugadorRepository, LogroRepository logroRepository, PartidoRepository partidoRepository,
                      InitializationConfig config) {
        this.equipoRepository = equipoRepository;
        this.jugadorRepository = jugadorRepository;
        this.logroRepository = logroRepository;
        this.partidoRepository = partidoRepository;
        this.config = config;
    }

    @Override
    public void run(String... args) throws Exception {


        if (equipoRepository.count() > 0) {
            System.out.println("La base de datos ya contiene datos. Saltando la inserción de datos iniciales.");
            return;
        }

        System.out.println("Iniciando la generación de datos iniciales de la NBA con la siguiente configuración:");
        System.out.println("  - Equipos a generar: " + config.getNumeroDeEquipos());
        System.out.println("  - Jugadores por equipo: entre " + config.getJugadoresMinPorEquipo() + " y " + config.getJugadoresMaxPorEquipo());
        System.out.println("  - Logros base a generar: " + config.getNumeroDeLogrosBase());
        System.out.println("  - Máximo de logros por jugador: " + config.getMaxLogrosPorJugador());
        System.out.println("  - Partidos a generar: " + config.getNumeroDePartidos());
        System.out.println("  - Rango de puntuación de partidos: " + config.getPuntuacionMinPartido() + "-" + config.getPuntuacionMaxPartido());
        System.out.println("  - Rango de edad de jugadores: " + config.getEdadMinJugador() + "-" + config.getEdadMaxJugador());
        System.out.println("  - Rango de altura de jugadores: " + config.getAlturaMinCmJugador() + "-" + config.getAlturaMaxCmJugador() + " cm");
        System.out.println("  - Partidos en rango de " + config.getDiasAntesFechaActualPartido() + " días antes a " + config.getDiasDespuesFechaActualPartido() + " días después.");

        List<Equipo> equipos = new ArrayList<>();
        for (int i = 0; i < config.getNumeroDeEquipos(); i++) {

            String nombre = nombresEquiposNBA.get(i);
            String ciudad = ciudadesNBA.get(i);
            String abreviatura = abreviaturasNBA.get(i);
            Integer anioFundacion = faker.number().numberBetween(config.getAnioFundacionMinEquipo(), config.getAnioFundacionMaxEquipo());
            String pabellon = pabellonesNBA.get(i);
            String entrenador = faker.name().fullName();

            Equipo equipo = new Equipo(nombre, ciudad, abreviatura, anioFundacion, pabellon, entrenador);
            equipos.add(equipo);
        }
        equipoRepository.saveAll(equipos);
        System.out.println("✅ " + equipos.size() + " equipos generados y guardados.");

        List<Jugador> jugadores = new ArrayList<>();
        for (Equipo equipo : equipos) {
            int numJugadoresPorEquipo = random.nextInt(config.getJugadoresMaxPorEquipo() - config.getJugadoresMinPorEquipo() + 1) + config.getJugadoresMinPorEquipo();
            for (int i = 0; i < numJugadoresPorEquipo; i++) {
                String nombre = faker.name().firstName();
                String apellido = faker.name().lastName();
                java.time.LocalDate fechaNacimiento = faker.date().birthday(config.getEdadMinJugador(), config.getEdadMaxJugador()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                Integer alturaCm = faker.number().numberBetween(config.getAlturaMinCmJugador(), config.getAlturaMaxCmJugador());
                Integer pesoKg = faker.number().numberBetween(config.getPesoMinKgJugador(), config.getPesoMaxKgJugador());
                String posicion = posicionesJugador[(int) (Math.random() * posicionesJugador.length)];
                Integer numeroCamiseta = faker.number().numberBetween(0, 99);
                Boolean activo = faker.bool().bool();

                Jugador jugador = new Jugador(nombre, apellido, fechaNacimiento, alturaCm, pesoKg, posicion, numeroCamiseta, activo);
                jugador.setEquipo(equipo); 
                jugadores.add(jugador);
            }
        }
        jugadorRepository.saveAll(jugadores);
        System.out.println("✅ " + jugadores.size() + " jugadores generados y guardados.");

        List<Logro> logros = new ArrayList<>();
        for (int i = 0; i < config.getNumeroDeLogrosBase(); i++) {
            String nombre = nombresLogros.get(i);
            String descripcion = faker.lorem().sentence();
            Integer anio = faker.number().numberBetween(1980, 2024); 

            Logro logro = new Logro(nombre, descripcion, anio);
            logros.add(logro);
        }
        logroRepository.saveAll(logros);
        System.out.println("✅ " + logros.size() + " logros generados y guardados.");

        for (Jugador jugador : jugadores) {
            int numLogrosAsignar = random.nextInt(config.getMaxLogrosPorJugador() + 1);
            for (int i = 0; i < numLogrosAsignar; i++) {
                Logro logroAleatorio = logros.get(random.nextInt(logros.size()));
                if (!jugador.getLogros().contains(logroAleatorio)) {
                    jugador.addLogro(logroAleatorio);
                }
            }
            jugadorRepository.save(jugador);
        }
        System.out.println("✅ Logros asignados aleatoriamente a jugadores.");

        List<Partido> partidos = new ArrayList<>();
        for (int i = 0; i < config.getNumeroDePartidos(); i++) {
            Equipo equipoLocal = equipos.get(random.nextInt(equipos.size()));
            Equipo equipoVisitante;
            do {
                equipoVisitante = equipos.get(random.nextInt(equipos.size()));
            } while (equipoVisitante.equals(equipoLocal));

            LocalDate fechaPartido = hoy.plusDays(faker.number().numberBetween(-config.getDiasAntesFechaActualPartido(), config.getDiasDespuesFechaActualPartido()));
            LocalTime horaPartido = LocalTime.of(faker.number().numberBetween(19, 23), random.nextInt(60));
            Integer puntuacionLocal = faker.number().numberBetween(config.getPuntuacionMinPartido(), config.getPuntuacionMaxPartido());
            Integer puntuacionVisitante = faker.number().numberBetween(config.getPuntuacionMinPartido(), config.getPuntuacionMaxPartido());
            String temporada = "2024-2025"; 
            String lugar = equipoLocal.getPabellon();

            Partido partido = new Partido(fechaPartido, horaPartido, puntuacionLocal, puntuacionVisitante, temporada, lugar, equipoLocal, equipoVisitante);
            partidos.add(partido);
        }
        partidoRepository.saveAll(partidos);
        System.out.println("✅ " + partidos.size() + " partidos generados y guardados.");

        System.out.println("\n🎉 Datos iniciales de la NBA generados y persistidos correctamente. ¡La aplicación está lista! 🎉");
    }
}