package com.stem.Proyecto.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Representa la entidad "Equipo" en la base de datos.
 * Esta clase guarda la información de cada equipo deportivo,
 * incluyendo sus jugadores y los partidos en los que ha participado
 * como local o visitante.
 */
@Entity
@Table(name = "equipos") // La tabla en la base de datos se llamará "equipos"
public class Equipo {

    /**
     * El identificador único del equipo.
     * Es la clave principal de la tabla y se genera automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * El nombre del equipo. No puede ser nulo y debe ser único.
     */
    @Column(nullable = false, unique = true)
    private String nombre;

    /**
     * La ciudad a la que pertenece el equipo. No puede ser nulo.
     */
    @Column(nullable = false)
    private String ciudad;

    /**
     * La abreviatura del nombre del equipo (máximo 3 caracteres). No puede ser nulo y debe ser única.
     */
    @Column(nullable = false, unique = true, length = 3)
    private String abreviatura;

    /**
     * El año en que se fundó el equipo.
     */
    @Column(name = "anio_fundacion")
    private Integer anioFundacion;

    /**
     * El nombre del pabellón o estadio donde juega el equipo como local.
     */
    private String pabellon;

    /**
     * El nombre del entrenador del equipo.
     */
    private String entrenador;

    /**
     * Lista de jugadores que pertenecen a este equipo.
     * {@code @JsonManagedReference} ayuda a Jackson a manejar las relaciones bidireccionales
     * y evitar problemas de bucles infinitos al convertir a JSON.
     * {@code CascadeType.ALL} significa que si se elimina un equipo, se eliminan sus jugadores.
     * {@code orphanRemoval = true} significa que si se quita un jugador de la lista del equipo, se elimina de la base de datos.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Jugador> jugadores = new ArrayList<>();

    /**
     * Lista de partidos en los que este equipo ha jugado como local.
     * {@code @JsonManagedReference} ayuda a Jackson a manejar las relaciones bidireccionales.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "equipoLocal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Partido> partidosLocales = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "equipoVisitante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Partido> partidosVisitantes = new ArrayList<>();

    /**
     * Constructor vacío requerido por JPA (Java Persistence API).
     */
    public Equipo() {
    }

    /**
     * Constructor para crear un nuevo equipo con todos sus datos básicos.
     *
     * @param nombre El nombre del equipo.
     * @param ciudad La ciudad del equipo.
     * @param abreviatura La abreviatura del equipo.
     * @param anioFundacion El año de fundación del equipo.
     * @param pabellon El pabellón donde juega el equipo.
     * @param entrenador El entrenador del equipo.
     */
    public Equipo(String nombre, String ciudad, String abreviatura, Integer anioFundacion, String pabellon, String entrenador) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.abreviatura = abreviatura;
        this.anioFundacion = anioFundacion;
        this.pabellon = pabellon;
        this.entrenador = entrenador;
    }

    // --- Métodos Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public Integer getAnioFundacion() {
        return anioFundacion;
    }

    public void setAnioFundacion(Integer anioFundacion) {
        this.anioFundacion = anioFundacion;
    }

    public String getPabellon() {
        return pabellon;
    }

    public void setPabellon(String pabellon) {
        this.pabellon = pabellon;
    }

    public String getEntrenador() {
        return entrenador;
    }

    public void setEntrenador(String entrenador) {
        this.entrenador = entrenador;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public List<Partido> getPartidosLocales() {
        return partidosLocales;
    }

    public void setPartidosLocales(List<Partido> partidosLocales) {
        this.partidosLocales = partidosLocales;
    }

    public List<Partido> getPartidosVisitantes() {
        return partidosVisitantes;
    }

    public void setPartidosVisitantes(List<Partido> partidosVisitantes) {
        this.partidosVisitantes = partidosVisitantes;
    }

    /**
     * Añade un jugador a la lista de jugadores de este equipo.
     * También establece el equipo en el objeto del jugador para mantener la relación.
     *
     * @param jugador El jugador a añadir.
     */
    public void addJugador(Jugador jugador) {
        this.jugadores.add(jugador);
        jugador.setEquipo(this); // Establece la relación inversa
    }

    /**
     * Elimina un jugador de la lista de jugadores de este equipo.
     * También quita el equipo del objeto del jugador para romper la relación.
     *
     * @param jugador El jugador a eliminar.
     */
    public void removeJugador(Jugador jugador) {
        this.jugadores.remove(jugador);
        jugador.setEquipo(null); // Rompe la relación inversa
    }
}