package Controller;

import Connection.ConnectionHandler;
import CustomerOrder.OrderCustomer;
import CustomerOrder.SaleList;
import Objects.Product;
import javafx.application.Platform;
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderCtmController {
    @FXML
    Label ctmId, ctmName,  emId, ctmPhone, ctmTotalPrice;
    @FXML
    TextArea ctmAddress;
    @FXML
    TableView historyProduct;
    @FXML
    TableColumn<SaleList, String> pdId, pdName;
    @FXML
            TableColumn<SaleList, Double> pdPrice, salePrice;
    @FXML
            TableColumn<SaleList, Integer> saleQuantity;
    OrderCustomer orderCustomer;
    Connection connection;
    ObservableList<SaleList> observableList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ConnectionHandler connectionHandler = new ConnectionHandler();
                connection = connectionHandler.getConnection();

                pdId.setCellValueFactory((TableColumn.CellDataFeatures<SaleList, String> p) -> new SimpleStringProperty(p.getValue().getProduct().getProductId()));
                pdName.setCellValueFactory((TableColumn.CellDataFeatures<SaleList, String> p) -> new SimpleStringProperty(p.getValue().getProduct().getName()));
                saleQuantity.setCellValueFactory((TableColumn.CellDataFeatures<SaleList, Integer> p) -> new SimpleIntegerProperty(p.getValue().getAmount()).asObject());
                pdPrice.setCellValueFactory((TableColumn.CellDataFeatures<SaleList, Double> p) -> new SimpleDoubleProperty(p.getValue().getPrice()).asObject());
                salePrice.setCellValueFactory((TableColumn.CellDataFeatures<SaleList, Double> p) -> new SimpleDoubleProperty(p.getValue().getOrderCustomer().getTotal_price()).asObject());


                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM sale_list WHERE oc_id = ?;");
                    preparedStatement.setString(1, orderCustomer.getId());
                    ResultSet rec = preparedStatement.executeQuery();


                    while (rec.next()) {
                        observableList.add(new SaleList(rec.getString(1), orderCustomer, Integer.parseInt(rec.getString(2)), Double.parseDouble(rec.getString(5))));
                    }
                } catch (SQLException e) {

                }

                historyProduct.setItems(observableList);

                ctmId.setText(orderCustomer.getId());
                emId.setText(orderCustomer.getEmployee_name());
                ctmName.setText(orderCustomer.getCustomer().getName());
                ctmPhone.setText(orderCustomer.getCustomer().getPhone());
                ctmAddress.setText(orderCustomer.getCustomer().getAddress());
                ctmTotalPrice.setText(String.valueOf(orderCustomer.getTotal_price()));
            }
        });
    }

    public void btnBack(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.close();

    }

    public void setOrderCustomer(OrderCustomer orderCustomer) {
        this.orderCustomer = orderCustomer;
    }
}
