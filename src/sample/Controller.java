package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.BackgroundFill;
//import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

//import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;


public class Controller {
    Stage primaryStage;
    boolean muted = false;
    MediaPlayer audioPlayer;
    File selectedFile;
    @FXML
    private Label volumeLabel;

    @FXML
    private StackPane logoPane;
    @FXML
    private Label fileName, totalDuration, currentDuration;
    @FXML
    private ImageView volumeIcon, imageView;

    @FXML
    private Slider slider;
    @FXML
    private Slider durationBar;
    @FXML
    private MediaView mediaView;

    @FXML
    void closeFile(ActionEvent event) {
        stopMedia(event);
        selectedFile = null;
        audioPlayer = null;
    }

    @FXML
    void exitProgram(ActionEvent event) {
        System.exit(0);
    }


    public void initialize() {
        slider.valueProperty().addListener(changeEvent-> {
            volumeLabel.setText(String.valueOf((int) slider.getValue()));
            if (audioPlayer != null) {
                audioPlayer.setVolume(slider.getValue() / 100.0);
            }
        });


    }

    @FXML
    void muteVolume(MouseEvent event) {
        try {
            if (!muted) {
                if (audioPlayer != null)
                    audioPlayer.setMute(true);
                volumeIcon.setImage(new Image("file:images\\mute.png"));
                muted = true;
            } else {

                if (audioPlayer != null)
                    audioPlayer.setMute(false);

                volumeIcon.setImage(new Image("file:images\\speakericon.png"));
                muted = false;
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }

    }
    @FXML
    protected void openFile(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open a File");
        fileChooser.getExtensionFilters().addAll
                (
                new FileChooser.ExtensionFilter("Audio files","*.mp3","*.wav"),
                new FileChooser.ExtensionFilter("Video Files","*.mp4"),
                new FileChooser.ExtensionFilter("All Files","*.*")
        );
//        fileChooser.setInitialDirectory(new File("file:/"));
        selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile != null) {
            primaryStage.setTitle(selectedFile.getName());
            loadMedia();
        }
    }
    public void reset() {
        audioPlayer.stop();
        audioPlayer.dispose();
        totalDuration.setText("0:00");
        currentDuration.setText("0:00");
        durationBar.setValue(0);
    }
    protected void loadMedia() {
        if(selectedFile == null)
            return;
        try {
            String source = selectedFile.toURI().toURL().toExternalForm();
            if(audioPlayer != null)
                reset();
            audioPlayer = new MediaPlayer(new Media(source));
            audioPlayer.setAutoPlay(true);
            volumeLabel.setText(String.valueOf(((int)(double) audioPlayer.getVolume() * 100)));
            slider.setValue(audioPlayer.getVolume() * 100);
            System.out.println(fileName);
            System.out.println(selectedFile);
            fileName.setText(selectedFile.getName());
            if(isAudioFile(selectedFile.getName()))
            {
                mediaView.setVisible(false);
                imageView.setImage(new Image("file:images\\music.png"));
                imageView.setVisible(true);
            }
            else if(selectedFile.getName().endsWith("mp4")) {
                logoPane.setVisible(false);
                imageView.setVisible(false);
                mediaView.setMediaPlayer(audioPlayer);
                mediaView.setVisible(true);
            }
            audioPlayer.setOnReady(new Runnable() {
                @Override
                public void run() {
                    String text = ((int)audioPlayer.getTotalDuration().toSeconds()/60 < 10)? "0":"";
                    text += String.valueOf((int)audioPlayer.getTotalDuration().toSeconds()/60);
                    text += ": ";
                    text += ((int)audioPlayer.getTotalDuration().toSeconds()%60 < 10)? "0":"";
                    text += String.valueOf((int)audioPlayer.getTotalDuration().toSeconds()%60);
                    totalDuration.setText(text);
                    durationBar.setMax(audioPlayer.getTotalDuration().toSeconds());
                    mediaView.setMediaPlayer(audioPlayer);
//                    mediaView.fitHeightProperty().bind(mediaView.getParent().);
//                    mediaView.fitWidthProperty().bind(mediaView.getParent().localToParentTransformProperty());
                    audioPlayer.play();
                    durationBar.setDisable(false);
                    durationBar.setStyle("-fx-control-inner-background:  #0377fc;");
                }
            });

            audioPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration t1) {
                    String text = ((int)t1.toSeconds()/60 < 10)? "0":"";
                    text += String.valueOf((int)t1.toSeconds()/60);
                    text += ": ";
                    text += ((int)t1.toSeconds()%60 < 10)? "0":"";
                    text += String.valueOf((int)t1.toSeconds()%60);
                    currentDuration.setText(text);
                    durationBar.setValue(t1.toSeconds());
//                    currentDuration.setText(String.format(((t1.toSeconds()/60)<10)"0":""+"%d"+(((int)t1.toSeconds()%60<10)?"0":"")+"%d",
//                            (int)t1.toSeconds()/60,(int)t1.toSeconds()%60));
                    //System.out.println(t1);
                }
            });
        }
        catch (MalformedURLException e) {
            System.out.println("Malformed URL");
        }
    }

    @FXML
    protected void onMouseClicked(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        double width = durationBar.getWidth();
        double percentage = (x/width);

        audioPlayer.pause();
        audioPlayer.seek(new Duration(percentage*(audioPlayer.getTotalDuration().toSeconds()*1000)));
        audioPlayer.play();

    }

    boolean isAudioFile(String fileName) {
        return fileName.endsWith("mp3") || fileName.endsWith("wav");
    }


    @FXML
    void pauseMedia(ActionEvent event) {
        if(audioPlayer != null)
            audioPlayer.pause();

    }

    @FXML
    void playMedia(ActionEvent event) {
        if(selectedFile == null)
            openFile(event); //opens and play
        else
            audioPlayer.play();
    }


    @FXML
    void stopMedia(ActionEvent event) {
        if(audioPlayer != null)
            audioPlayer.stop();
    }


    public void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }
    public Slider getSlider() {
        return slider;
    }
}