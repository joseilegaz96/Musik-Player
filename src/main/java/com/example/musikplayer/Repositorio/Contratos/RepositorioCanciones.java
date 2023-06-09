package com.example.musikplayer.Repositorio.Contratos;

import java.sql.Time;
import java.util.List;

public interface RepositorioCanciones<T> {
    List<T> GetAllCanciones();
    List<T> GetNameFiles();
    int lastId();
    void GetId(int id);
    void insert(int id,String nombre_cancion, Time duracion, String genero, int artista, String ruta, String nombre_fichero, String album);
    boolean update(int id);
    boolean delete(String nombreCancion);
}
