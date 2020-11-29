package Controller;

import Connection.ConnectionHandler;
import Objects.Warehouse;
import User.UserID;
import Objects.Product;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tool.*;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HomeController {

    @FXML
    TableView stockProduct, t_warehouse;
    @FXML
    RadioButton lowToHigh, highToLow;
    @FXML
    CheckBox f_p_type1, f_p_type2, f_p_type3, f_p_type4, f_p_type5, f_p_type6, f_p_type7;
    @FXML
    TableColumn<Product, String> idProducts, nameProducts, saveProducts, typeProducts, invenProducts;
    @FXML
    TableColumn<Product, Double> priceProducts;
    @FXML
    TableColumn<Product, Integer> amountProducts;
    @FXML
    TableColumn<Warehouse, String> t_nameWH, t_levelWH, t_nameShelf, t_levelShelf;
    @FXML
    Hyperlink f_em_name;
    ObservableList<Product> observableList = FXCollections.observableArrayList();
    ObservableList<Warehouse> warehouseObservableList = FXCollections.observableArrayList();

    public static void setProduct(Product product) {
        HomeController.product = product;
    }

    public static Product product;


    UserID user; // dont forget new user naja khun nize

    public void initialize() {
        idProducts.setCellValueFactory((TableColumn.CellDataFeatures<Product, String> p) -> new SimpleStringProperty(p.getValue().getProductId()));
        amountProducts.setCellValueFactory((TableColumn.CellDataFeatures<Product, Integer> p) -> new SimpleIntegerProperty(p.getValue().getAmount()).asObject());
        nameProducts.setCellValueFactory((TableColumn.CellDataFeatures<Product, String> p) -> new SimpleStringProperty(p.getValue().getName()));
        typeProducts.setCellValueFactory((TableColumn.CellDataFeatures<Product, String> p) -> new SimpleStringProperty(p.getValue().getType()));
        priceProducts.setCellValueFactory((TableColumn.CellDataFeatures<Product, Double> p) -> new SimpleDoubleProperty(p.getValue().getPrice()).asObject());
        saveProducts.setCellValueFactory((TableColumn.CellDataFeatures<Product, String> p) -> new SimpleStringProperty(p.getValue().getSaveDate()));


        t_nameWH.setCellValueFactory((TableColumn.CellDataFeatures<Warehouse, String> p) -> new SimpleStringProperty(p.getValue().getName()));
        t_levelWH.setCellValueFactory((TableColumn.CellDataFeatures<Warehouse, String> p) -> new SimpleStringProperty(p.getValue().getLevel()));
        t_nameShelf.setCellValueFactory((TableColumn.CellDataFeatures<Warehouse, String> p) -> new SimpleStringProperty(p.getValue().getShelf()));
        t_levelShelf.setCellValueFactory((TableColumn.CellDataFeatures<Warehouse, String> p) -> new SimpleStringProperty(p.getValue().getShelfLevel()));
        Platform.runLater(new Runnable() {
            public void run() {
                //แสดงตาราง Product ทั้งหมด

                f_em_name.setText(user.getName());
                System.out.println(user.getRank());
                ConnectionHandler connectionHandler = new ConnectionHandler();
                Connection connection = connectionHandler.getConnection();

                try {
                    ResultSet rec = connection.createStatement().executeQuery("SELECT * FROM product");


                    while (rec.next()) {
                        ResultSet getType = connection.createStatement().executeQuery("SELECT t_name FROM type WHERE t_id = \""+rec.getString(6)+"\"");
                        getType.next();
                        observableList.add(new Product(rec.getString(1),rec.getString(2), Double.parseDouble(rec.getString(3)), getType.getString(1),  Integer.parseInt(rec.getString(4)), rec.getString(5)));

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }});
        stockProduct.setItems(observableList);
        ConnectionHandler connectionHandler = new ConnectionHandler();
        Connection connection = connectionHandler.getConnection();
        stockProduct.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
                    PreparedStatement preparedStatement = null;
                    try {
                        warehouseObservableList.clear();
                        String QueryText = "SELECT wh_id FROM warehouselist WHERE pd_id = ?;";
                        preparedStatement = connection.prepareStatement(QueryText);
                        preparedStatement.setString(1, observableList.get(new_val.intValue()).getProductId());
                        ResultSet rec = preparedStatement.executeQuery();
                        while (rec.next()) {
                            preparedStatement = connection.prepareStatement("SELECT * FROM warehouse WHERE wh_id = \"" + rec.getString(1) + "\"");
                            ResultSet getwarehouse = preparedStatement.executeQuery();
                            getwarehouse.next();
                            warehouseObservableList.add(new Warehouse(getwarehouse.getString(2),getwarehouse.getString(3),getwarehouse.getString(4),getwarehouse.getString(5)));
                        }
                        t_warehouse.setItems(warehouseObservableList);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                });
    }

    public void btnFilter(ActionEvent actionEvent) throws IOException {
        if(selectedFilter().equals("น้อย-มาก")) {
            amountProducts.setSortType(TableColumn.SortType.ASCENDING);
            stockProduct.getSortOrder().add(amountProducts);
            stockProduct.sort();
        }else if (selectedFilter().equals("มาก-น้อย")){
            amountProducts.setSortType(TableColumn.SortType.DESCENDING);
            stockProduct.getSortOrder().add(amountProducts);
            stockProduct.sort();
        }
    }

    public void btnGotoLotPage(ActionEvent actionEvent) throws IOException {
        Button btn = (Button) actionEvent.getSource();
        Stage stage = (Stage) btn.getScene().getWindow();
        if (user.getRank().equals("Manager")) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AddOrderCtmPage.fxml"));
            stage.setScene(new Scene((Parent)fxmlLoader.load(),900,600));
            AddOrderCtmController addOrderCtmController = fxmlLoader.getController();
            addOrderCtmController.setUserID(user);
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AddOrderSupPage.fxml"));
            stage.setScene(new Scene((Parent)fxmlLoader.load(),900,600));
            AddOrderSupController addOrderSupController = fxmlLoader.getController();
            addOrderSupController.setUserID(user);
        }
        stage.show();
    }

    public void btnGoToAddProduct(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/addProductPage.fxml"));
        stage.setScene(new Scene((Parent)fxmlLoader.load(),900,600));
        AddProductController Controller = fxmlLoader.getController();
        stage.showAndWait();
        if (product!=null) {
            observableList.add(product);
            stockProduct.setItems(observableList);
        }
    }


    public void btnLotsHistory(MouseEvent actionEvent) throws IOException {
        ImageView btn = (ImageView) actionEvent.getSource();
        Stage stage = (Stage) btn.getScene().getWindow();
        if (user.getRank().equals("Inventory")) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/lotsHistoryPage.fxml"));
            stage.setScene(new Scene((Parent)fxmlLoader.load(),900,600));
            LotsHistoryController controller = fxmlLoader.getController();
            controller.setUserID(user);
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/OrderCtmHistoryPage.fxml"));
            stage.setScene(new Scene((Parent)fxmlLoader.load(),900,600));
            OrderCtmHistoryController controller = fxmlLoader.getController();
            controller.setUserID(user);
        }
        stage.show();
    }

    public void btnGotoEditUser(ActionEvent actionEvent) throws IOException {
        Hyperlink btn = (Hyperlink) actionEvent.getSource();
        Stage stage = (Stage) btn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/editUserPage.fxml"));
        stage.setScene(new Scene((Parent)fxmlLoader.load(),900,600));
        EditUserController editUserController = fxmlLoader.getController();
        editUserController.setUser(user);
        stage.show();
    }

    public void btnGotoEditProduct(ActionEvent actionEvent) throws IOException {
        if (stockProduct.getSelectionModel().getSelectedItem() != null) {
        System.out.println(stockProduct.getSelectionModel().getSelectedItem());
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/editProductPage.fxml"));
        stage.setScene(new Scene(fxmlLoader.load(),900,600));
        EditProductController editProductController = fxmlLoader.getController();
        editProductController.setProduct((Product) stockProduct.getSelectionModel().getSelectedItem());
        stage.showAndWait();
        this.updateTable();
        } else {
            setNotic setNotic = new setNoticClass();
            setNotic.showNotic("กรุณาเลือกสินค่าเพื่อทำการแก้ไข","Error");
        }

    }

    public void btnLogout(ActionEvent actionEvent) throws IOException {
        setNotic setNotic = new setNoticClass();
        if (setNotic.showComfirm("ต้องการ logout หรือไม่","Confirmation")) {
        Hyperlink btn = (Hyperlink) actionEvent.getSource();
        Stage stage = (Stage) btn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/loginPage.fxml"));
        stage.setScene(new Scene((Parent)fxmlLoader.load(),900,600));
        LoginController LoginController = fxmlLoader.getController();
        setNotic.showNotic("Logout!", "Logout");
        stage.show();
        }

    }

    public void btnAddType(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/addTypePage.fxml"));
        stage.setScene(new Scene((Parent)fxmlLoader.load(),900,600));
        AddTypeController addTypeController = fxmlLoader.getController();
        addTypeController.setUser(user);
        stage.showAndWait();
    }

    public void btnAddWarehouse(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/addWarehousePage.fxml"));
        stage.setScene(new Scene((Parent)fxmlLoader.load(),900,600));
        AddWarehouseController addWarehouseController = fxmlLoader.getController();
        addWarehouseController.setUser(user);
        stage.showAndWait();
    }

    public void setUser(UserID user){
        this.user = user;
    }

    public void updateTable() {
        ConnectionHandler connectionHandler = new ConnectionHandler();
        Connection connection = connectionHandler.getConnection();

        try {
            ResultSet rec = connection.createStatement().executeQuery("SELECT * FROM product");
            observableList.clear();

            while (rec.next()) {
                ResultSet getType = connection.createStatement().executeQuery("SELECT t_name FROM type WHERE t_id = \""+rec.getString(6)+"\"");
                getType.next();
                observableList.add(new Product(rec.getString(1),rec.getString(2), Double.parseDouble(rec.getString(3)), getType.getString(1),  Integer.parseInt(rec.getString(4)), rec.getString(5)));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        stockProduct.setItems(this.observableList);
    }

    public ToggleGroup filter(){
        setToggle setToggle = new setToggleClass();
        ToggleGroup toggleGroup = new ToggleGroup();
        setToggle.setToggle(lowToHigh,"Manager",toggleGroup);
        setToggle.setToggle(highToLow,"Inventory",toggleGroup);
        return  toggleGroup;
    }
    public String selectedFilter(){
        checkEmpty checkEmpty = new CheckEmptyClass();
        return checkEmpty.checkSelectedRiobtn(filter());
    }

    public void updateTable1(String sql) {
        ConnectionHandler connectionHandler = new ConnectionHandler();
        Connection connection = connectionHandler.getConnection();

        try {
            ResultSet rec = connection.createStatement().executeQuery(sql);
            observableList.clear();
            while (rec.next()) {
                //this.observableList.add(new Product(rec.getString(1), Double.parseDouble(rec.getString(2)), rec.getString(3),rec.getString(4), rec.getString(5), Double.parseDouble(rec.getString(6)), rec.getString(7)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        stockProduct.setItems(this.observableList);
    }
}
