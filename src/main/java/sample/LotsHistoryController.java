package sample;

import Connection.ConnectionHandler;
import User.UserID;
import Warehouse.Has;
import Warehouse.Lots;
import Warehouse.Product;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
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
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import tool.setNotic;
import tool.setNoticClass;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LotsHistoryController {

    @FXML
    Label noResult;
    @FXML
    TableView historyProduct;
    @FXML
    TableColumn<Has, String> idProducts;
    @FXML
    TableColumn<Has, Double> amountProducts;
    @FXML
    TableColumn<Has, String> idLots;
    ObservableList observableList = FXCollections.observableArrayList();
    UserID userID;

    public void setUserID(UserID userID) {
        this.userID = userID;
    }

    @FXML
    public void initialize() {
        idProducts.setCellValueFactory((TableColumn.CellDataFeatures<Has, String> p) -> new SimpleStringProperty(p.getValue().getP_id()));
        amountProducts.setCellValueFactory((TableColumn.CellDataFeatures<Has, Double> p) -> new SimpleDoubleProperty(p.getValue().getP_amount()).asObject());
        idLots.setCellValueFactory((TableColumn.CellDataFeatures<Has, String> p) -> new SimpleStringProperty(p.getValue().getL_id()));


        Platform.runLater(new Runnable() {
            public void run() {
                ConnectionHandler connectionHandler = new ConnectionHandler();
                Connection connection = connectionHandler.getConnection();

                try {
                     ResultSet rec = connection.createStatement().executeQuery("SELECT * FROM has");

                    if (!rec.next()){ // ไม่มีข้อมูล
                        setNotic setNotic = new setNoticClass();
                        setNotic.showNotic("ยังไม่มีประวัติการสั่งซื้อสินค้าจาก supplier","Not Found");
                        noResult.setText("ยังไม่มีประวัติการสั่งซื้อสินค้าจาก supplier");
                    }
                    else {
                        //แสดงตาราง HAS ทั้งหมด
                        while (rec.next()) {
                            observableList.add(new Has(rec.getString(1), rec.getString(3), Double.parseDouble(rec.getString(2))));
                        }
                        historyProduct.setItems(observableList);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
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
