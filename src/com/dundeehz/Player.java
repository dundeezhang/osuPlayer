package com.dundeehz;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Player extends Thread {

    private String songLocation;
    public void run()
    {
        Media hit = new Media(new File(songLocation).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
    }

    public void updateLoc(String location) {
        this.songLocation = location;
    }
}