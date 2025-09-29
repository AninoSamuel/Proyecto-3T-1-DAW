package com.stem.Proyecto.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Esta clase representa un partido de un deporte entre dos equipos.
 * Guarda información como la fecha, hora, lugar, puntuaciones y los equipos que jugaron.
 */
@Entity
@Table(name = "partidos")
public class Partido {

    /** ID único del partido (clave primaria en la base de datos). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Fecha en la que se jugó el partido. */
    @Column(nullable = false)
    private LocalDate fecha;

    /** Hora en la que comenzó el partido. */
    @Column(nullable = false)
    private LocalTime hora;

    /** Puntos que anotó el equipo local. */
    @Column(name = "puntuacion_local")
    private Integer puntuacionLocal;

    /** Puntos que anotó el equipo visitante. */
    @Column(name = "puntuacion_visitante")
    private Integer puntuacionVisitante;

    /** Temporada a la que pertenece este partido (por ejemplo, "2024-2025"). */
    @Column(nullable = false)
    private String temporada;

    /** Lugar donde se jugó el partido. */
    private String lugar;

    /** Equipo que jugó como local. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equipo_local_id", nullable = false)
    private Equipo equipoLocal;

    /** Equipo que jugó como visitante. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equipo_visitante_id", nullable = false)
    private Equipo equipoVisitante;

    /** Constructor vacío necesario para JPA. */
    public Partido() {}

    /**
     * Constructor para crear un partido con todos sus datos.
     *
     * @param fecha Fecha del partido
     * @param hora Hora del partido
     * @param puntuacionLocal Puntos del equipo local
     * @param puntuacionVisitante Puntos del equipo visitante
     * @param temporada Temporada del partido
     * @param lugar Lugar donde se jugó
     * @param equipoLocal Equipo local
     * @param equipoVisitante Equipo visitante
     */
    public Partido(LocalDate fecha, LocalTime hora, Integer puntuacionLocal, Integer puntuacionVisitante, String temporada, String lugar, Equipo equipoLocal, Equipo equipoVisitante) {
        this.fecha = fecha;
        this.hora = hora;
        this.puntuacionLocal = puntuacionLocal;
        this.puntuacionVisitante = puntuacionVisitante;
        this.temporada = temporada;
        this.lugar = lugar;
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
    }

    // ----- Métodos Getters y Setters -----

    /** @return ID del partido */
    public Long getId() { return id; }

    /** @param id ID del partido */
    public void setId(Long id) { this.id = id; }

    /** @return Fecha del partido */
    public LocalDate getFecha() { return fecha; }

    /** @param fecha Fecha del partido */
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    /** @return Hora del partido */
    public LocalTime getHora() { return hora; }

    /** @param hora Hora del partido */
    public void setHora(LocalTime hora) { this.hora = hora; }

    /** @return Puntos del equipo local */
    public Integer getPuntuacionLocal() { return puntuacionLocal; }

    /** @param puntuacionLocal Puntos del equipo local */
    public void setPuntuacionLocal(Integer puntuacionLocal) { this.puntuacionLocal = puntuacionLocal; }

    /** @return Puntos del equipo visitante */
    public Integer getPuntuacionVisitante() { return puntuacionVisitante; }

    /** @param puntuacionVisitante Puntos del equipo visitante */
    public void setPuntuacionVisitante(Integer puntuacionVisitante) { this.puntuacionVisitante = puntuacionVisitante; }

    /** @return Temporada del partido */
    public String getTemporada() { return temporada; }

    /** @param temporada Temporada del partido */
    public void setTemporada(String temporada) { this.temporada = temporada; }

    /** @return Lugar donde se jugó el partido */
    public String getLugar() { return lugar; }

    /** @param lugar Lugar donde se jugó el partido */
    public void setLugar(String lugar) { this.lugar = lugar; }

    /** @return Equipo local */
    public Equipo getEquipoLocal() { return equipoLocal; }

    /** @param equipoLocal Equipo local */
    public void setEquipoLocal(Equipo equipoLocal) { this.equipoLocal = equipoLocal; }

    /** @return Equipo visitante */
    public Equipo getEquipoVisitante() { return equipoVisitante; }

    /** @param equipoVisitante Equipo visitante */
    public void setEquipoVisitante(Equipo equipoVisitante) { this.equipoVisitante = equipoVisitante; }


    /**
     * Devuelve el nombre del equipo local.
     * @return Nombre del equipo local o "Desconocido" si no existe.
     */
    @JsonProperty("nombreEquipoLocal")
    public String getNombreEquipoLocal() {
        return (this.equipoLocal != null) ? this.equipoLocal.getNombre() : "Desconocido";
    }

    /**
     * Devuelve el ID del equipo local.
     * @return ID del equipo local o null si no existe.
     */
    @JsonProperty("idEquipoLocal")
    public Long getIdEquipoLocal() {
        return (this.equipoLocal != null) ? this.equipoLocal.getId() : null;
    }

    /**
     * Devuelve el nombre del equipo visitante.
     * @return Nombre del equipo visitante o "Desconocido" si no existe.
     */
    @JsonProperty("nombreEquipoVisitante")
    public String getNombreEquipoVisitante() {
        return (this.equipoVisitante != null) ? this.equipoVisitante.getNombre() : "Desconocido";
    }

    /**
     * Devuelve el ID del equipo visitante.
     * @return ID del equipo visitante o null si no existe.
     */
    @JsonProperty("idEquipoVisitante")
    public Long getIdEquipoVisitante() {
        return (this.equipoVisitante != null) ? this.equipoVisitante.getId() : null;
    }
}
