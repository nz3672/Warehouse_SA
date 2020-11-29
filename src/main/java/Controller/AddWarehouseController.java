package Controller;

import Objects.Type;
import Objects.Warehouse;
import User.UserID;
import Connection.ConnectionHandler;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tool.setNotic;
import tool.setNoticClass;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddWarehouseController {
    @FXML
    TableView t_warehouse;
    @FXML
    TableColumn<Warehouse, String> nameWH, levelWH, nameShelf,levelShelf;
    @FXML
    TextField w_nameWH, w_levelWH, w_nameShelf, w_levelShelf;

    ObservableList<Warehouse> warehouseObservableList = FXCollections.observableArrayList();

    private Type type;
    private Connection connection;
    UserID user;

    @FXML
    public void initialize(){
        ConnectionHandler connectionHandler = new ConnectionHandler();
        connection = connectionHandler.getConnection();

        nameWH.setCellValueFactory((TableColumn.CellDataFeatures<Warehouse, String> p) -> new SimpleStringProperty(p.getValue().getName()));
        levelWH.setCellValueFactory((TableColumn.CellDataFeatures<Warehouse, String> p) -> new SimpleStringProperty(p.getValue().getLevel()));
        nameShelf.setCellValueFactory((TableColumn.CellDataFeatures<Warehouse, String> p) -> new SimpleStringProperty(p.getValue().getShelf()));
        levelShelf.setCellValueFactory((TableColumn.CellDataFeatures<Warehouse, String> p) -> new SimpleStringProperty(p.getValue().getShelfLevel()));

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM warehouse");
                    ResultSet getWarehouse = preparedStatement.executeQuery();

                    while (getWarehouse.next()) {
                        Warehouse warehouse = new Warehouse(getWarehouse.getString(2), getWarehouse.getString(3), getWarehouse.getString(4), getWarehouse.getString(5));
                        PreparedStatement ps = connection.prepareStatement("SELECT wh_id FROM warehouse WHERE wh_name = ? AND wh_level = ? AND wh_shelf = ? AND wh_shelf_level = ?");
                        ps.setString(1,getWarehouse.getString(2));
                        ps.setString(2,getWarehouse.getString(3));
                        ps.setString(3,getWarehouse.getString(4));
                        ps.setString(4,getWarehouse.getString(5));
                        ResultSet rec = ps.executeQuery();
                        rec.next();
                        warehouse.setId(rec.getString(1));
                        warehouseObservableList.add(warehouse);
                    }
                    t_warehouse.setItems(warehouseObservableList);
                } catch (SQLException e) {

                }

            }
        });
    }

    public void btnAddType(ActionEvent actionEvent) throws IOException, SQLException {
        PreparedStatement checkP_id = connection.prepareStatement("SELECT wh_id FROM warehouse WHERE wh_name = ? AND wh_level = ? AND wh_shelf = ? AND wh_shelf_level = ?");
        checkP_id.setString(1, w_nameWH.getText());
        checkP_id.setString(2, w_levelWH.getText());
        checkP_id.setString(3, w_nameShelf.getText());
        checkP_id.setString(4, w_levelShelf.getText());
        ResultSet rs = checkP_id.executeQuery();
        if (!w_nameWH.getText().equals(" ") || !w_levelWH.getText().equals(" ") || !w_nameShelf.getText().equals(" ") || !w_levelShelf.getText().equals(" ")) {
            if (!rs.next()) {
                PreparedStatement addintotable = connection.prepareStatement("INSERT INTO warehouse (wh_name, wh_level, wh_shelf, wh_shelf_level) VALUES (?,?,?,?)");
                addintotable.setString(1, w_nameWH.getText());
                addintotable.setString(2, w_levelWH.getText());
                addintotable.setString(3, w_nameShelf.getText());
                addintotable.setString(4, w_levelShelf.getText());
                addintotable.executeUpdate();
                this.updatetable();
                w_nameWH.clear();
                w_levelWH.clear();
                w_nameShelf.clear();
                w_levelShelf.clear();
            } else {
                setNotic setNotic = new setNoticClass();
                setNotic.showNotic("ข้อมูลรหัสประเภทสินค้าหรือชื่อซ้ำ กรุณากรอกใหม่อีกครั้ง", "Error");
            }
        } else {
            setNotic setNotic = new setNoticClass();
            setNotic.showNotic("กรุณากรอกข้อมูลให้ครบถ้วน2", "Error");
        }

    }

    public void btnBack(ActionEvent actionEvent) throws IOException {
        Button btn = (Button) actionEvent.getSource();
        Stage stage = (Stage) btn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/HomePage.fxml"));
        stage.setScene(new Scene((Parent)fxmlLoader.load(),900,600));
        HomeController controller = fxmlLoader.getController();
        controller.setUser(user);
        stage.close();
    }


    public void btnDelete(ActionEvent actionEvent) throws IOException, SQLException {
        setNotic setNotic = new setNoticClass();
        if (t_warehouse.getSelectionModel().getSelectedItem() != null) {
            if (setNotic.showComfirm("คุณต้องการลบคลังสินค้าที่เลือกหรือไม่", "Confirmation")) {
                String sql = "DELETE FROM warehouse WHERE wh_id = ?;";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, warehouseObservableList.get(t_warehouse.getSelectionModel().getSelectedIndex()).getId());
                preparedStatement.executeUpdate();
                t_warehouse.getItems().removeAll(t_warehouse.getSelectionModel().getSelectedItem());
                this.updatetable();
            }
        } else {
            setNotic.showNotic("กรุณาเลือกคลังสินค้าที่ต้องการจะลบ","Error");
        }

    }

    public void updatetable () throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM warehouse");
        ResultSet getWarehouse = preparedStatement.executeQuery();
        warehouseObservableList.clear();
        while (getWarehouse.next()) {
            Warehouse warehouse = new Warehouse(getWarehouse.getString(2), getWarehouse.getString(3), getWarehouse.getString(4), getWarehouse.getString(5));
            PreparedStatement ps = connection.prepareStatement("SELECT wh_id FROM warehouse WHERE wh_name = ? AND wh_level = ? AND wh_shelf = ? AND wh_shelf_level = ?");
            ps.setString(1,getWarehouse.getString(2));
            ps.setString(2,getWarehouse.getString(3));
            ps.setString(3,getWarehouse.getString(4));
            ps.setString(4,getWarehouse.getString(5));
            ResultSet rec = ps.executeQuery();
            rec.next();
            warehouse.setId(rec.getString(1));
            warehouseObservableList.add(warehouse);}
        t_warehouse.setItems(warehouseObservableList);
    }

    public void setUser(UserID user) {
        this.user = user;
    }
}
