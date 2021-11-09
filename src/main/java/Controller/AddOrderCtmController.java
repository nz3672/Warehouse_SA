package Controller; //changed to qty

import Connection.ConnectionHandler;
import Objects.Product;
import User.UserID;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import tool.checkString;
import tool.checkStringClass;
import tool.setNotic;
import tool.setNoticClass;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class AddOrderCtmController {
    @FXML
    TextField f_l_id, f_cusName,f_cusPhone;
    @FXML
    TextArea f_cusAddress;
    @FXML
    DatePicker f_l_save_date, f_l_pay_date;
    @FXML
    TableView f_products;
    @FXML
    TableColumn<Product, String> idProducts, nameProducts;
    @FXML
    TableColumn<Product, Double> priceProducts;
    @FXML
    TableColumn<Product, Integer> amountProducts;

    @FXML
    ObservableList<Product> productObservableList = FXCollections.observableArrayList();
    UserID userID;

    public static Product product;
    checkString checkString;

    public void setUserID(UserID userID) {
        this.userID = userID;
    }

    public void initialize() {
        checkString = new checkStringClass();
        idProducts.setCellValueFactory((TableColumn.CellDataFeatures<Product, String> p) -> new SimpleStringProperty(p.getValue().getProductId()));
        amountProducts.setCellValueFactory((TableColumn.CellDataFeatures<Product, Integer> p) -> new SimpleIntegerProperty(p.getValue().getQuantity()).asObject());
        nameProducts.setCellValueFactory((TableColumn.CellDataFeatures<Product, String> p) -> new SimpleStringProperty(p.getValue().getName()));
        priceProducts.setCellValueFactory((TableColumn.CellDataFeatures<Product, Double> p) -> new SimpleDoubleProperty(p.getValue().getPrice()).asObject());
        //amountProducts.setCellValueFactory(data -> data.getValue().getQuantity());
        f_l_save_date.setValue(LocalDate.now());

        //v2 by tong
        f_l_pay_date.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) > 0  || date.compareTo(today.minus(1, ChronoUnit.DAYS)) < 0 ); // not sure by tong
            }
        });

    }

    public void btnAddProduct(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AddProductFromCtmPage.fxml"));
        stage.setScene(new Scene( (Parent) fxmlLoader.load(),1100,600));
        AddProductFromCtmController addProductController = fxmlLoader.getController();
        stage.showAndWait();
        if (product != null) {
            productObservableList.add(product);
            f_products.setItems(productObservableList);
            product = null;
        }
    }

    public void btnSubmit(ActionEvent actionEvent) throws IOException, SQLException {
        ConnectionHandler connectionHandler = new ConnectionHandler();
        Connection connection = connectionHandler.getConnection();

            if (productObservableList.size() == 0 || !this.checkTextfields() || f_l_pay_date.getValue() == null) {
                if (f_cusPhone.getText().length() != 10) {
                    setNotic setNotic = new setNoticClass();
                    setNotic.showNotic("กรุณากรอกเบอร์โทรศัพท์ให้ถูกต้อง", "Error");
                } else if (false/*productObservableList.size() == 0*/) {
                    setNotic setNotic = new setNoticClass();
                    setNotic.showNotic("กรุณาเพิ่มสินค้า เพื่อไปต่อ", "Error");
                } else {
                    setNotic setNotic = new setNoticClass();
                    setNotic.showNotic("กรุณากรอกข้อมูลให้ถูกต้อง", "Error");
                }
            } else {
                String sql = "INSERT INTO customer (c_name, c_phone, c_address) VALUES (?, ?, ?);";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, f_cusName.getText());
                preparedStatement.setString(2, f_cusPhone.getText());
                preparedStatement.setString(3, f_cusAddress.getText());
                preparedStatement.executeUpdate();
                PreparedStatement ps = connection.prepareStatement("SELECT c_id FROM customer WHERE c_name = \"" + f_cusName.getText() + "\";");
                ResultSet c_id = ps.executeQuery();
                c_id.next();
                sql = "INSERT INTO order_customer VALUES (?,?,?,?,?,?,?);";
                preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setString(1, f_l_id.getText());
                preparedStatement.setString(2, c_id.getString(1));
                preparedStatement.setString(3, userID.getEmployeeId());
                preparedStatement.setString(4, f_l_pay_date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                preparedStatement.setString(5, f_l_save_date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                preparedStatement.setString(6, String.valueOf(0));
                preparedStatement.setString(7, "เตรียมสินค้า");
                preparedStatement.executeUpdate();
                double i = 0;
                for (Product a : productObservableList) {
                    String query = "SELECT pd_qty FROM product WHERE pd_id = ?;";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, a.getProductId());
                    ResultSet rec = preparedStatement.executeQuery();
                    rec.next();
                    int productdiff = rec.getInt(1) - a.getQuantity();
                    sql = "UPDATE product SET pd_qty = ? , pd_save_date = ? WHERE pd_id = ?;";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, String.valueOf(productdiff));
                    preparedStatement.setString(2, f_l_save_date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    preparedStatement.setString(3, a.getProductId());
                    preparedStatement.executeUpdate();

                    sql = "INSERT INTO sale_list (sale_qty, oc_id, pd_id, sale_price, sale_seller) VALUES (?,?,?,?,?);";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, String.valueOf(a.getQuantity()));
                    preparedStatement.setString(2, f_l_id.getText());
                    preparedStatement.setString(3, a.getProductId());
                    preparedStatement.setString(4, String.valueOf(a.getPrice()));
                    preparedStatement.setString(5, userID.getName());
                    preparedStatement.executeUpdate();
                    i+=a.getPrice();
                }

                preparedStatement = connection.prepareStatement("UPDATE order_customer SET oc_price = ? WHERE oc_id = ?;");
                preparedStatement.setString(1, String.valueOf(i));
                preparedStatement.setString(2, f_l_id.getText());
                preparedStatement.executeUpdate();
                setNotic setNotic = new setNoticClass();
                setNotic.showNotic("เพิ่มรายการสั่งซื้อสำเร็จ", "Success!");
                Button btn = (Button) actionEvent.getSource();
                Stage stage = (Stage) btn.getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/HomePage.fxml"));
                stage.setScene(new Scene((Parent) fxmlLoader.load(), 1100, 600));
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
        stage.setScene(new Scene((Parent)fxmlLoader.load(),1100,600));
        HomeController homeController = fxmlLoader.getController();
        homeController.setUser(userID);
        stage.show();
    }

    public boolean checkTextfields() {
        if (!checkString.checkString(f_cusAddress.getText(), "./") || f_cusPhone.getText().length() != 10|| !checkString.checkNum(f_cusPhone.getText())
                || !checkString.checkString(f_cusName.getText()) || !checkString.checkString(f_l_id.getText(), "-")) {
            return false;
        }
        return true;
    }

    public void setProduct(Product product){
        this.product = product;
    }
}
