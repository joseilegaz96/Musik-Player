module com.example.musikplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.commons;
    requires de.jensd.fx.glyphs.iconsfivetwofive;
    requires de.jensd.fx.glyphs.fontawesome;
    requires javafx.media;
    requires jakarta.persistence;
    requires java.sql;

    opens com.example.musikplayer to javafx.fxml;
    opens com.example.musikplayer.Controller to javafx.fxml;
    opens com.example.musikplayer.Repositorio.DAO to javafx.base;

    exports com.example.musikplayer;
    exports com.example.musikplayer.Controller;
}