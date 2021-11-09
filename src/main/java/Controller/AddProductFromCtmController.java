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
import tool.checkString;
import tool.checkStringClass;
import tool.setNoticClass;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddProductFromCtmController {
    @FXML
    ChoiceBox p_l_name;
    @FXML
    Label p_l_id;
    @FXML
    TextField p_l_amount, p_l_price;

    ObservableList<String> productNamelist;
    tool.checkString checkString;
    Product product;
    private int quantity_limit;

    @FXML
    public void initialize(){
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
                                    quantity_limit = Integer.parseInt(rec.getString(4));
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        });
            }
        });

    }

    public void btnSubmit(ActionEvent actionEvent) throws IOException {
        if (p_l_name.getValue() != null && checkString.checkNum(p_l_amount.getText()) && checkString.checkNum(p_l_price.getText()) && quantity_limit >= Integer.parseInt(p_l_amount.getText())) {
        product = new Product(p_l_id.getText(), p_l_name.getValue().toString(), Integer.parseInt(p_l_amount.getText()), "date");
        product.setPrice(Double.parseDouble(p_l_price.getText()));
        Button btnSubToLot = (Button) actionEvent.getSource();
        Stage stage = (Stage) btnSubToLot.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AddOrderCtmPage.fxml"));
        fxmlLoader.load();
        AddOrderCtmController addOrderCtmController = fxmlLoader.getController();
        addOrderCtmController.setProduct(product); // เอาไปปรอ้นในเทเบิ้ลของหน้านี้
        stage.close(); }
        else if (quantity_limit < Integer.parseInt(p_l_amount.getText())){
            setNoticClass setNoticClass = new setNoticClass();
            setNoticClass.showNotic("สินค้าในคลังมีปริมาณต่ำกว่าสินค้าที่จะขาย","Error");
        } else {
            setNoticClass setNoticClass = new setNoticClass();
            setNoticClass.showNotic("กรุณากรอกข้อมูลให้ถูกต้อง","Error");

        }
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
