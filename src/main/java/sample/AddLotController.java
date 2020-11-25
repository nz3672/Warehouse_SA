package sample;

import Connection.ConnectionHandler;
import User.UserID;
import Warehouse.Product;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tool.setNotic;
import tool.setNoticClass;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class AddLotController {
    @FXML
    TextField f_l_id, f_l_supplier;
    @FXML
    DatePicker f_l_save_date, f_l_got_date;
    @FXML
    TableView f_products;
    @FXML
    TableColumn<Product, String> idProducts, nameProducts;
    @FXML
    TableColumn<Product, Double> amountProducts;

    @FXML
    ObservableList<Product> productObservableList = FXCollections.observableArrayList();
    UserID userID;

    public void setUserID(UserID userID) {
        this.userID = userID;
    }

    public static Product product;

    public void initialize() {
        idProducts.setCellValueFactory((TableColumn.CellDataFeatures<Product, String> p) -> new SimpleStringProperty(p.getValue().getProductId()));
        amountProducts.setCellValueFactory((TableColumn.CellDataFeatures<Product, Double> p) -> new SimpleDoubleProperty(p.getValue().getAmount()).asObject());
        nameProducts.setCellValueFactory((TableColumn.CellDataFeatures<Product, String> p) -> new SimpleStringProperty(p.getValue().getName()));
        //amountProducts.setCellValueFactory(data -> data.getValue().getAmount());
        f_l_save_date.setValue(LocalDate.now());

    }




    public void btnAddProduct(ActionEvent actionEvent) throws IOException{
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AddProductFromLotPage.fxml"));
        stage.setScene(new Scene( (Parent) fxmlLoader.load(),900,600));
        AddProductFromLotController addProductController = fxmlLoader.getController();
        stage.showAndWait();
        productObservableList.add(product);
        f_products.setItems(productObservableList);
    }

    public void btnSubmit(ActionEvent actionEvent) throws IOException, SQLException {
        ConnectionHandler connectionHandler = new ConnectionHandler();
        Connection connection = connectionHandler.getConnection();

        if (productObservableList.size() == 0) {
            setNotic setNotic = new setNoticClass();
            setNotic.showNotic("กรุณาเพิ่มสินค้า เพื่อไปต่อ","Error");
        } else {
            String sql = "INSERT INTO lots VALUES (?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userID.getEmployeeId());
            preparedStatement.setString(2, f_l_id.getText());
            preparedStatement.setString(3, f_l_supplier.getText());
            preparedStatement.setString(4, f_l_got_date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            preparedStatement.setString(5, f_l_save_date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            preparedStatement.executeUpdate();

            for (Product a : productObservableList) {
                String query = "SELECT P_amount FROM product WHERE P_id = ?;";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, a.getProductId());
                ResultSet rec = preparedStatement.executeQuery();
                rec.next();
                double productsum = rec.getDouble(1) + a.getAmount();
                sql = "UPDATE product SET P_amount = ? WHERE P_id = ?;";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, String.valueOf(productsum));
                preparedStatement.setString(2, a.getProductId());
                preparedStatement.executeUpdate();

                sql = "INSERT INTO has VALUES (?,?,?);";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, a.getProductId());
                preparedStatement.setString(2, f_l_id.getText());
                preparedStatement.setString(3, String.valueOf(a.getAmount()));
                preparedStatement.executeUpdate();
            }
            setNotic setNotic = new setNoticClass();
            setNotic.showNotic("เพิ่มรายการสั่งซื้อสำเร็จ","Success!");
            Button btn = (Button) actionEvent.getSource();
            Stage stage = (Stage) btn.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/HomePage.fxml"));
            stage.setScene(new Scene((Parent)fxmlLoader.load(),900,600));
            HomeController homeController = fxmlLoader.getController();
            homeController.setUser(userID);
            stage.show();


        }
    }

    public void btnDeleteProduct(ActionEvent actionEvent){
        f_products.getItems().removeAll(f_products.getSelectionModel().getSelectedItem());
    }

    public void btnBack(ActionEvent actionEvent) throws IOException {
        Button btn = (Button) actionEvent.getSource();
        Stage stage = (Stage) btn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/HomePage.fxml"));
        stage.setScene(new Scene((Parent)fxmlLoader.load(),900,600));
        HomeController homeController = fxmlLoader.getController();
        homeController.setUser(userID);
        stage.show();
    }

    public void setProduct(Product product){
        this.product = product;
    }
}
