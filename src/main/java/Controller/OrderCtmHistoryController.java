package Controller;

import Connection.ConnectionHandler;
import CustomerOrder.Customer;
import CustomerOrder.OrderCustomer;
import CustomerOrder.SaleList;
import User.UserID;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.stage.Stage;
import tool.setNotic;
import tool.setNoticClass;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class OrderCtmHistoryController {
    @FXML
    TextField searchText;
    @FXML
    TableView historyProduct;
    @FXML
    TableColumn<OrderCustomer,String> ctmStatus, ctmName, emName,ctmPay,ctmSave, ctmId;
    @FXML
    TableColumn<OrderCustomer,Double> ctmPrice;
    @FXML
    TableColumn<OrderCustomer, CheckBox> ctmCheckbox;
    @FXML
    Label noResult;
    @FXML
            ChoiceBox<String> status;
    @FXML
            DatePicker fromDatepicker, toDatepicker;
    ObservableList<OrderCustomer> observableList = FXCollections.observableArrayList();
    ObservableList<String> orderStatus = FXCollections.observableArrayList();

    UserID userID;

    Connection connection;

    @FXML
    public void initialize() {


        Platform.runLater(new Runnable() {
            public void run() {
                ConnectionHandler connectionHandler = new ConnectionHandler();
                connection = connectionHandler.getConnection();

                orderStatus.addAll("สถานะทั้งหมด","เตรียมสินค้า","กำลังจัดส่ง","ยืนยันรับสินค้า");
                status.setItems(orderStatus);
                status.setValue("สถานะทั้งหมด");

                try {
                    ResultSet rec = connection.createStatement().executeQuery("SELECT * FROM order_customer");
                    if (!rec.next()){ // ไม่มีข้อมูล
                        setNotic setNotic = new setNoticClass();
                        setNotic.showNotic("ยังไม่มีประวัติการสั่งซื้อสินค้าจาก customer","Not Found");
                        noResult.setText("ยังไม่มีประวัติการสั่งซื้อสินค้าจาก customer");
                    }
                    else {
                        updateTable();

                        fromDatepicker.valueProperty().addListener(((observable, oldValue, newValue) -> {
                            toDatepicker.setDayCellFactory(picker -> new DateCell() {
                                public void updateItem(LocalDate date, boolean empty) {
                                    super.updateItem(date, empty);

                                    setDisable(empty || date.compareTo(newValue) < 0 );
                                }
                            });
                        }));

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        ctmStatus.setCellValueFactory((TableColumn.CellDataFeatures<OrderCustomer, String> p) -> new SimpleStringProperty(p.getValue().getStatus()));
        ctmId.setCellValueFactory((TableColumn.CellDataFeatures<OrderCustomer, String> p) -> new SimpleStringProperty(p.getValue().getId()));
        ctmName.setCellValueFactory((TableColumn.CellDataFeatures<OrderCustomer, String> p) -> new SimpleStringProperty(p.getValue().getCustomer().getName()));
        emName.setCellValueFactory((TableColumn.CellDataFeatures<OrderCustomer, String> p) -> new SimpleStringProperty(p.getValue().getEmployee_name()));
        ctmPrice.setCellValueFactory((TableColumn.CellDataFeatures<OrderCustomer, Double> p) -> new SimpleDoubleProperty(p.getValue().getTotal_price()).asObject());

        ctmPay.setCellValueFactory((TableColumn.CellDataFeatures<OrderCustomer, String> p) -> new SimpleStringProperty(p.getValue().getPay_date()));
        ctmSave.setCellValueFactory((TableColumn.CellDataFeatures<OrderCustomer, String> p) -> new SimpleStringProperty(p.getValue().getSave_date()));
        ctmCheckbox.setCellValueFactory((TableColumn.CellDataFeatures<OrderCustomer, CheckBox> p) -> new SimpleObjectProperty<>(p.getValue().getCheckBox()));
        ctmSave.setSortType(TableColumn.SortType.DESCENDING);
        historyProduct.getSortOrder().add(ctmSave);
    }

    public void btnDetails(ActionEvent actionEvent) throws IOException {
        if (historyProduct.getSelectionModel().getSelectedItem() != null) {
            Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/orderCtmPage.fxml"));
        stage.setScene(new Scene((Parent) fxmlLoader.load(), 1100, 600));
        OrderCtmController orderCtmController = fxmlLoader.getController();
        orderCtmController.setOrderCustomer((OrderCustomer) historyProduct.getSelectionModel().getSelectedItem());
        stage.showAndWait();
    }
    }

    public void btnSortstatus(ActionEvent actionEvent) throws SQLException {
        ObservableList<OrderCustomer> orderCustomers = FXCollections.observableArrayList();
        if (!status.getValue().equals("สถานะทั้งหมด")) {
        for (OrderCustomer a : observableList) {
            if (a.getStatus().equals(status.getValue())) {
                orderCustomers.add(a);
            }
        }

            historyProduct.setItems(orderCustomers);
        } else {
            this.updateTable();
        }
    }

    public void btnStatus(ActionEvent actionEvent) throws SQLException {
            int i = 0;
        for (OrderCustomer a : observableList) {
            if (a.getCheckBox().isSelected()) {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE order_customer SET oc_status = ? WHERE oc_id = ?;");
                String g = "";
                for (int j = 1; j < 4; j++) {
                    if (orderStatus.get(j).equals(a.getStatus())) {
                        if (j < 3) {
                            g = orderStatus.get(j + 1);
                        } else {
                            g = orderStatus.get(3);
                        }
                    }
                }
                preparedStatement.setString(1, g);
                preparedStatement.setString(2, a.getId());
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
        FilteredList<OrderCustomer> filteredList = new FilteredList<>(observableList, b -> true);

        filteredList.setPredicate(ti -> {
                    LocalDate minDate = fromDatepicker.getValue();
                    LocalDate maxDate = toDatepicker.getValue();

                    // get final values != null
                    final LocalDate finalMin = minDate == null ? LocalDate.MIN : minDate;
                    final LocalDate finalMax = maxDate == null ? LocalDate.MAX : maxDate;

                    // values for openDate need to be in the interval [finalMin, finalMax]
                    if (!finalMin.isAfter(LocalDate.parse(ti.getSave_date(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))) && !finalMax.isBefore(LocalDate.parse(ti.getSave_date(), DateTimeFormatter.ofPattern("yyyy-MM-dd")))) {
                        return true;
                    }
                    else {
                        return false;
                    }

                });


        SortedList<OrderCustomer> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(historyProduct.comparatorProperty());
        historyProduct.setItems(sortedList);
    }

    public void btnSearch(ActionEvent actionEvent) {
        FilteredList<OrderCustomer> filteredList = new FilteredList<>(observableList, b -> true);

        filteredList.setPredicate(orderCustomer -> {

            if (searchText.getText() == null || searchText.getText().isEmpty()) {
                return true;

            }

            String lowerCaseFilter = searchText.getText().toLowerCase();

            if (orderCustomer.getId().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                return true; // Filter by ID
            } else if (orderCustomer.getCustomer().getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                return true; // Filter by Customer Name
            } else if (orderCustomer.getEmployee_name().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                return true; // Filter by Employee Name
            } else {
                return false; // Doesn't Match
            }
        });

        SortedList<OrderCustomer> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(historyProduct.comparatorProperty());
        historyProduct.setItems(sortedList);

    }

    public void btnBack(ActionEvent actionEvent) throws IOException {
        Button btn = (Button) actionEvent.getSource();
        Stage stage = (Stage) btn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/HomePage.fxml"));
        stage.setScene(new Scene((Parent)fxmlLoader.load(),1100,600));
        HomeController controller = fxmlLoader.getController();
        controller.setUser(userID);
        stage.show();
    }

    public void updateTable () throws SQLException {
        ResultSet rec = connection.createStatement().executeQuery("SELECT * FROM order_customer");
        observableList.clear();

        while (rec.next()) {
            ResultSet getCustomer = connection.createStatement().executeQuery("SELECT * FROM customer WHERE c_id = " + rec.getString(2));
            getCustomer.next();
            ResultSet getEmployee = connection.createStatement().executeQuery("SELECT em_name FROM employee WHERE em_id = \"" + rec.getString(3) + "\"");
            getEmployee.next();
            OrderCustomer orderCustomer = new OrderCustomer(rec.getString(1),
                    new Customer(getCustomer.getString(1), getCustomer.getString(2),
                            getCustomer.getString(4),getCustomer.getString(3)),getEmployee.getString(1), rec.getString(4),
                    rec.getString(5), Double.parseDouble(rec.getString(6)), rec.getString(7));

            observableList.add(orderCustomer);
        }
        historyProduct.setItems(observableList);
    }

    public void setUserID(UserID userID) {
        this.userID = userID;
    }
}
