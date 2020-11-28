package Controller;

import Connection.ConnectionHandler;
import Objects.Product;
import Objects.Type;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tool.setNotic;
import tool.setNoticClass;


import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class AddTypeController {
    @FXML
    TableView t_Type;
    @FXML
    TableColumn<Type,String> t_idType, t_nameType;
    @FXML
    TextField nameType,idType;
    @FXML
    ObservableList<Type> typeObservableList = FXCollections.observableArrayList();

    private Type type;
    private Connection connection;

    public void initialize(){
        ConnectionHandler connectionHandler = new ConnectionHandler();
        connection = connectionHandler.getConnection();
    }

    public void btnAddType(ActionEvent actionEvent) throws IOException, SQLException {
        PreparedStatement checkP_id = connection.prepareStatement("SELECT pd_id FROM product WHERE pd_id = ?");
        checkP_id.setString(1, idType.getText());
        ResultSet rs = checkP_id.executeQuery();
        boolean checkpID = true;
        for (Type a : typeObservableList) {
            if (a.getId().equals(idType.getText())) {
                checkpID = false;
                break;

            }
        }
        if (idType.getText().equals(" ") || nameType.getText().equals(" ")) {
            if (!rs.next() && checkpID) {
                type = new Type(idType.getText(), nameType.getText());
                typeObservableList.add(type);
                t_Type.setItems(typeObservableList);
            } else {
                setNotic setNotic = new setNoticClass();
                setNotic.showNotic("กรุณากรอกข้อมูลให้ครบถ้วน", "Error");
            }
        } else {
            setNotic setNotic = new setNoticClass();
            setNotic.showNotic("กรุณากรอกข้อมูลให้ครบถ้วน", "Error");
        }

    }

    public void btnSubmit(ActionEvent actionEvent) throws  IOException,SQLException{
        if (typeObservableList.size() != 0){


            DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");

            String sql = "INSERT INTO type VALUES (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (Type a : typeObservableList) {
                preparedStatement.setString(1, String.valueOf(a.getId()));
                preparedStatement.setString(2, a.getName());
                preparedStatement.executeUpdate();
            }

            setNotic setNotic = new setNoticClass();
            setNotic.showNotic("เพิ่มข้อมูลเเสร็จสิ้น","Success!");

            Button btnSubToLot = (Button) actionEvent.getSource();
            Stage stage = (Stage) btnSubToLot.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/homePage.fxml"));
            fxmlLoader.load();
            HomeController addLotController = fxmlLoader.getController();
            stage.close();
        }
        else {
            setNotic setNotic = new setNoticClass();
            setNotic.showNotic("กรุณากรอกให้ครบ","Error");
        }
    }

    public void btnDelete(ActionEvent actionEvent) throws IOException{
        t_Type.getItems().removeAll(t_Type.getSelectionModel().getSelectedItem());
    }
}
