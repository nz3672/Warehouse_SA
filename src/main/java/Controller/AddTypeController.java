package Controller;

import Connection.ConnectionHandler;
import Objects.Product;
import Objects.Type;
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
import java.time.format.DateTimeFormatter;

public class AddTypeController {
    @FXML
    TableView t_Type;
    @FXML
    TableColumn<Type,String> t_idType, t_nameType;
    @FXML
    TextField nameType,idType;

    ObservableList<Type> typeObservableList = FXCollections.observableArrayList();

    private Type type;
    private Connection connection;
    UserID user;

    @FXML
    public void initialize(){
        ConnectionHandler connectionHandler = new ConnectionHandler();
        connection = connectionHandler.getConnection();

        t_idType.setCellValueFactory((TableColumn.CellDataFeatures<Type, String> p) -> new SimpleStringProperty(p.getValue().getId()));
        t_nameType.setCellValueFactory((TableColumn.CellDataFeatures<Type, String> p) -> new SimpleStringProperty(p.getValue().getName()));

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM type");
                    ResultSet getType = preparedStatement.executeQuery();

                    while (getType.next()) {
                        typeObservableList.add(new Type(getType.getString(1), getType.getString(2)));
                    }
                    t_Type.setItems(typeObservableList);
                } catch (SQLException e) {

                }

            }
        });
    }

    public void btnAddType(ActionEvent actionEvent) throws IOException, SQLException {
        PreparedStatement checkP_id = connection.prepareStatement("SELECT t_id FROM type WHERE t_id = ? OR t_name = ?");
        checkP_id.setString(1, idType.getText());
        checkP_id.setString(2, nameType.getText());
        ResultSet rs = checkP_id.executeQuery();
        if (!idType.getText().equals(" ") || !nameType.getText().equals(" ")) {
            if (!rs.next()) {
                PreparedStatement addintotable = connection.prepareStatement("INSERT INTO type (t_name) VALUES (?)");
                addintotable.setString(1, nameType.getText());
                addintotable.executeUpdate();
                this.updatetable();
                idType.clear();
                nameType.clear();
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
        if (t_Type.getSelectionModel().getSelectedItem() != null) {
            if (setNotic.showComfirm("คุณต้องการลบประเภทสินค้าที่เลือกหรือไม่", "Confirmation")) {
                String sql = "DELETE FROM type WHERE t_id = ?;";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, typeObservableList.get(t_Type.getSelectionModel().getSelectedIndex()).getId());
                preparedStatement.executeUpdate();
                t_Type.getItems().removeAll(t_Type.getSelectionModel().getSelectedItem());
                this.updatetable();
            }
        } else {
            setNotic.showNotic("กรุณาเลือกประเภทสินค้าที่ต้องการจะลบ","Error");
        }

    }

    public void updatetable () throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM type");
        ResultSet getType = preparedStatement.executeQuery();
        typeObservableList.clear();
        while (getType.next()) {
            typeObservableList.add(new Type(getType.getString(1), getType.getString(2)));
        }
        t_Type.setItems(typeObservableList);
    }

    public void setUser(UserID user) {
        this.user = user;
    }
}
