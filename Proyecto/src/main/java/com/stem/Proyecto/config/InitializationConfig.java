package com.stem.Proyecto.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration 
@ConfigurationProperties(prefix = "nba.inicializacion") 
public class InitializationConfig {

    private int numeroDeEquipos = 10;
    private int jugadoresMinPorEquipo = 10;
    private int jugadoresMaxPorEquipo = 15;
    private int numeroDeLogrosBase = 11; 
    private int maxLogrosPorJugador = 2; 
    private int numeroDePartidos = 50;
    private int anioFundacionMinEquipo = 1946;
    private int anioFundacionMaxEquipo = 2000;
    private int alturaMinCmJugador = 180;
    private int alturaMaxCmJugador = 220;
    private int pesoMinKgJugador = 80;
    private int pesoMaxKgJugador = 120;
    private int edadMinJugador = 18;
    private int edadMaxJugador = 35;
    private int puntuacionMinPartido = 80;
    private int puntuacionMaxPartido = 130;
    private int diasAntesFechaActualPartido = 30; 
    private int diasDespuesFechaActualPartido = 30; 

    public int getNumeroDeEquipos() {
        return numeroDeEquipos;
    }

    public void setNumeroDeEquipos(int numeroDeEquipos) {
        this.numeroDeEquipos = numeroDeEquipos;
    }

    public int getJugadoresMinPorEquipo() {
        return jugadoresMinPorEquipo;
    }

    public void setJugadoresMinPorEquipo(int jugadoresMinPorEquipo) {
        this.jugadoresMinPorEquipo = jugadoresMinPorEquipo;
    }

    public int getJugadoresMaxPorEquipo() {
        return jugadoresMaxPorEquipo;
    }

    public void setJugadoresMaxPorEquipo(int jugadoresMaxPorEquipo) {
        this.jugadoresMaxPorEquipo = jugadoresMaxPorEquipo;
    }

    public int getNumeroDeLogrosBase() {
        return numeroDeLogrosBase;
    }

    public void setNumeroDeLogrosBase(int numeroDeLogrosBase) {
        this.numeroDeLogrosBase = numeroDeLogrosBase;
    }

    public int getMaxLogrosPorJugador() {
        return maxLogrosPorJugador;
    }

    public void setMaxLogrosPorJugador(int maxLogrosPorJugador) {
        this.maxLogrosPorJugador = maxLogrosPorJugador;
    }

    public int getNumeroDePartidos() {
        return numeroDePartidos;
    }

    public void setNumeroDePartidos(int numeroDePartidos) {
        this.numeroDePartidos = numeroDePartidos;
    }

    public int getAnioFundacionMinEquipo() {
        return anioFundacionMinEquipo;
    }

    public void setAnioFundacionMinEquipo(int anioFundacionMinEquipo) {
        this.anioFundacionMinEquipo = anioFundacionMinEquipo;
    }

    public int getAnioFundacionMaxEquipo() {
        return anioFundacionMaxEquipo;
    }

    public void setAnioFundacionMaxEquipo(int anioFundacionMaxEquipo) {
        this.anioFundacionMaxEquipo = anioFundacionMaxEquipo;
    }

    public int getAlturaMinCmJugador() {
        return alturaMinCmJugador;
    }

    public void setAlturaMinCmJugador(int alturaMinCmJugador) {
        this.alturaMinCmJugador = alturaMinCmJugador;
    }

    public int getAlturaMaxCmJugador() {
        return alturaMaxCmJugador;
    }

    public void setAlturaMaxCmJugador(int alturaMaxCmJugador) {
        this.alturaMaxCmJugador = alturaMaxCmJugador;
    }

    public int getPesoMinKgJugador() {
        return pesoMinKgJugador;
    }

    public void setPesoMinKgJugador(int pesoMinKgJugador) {
        this.pesoMinKgJugador = pesoMinKgJugador;
    }

    public int getPesoMaxKgJugador() {
        return pesoMaxKgJugador;
    }

    public void setPesoMaxKgJugador(int pesoMaxKgJugador) {
        this.pesoMaxKgJugador = pesoMaxKgJugador;
    }

    public int getEdadMinJugador() {
        return edadMinJugador;
    }

    public void setEdadMinJugador(int edadMinJugador) {
        this.edadMinJugador = edadMinJugador;
    }

    public int getEdadMaxJugador() {
        return edadMaxJugador;
    }

    public void setEdadMaxJugador(int edadMaxJugador) {
        this.edadMaxJugador = edadMaxJugador;
    }

    public int getPuntuacionMinPartido() {
        return puntuacionMinPartido;
    }

    public void setPuntuacionMinPartido(int puntuacionMinPartido) {
        this.puntuacionMinPartido = puntuacionMinPartido;
    }

    public int getPuntuacionMaxPartido() {
        return puntuacionMaxPartido;
    }

    public void setPuntuacionMaxPartido(int puntuacionMaxPartido) {
        this.puntuacionMaxPartido = puntuacionMaxPartido;
    }

    public int getDiasAntesFechaActualPartido() {
        return diasAntesFechaActualPartido;
    }

    public void setDiasAntesFechaActualPartido(int diasAntesFechaActualPartido) {
        this.diasAntesFechaActualPartido = diasAntesFechaActualPartido;
    }

    public int getDiasDespuesFechaActualPartido() {
        return diasDespuesFechaActualPartido;
    }

    public void setDiasDespuesFechaActualPartido(int diasDespuesFechaActualPartido) {
        this.diasDespuesFechaActualPartido = diasDespuesFechaActualPartido;
    }
}