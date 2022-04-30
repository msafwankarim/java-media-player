package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui.fxml"));
        Parent rootNode = loader.load();
//        Parent rootNode = FXMLLoader.load(getClass().getResource("ui.fxml"));
        Controller controller = loader.getController();

        controller.primaryStage = primaryStage;
        Scene scene = new Scene(rootNode,660,480);
        primaryStage.getIcons().add(new Image("file:images/video-player.png"));
        primaryStage.setTitle("SK - Media Player");
        primaryStage.setScene(scene);

        primaryStage.show();

    }



    public static void main(String[] args) {
        launch(args);
    }
}
