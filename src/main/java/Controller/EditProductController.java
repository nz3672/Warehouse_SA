package Controller;

import Connection.ConnectionHandler;
import Objects.Product;
import Objects.Warehouse;
import javafx.application.Platform;
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
import org.springframework.security.core.parameters.P;
import tool.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EditProductController { //หน้านี้จะถูกกดแก้ไขจากหน้า homepage ปุ่มกดแก้ไขเพื่อเข้าหน้านี้จะอยู่ตรงรายละเอียดใน table ของสินค้านั้น
    // เพราะฉะนั้นพอกดเข้ามา จะต้องนำพา product ของหน้านั้นที่เรากดมาด้วย เพื่อจะได้รู้ว่าจะแก้ไขอะไร is setProduct()
    @FXML
    TextField f_p_name, f_p_id, f_p_price, f_p_amount;
    @FXML
    DatePicker f_p_save_date;
    @FXML ChoiceBox f_warehouse;
    @FXML
    ObservableList<Warehouse> warehouseObservableList = FXCollections.observableArrayList();
    @FXML
    TableView t_warehouse;
    @FXML
    TableColumn<Warehouse, String> idNameWh, levelWh, nameShelf, levelShelf;

    public static Product product;
    private Warehouse warehouse;
    private Connection connection;



    public void initialize() { //set every field to it's details
        ConnectionHandler connectionHandler = new ConnectionHandler();
        this.connection = connectionHandler.getConnection();
        Platform.runLater(new Runnable() {
            public void run() {
                idNameWh.setCellValueFactory((TableColumn.CellDataFeatures<Warehouse, String> w) -> new SimpleStringProperty(w.getValue().getId()));
                levelWh.setCellValueFactory((TableColumn.CellDataFeatures<Warehouse, String> w) -> new SimpleStringProperty(w.getValue().getLevel()));
                nameShelf.setCellValueFactory((TableColumn.CellDataFeatures<Warehouse, String> w) -> new SimpleStringProperty(w.getValue().getShelf()));
                levelShelf.setCellValueFactory((TableColumn.CellDataFeatures<Warehouse, String> w ) -> new SimpleStringProperty(w.getValue().getShelfLevel()));

                try {
                    ResultSet getwh_id = connection.prepareStatement("SELECT wh_id FROM warehouselist WHERE pd_id = \""+ product.getProductId()+ "\";").executeQuery();
                    while (getwh_id.next()) {
                        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM warehouse WHERE wh_id = ?");
                        preparedStatement.setString(1, getwh_id.getString(1));
                        ResultSet getwarehouse = preparedStatement.executeQuery();
                        getwarehouse.next();
                        warehouseObservableList.add(new Warehouse(getwarehouse.getString(1),getwarehouse.getString(2),getwarehouse.getString(3),getwarehouse.getString(4)));
                    }

                } catch (SQLException e) {

                }

                t_warehouse.setItems(warehouseObservableList);

                f_p_save_date.setValue(LocalDate.now());
                f_p_id.setText(product.getProductId());
                f_p_name.setText(product.getName());
                f_p_price.setText(String.valueOf(product.getPrice()));
                f_p_amount.setText(String.valueOf(product.getAmount()));
            }
        });
    }


    public void setWarehouse(Warehouse warehouse){
        this.warehouse = warehouse;
    }

    public void btnDeleteProduct(ActionEvent actionEvent) throws IOException, SQLException {


        setNotic setNotic = new setNoticClass();
        if (setNotic.showComfirm("ต้องการลบหรือไม่","Confirmation")) {
        String sql = "DELETE FROM product WHERE pd_id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, f_p_id.getText());
        preparedStatement.executeUpdate();
        Button btn = (Button) actionEvent.getSource();
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.close();}

    }

    public void btnDeleteWareh(ActionEvent actionEvent) throws IOException, SQLException {
        String sql = "DELETE FROM warehouselist WHERE pd_id = ? AND wh_id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, f_p_id.getText());
        preparedStatement.setString(2, warehouseObservableList.get(t_warehouse.getSelectionModel().getSelectedIndex()).getId());
        preparedStatement.executeUpdate();
        t_warehouse.getItems().removeAll(t_warehouse.getSelectionModel().getSelectedItem());

    }

    public void btnEditProduct(ActionEvent actionEvent) throws IOException, SQLException {
        checkEmpty checkTextfieldEmpty = new CheckEmptyClass();
        setNotic setNotic = new setNoticClass();
        if (setNotic.showComfirm("ต้องการแก้ไขสินค้าหรือไม่","Confirmation")) {

            ConnectionHandler connectionHandler = new ConnectionHandler();
            Connection connection = connectionHandler.getConnection();

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            String sql = "UPDATE product SET pd_amount = ?, pd_name = ?, pd_price = ?, pd_save_date = ? WHERE pd_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(f_p_amount.getText()));
            preparedStatement.setString(2, f_p_name.getText());
            preparedStatement.setString(3, String.valueOf(f_p_price.getText()));
            preparedStatement.setString(4, f_p_save_date.getValue().format(dateTimeFormatter));
            preparedStatement.setString(5, f_p_id.getText());
            preparedStatement.executeUpdate();
            product = new Product(f_p_id.getText(), f_p_name.getText(), Integer.parseInt(f_p_amount.getText()), f_p_save_date.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            product.setPrice(Double.parseDouble(f_p_price.getText()));
            Button btn = (Button) actionEvent.getSource();
            Stage stage = (Stage) btn.getScene().getWindow();
            stage.close();
        }
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
