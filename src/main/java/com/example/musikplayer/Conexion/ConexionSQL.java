package com.example.musikplayer.Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionSQL {
    private String url = "jdbc:mysql://127.0.0.1:3306/musica";

    private String user = "root";

    private String password = "";
    Connection conectar;

    public void conectar() {
        try {
            conectar = DriverManager.getConnection(url,user,password);
            System.out.println("Conectado");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Statement createStatement() throws SQLException {
        return conectar.createStatement();
    }


    public void desconectar() {
        try {
            conectar.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
