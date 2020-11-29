package Controller;

import Connection.ConnectionHandler;
import User.PasswordBCrypt;
import User.UserID;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tool.setNotic;
import tool.setNoticClass;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditUserController {
    @FXML
    TextField f_em_name,f_em_username, f_em_password, f_em_id, f_em_rank;

    UserID user;
    public void initialize() {
        Platform.runLater(new Runnable() {
            public void run() {
                //ทำให้ f em id แสดงใน text field
                f_em_name.setText(user.getName());
                f_em_id.setText(user.getEmployeeId());
                f_em_username.setText(user.getUsername());
                f_em_rank.setText(user.getRank());
            }
        });
    }

    public void btnBack(ActionEvent actionEvent) throws IOException {
        Button btn = (Button) actionEvent.getSource();
        Stage stage = (Stage) btn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/HomePage.fxml"));
        stage.setScene(new Scene((Parent)fxmlLoader.load(),900,600));
        HomeController homeController = fxmlLoader.getController();
        homeController.setUser(user);
        stage.show();
    }

    public void btnSubmit(ActionEvent actionEvent) throws IOException, SQLException {
        // save as to db
        // อาจจะต้อง find หา id user จากนั้นค่อนเขียนค่าทับ
        // เอันไหนใส่มาก็เขียนลง db อันไหนไม่ใส่ก็ไม่เขียนลง
        String hashed = user.getPassword();
        if (!f_em_password.getText().equals("")){
            PasswordBCrypt passwordBCrypt = new PasswordBCrypt(user);
            passwordBCrypt.encryptPassword();
            hashed = passwordBCrypt.getHashed();
        }

        user = new UserID(f_em_id.getText(), f_em_name.getText(), f_em_username.getText(), hashed, f_em_rank.getText());
        ConnectionHandler connectionHandler = new ConnectionHandler();
        Connection connection = connectionHandler.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE employee SET em_name = ?, em_username = ?, em_pwd = ? WHERE em_id = ?;");
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getUsername());
        preparedStatement.setString(3, user.getPassword());
        preparedStatement.setString(4, user.getEmployeeId());
        preparedStatement.executeUpdate();

        if (!f_em_name.getText().equals("")){

        }
        else if (!f_em_username.getText().equals("")){

        }
        else if (!f_em_password.getText().equals("")){

        }
        Button btn = (Button) actionEvent.getSource();
        Stage stage = (Stage) btn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/HomePage.fxml"));
        stage.setScene(new Scene((Parent)fxmlLoader.load(),900,600));
        HomeController homeController = fxmlLoader.getController();
        homeController.setUser(user);
        setNotic setNotic = new setNoticClass();
        setNotic.showNotic("แก้ไขเสร็จสิ้นแล้ว", "Success!");
        stage.show();
    }

    public void btnDeleteUser(ActionEvent actionEvent) throws IOException, SQLException {
        ConnectionHandler connectionHandler = new ConnectionHandler();
        Connection connection = connectionHandler.getConnection();
        setNotic setNotic = new setNoticClass();
        if (setNotic.showComfirm("ต้องการลบหรือไม่","Confirmation")) {
            String sql = "DELETE FROM employee WHERE Em_id=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, f_em_id.getText());
            preparedStatement.executeUpdate();
            setNotic.showNotic("ลบบัญชีผู้ใช้เสร็จสิ้น กำลังย้อนไปยังหน้า login", "Success!");
            Button btn = (Button) actionEvent.getSource();
            Stage stage = (Stage) btn.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/loginPage.fxml"));
            stage.setScene(new Scene((Parent)fxmlLoader.load(),900,600));
            LoginController LoginController = fxmlLoader.getController();
            stage.show();
        }
    }

    public void setUser(UserID user){
        this.user = user;
    }


}