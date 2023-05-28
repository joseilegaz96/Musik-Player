package com.example.musikplayer.Controller;

import com.example.musikplayer.Conexion.ConexionSQL;
import com.example.musikplayer.Main;
import com.example.musikplayer.Repositorio.DAO.Artista;
import com.example.musikplayer.Repositorio.DAO.Cancion;
import com.example.musikplayer.Repositorio.Implementaciones.ArtistaSQL;
import com.example.musikplayer.Repositorio.Implementaciones.CancionesSQL;
import com.mpatric.mp3agic.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.*;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class MainController {

    public Button btnPlayPuase;
    public Text txtNombreCancion;
    public Text txtNombreArtista;
    public Slider volumen;
    public TextField txtFieldLista;
    public ListView listaCanciones;
    public FontAwesomeIconView iconPlay;
    public ProgressBar durationBar;
    public Button btnVolume;
    private Timer timer;
    private TimerTask task;

    private boolean running;
    private Media media;
    private MediaPlayer mp;
    private String userDir = System.getProperty("user.home");


    private ObservableList<String> savePlayListName = FXCollections.observableArrayList();

    public void  initialize() {
        crearDirecotorio();
        aniadirCanciones();
        eliminarCanciones();
        listaCanciones.setBackground(new Background(new BackgroundFill(Color.valueOf("#232323"), null, null)));
        listaCanciones.setVisible(false);
        txtFieldLista.setVisible(false);
        txtFieldLista.setManaged(false);

        volumen.setValue(100);
        volumen.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                mp.setVolume(volumen.getValue() * 0.01);
            }
        });
    }

    public ArrayList<String> comprobarCanciones() {
        String ruta = userDir + "/Musik";
        File file = new File(ruta);
        String[] arrayFicheroDirectorio = file.list();

        ArrayList<String> cancionesBaseDatos = new ArrayList<>();
        List<String> cancionesDirectorio = Arrays.asList(arrayFicheroDirectorio);
        ArrayList<String> diferencias = new ArrayList<>(cancionesDirectorio);
        ArrayList<String> anydirCanciones = new ArrayList<>();

        CancionesSQL cancionesSQL = new CancionesSQL();
        cancionesSQL.GetNameFiles().forEach(cancion -> {
            cancionesBaseDatos.add(cancion.getNombreFichero());
        });

        diferencias.removeAll(cancionesBaseDatos);

        Iterator<String> iterator = diferencias.iterator();
        while (iterator.hasNext()) {
             anydirCanciones.add(iterator.next());
        }
        return anydirCanciones;
    }


    public void aniadirCanciones() {
        ArrayList<String> listaCanciones = comprobarCanciones();
        Mp3File mp3file = null;
        for (String canciones : listaCanciones) {
            try {
            mp3file = new Mp3File(userDir + "/Musik/"+canciones);
            CancionesSQL cancionesSQL = new CancionesSQL();
            int id = cancionesSQL.lastId();
                if (mp3file.hasId3v2Tag()) {
                    ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                    int duracion = (int)(mp3file.getLengthInMilliseconds()/1000);
                    Time time = new Time(0,duracion/60,duracion%60);
                    int artista = obtenerArtista(id3v2Tag.getArtist());
                    String titulo = id3v2Tag.getTitle();
                    String album =  id3v2Tag.getAlbum();
                    String genero = id3v2Tag.getGenreDescription();



                    if(titulo == null) {
                        titulo = canciones;
                    }

                    if(album == null) {
                        album = "Desconocido";
                    }

                    cancionesSQL.insert(id+1,titulo,time,genero,artista,"Musik",canciones,album);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedTagException e) {
                throw new RuntimeException(e);
            } catch (InvalidDataException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int obtenerArtista(String nombreArtista) {
        if(nombreArtista == null ) {
           return 1;
        } else {
            ArtistaSQL artistaSQL = new ArtistaSQL();
            Artista nuevoArstista = artistaSQL.GetAllArtista(nombreArtista);
            if(nuevoArstista == null) {
                artistaSQL.InsertarArtista(nombreArtista);
                Artista crearArtista = artistaSQL.GetAllArtista(nombreArtista);
                return crearArtista.getId();
            }
            return nuevoArstista.getId();
        }
    }
    public void eliminarCanciones() {}

    public void crearDirecotorio() {
        File directorio = new File(userDir+"/Musik3");
        if(!directorio.exists()) {
            directorio.mkdirs();
        }
    }

    public void playSong(ActionEvent actionEvent) {
        progressBar();
        TableViewController tableViewController = Main.saveFxml.getController();

        if(mp == null) {
            File file = new File(userDir + "/" + tableViewController.getRuta() + "/" + tableViewController.getNombreCancion());
            media = new Media(file.toURI().toASCIIString());
            mp = new MediaPlayer(media);
            mp.play();
        }

        MediaPlayer.Status status = mp.getStatus();



        if(!tableViewController.getNombreCancion().equals(mp.getMedia())) {
            if(!mp.equals(MediaPlayer.Status.PLAYING))
            mp.stop();
            File file = new File(userDir + "/" + tableViewController.getRuta() + "/" + tableViewController.getNombreCancion());
            media = new Media(file.toURI().toASCIIString());
            mp = new MediaPlayer(media);
            mp.play();
        }


        if(mp.equals(MediaPlayer.Status.PLAYING)) {
            mp.pause();
        } else {
            mp.play();
        }
    }




    public void progressBar() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                running = true;
                double current = mp.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                durationBar.setProgress(current/end);
                if(current/end == 1) {
                    cancelTimer();
                }
            }
        };
        timer.scheduleAtFixedRate(task,1000,1000);
    }

    public void cancelTimer() {
        try {
            running = false;
            timer.cancel();
        } catch (Exception e) {

        }

    }

    public void nextSong(ActionEvent actionEvent) {
        TableViewController tableViewController = Main.saveFxml.getController();
        MediaPlayer.Status status = mp.getStatus();

        if(status.equals(MediaPlayer.Status.PLAYING) || (status.equals(MediaPlayer.Status.PAUSED))) {
            mp.stop();
            tableViewController.nextSong();
            File file = new File(userDir + "/" + tableViewController.getRuta() + "/" + tableViewController.getNombreCancion());
            media = new Media(file.toURI().toASCIIString());
            mp = new MediaPlayer(media);
            mp.play();

            /*txtNombreArtista.setText(tableViewController.getNombreArtista());
            txtNombreCancion.setText(tableViewController.getNombreCancion());*/
        }
    }

    public void previusSong(ActionEvent actionEvent) {
        TableViewController tableViewController = Main.saveFxml.getController();
        MediaPlayer.Status status = mp.getStatus();

        if (status.equals(MediaPlayer.Status.PLAYING) || (status.equals(MediaPlayer.Status.PAUSED))) {
            mp.stop();
            tableViewController.previusSong();
            File file = new File(userDir + "/" + tableViewController.getRuta() + "/" + tableViewController.getNombreCancion());
            media = new Media(file.toURI().toASCIIString());
            mp = new MediaPlayer(media);
            mp.play();


            /*txtNombreArtista.setText(tableViewController.getNombreArtista());
            txtNombreCancion.setText(tableViewController.getNombreCancion());*/
        }
    }

    public void loadMusic(ActionEvent actionEvent) {
        try {
            AnchorPane fxmlLoader = FXMLLoader.load(getClass().getClassLoader().getResource("resources/tableViewView.fxml"));
            Main.mainScene.setCenter(fxmlLoader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void loadSearch(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("search.fxml"));
        try {
            Main.mainScene.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void visivilityName(ActionEvent actionEvent) {
        txtFieldLista.setVisible(true);
        txtFieldLista.setManaged(!txtFieldLista.isManaged());
    }

    public void addPlayList(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER) {
            savePlayListName.add(txtFieldLista.getText());
            listaCanciones.setVisible(true);
            listaCanciones.setItems(savePlayListName);
            txtFieldLista.setVisible(false);
            txtFieldLista.setManaged(!txtFieldLista.isManaged());
            txtFieldLista.setText("");
            txtFieldLista.setPromptText("Nombre Playlist");
        }

    }

    public void muteVolume(ActionEvent actionEvent) {
        if (volumen.getValue() == 0) {
            volumen.setValue(100);
        }else if(volumen.getValue() == 100) {
            volumen.setValue(0);
        }
    }

    public void editValue(MouseEvent mouseEvent) {
    }

    public void deleteValue(ActionEvent actionEvent) {
    }

    public void loadBuscar(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/searhView.fxml"));
        Main.mainScene.setCenter(root);
    }

    public void loadHome(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/tableViewView.fxml.fxml"));
        Main.mainScene.setCenter(root);
    }

    public void pauseSong(MouseEvent mouseEvent) {
        TableViewController tableViewController = Main.saveFxml.getController();
        MediaPlayer.Status status = mp.getStatus();


    }
}
