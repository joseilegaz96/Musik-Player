package com.example.musikplayer.Controller;

import com.example.musikplayer.Repositorio.DAO.Cancion;
import com.example.musikplayer.Repositorio.Implementaciones.CancionesSQL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TableViewController {

    public TextField search;
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
    Cancion cancion = new Cancion();

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


    public String[] selectItem(MouseEvent mouseEvent) {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if(mouseEvent.getClickCount() == 2) {
                String[] cancion = new String[2];
                cancion[0] = getRuta();
                cancion[1] = getNombreCancion();
                return cancion;
            }
        }

        return new String[0];
    }
}