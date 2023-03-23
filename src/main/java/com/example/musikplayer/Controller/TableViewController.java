package com.example.musikplayer.Controller;

import com.example.musikplayer.Repositorio.DAO.Cancion;
import com.example.musikplayer.Repositorio.Implementaciones.CancionesSQL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import java.sql.Time;

public class TableViewController {

    @FXML
    private TableColumn<Cancion, String> columnAlbum;

    @FXML
    private TableColumn<Cancion, String> columnArtista;

    @FXML
    private TableColumn<Cancion, Double> columnDuration;

    @FXML
    private TableColumn<Cancion, String> columnTItle;

    @FXML
    private TableColumn<Cancion, String> columnId;

    @FXML
    private TableView<Cancion> tabla;

    @FXML
    private ObservableList<Cancion> listaDeCanciones = FXCollections.observableArrayList();

    CancionesSQL cancionesSQL = new CancionesSQL();

    public void initialize() {
        System.out.println(cancionesSQL.GetAll());
        columnId.setCellValueFactory(new PropertyValueFactory<Cancion,String>("id"));
        columnAlbum.setCellValueFactory(new PropertyValueFactory<Cancion,String>("album"));
        columnArtista.setCellValueFactory(new PropertyValueFactory<Cancion,String>("artista"));
        columnDuration.setCellValueFactory(new PropertyValueFactory<Cancion,Double>("duracion"));
        columnTItle.setCellValueFactory(new PropertyValueFactory<Cancion,String>("nombreCancion"));
        listaDeCanciones.addAll(cancionesSQL.GetAll());
        tabla.setItems(listaDeCanciones);

    }

    public Time getDuracion() {
        return tabla.getSelectionModel().getSelectedItem().getDuracion();
    }

    public String getRuta() {
        return tabla.getSelectionModel().getSelectedItem().getRuta();
    }
    public String getNombreCancion() {
        return tabla.getSelectionModel().getSelectedItem().getNombreFichero();
    }

    public String getNombreArtista() {
        return tabla.getSelectionModel().getSelectedItem().getArtista();
    }

    public  void nextSong() {
       tabla.getSelectionModel().selectNext();
    }

    public void previusSong() {
        tabla.getSelectionModel().selectPrevious();
    }


    public void selectItem(MouseEvent mouseEvent) {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if(mouseEvent.getClickCount() == 2) {
                System.out.println("aaa");
            }
        }
    }
}