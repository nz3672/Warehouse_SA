package Controller;

import Connection.ConnectionHandler;
import Objects.Product;
import Objects.Warehouse;
import Objects.WarehouseList;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.util.StringUtils;
import tool.checkString;
import tool.checkStringClass;
import tool.setNoticClass;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class AddProductFromLotController {

    @FXML
    ChoiceBox p_l_name, chooseWh;
    @FXML
    Label p_l_id;
    @FXML
    DatePicker p_l_save_date;
    @FXML
    TextField p_l_amount, p_l_price;
    @FXML
    CheckBox f_wh, f_new_wh;

    ObservableList<String> productNamelist;
    ObservableList<String> warehouseList = FXCollections.observableArrayList();
    Product product;
    Warehouse warehouse;
    WarehouseList whList;
    checkString checkString;

    @FXML
    public void initialize() {
        Platform.runLater(new Runnable() {
            public void run() {
                checkString = new checkStringClass();
                ConnectionHandler connectionHandler = new ConnectionHandler();
                Connection connection = connectionHandler.getConnection();
                productNamelist = FXCollections.observableArrayList();

                String sql = "SELECT pd_name FROM product";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    ResultSet rec = preparedStatement.executeQuery();
                    while (rec.next()) {
                        String data = rec.getString(1);
                        productNamelist.add(data);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM warehouse");
                    ResultSet rec = preparedStatement.executeQuery();
                    while (rec.next()) {
                        Warehouse data = new Warehouse(rec.getString(2), rec.getString(3), rec.getString(4), rec.getString(5));
                        data.setId(rec.getString(1));
                        warehouseList.add(data.toString());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                chooseWh.setItems(warehouseList);

                p_l_name.setItems(productNamelist);
                p_l_name.getSelectionModel().selectedIndexProperty().addListener(
                        (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
                            PreparedStatement preparedStatement = null;
                            try {
                                String QueryText = "SELECT * FROM product WHERE pd_name = ?;";
                                preparedStatement = connection.prepareStatement(QueryText);
                                preparedStatement.setString(1, productNamelist.get(new_val.intValue()));
                                ResultSet rec = preparedStatement.executeQuery();
                                if (rec.next()) {
                                    p_l_id.setText(rec.getString(1));
                                    preparedStatement = connection.prepareStatement("SELECT pd_id FROM warehouselist WHERE pd_id = \"" + rec.getString(1) + "\"");
                                    ResultSet checkwhere = preparedStatement.executeQuery();
                                    f_wh.setDisable(!checkwhere.next());
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        });
            }
        });
        //ให้รายชื่อสินค้าสามารถแสดงบน ChoiceBox ได้
    }

    public void btnSubmit(ActionEvent actionEvent) throws IOException, SQLException {
        if (p_l_name.getValue() != null && checkString.checkNum(p_l_price.getText()) && checkString.checkNum(p_l_amount.getText()) && ((f_new_wh.isSelected() && chooseWh.getValue() != null) || f_wh.isSelected())) {
            ConnectionHandler connectionHandler = new ConnectionHandler();
            Connection connection = connectionHandler.getConnection();
            product = new Product(p_l_id.getText(), p_l_name.getValue().toString(), Integer.parseInt(p_l_amount.getText()), "date");
            product.setPrice(Double.parseDouble(p_l_price.getText()));
            if (f_new_wh.isSelected()) {
                String[] warehouseID = chooseWh.getValue().toString().split(" ");// extract warehouse details from warehouseobservable list
                String[] warehouseDetails = warehouseID[1].split(",");
                Warehouse warehouse = new Warehouse(warehouseDetails[0], warehouseDetails[1], warehouseDetails[2], warehouseDetails[3]);
                warehouse.setId(warehouseID[0]);
                System.out.println(warehouseID[0]);
                whList = new WarehouseList(warehouse, product);
            }
            Button btnSubToLot = (Button) actionEvent.getSource();
            Stage stage = (Stage) btnSubToLot.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AddOrderSupPage.fxml"));
            fxmlLoader.load();
            AddOrderSupController addOrderSupController = fxmlLoader.getController();
            addOrderSupController.setProduct(product);// เอาไปปรอ้นในเทเบิ้ลของหน้านี้
            if (f_new_wh.isSelected()) {
                addOrderSupController.setWarehouseList(whList);
            }
            stage.close();
        } else {
            setNoticClass setNoticClass = new setNoticClass();
            setNoticClass.showNotic("กรุณากรอกข้อมูลให้ถูกต้อง", "Error");
        }
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
