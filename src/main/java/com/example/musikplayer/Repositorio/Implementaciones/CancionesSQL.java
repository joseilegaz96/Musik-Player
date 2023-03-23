package com.example.musikplayer.Repositorio.Implementaciones;

import com.example.musikplayer.Conexion.ConexionSQL;
import com.example.musikplayer.Repositorio.DAO.Cancion;
import com.example.musikplayer.Repositorio.Contratos.Repositorio;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CancionesSQL implements Repositorio<Cancion> {
    ConexionSQL conectar = new ConexionSQL();

    @Override
    public List<Cancion> GetAll() {
        List<Cancion> canciones = new ArrayList<>();
        String sql = "SELECT c.id,c.nombre_cancion,c.duracion,c.genero,c.ruta,c.nombre_fichero,c.album,a.artista FROM canciones c  INNER JOIN  artista a ON c.artista = a.id";
        conectar.conectar();

        try(Statement stm = conectar.createStatement();
            ResultSet rs = stm.executeQuery(sql)) {

            while (rs.next()) {
                Cancion cancion = new Cancion();
                cancion.setId(rs.getInt("c.id"));
                cancion.setNombreCancion(rs.getString("c.nombre_cancion"));
                cancion.setDuracion(rs.getTime("c.duracion"));
                cancion.setGenero(rs.getString("c.genero"));
                cancion.setArtista(rs.getString("a.artista"));
                cancion.setRuta(rs.getString("c.ruta"));
                cancion.setNombreFichero(rs.getString("c.nombre_fichero"));
                cancion.setAlbum(rs.getString("c.album"));
                canciones.add(cancion);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return canciones;
    }

    @Override
    public void GetId(int id) {

    }

    @Override
    public void insert() {
    }

    @Override
    public boolean update(int id) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
