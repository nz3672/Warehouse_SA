package Controller;

import Connection.ConnectionHandler;
import User.UserID;
import Objects.Product;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tool.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddProductController {
    @FXML
    TextField f_p_name,f_p_id, f_p_price;

    @FXML
    ChoiceBox f_type;

    @FXML
    DatePicker f_p_save_date;

    @FXML TableView f_product;
    @FXML TableColumn<Product, String> f_idProduct, f_nameProduct;
    @FXML ObservableList<Product> productObservableList = FXCollections.observableArrayList();
    private Connection connection;
    UserID userID;
    Product product;
    ObservableList<String> group;

    public void initialize() {
        f_idProduct.setCellValueFactory((TableColumn.CellDataFeatures<Product, String> p) -> new SimpleStringProperty(p.getValue().getProductId()));
        f_nameProduct.setCellValueFactory((TableColumn.CellDataFeatures<Product, String> p) -> new SimpleStringProperty(p.getValue().getName()));

        ConnectionHandler connectionHandler = new ConnectionHandler();
        connection = connectionHandler.getConnection();

        f_p_save_date.setValue(LocalDate.now());
        //group choicbox by tong
        group = FXCollections.observableArrayList();
        String sqlType = "SELECT t_name FROM type";
        f_type.setItems(setGroup(group,connection,sqlType));
    }


    public void btnSubmit(ActionEvent actionEvent) throws IOException, SQLException {

        if (productObservableList.size() != 0){


            DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");

            String sql = "INSERT INTO product VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (Product a : productObservableList) {
                preparedStatement.setString(1, String.valueOf(a.getProductId()));
                preparedStatement.setString(2, a.getName());
                preparedStatement.setString(3, String.valueOf(a.getPrice()));
                preparedStatement.setString(4, String.valueOf(a.getAmount()));
                preparedStatement.setString(5, a.getSaveDate());
                PreparedStatement getT_id = connection.prepareStatement("SELECT t_id FROM type WHERE t_name = \""+a.getType()+"\"");
                ResultSet t_id = getT_id.executeQuery();
                t_id.next();
                preparedStatement.setString(6, t_id.getString(1));
                preparedStatement.executeUpdate();
            }

            setNotic setNotic = new setNoticClass();
            setNotic.showNotic("เพิ่มข้อมูลเสินค้าเสร็จสิ้น","Success!");

            Button btnSubToLot = (Button) actionEvent.getSource();
            Stage stage = (Stage) btnSubToLot.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/homePage.fxml"));
            fxmlLoader.load();
            HomeController addLotController = fxmlLoader.getController();
            addLotController.setProduct(product);
            addLotController.setUser(userID);
            stage.close();
        }
        else {
                setNotic setNotic = new setNoticClass();
                setNotic.showNotic("กรุณากรอกสินค้าที่จะเพิ่ม","Error");
        }
    }

    public void btnAddProduct(ActionEvent actionEvent) throws IOException, SQLException {
        PreparedStatement checkP_id = connection.prepareStatement("SELECT pd_id FROM product WHERE pd_id = ?");
        checkP_id.setString(1, f_p_id.getText());
        ResultSet rs = checkP_id.executeQuery();
        boolean checkpID = true;
        for (Product a : productObservableList) {
            if (a.getProductId().equals(f_p_id.getText())) {
                checkpID = false;
                break;
            }
        }
        if (!f_p_id.getText().equals(" ") || !f_p_name.getText().equals(" ") || !f_p_price.getText().equals(" ")) {
            if (!rs.next() && checkpID) {
                product = new Product(f_p_id.getText(), f_p_name.getText(),
                        Integer.parseInt(f_p_price.getText()), f_p_save_date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                product.setType(f_type.getValue().toString());
                productObservableList.add(product);
                f_product.setItems(productObservableList);
            } else {
                setNotic setNotic = new setNoticClass();
                setNotic.showNotic("กรุณากรอกข้อมูลสินค้าให้ครบถ้วน", "Error");
            }
        } else {
            setNotic setNotic = new setNoticClass();
            setNotic.showNotic("กรุณากรอกข้อมูลสินค้าให้ครบถ้วน", "Error");
        }
    }

    public ObservableList<String> setGroup(ObservableList<String> group, Connection connection, String sql){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rec = preparedStatement.executeQuery();
            while (rec.next()) {
                String data = rec.getString(1);
                group.add(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return group;
    }

    public void btnDeleteProduct(ActionEvent actionEvent){
        f_product.getItems().removeAll(f_product.getSelectionModel().getSelectedItem());
    }
}
