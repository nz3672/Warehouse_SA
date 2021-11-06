package Controller;

import Connection.ConnectionHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/loginPage.fxml"));
        primaryStage.setTitle("SupOrder SupOrder");
        primaryStage.setScene(new Scene(root, 900, 600));
        ConnectionHandler connectionHandler = new ConnectionHandler();
        connectionHandler.getConnection();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
