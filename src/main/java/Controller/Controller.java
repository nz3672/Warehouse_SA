package Controller;

import Connection.ConnectionHandler;
import User.PasswordBCrypt;
import User.UserID;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.sql.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Controller {
    @FXML
    public TextField f_em_username, f_em_password;
    public Label textTest;
    private UserID userID;
    private PasswordBCrypt pwdBcrypt;

    public void btnLoginHandler(ActionEvent actionEvent) throws SQLException, IOException {//with db
        ConnectionHandler connectionHandler = new ConnectionHandler();
        Connection connection = connectionHandler.getConnection();

        String sql = "SELECT * FROM  employee WHERE Em_username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, f_em_username.getText());
        ResultSet rec = preparedStatement.executeQuery();
        boolean checkpwd = false;


        if (!rec.next()) {
            showLoginResultDialog("Wrong username or password",null,"Login failed");
        } else {
                checkpwd = BCrypt.checkpw(f_em_password.getText(), rec.getString(4));
                System.out.println(checkpwd);
                if (checkpwd) {
                    showLoginResultDialog("Login successful!", null, "Successful");
                    userID = new UserID(rec.getString(1), rec.getString(2), rec.getString(3), rec.getString(4));
                    Button btn = (Button) actionEvent.getSource();
                    Stage stage = (Stage) btn.getScene().getWindow();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/homePage.fxml"));
                    stage.setScene(new Scene((Parent) fxmlLoader.load(), 900, 600));
                    HomeController controller = fxmlLoader.getController();
                    controller.setUser(userID);
                    stage.show();
                } else {
                    showLoginResultDialog("Wrong username or password",null,"Login failed");
                }

        }
    }

    private void showLoginResultDialog(String info, String header, String title) {//show login result
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(info);
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    public void btnForRegis(ActionEvent actionEvent) throws IOException { // going to register page
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/registerPage.fxml"));
        stage.setScene(new Scene((Parent) fxmlLoader.load(),900,600));
        RegisterController registerController = fxmlLoader.getController();
        stage.show();
    }
}
