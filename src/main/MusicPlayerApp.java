package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import audio.MP3Player;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayerApp extends Application {
    private MP3Player mp3Player;
    private List<String> playlist = new ArrayList<>();
    private ListView<String> playlistView = new ListView<>();

    @Override
    public void start(Stage primaryStage) {
        // Gombok létrehozása
        Button playButton = new Button("Play");
        Button pauseButton = new Button("Pause");
        Button stopButton = new Button("Stop");
        Button addFolderButton = new Button("Add Folder");

        // Hangerő-szabályozó csúszka
        Slider volumeSlider = new Slider(0, 1, 0.5);
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);

        // Mappabetöltő funkció hozzáadása
        DirectoryChooser directoryChooser = new DirectoryChooser();
        addFolderButton.setOnAction(event -> {
            File folder = directoryChooser.showDialog(primaryStage);
            if (folder != null && folder.isDirectory()) {
                File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp3"));
                if (files != null) {
                    playlist.clear();
                    playlistView.getItems().clear();
                    for (File file : files) {
                        playlist.add(file.getAbsolutePath());
                        playlistView.getItems().add(file.getName());
                    }
                }
            }
        });

        // Gombok eseménykezelése
        playButton.setOnAction(event -> {
            String selectedSong = playlistView.getSelectionModel().getSelectedItem();
            if (selectedSong != null) {
                if (mp3Player != null) {
                    mp3Player.stop(); // Állítsuk meg az előző lejátszást
                }
                String filePath = playlist.get(playlistView.getSelectionModel().getSelectedIndex());
                mp3Player = new MP3Player(filePath);
                mp3Player.play();
                mp3Player.setVolume(volumeSlider.getValue());
            }
        });
        pauseButton.setOnAction(event -> {
            if (mp3Player != null) mp3Player.pause();
        });
        stopButton.setOnAction(event -> {
            if (mp3Player != null) mp3Player.stop();
        });

        // Hangerő csúszka eseménykezelése
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (mp3Player != null) mp3Player.setVolume(newValue.doubleValue());
        });

        // Elrendezés beállítása
        HBox buttonBox = new HBox(10, playButton, pauseButton, stopButton, addFolderButton);
        VBox root = new VBox(15, buttonBox, volumeSlider, playlistView);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Music Player App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
