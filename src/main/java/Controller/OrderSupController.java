package Controller;

import Connection.ConnectionHandler;
import CustomerOrder.OrderCustomer;
import SupOrder.OrderSupplier;
import SupOrder.PurchaseList;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderSupController {
    @FXML
    Label supId, supName,  emId, supPhone;
    @FXML
    TextArea supAddress;
    @FXML
    TableView historyProduct;
    @FXML
    TableColumn<PurchaseList, String> pdId, pdName;
    @FXML
    TableColumn<PurchaseList, Integer> purchaseQuantity;
    @FXML
            TableColumn<PurchaseList, Double> purchasePrice;
    OrderSupplier orderSupplier;
    Connection connection;
    ObservableList<PurchaseList> observableList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ConnectionHandler connectionHandler = new ConnectionHandler();
                connection = connectionHandler.getConnection();

                pdId.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseList, String> p) -> new SimpleStringProperty(p.getValue().getProduct().getProductId()));
                pdName.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseList, String> p) -> new SimpleStringProperty(p.getValue().getProduct().getName()));
                purchaseQuantity.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseList, Integer> p) -> new SimpleIntegerProperty(p.getValue().getpAmount()).asObject());
                purchasePrice.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseList, Double> p) -> new SimpleDoubleProperty(p.getValue().getProduct().getPrice()).asObject());

                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM purchase_list WHERE os_id = ?;");
                    preparedStatement.setString(1, orderSupplier.getLotId());
                    ResultSet rec = preparedStatement.executeQuery();


                    while (rec.next()) {
                        observableList.add(new PurchaseList(rec.getString(1), orderSupplier, Integer.parseInt(rec.getString(2))));
                    }
                } catch (SQLException e) {

                }

                historyProduct.setItems(observableList);

                supId.setText(orderSupplier.getLotId());
                emId.setText(orderSupplier.getEmployee_name());
                supName.setText(orderSupplier.getSupplier().getName());
                supPhone.setText(orderSupplier.getSupplier().getPhone());
                supAddress.setText(orderSupplier.getSupplier().getAddress());
            }
        });
    }

    public void btnBack(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.close();

    }

    public void setOrderSupplier(OrderSupplier orderSupplier) {
        this.orderSupplier = orderSupplier;
    }
}

