package com.example.musikplayer.Conexion;

import javafx.scene.control.Alert;

import java.sql.*;

public class ConexionSQL {
    private String url = "jdbc:mysql://127.0.0.1:3306/musica";

    private String user = "root";

    private String password = "";
    Connection conectar;

    public void conectar() {
        try {
            conectar = DriverManager.getConnection(url,user,password);
        } catch (SQLException e) {
          Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage());
          alert.setTitle("SQL Connection Failure");
          alert.setHeaderText("La base de datos no esta arrancada");
          alert.showAndWait();
        }
    }

    public Statement createStatement() throws SQLException {
        return conectar.createStatement();
    }

    public PreparedStatement createPreparedStatement(String sql) throws SQLException {
        return conectar.prepareStatement(sql);
    }


    public void desconectar() {
        try {
            conectar.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
