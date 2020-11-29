package Controller;

import Connection.ConnectionHandler;
import User.PasswordBCrypt;
import User.UserID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tool.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterController {
    @FXML
    TextField f_em_username, f_em_password, f_em_name, f_em_id;
    @FXML
    Label textEmpty; // Tonqs have to create by yourself
    @FXML
    RadioButton checkInvt, checkMana;
    UserID userID;
    private PasswordBCrypt pwdBcrypt;

    @FXML
    public void initialize(){
        this.rank();
    }

    public void btnRegister(ActionEvent actionEvent) throws IOException, SQLException {
        if (checkEmpty() == true){
            userID = new UserID(f_em_id.getText(),f_em_name.getText()
                    ,f_em_username.getText(),f_em_password.getText(),selectedRank()); // v.2 tong add employee's rank
            pwdBcrypt = new PasswordBCrypt(userID);
            pwdBcrypt.encryptPassword();
            userID.setPassword(pwdBcrypt.getHashed()); // encrypt password
            ConnectionHandler connectionHandler = new ConnectionHandler();
            Connection connection = connectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM  employee WHERE Em_username = ? or Em_id= ? ;");
            preparedStatement.setString(1, f_em_username.getText());
            preparedStatement.setString(2, f_em_id.getText());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                String sql = "INSERT INTO employee VALUES (?, ?, ?, ?, ?);";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, userID.getEmployeeId());
                preparedStatement.setString(2, userID.getName());
                preparedStatement.setString(3, userID.getUsername());
                preparedStatement.setString(4, userID.getPassword());
                preparedStatement.setString(5,userID.getRank()); // v2 by tong
                preparedStatement.executeUpdate();
                Button btn = (Button) actionEvent.getSource();
                Stage stage = (Stage) btn.getScene().getWindow();
                setNotic setNotic = new setNoticClass();
                setNotic.showNotic("ลงทะเบียนสำเร็จ","Success!");
                stage.close();
            } else {
                setNotic setNotic = new setNoticClass();
                setNotic.showNotic("ชื่อผู้ใช้หรือรหัสพนักงานซ้ำ กรุณากรอกใหม่อีกครั้ง","Invalid ID");
            }
        }
        else {
            setNotic setNotic = new setNoticClass();
            setNotic.showNotic("กรุณากรอกข้อมูลให้ครบถ้วน","Error");
            //textEmpty.setText("กรุณากรอกข้อมูลให้ครบถ้วน");
        }
    }

    public boolean checkEmpty(){
        if (f_em_username.getText().equals("") || f_em_password.getText().equals("") ||
                f_em_name.getText().equals("") || f_em_id.getText().equals("")){
            return false;
        }
        else{
            return true;
        }
    }

    public ToggleGroup rank(){
        setToggle setToggle = new setToggleClass();
        ToggleGroup toggleGroup = new ToggleGroup();
        setToggle.setToggle(checkMana,"Manager",toggleGroup);
        setToggle.setToggle(checkInvt,"Inventory",toggleGroup);
        return  toggleGroup;
    }
    public String selectedRank(){
        checkEmpty checkEmpty = new CheckEmptyClass();
        return checkEmpty.checkSelectedRiobtn(rank());
    }
}
