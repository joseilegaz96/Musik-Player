package com.example.musikplayer.Controller;

import com.example.musikplayer.Conexion.ConexionSQL;
import com.example.musikplayer.Repositorio.DAO.Cancion;
import com.example.musikplayer.Repositorio.Implementaciones.CancionesSQL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.sql.Time;


public class TableViewController {

    public TextField search;
    public ContextMenu menuItem;
    public Button eliminarCancion;
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

    private String userDir = System.getProperty("user.home");


    public void initialize() {
        //System.out.println(cancionesSQL.GetAll());
        columnId.setCellValueFactory(new PropertyValueFactory<Cancion,String>("id"));
        columnAlbum.setCellValueFactory(new PropertyValueFactory<Cancion,String>("album"));
        columnArtista.setCellValueFactory(new PropertyValueFactory<Cancion,String>("artista"));
        columnDuration.setCellValueFactory(new PropertyValueFactory<Cancion,Double>("duracion"));
        columnTItle.setCellValueFactory(new PropertyValueFactory<Cancion,String>("nombreCancion"));
        listaDeCanciones.addAll(cancionesSQL.GetAllCanciones());
        tabla.setItems(listaDeCanciones);

        FilteredList<Cancion> filteredList = new FilteredList<>(listaDeCanciones, b -> true);

        search.textProperty().addListener((observable, oldValue, newValue) -> {
          filteredList.setPredicate(cancion -> {
              if(newValue == null | newValue.isEmpty()) {
                  return true;
              }

              String lowerFiler = newValue.toLowerCase();


              return cancion.getNombreCancion().toLowerCase().contains(lowerFiler) ||
               cancion.getArtista().toLowerCase().contains(lowerFiler) ;

          });
        });

        SortedList<Cancion> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tabla.comparatorProperty());
        tabla.setItems(sortedList);

    }

    public void addMenu(String itemMenu) {
        menuItem.getItems().add((new MenuItem(itemMenu)));
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

    public void deleteSong(ActionEvent actionEvent) {
        Cancion cancion = tabla.getSelectionModel().getSelectedItem();
        File ficheroCancion = new File(userDir+"/"+getRuta()+"/"+getNombreCancion());

        cancionesSQL.delete(getNombreCancion());

        if(ficheroCancion.delete()) {
            System.out.println("Fichero borrado");
        } else {
            System.out.println("fichero no borrado");
        }

        listaDeCanciones.remove(cancion);

    }
}