package com.stem.Proyecto.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;    
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

/**
 * Representa la entidad "Logro" en la base de datos.
 * Esta clase guarda la información de cada logro que los jugadores pueden conseguir.
 */
@Entity
@Table(name = "logros") // La tabla en la base de datos se llamará "logros"
public class Logro {

    /**
     * El identificador único del logro.
     * Es la clave principal de la tabla y se genera automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * El nombre del logro. No puede ser nulo y debe ser único.
     */
    @Column(nullable = false, unique = true)
    private String nombre;

    /**
     * Una descripción detallada del logro (máximo 500 caracteres).
     */
    @Column(length = 500)
    private String descripcion;

    /**
     * El año en que se consiguió o se estableció el logro.
     */
    private Integer anio;

    /**
     * La lista de jugadores que han conseguido este logro.
     * {@code @JsonIgnore} le dice a Jackson que ignore esta propiedad al convertir a JSON para evitar bucles.
     * {@code @ManyToMany(mappedBy = "logros")} indica que esta es la relación inversa
     * de la que está definida en la entidad {@link Jugador} en el campo 'logros'.
     */
    @JsonIgnore // Ignora esta propiedad al convertir a JSON para evitar bucles infinitos
    @ManyToMany(mappedBy = "logros")
    private Set<Jugador> jugadoresConEsteLogro = new HashSet<>();
    /**
     * Constructor vacío requerido por JPA (Java Persistence API).
     */
    public Logro() {
    }

    /**
     * Constructor para crear un nuevo logro con su nombre, descripción y año.
     *
     * @param nombre El nombre del logro.
     * @param descripcion Una descripción del logro.
     * @param anio El año del logro.
     */
    public Logro(String nombre, String descripcion, Integer anio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.anio = anio;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Set<Jugador> getJugadoresConEsteLogro() {
        return jugadoresConEsteLogro;
    }

    public void setJugadoresConEsteLogro(Set<Jugador> jugadoresConEsteLogro) {
        this.jugadoresConEsteLogro = Objects.requireNonNullElseGet(jugadoresConEsteLogro, HashSet::new);
    }

    // La propiedad @JsonProperty("jugadores") para JSON
    // Asegúrate de que JugadorSimpleDTO también se adapte a un Set si es necesario
    @JsonProperty("jugadores")
    public List<JugadorSimpleDTO> getJugadoresJson() {
        if (this.jugadoresConEsteLogro == null) {
            return new ArrayList<>(); // Devuelve una lista vacía si el set es null
        }
        // Mapea cada Jugador del SET a un JugadorSimpleDTO
        // El Collectors.toList() aquí es porque JSON suele serializar un Set como un array de todas formas.
        return this.jugadoresConEsteLogro.stream()
                .map(jugador -> new JugadorSimpleDTO(jugador.getId(), jugador.getNombre(), jugador.getApellido()))
                .collect(Collectors.toList());
    }

    // Clase interna estática para la representación simplificada del Jugador.
    public static class JugadorSimpleDTO {
        public Long id;
        public String nombre;
        public String apellido;

        public JugadorSimpleDTO(Long id, String nombre, String apellido) {
            this.id = id;
            this.nombre = nombre;
            this.apellido = apellido;
        }
    }

    /**
     * Añade un jugador a la lista de jugadores que tienen este logro.
     * Este método es vital para mantener la bidireccionalidad de la relación.
     *
     * @param jugador El jugador a añadir.
     */
    public void addJugador(Jugador jugador) {
        if (this.jugadoresConEsteLogro == null) {
            this.jugadoresConEsteLogro = new HashSet<>();
        }
        this.jugadoresConEsteLogro.add(jugador);
        // Asegúrate de que el lado del jugador también tenga este logro
        if (jugador.getLogros() != null && !jugador.getLogros().contains(this)) {
            jugador.getLogros().add(this);
        }
    }

    /**
     * Quita un jugador de la lista de jugadores que tienen este logro.
     * Este método es vital para mantener la bidireccionalidad de la relación.
     *
     * @param jugador El jugador a quitar.
     */
    public void removeJugador(Jugador jugador) {
        if (this.jugadoresConEsteLogro != null) {
            this.jugadoresConEsteLogro.remove(jugador);
        }
        // Asegúrate de que el lado del jugador también elimine este logro
        if (jugador.getLogros() != null) {
            jugador.getLogros().remove(this);
        }
    }

/**
 * Compara este objeto {@code Logro} con otro para verificar igualdad.
 * Dos logros se consideran iguales si tienen el mismo ID.
 *
 * @param o el objeto a comparar con este logro
 * @return {@code true} si los objetos son iguales, {@code false} en caso contrario
 */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Logro logro = (Logro) o;
        return Objects.equals(id, logro.id);
    }

    /**
     * Genera un código hash para este logro basado en su ID.
     *
     * @return el código hash calculado
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Devuelve una representación en forma de cadena del objeto {@code Logro}.
     * Incluye el ID, nombre, descripción y año del logro.
     *
     * @return una cadena que representa al logro
     */
    @Override
    public String toString() {
        return "Logro{" +
            "id=" + id +
            ", nombre='" + nombre + '\'' +
            ", descripcion='" + descripcion + '\'' +
            ", anio=" + anio +
            '}';
    }

}