package com.example.musikplayer.Controller;

import com.example.musikplayer.Main;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;


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
        iconPlay.setVisible(false);
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


    public void playSong(ActionEvent actionEvent) {
        progressBar();
        TableViewController tableViewController = Main.saveFxml.getController();

        if(mp == null) {
            media = new Media(new File(userDir + "/" + tableViewController.getRuta()).toURI() + "/" + tableViewController.getNombreCancion());
            mp = new MediaPlayer(media);
            mp.play();

            /*txtNombreArtista.setText(tableViewController.getNombreArtista());
            txtNombreCancion.setText(tableViewController.getNombreCancion());*/
        }

        MediaPlayer.Status status = mp.getStatus();
        if(mp != null) {
            if(status.equals(MediaPlayer.Status.PLAYING)) {
                mp.pause();
                cancelTimer();
                /*txtNombreArtista.setText(tableViewController.getNombreArtista());
                txtNombreCancion.setText(tableViewController.getNombreCancion());*/
            } else {
                mp.play();
                progressBar();

            }
        }

        String[] parts = media.getSource().split("/");
        String cargadoDesdeMedia = parts[6];
        String datoClickadoTableView = tableViewController.getNombreCancion();

        if(!cargadoDesdeMedia.equals(datoClickadoTableView)) {
            mp.stop();
            media = new Media(new File(userDir + "/" + tableViewController.getRuta()).toURI() + "/" + tableViewController.getNombreCancion());
            mp = new MediaPlayer(media);
            mp.play();

            /*txtNombreArtista.setText(tableViewController.getNombreArtista());
            txtNombreCancion.setText(tableViewController.getNombreCancion());*/
        }

        mp.currentTimeProperty().addListener((observableValue, oldDuration, newDuration) -> {
            double tiempo = mp.getCurrentTime().toMinutes();
            Time duracio = tableViewController.getDuracion();

            System.out.println(tiempo);
            System.out.println(duracio);
        });


    }

    public void progressBar() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                running = true;

                double current = mp.getCurrentTime().toSeconds();

                double end = media.getDuration().toSeconds();
                System.out.println(current/end);
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
            media = new Media(new File(userDir+"/"+tableViewController.getRuta()).toURI()+"/"+tableViewController.getNombreCancion());
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
            media = new Media(new File(userDir+"/"+tableViewController.getRuta()).toURI()+"/"+tableViewController.getNombreCancion());
            mp = new MediaPlayer(media);

            mp.play();
            /*txtNombreArtista.setText(tableViewController.getNombreArtista());
            txtNombreCancion.setText(tableViewController.getNombreCancion());*/
        }
    }

    public void loadMusic(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("tableViewView.fxml"));
        try {
            Main.mainScene.setCenter(fxmlLoader.load());
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

}
