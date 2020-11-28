package Controller;

import Connection.ConnectionHandler;
import User.UserID;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tool.CheckEmptyClass;
import tool.checkEmpty;
import tool.setNotic;
import tool.setNoticClass;


import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HomeController {

    @FXML
    TableView stockProduct;
    @FXML
    CheckBox lowToHigh, highToLow;
    @FXML
    CheckBox f_p_type1, f_p_type2, f_p_type3, f_p_type4, f_p_type5, f_p_type6, f_p_type7;
    @FXML
    TableColumn<Product, String> idProducts, nameProducts, saveProducts, typeProducts, invenProducts;
    @FXML
    TableColumn<Product, Double> priceProducts;
    @FXML
    TableColumn<Product, Integer> amountProducts;
    @FXML
    Hyperlink f_em_name;
    ObservableList<Product> observableList = FXCollections.observableArrayList();

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

        Platform.runLater(new Runnable() {
            public void run() {
                //แสดงตาราง Product ทั้งหมด

                //f_em_name.setText(user.getName());
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
    }

    public void btnFilter(ActionEvent actionEvent) throws IOException {
        checkEmpty checkTextfieldEmpty = new CheckEmptyClass();
        if (checkTextfieldEmpty.checkCheckboxEmpty(f_p_type1) || checkTextfieldEmpty.checkCheckboxEmpty(f_p_type1) || checkTextfieldEmpty.checkCheckboxEmpty(f_p_type1) || checkTextfieldEmpty.checkCheckboxEmpty(f_p_type1)
        || checkTextfieldEmpty.checkCheckboxEmpty(f_p_type1) || checkTextfieldEmpty.checkCheckboxEmpty(f_p_type1) || checkTextfieldEmpty.checkCheckboxEmpty(f_p_type1) || checkTextfieldEmpty.checkCheckboxEmpty(lowToHigh)
        || checkTextfieldEmpty.checkCheckboxEmpty(highToLow)) {
            //product inventory
            if (checkTextfieldEmpty.checkCheckboxEmpty(lowToHigh)) {
                amountProducts.setSortType(TableColumn.SortType.ASCENDING);
                stockProduct.getSortOrder().add(amountProducts);
                stockProduct.sort();
            }
            if (checkTextfieldEmpty.checkCheckboxEmpty(highToLow)) {
                amountProducts.setSortType(TableColumn.SortType.DESCENDING);
                stockProduct.getSortOrder().add(amountProducts);
                stockProduct.sort();
            }
        } else {
            updateTable();
        }
    }

    public void btnGotoLotPage(ActionEvent actionEvent) throws IOException {
//        Button btn = (Button) actionEvent.getSource();
//        Stage stage = (Stage) btn.getScene().getWindow();
//        if (user.getRank().equals("Manager")) {
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AddOrderCtmPage.fxml"));
//            stage.setScene(new Scene((Parent)fxmlLoader.load(),900,600));
//            AddOrderCtmController addOrderCtmController = fxmlLoader.getController();
//            addOrderCtmController.setUserID(user);
//        } else {
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AddOrderSupPage.fxml"));
//            stage.setScene(new Scene((Parent)fxmlLoader.load(),900,600));
//            AddOrderSupController addOrderSupController = fxmlLoader.getController();
//            addOrderSupController.setUserID(user);
//        }
//        stage.show();
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/lotsHistoryPage.fxml"));
        stage.setScene(new Scene((Parent)fxmlLoader.load(),900,600));
        LotsHistoryController controller = fxmlLoader.getController();
        //controller.setUserID(user);
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
//        if (stockProduct.getSelectionModel().getSelectedItem() != null) {
//        System.out.println(stockProduct.getSelectionModel().getSelectedItem());
//        Stage stage = new Stage();
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/editProductPage.fxml"));
//        stage.setScene(new Scene(fxmlLoader.load(),900,600));
//        EditProductController editProductController = fxmlLoader.getController();
//        editProductController.setProduct((Product) stockProduct.getSelectionModel().getSelectedItem());
//        stage.showAndWait();
//        this.updateTable();
//        } else {
//            setNotic setNotic = new setNoticClass();
//            setNotic.showNotic("กรุณาเลือกสินค่าเพื่อทำการแก้ไข","Error");
//        }

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
                //this.observableList.add(new Product(rec.getString(1), Double.parseDouble(rec.getString(2)), rec.getString(3),rec.getString(4), rec.getString(5), Double.parseDouble(rec.getString(6)), rec.getString(7)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        stockProduct.setItems(this.observableList);
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
