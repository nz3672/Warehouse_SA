package Controller;

import Connection.ConnectionHandler;
import Objects.Product;
import SupOrder.OrderSupplier;
import SupOrder.Supplier;
import User.UserID;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LotsHistoryController {
    @FXML
    TextField searchText;
    @FXML
    TableView historyProduct;
    @FXML
    TableColumn<OrderSupplier, String> supStatus, supName, emName, supRecieve, supSave, supId;
    @FXML
    TableColumn<OrderSupplier, CheckBox> supCheckbox;
    @FXML
    Label noResult;
    @FXML
    ChoiceBox<String> status;
    @FXML
    DatePicker fromDatepicker, toDatepicker;
    ObservableList<OrderSupplier> observableList = FXCollections.observableArrayList();
    ObservableList<String> orderStatus = FXCollections.observableArrayList();


    UserID userID;

    Connection connection;

    @FXML
    public void initialize() {


        Platform.runLater(new Runnable() {
            public void run() {
                ConnectionHandler connectionHandler = new ConnectionHandler();
                connection = connectionHandler.getConnection();


                orderStatus.addAll("สถานะทั้งหมด", "เตรียมสินค้า", "กำลังจัดส่ง", "ยืนยันรับสินค้า", "ตีกลับสินค้า");
                status.setItems(orderStatus);
                status.setValue("สถานะทั้งหมด");

                try {
                    ResultSet rec = connection.createStatement().executeQuery("SELECT * FROM order_customer");
                    if (!rec.next()) { // ไม่มีข้อมูล
                        setNotic setNotic = new setNoticClass();
                        setNotic.showNotic("ยังไม่มีประวัติการสั่งซื้อสินค้าจาก customer", "Not Found");
                        noResult.setText("ยังไม่มีประวัติการสั่งซื้อสินค้าจาก customer");
                    } else {
                        updateTable();

                        fromDatepicker.valueProperty().addListener(((observable, oldValue, newValue) -> {
                            toDatepicker.setDayCellFactory(picker -> new DateCell() {
                                public void updateItem(LocalDate date, boolean empty) {
                                    super.updateItem(date, empty);

                                    setDisable(empty || date.compareTo(newValue) < 0);
                                }
                            });
                        }));

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        supStatus.setCellValueFactory((TableColumn.CellDataFeatures<OrderSupplier, String> p) -> new SimpleStringProperty(p.getValue().getStatus()));
        supId.setCellValueFactory((TableColumn.CellDataFeatures<OrderSupplier, String> p) -> new SimpleStringProperty(p.getValue().getLotId()));
        supName.setCellValueFactory((TableColumn.CellDataFeatures<OrderSupplier, String> p) -> new SimpleStringProperty(p.getValue().getSupplier().getName()));
        emName.setCellValueFactory((TableColumn.CellDataFeatures<OrderSupplier, String> p) -> new SimpleStringProperty(p.getValue().getEmployee_name()));
        supRecieve.setCellValueFactory((TableColumn.CellDataFeatures<OrderSupplier, String> p) -> new SimpleStringProperty(p.getValue().getDateRecieve()));
        supSave.setCellValueFactory((TableColumn.CellDataFeatures<OrderSupplier, String> p) -> new SimpleStringProperty(p.getValue().getDateSave()));
        supCheckbox.setCellValueFactory((TableColumn.CellDataFeatures<OrderSupplier, CheckBox> p) -> new SimpleObjectProperty<>(p.getValue().getCheckBox()));
        supSave.setSortType(TableColumn.SortType.DESCENDING);
        historyProduct.getSortOrder().add(supSave);
    }

    public void btnDetails(ActionEvent actionEvent) throws IOException {
        if (historyProduct.getSelectionModel().getSelectedItem() != null) {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/orderSupPage.fxml"));
            stage.setScene(new Scene((Parent) fxmlLoader.load(), 1100, 600));
            OrderSupController orderSupController = fxmlLoader.getController();
            orderSupController.setOrderSupplier((OrderSupplier) historyProduct.getSelectionModel().getSelectedItem());
            stage.showAndWait();
        }
    }

    public void btnSortstatus(ActionEvent actionEvent) throws SQLException {
        ObservableList<OrderSupplier> orderSuppliers = FXCollections.observableArrayList();
        if (!status.getValue().equals("สถานะทั้งหมด")) {
            for (OrderSupplier a : observableList) {
                if (a.getStatus().equals(status.getValue())) {
                    orderSuppliers.add(a);
                }
            }

            historyProduct.setItems(orderSuppliers);
        } else {
            this.updateTable();
        }
    }

    public void btnStatus(ActionEvent actionEvent) throws SQLException {
            int i = 0;
            for (OrderSupplier a : observableList) {
                if (a.getCheckBox().isSelected()) {
                    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE order_sup SET os_status = ? WHERE os_id = ?;");
                    String g = "";
                    for (int j = 1 ; j < 4 ; j++) {
                        if (orderStatus.get(j).equals(a.getStatus())){
                            if (j < 3) {
                                g = orderStatus.get(j+1);
                            } else {
                                g = orderStatus.get(j);
                            }
                        }
                    }
                    preparedStatement.setString(1, g);
                    preparedStatement.setString(2, a.getLotId());
                    preparedStatement.executeUpdate();
                    i++;
                }
            }
            this.updateTable();
            if (i > 0) {
                setNotic setNotic = new setNoticClass();
                setNotic.showNotic("เปลี่ยนแปลงสถานะเสร็จสิ้น", "Success");
            }
    }

    public void btnDaterange(ActionEvent actionEvent) {
        FilteredList<OrderSupplier> filteredList = new FilteredList<>(observableList, b -> true);

        filteredList.setPredicate(ti -> {
            LocalDate minDate = fromDatepicker.getValue();
            LocalDate maxDate = toDatepicker.getValue();

            // get final values != null
            final LocalDate finalMin = minDate == null ? LocalDate.MIN : minDate;
            final LocalDate finalMax = maxDate == null ? LocalDate.MAX : maxDate;

            // values for openDate need to be in the interval [finalMin, finalMax]
            if (!finalMin.isAfter(LocalDate.parse(ti.getDateSave(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))) && !finalMax.isBefore(LocalDate.parse(ti.getDateSave(), DateTimeFormatter.ofPattern("yyyy-MM-dd")))) {
                return true;
            } else {
                return false;
            }

        });


        SortedList<OrderSupplier> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(historyProduct.comparatorProperty());
        historyProduct.setItems(sortedList);
    }

    public void btnSearch(ActionEvent actionEvent) {
        FilteredList<OrderSupplier> filteredList = new FilteredList<>(observableList, b -> true);

        filteredList.setPredicate(orderSup -> {

            if (searchText.getText() == null || searchText.getText().isEmpty()) {
                return true;

            }

            String lowerCaseFilter = searchText.getText().toLowerCase();

            if (orderSup.getLotId().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                return true; // Filter by ID
            } else if (orderSup.getSupplier().getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                return true; // Filter by Customer Name
            } else if (orderSup.getEmployee_name().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                return true; // Filter by Employee Name
            } else {
                return false; // Doesn't Match
            }
        });

        SortedList<OrderSupplier> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(historyProduct.comparatorProperty());
        historyProduct.setItems(sortedList);

    }

    public void btnBack(ActionEvent actionEvent) throws IOException {
        Button btn = (Button) actionEvent.getSource();
        Stage stage = (Stage) btn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/HomePage.fxml"));
        stage.setScene(new Scene((Parent) fxmlLoader.load(), 1100, 600));
        HomeController controller = fxmlLoader.getController();
        controller.setUser(userID);
        stage.show();
    }

    public void updateTable() throws SQLException {
        ResultSet rec = connection.createStatement().executeQuery("SELECT * FROM order_sup");
        observableList.clear();

        while (rec.next()) {
            ResultSet getSupplier = connection.createStatement().executeQuery("SELECT * FROM suppiler WHERE s_id = " + rec.getString(2));
            getSupplier.next();
            ResultSet getEmployee = connection.createStatement().executeQuery("SELECT em_name FROM employee WHERE em_id = \"" + rec.getString(3) + "\"");
            getEmployee.next();
            OrderSupplier orderSupplier = new OrderSupplier(rec.getString(1), new Supplier(getSupplier.getString(1), getSupplier.getString(2),
                    getSupplier.getString(3), getSupplier.getString(4)), getEmployee.getString(1), rec.getString(4), rec.getString(5), rec.getString(6));

            observableList.add(orderSupplier);
        }
        historyProduct.setItems(observableList);

        supSave.setSortType(TableColumn.SortType.DESCENDING);
        historyProduct.getSortOrder().add(supSave);
    }

    public void setUserID(UserID userID) {
        this.userID = userID;
    }

}
