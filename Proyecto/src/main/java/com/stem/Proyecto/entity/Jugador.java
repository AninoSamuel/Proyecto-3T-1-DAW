package com.stem.Proyecto.entity;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;       

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Representa un jugador dentro del sistema deportivo.
 * Esta entidad se mapea a la tabla "jugadores" en la base de datos.
 * Contiene información personal, física, y de relación con equipos y logros.
 */
@Entity
@Table(name = "jugadores")
public class Jugador {

    /** Identificador único del jugador. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre del jugador. */
    @Column(nullable = false)
    private String nombre;

    /** Apellido del jugador. */
    @Column(nullable = false)
    private String apellido;

    /** Fecha de nacimiento del jugador. */
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    /** Altura del jugador en centímetros. */
    @Column(name = "altura_cm")
    private Integer alturaCm;

    /** Peso del jugador en kilogramos. */
    @Column(name = "peso_kg")
    private Integer pesoKg;

    /** Posición en la que juega el jugador. */
    private String posicion;

    /** Número de camiseta asignado al jugador. */
    @Column(name = "numero_camiseta")
    private Integer numeroCamiseta;

    /** Indica si el jugador está activo. */
    private Boolean activo;

    /** Equipo al que pertenece el jugador. */
    @ManyToOne(fetch = FetchType.EAGER) // FetchType.EAGER para Equipo puede ser ok si siempre lo necesitas,
                                      // pero Lazy es más común para evitar cargas innecesarias.
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;

    /** Lista de logros obtenidos por el jugador. */
    @ManyToMany(fetch = FetchType.LAZY) 
    @JoinTable(
            name = "jugadores_logros",
            joinColumns = @JoinColumn(name = "jugador_id"),
            inverseJoinColumns = @JoinColumn(name = "logro_id")
    )
    private Set<Logro> logros = new HashSet<>(); 

    /** Constructor vacío requerido por JPA. */
    public Jugador() {}

    /**
     * Constructor para crear un jugador con atributos básicos.
     *
     * @param nombre Nombre del jugador
     * @param apellido Apellido del jugador
     * @param fechaNacimiento Fecha de nacimiento
     * @param alturaCm Altura en centímetros
     * @param pesoKg Peso en kilogramos
     * @param posicion Posición de juego
     * @param numeroCamiseta Número de camiseta
     * @param activo Estado de actividad del jugador
     */
    public Jugador(String nombre, String apellido, LocalDate fechaNacimiento, Integer alturaCm, Integer pesoKg, String posicion, Integer numeroCamiseta, Boolean activo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.alturaCm = alturaCm;
        this.pesoKg = pesoKg;
        this.posicion = posicion;
        this.numeroCamiseta = numeroCamiseta;
        this.activo = activo;
    }

    // --- Getters y Setters ---

    /** @return ID del jugador. */
    public Long getId() {
        return id;
    }

    /** @param id ID del jugador. */
    public void setId(Long id) {
        this.id = id;
    }

    /** @return Nombre del jugador. */
    public String getNombre() {
        return nombre;
    }

    /** @param nombre Nombre del jugador. */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** @return Apellido del jugador. */
    public String getApellido() {
        return apellido;
    }

    /** @param apellido Apellido del jugador. */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /** @return Fecha de nacimiento del jugador. */
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    /** @param fechaNacimiento Fecha de nacimiento del jugador. */
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Calcula la edad del jugador a partir de su fecha de nacimiento.
     *
     * @return Edad actual del jugador o null si no se ha establecido fecha de nacimiento.
     */
    @JsonProperty
    public Integer getEdad() {
        if (this.fechaNacimiento != null) {
            return Period.between(this.fechaNacimiento, LocalDate.now()).getYears();
        }
        return null;
    }

    /** @return Altura del jugador en centímetros. */
    public Integer getAlturaCm() {
        return alturaCm;
    }

    /** @param alturaCm Altura en centímetros. */
    public void setAlturaCm(Integer alturaCm) {
        this.alturaCm = alturaCm;
    }

    /** @return Peso del jugador en kilogramos. */
    public Integer getPesoKg() {
        return pesoKg;
    }

    /** @param pesoKg Peso en kilogramos. */
    public void setPesoKg(Integer pesoKg) {
        this.pesoKg = pesoKg;
    }

    /** @return Posición de juego del jugador. */
    public String getPosicion() {
        return posicion;
    }

    /** @param posicion Posición de juego. */
    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    /** @return Número de camiseta del jugador. */
    public Integer getNumeroCamiseta() {
        return numeroCamiseta;
    }

    /** @param numeroCamiseta Número de camiseta. */
    public void setNumeroCamiseta(Integer numeroCamiseta) {
        this.numeroCamiseta = numeroCamiseta;
    }

    /** @return true si el jugador está activo, false en caso contrario. */
    public Boolean getActivo() {
        return activo;
    }

    /** @param activo Estado de actividad del jugador. */
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    /** @return Equipo al que pertenece el jugador. */
    public Equipo getEquipo() {
        return equipo;
    }

    /** @param equipo Equipo al que pertenece. */
    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    /** @return Set de logros obtenidos por el jugador. */
    public Set<Logro> getLogros() {
        return logros;
    }

    /** @param logros Set de logros. */
    public void setLogros(Set<Logro> logros) {
        this.logros = Objects.requireNonNullElseGet(logros, HashSet::new); 
    }

    /**
     * Añade un logro a la colección de logros del jugador y actualiza la relación inversa.
     *
     * @param logro Logro a añadir.
     */
    public void addLogro(Logro logro) {
        if (this.logros == null) {
            this.logros = new HashSet<>(); // Asegura inicialización si fuera null
        }
        if (!this.logros.contains(logro)) { // Evita añadir si ya existe (Set lo gestiona, pero es buena práctica explícita)
            this.logros.add(logro);
            // Asegura que el lado del logro también tenga este jugador
            if (logro.getJugadoresConEsteLogro() != null && !logro.getJugadoresConEsteLogro().contains(this)) {
                logro.getJugadoresConEsteLogro().add(this);
            }
        }
    }

    /**
     * Elimina un logro de la colección de logros del jugador y actualiza la relación inversa.
     *
     * @param logro Logro a eliminar.
     */
    public void removeLogro(Logro logro) {
        if (this.logros != null) {
            if (this.logros.remove(logro)) { // Remueve el logro del jugador
                // Si el logro fue removido, actualiza el lado del logro
                if (logro.getJugadoresConEsteLogro() != null) {
                    logro.getJugadoresConEsteLogro().remove(this);
                }
            }
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jugador jugador = (Jugador) o;
        return Objects.equals(id, jugador.id); 
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); 
    }

    @Override
    public String toString() {
        return "Jugador{" +
               "id=" + id +
               ", nombre='" + nombre + '\'' +
               ", apellido='" + apellido + '\'' +
               ", posicion='" + posicion + '\'' +
               ", numeroCamiseta=" + numeroCamiseta +
               '}';
    }
}