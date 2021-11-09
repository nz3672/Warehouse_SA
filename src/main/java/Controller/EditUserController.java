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
import org.springframework.util.StringUtils;
import tool.checkString;
import tool.checkStringClass;
import tool.setNotic;
import tool.setNoticClass;


import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditUserController {
    @FXML
    TextField f_em_name,f_em_username, f_em_password, f_em_id, f_em_rank;

    UserID user;
    checkString checkString;
    public void initialize() {
        Platform.runLater(new Runnable() {
            public void run() {
                //ทำให้ f em id แสดงใน text field
                checkString = new checkStringClass();
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
        stage.setScene(new Scene((Parent)fxmlLoader.load(),1100,600));
        HomeController homeController = fxmlLoader.getController();
        homeController.setUser(user);
        stage.show();
    }

    public void btnSubmit(ActionEvent actionEvent) throws IOException, SQLException {
        // save as to db
        // อาจจะต้อง find หา id user จากนั้นค่อนเขียนค่าทับ
        // เอันไหนใส่มาก็เขียนลง db อันไหนไม่ใส่ก็ไม่เขียนลง
        String hashed = user.getPassword();
        if (StringUtils.hasText(f_em_password.getText())){
            PasswordBCrypt passwordBCrypt = new PasswordBCrypt(user);
            System.out.println(f_em_password.getText());
            passwordBCrypt.encryptPassword();
            hashed = passwordBCrypt.getHashed();
        }

        user = new UserID(f_em_id.getText(), f_em_name.getText(), f_em_username.getText(), hashed, f_em_rank.getText());
        ConnectionHandler connectionHandler = new ConnectionHandler();
        Connection connection = connectionHandler.getConnection();
        if (!checkString.checkString(f_em_name.getText()) || !(f_em_name.getText().length()<=100 && f_em_name.getText().length()>=1)
                || !(f_em_password.getText().length()<=14 && f_em_password.getText().length()>=5 || !StringUtils.hasText(f_em_password.getText()))
                || (StringUtils.containsWhitespace(f_em_password.getText()) && f_em_password.getText().length() != 0)){
            setNotic setNotic = new setNoticClass();
            setNotic.showNotic("กรุณากรอกข้อมูลให้ถูกต้อง","Error");
        }
        else if (!(f_em_password.getText().length()<=14 && f_em_password.getText().length()>=5 || !StringUtils.hasText(f_em_password.getText()))
                || !(f_em_name.getText().length()<=100 && f_em_name.getText().length()>=1)){
            setNotic setNotic = new setNoticClass();
            setNotic.showNotic("ข้อมูลไม่ครบถ้วน","Error");
        }
        else {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE employee SET em_name = ?, em_username = ?, em_pwd = ? WHERE em_id = ?;");
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getEmployeeId());
            preparedStatement.executeUpdate();

            Button btn = (Button) actionEvent.getSource();
            Stage stage = (Stage) btn.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/HomePage.fxml"));
            stage.setScene(new Scene((Parent)fxmlLoader.load(),1100,600));
            HomeController homeController = fxmlLoader.getController();
            homeController.setUser(user);
            setNotic setNotic = new setNoticClass();
            setNotic.showNotic("แก้ไขเสร็จสิ้นแล้ว", "Success!");
            stage.show();
        }

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
            stage.setScene(new Scene((Parent)fxmlLoader.load(),1100,600));
            LoginController LoginController = fxmlLoader.getController();
            stage.show();
        }
    }

    public void setUser(UserID user){
        this.user = user;
    }




}