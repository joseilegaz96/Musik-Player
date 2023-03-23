package com.example.musikplayer.Repositorio.DAO;

import java.sql.Time;

public class Cancion {
    int id;
    String nombreCancion;
    Time duracion;
    String genero;
    String artista;
    String ruta;
    String album;
    String nombreFichero;

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getNombreFichero() {
        return nombreFichero;
    }

    public void setNombreFichero(String nombreFichero) {
        this.nombreFichero = nombreFichero;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreCancion() {
        return nombreCancion;
    }

    public void setNombreCancion(String nombreCancion) {
        this.nombreCancion = nombreCancion;
    }

    public Time getDuracion() {
        return duracion;
    }

    public void setDuracion(Time duracion) {
        this.duracion = duracion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    @Override
    public String toString() {
        return "Cancion{" +
                "id=" + id +
                ", nombreCancion='" + nombreCancion + '\'' +
                ", duracion=" + duracion +
                ", genero='" + genero + '\'' +
                ", artista=" + artista +
                '}';
    }
}
