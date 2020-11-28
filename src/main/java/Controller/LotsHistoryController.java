package Controller;

import Connection.ConnectionHandler;
import Objects.Product;
import SupOrder.OrderSupplier;
import SupOrder.Supplier;
import User.UserID;
import SupOrder.PurchaseList;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import tool.setNotic;
import tool.setNoticClass;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LotsHistoryController {

    @FXML
    Label noResult;
    @FXML
    TableView historyProduct;
    @FXML
    TableColumn<PurchaseList, String> idOrder, supName, supNum, dateSave, dateRecieve;
    @FXML
    TableColumn<PurchaseList, String> idProducts, nameProducts,amountProducts;
    ObservableList<PurchaseList> observableList = FXCollections.observableArrayList();
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
                     ResultSet rec = connection.createStatement().executeQuery("SELECT * FROM purchase_list");
                    if (!rec.next()){ // ไม่มีข้อมูล
                        setNotic setNotic = new setNoticClass();
                        setNotic.showNotic("ยังไม่มีประวัติการสั่งซื้อสินค้าจาก supplier","Not Found");
                        noResult.setText("ยังไม่มีประวัติการสั่งซื้อสินค้าจาก supplier");
                    }
                    else {
                        //แสดงตาราง HAS ทั้งหมด
                        rec.previous();
                        while (rec.next()) {
                            ResultSet getOrdersup = connection.createStatement().executeQuery("SELECT * FROM order_sup WHERE os_id = \""+ rec.getString(3)+"\"");
                            getOrdersup.next();
                            ResultSet getSupplier = connection.createStatement().executeQuery("SELECT * FROM suppiler WHERE s_id = " + getOrdersup.getString(2));
                            getSupplier.next();
                            OrderSupplier orderSupplier = new OrderSupplier(getOrdersup.getString(1),new Supplier(getSupplier.getString(1),getSupplier.getString(2),getSupplier.getString(3),getSupplier.getString(4)),getOrdersup.getString(3) , getOrdersup.getString(4));

                            observableList.add(new PurchaseList(rec.getString(1), orderSupplier, Integer.parseInt(rec.getString(2))));
                        }
                        historyProduct.setItems(observableList);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        idOrder.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseList, String> p) -> new SimpleStringProperty(p.getValue().getOrderSupplier().getLotId()));
        supName.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseList, String> p) -> new SimpleStringProperty(p.getValue().getOrderSupplier().getSupplier().getName()));
        supNum.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseList, String> p) -> new SimpleStringProperty(p.getValue().getOrderSupplier().getSupplier().getPhone()));
        dateSave.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseList, String> p) -> new SimpleStringProperty(p.getValue().getOrderSupplier().getDateSave()));
        dateRecieve.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseList, String> p) -> new SimpleStringProperty(p.getValue().getOrderSupplier().getDateRecieve()));

        idProducts.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseList, String> p) -> new SimpleStringProperty(p.getValue().getProduct().getProductId()));
        nameProducts.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseList, String> p) -> new SimpleStringProperty(p.getValue().getProduct().getName()));
        amountProducts.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseList, String> p) -> new SimpleStringProperty(String.valueOf(p.getValue().getpAmount())));

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
