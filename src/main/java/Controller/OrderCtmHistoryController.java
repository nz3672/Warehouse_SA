package Controller;

import Connection.ConnectionHandler;
import CustomerOrder.Customer;
import CustomerOrder.OrderCustomer;
import CustomerOrder.SaleList;
import SupOrder.OrderSupplier;
import SupOrder.PurchaseList;
import SupOrder.Supplier;
import User.UserID;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import tool.setNotic;
import tool.setNoticClass;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class OrderCtmHistoryController {
    @FXML
    TableView historyProduct;
    @FXML
    TableColumn<SaleList,String> idOrder, ctmName, ctmPhone,saveDate,idProducts,productName,amountProducts;
    @FXML
    Label noResult;
    ObservableList<SaleList> observableList = FXCollections.observableArrayList();

    UserID userID;

    public void setUserID(UserID userID) {
        this.userID = userID;
    }

    @FXML
    public void initialize() {


        Platform.runLater(new Runnable() {
            public void run() {
                ConnectionHandler connectionHandler = new ConnectionHandler();
                Connection connection = connectionHandler.getConnection();

                try {
                    ResultSet rec = connection.createStatement().executeQuery("SELECT * FROM sale_list");
                    if (!rec.next()){ // ไม่มีข้อมูล
                        setNotic setNotic = new setNoticClass();
                        setNotic.showNotic("ยังไม่มีประวัติการสั่งซื้อสินค้าจาก customer","Not Found");
                        noResult.setText("ยังไม่มีประวัติการสั่งซื้อสินค้าจาก customer");
                    }
                    else {
                        rec.previous();
                        while (rec.next()) {
                            ResultSet getOrder = connection.createStatement().executeQuery("SELECT * FROM order_customer WHERE oc_id = \""+ rec.getString(3)+"\"");
                            getOrder.next();
                            ResultSet getCustomer = connection.createStatement().executeQuery("SELECT * FROM customer WHERE c_id = " + getOrder.getString(2));
                            getCustomer.next();
                            OrderCustomer orderCustomer = new OrderCustomer(getOrder.getString(1),
                                    new Customer(getCustomer.getString(1), getCustomer.getString(2),
                                            getCustomer.getString(3),getCustomer.getString(4)),
                                    getOrder.getString(3));

                            observableList.add(new SaleList(rec.getString(1), orderCustomer, Integer.parseInt(rec.getString(2))));
                        }
                        historyProduct.setItems(observableList);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        idOrder.setCellValueFactory((TableColumn.CellDataFeatures<SaleList, String> p) -> new SimpleStringProperty(p.getValue().getOrderCustomer().getId()));
        ctmName.setCellValueFactory((TableColumn.CellDataFeatures<SaleList, String> p) -> new SimpleStringProperty(p.getValue().getOrderCustomer().getCustomer().getName()));
        ctmPhone.setCellValueFactory((TableColumn.CellDataFeatures<SaleList, String> p) -> new SimpleStringProperty(p.getValue().getOrderCustomer().getCustomer().getPhone()));
        saveDate.setCellValueFactory((TableColumn.CellDataFeatures<SaleList, String> p) -> new SimpleStringProperty(p.getValue().getOrderCustomer().getDate()));

        idProducts.setCellValueFactory((TableColumn.CellDataFeatures<SaleList, String> p) -> new SimpleStringProperty(p.getValue().getProduct().getProductId()));
        productName.setCellValueFactory((TableColumn.CellDataFeatures<SaleList, String> p) -> new SimpleStringProperty(p.getValue().getProduct().getName()));
        amountProducts.setCellValueFactory((TableColumn.CellDataFeatures<SaleList, String> p) -> new SimpleStringProperty(String.valueOf(p.getValue().getAmount())));

    }

    public void btnBack(ActionEvent actionEvent) throws IOException {
        Button btn = (Button) actionEvent.getSource();
        Stage stage = (Stage) btn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/HomePage.fxml"));
        stage.setScene(new Scene((Parent)fxmlLoader.load(),900,600));
        HomeController controller = fxmlLoader.getController();
        controller.setUser(userID);
        stage.show();
    }
}
