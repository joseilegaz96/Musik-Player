package com.example.musikplayer.Repositorio.Implementaciones;

import com.example.musikplayer.Conexion.ConexionSQL;
import com.example.musikplayer.Repositorio.DAO.Cancion;
import com.example.musikplayer.Repositorio.Contratos.Repositorio;


import java.sql.*;
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
    public List<Cancion> GetNameFiles() {
        List<Cancion> nombreFichero = new ArrayList<>();
        String sql = "SELECT nombre_fichero from canciones";
        conectar.conectar();

        try(Statement stm = conectar.createStatement();
            ResultSet rs = stm.executeQuery(sql)) {
            while (rs.next()) {
                Cancion cancion = new Cancion();
                cancion.setNombreFichero(rs.getString("nombre_fichero"));
                nombreFichero.add(cancion);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return nombreFichero;
    }

    @Override
    public int lastId() {
        conectar.conectar();
        String sql = "SELECT id FROM canciones ORDER BY id DESC LIMIT 1";
        int numero= 0;
        try {
            Statement stm = conectar.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            if(rs.next()) {
                numero = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return  numero;
    }

    @Override
    public void GetId(int id) {

    }

    @Override
    public void insert(int id,String nombre_cancion, Time duracion, String genero, int artista, String ruta, String nombre_fichero, String album) {
        String SQL_INSERT = "INSERT INTO canciones (id,nombre_cancion,duracion,genero,artista,ruta,nombre_fichero,album) VALUES (?,?,?,?,?,?,?,?)";
        conectar.conectar();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = conectar.createPreparedStatement(SQL_INSERT);
            preparedStatement.setInt(1,id);
            preparedStatement.setString(2,nombre_cancion);
            preparedStatement.setTime(3,duracion);
            preparedStatement.setString(4,genero);
            preparedStatement.setInt(5,artista);
            preparedStatement.setString(6,ruta);
            preparedStatement.setString(7,nombre_fichero);
            preparedStatement.setString(8,album);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
