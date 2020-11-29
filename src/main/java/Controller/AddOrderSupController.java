package Controller;

import Connection.ConnectionHandler;
import Objects.Warehouse;
import Objects.WarehouseList;
import SupOrder.Supplier;
import User.UserID;
import Objects.Product;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.util.ArrayList;

public class AddOrderSupController {
    @FXML
    TextField f_l_id, f_l_supName, f_l_supPhone;
    @FXML
    TextArea f_l_supAddress;
    @FXML
    DatePicker f_l_save_date, f_l_got_date;
    @FXML
    TableView f_products;
    @FXML
    TableColumn<Product, String> idProducts, nameProducts;
    @FXML
    TableColumn<Product, Double> amountProducts;
    public static WarehouseList warehouseList;
    ArrayList<WarehouseList> warehouseListArrayList;


    @FXML
    ObservableList<Product> productObservableList = FXCollections.observableArrayList();
    UserID userID;
    Supplier supplier; // v2 by tong

    public void setUserID(UserID userID) {
        this.userID = userID;
    }

    public static Product product;

    public void initialize() {
        warehouseListArrayList = new ArrayList<>();
        idProducts.setCellValueFactory((TableColumn.CellDataFeatures<Product, String> p) -> new SimpleStringProperty(p.getValue().getProductId()));
        amountProducts.setCellValueFactory((TableColumn.CellDataFeatures<Product, Double> p) -> new SimpleDoubleProperty(p.getValue().getAmount()).asObject());
        nameProducts.setCellValueFactory((TableColumn.CellDataFeatures<Product, String> p) -> new SimpleStringProperty(p.getValue().getName()));
        f_l_save_date.setValue(LocalDate.now());
        f_l_save_date.setDisable(true);
        //v2 by tong
        f_l_got_date.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) > 0  || date.compareTo(today) < -1); // not sure by tong
            }
        });

    }




    public void btnAddProduct(ActionEvent actionEvent) throws IOException{
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AddProductFromLotPage.fxml"));
        stage.setScene(new Scene( (Parent) fxmlLoader.load(),900,600));
        AddProductFromLotController addProductController = fxmlLoader.getController();
        stage.showAndWait();

        if (product !=null && warehouseList != null) {
            System.out.println(warehouseListArrayList.size());
            warehouseListArrayList.add(warehouseList);
            productObservableList.add(product);
            f_products.setItems(productObservableList);
            product = null;
            warehouseList = null;
        }
    }

    public void btnSubmit(ActionEvent actionEvent) throws IOException, SQLException {
        ConnectionHandler connectionHandler = new ConnectionHandler();
        Connection connection = connectionHandler.getConnection();

        if (productObservableList.size() == 0) {
            setNotic setNotic = new setNoticClass();
            setNotic.showNotic("กรุณาเพิ่มสินค้า เพื่อไปต่อ","Error");
        } else {
            String sql = "INSERT INTO suppiler (s_name, s_phone, s_address) VALUES (?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, f_l_supName.getText());
            preparedStatement.setString(2, f_l_supPhone.getText());
            preparedStatement.setString(3, f_l_supAddress.getText());
            preparedStatement.executeUpdate();
            PreparedStatement ps = connection.prepareStatement("SELECT s_id FROM suppiler WHERE s_name = \"" + f_l_supName.getText() + "\";");
            ResultSet s_id = ps.executeQuery();
            s_id.next();
            sql = "INSERT INTO order_sup VALUES (?,?,?,?);";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, f_l_id.getText());
            preparedStatement.setString(2, s_id.getString(1));
            preparedStatement.setString(3, f_l_got_date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            preparedStatement.setString(4, f_l_save_date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            preparedStatement.executeUpdate();

            for (Product a : productObservableList) {
                String query = "SELECT pd_amount FROM product WHERE pd_id = ?;";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, a.getProductId());
                ResultSet rec = preparedStatement.executeQuery();
                rec.next();
                int productsum = rec.getInt(1) + a.getAmount();
                sql = "UPDATE product SET pd_amount = ? WHERE pd_id = ?;";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, String.valueOf(productsum));
                preparedStatement.setString(2, a.getProductId());
                preparedStatement.executeUpdate();

                sql = "INSERT INTO purchase_list (pu_amount, os_id, pd_id) VALUES (?,?,?);";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, String.valueOf(a.getAmount()));
                preparedStatement.setString(2, f_l_id.getText());
                preparedStatement.setString(3, a.getProductId());
                preparedStatement.executeUpdate();
            }

            for (WarehouseList a : warehouseListArrayList) {
                String query = "INSERT INTO warehouselist (wh_id, pd_id) VALUES (?, ?)";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, a.getWarehouse().getId());
                preparedStatement.setString(2, a.getProduct().getProductId());
                ResultSet rec = preparedStatement.executeQuery();
                rec.next();
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


    public WarehouseList getWarehouseList() {
        return warehouseList;
    }

    public void setWarehouseList(WarehouseList warehouseList) {
        this.warehouseList = warehouseList;
    }

    public ArrayList<WarehouseList> getWarehouseListArrayList() {
        return warehouseListArrayList;
    }

    public void setWarehouseListArrayList(ArrayList<WarehouseList> warehouseListArrayList) {
        this.warehouseListArrayList = warehouseListArrayList;
    }

}
