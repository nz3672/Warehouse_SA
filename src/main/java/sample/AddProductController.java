package sample;

import Connection.ConnectionHandler;
import User.UserID;
import Warehouse.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tool.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddProductController {
    @FXML
    TextField f_p_name,f_p_id, f_p_price;

    @FXML
    RadioButton f_p_inventory1, f_p_inventory2, f_p_inventory3;

    @FXML RadioButton f_p_type1, f_p_type2, f_p_type3, f_p_type4, f_p_type5, f_p_type6, f_p_type7;

    @FXML
    DatePicker f_p_save_date;

    UserID userID;
    Product product;

    public void initialize() {
        this.setGroupInvt();
        this.setGroupType();
        f_p_save_date.setValue(LocalDate.now());
    }


    public ToggleGroup setGroupInvt(){
        setToggle setToggle = new setToggleClass();
        ToggleGroup groupInvt = new ToggleGroup();
        setToggle.setToggle(f_p_inventory1,"โกดัง1",groupInvt);
        setToggle.setToggle(f_p_inventory2,"โกดัง2",groupInvt);
        setToggle.setToggle(f_p_inventory3,"โกดัง3",groupInvt);
        return groupInvt;
    }

    public ToggleGroup setGroupType(){
        setToggle setToggle = new setToggleClass();
        ToggleGroup groupType = new ToggleGroup();
        setToggle.setToggle(f_p_type1,"ข้อต่อ",groupType);
        setToggle.setToggle(f_p_type2,"สปริง-สกรู",groupType);
        setToggle.setToggle(f_p_type3,"หมวดคัตติ้งทูลส์",groupType);
        setToggle.setToggle(f_p_type4,"หมวดเครื่องมือขัด",groupType);
        setToggle.setToggle(f_p_type5,"แม่เหล็กและเครื่องมือวัด",groupType);
        setToggle.setToggle(f_p_type6,"อะไหล่แม่พิมพ์ปั๊มโลหะ",groupType);
        setToggle.setToggle(f_p_type7,"อะไหล่แม่พิมพ์พลาสติก",groupType);
        return groupType;
    }

    public String checkSelectedInvt(){
        checkEmpty checkEmpty = new CheckEmptyClass();
        return  checkEmpty.checkSelectedRiobtn(setGroupInvt()); // string ของชื่อโกดัง ก็เอาบันทึกลง db ได้เลย retrun ไปให้แล้วใน btnSubmit
        //ลองปริ้น บรรทัด return ก่อนนะว่าเป็นชื่อโกดังจริงไหม
    }

    public String checkSelectedType(){
        checkEmpty checkEmpty = new CheckEmptyClass();
        return checkEmpty.checkSelectedRiobtn(setGroupType()); // string ของชื่อโกดัง ก็เอาบันทึกลง db ได้เลย retrun ไปให้แล้วใน btnSubmit
        //ลองปริ้น บรรทัด return ก่อนนะว่าเป็นชื่ออประเภทจริงไหม
    }

    public void btnSubmit(ActionEvent actionEvent) throws IOException, SQLException {
        ConnectionHandler connectionHandler = new ConnectionHandler();
        Connection connection = connectionHandler.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM  product WHERE P_id = ? ;");
        preparedStatement.setString(1, f_p_id.getText());
        ResultSet resultSet = preparedStatement.executeQuery();

        if (checkEmpty() && !resultSet.next()){
            product = new Product(f_p_id.getText(),f_p_name.getText()
                    ,Double.parseDouble(f_p_price.getText()),checkSelectedType(),checkSelectedInvt(),f_p_save_date.getValue().toString());


            DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");

            String sql = "INSERT INTO product VALUES (?, ?, ?, ?, ?, ?, ?);";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(product.getProductId()));
            preparedStatement.setString(2, String.valueOf(product.getAmount()));
            preparedStatement.setString(3, product.getName());
            preparedStatement.setString(4, product.getInventoryName());
            preparedStatement.setString(5, product.getType());
            preparedStatement.setString(6, String.valueOf(product.getPrice()));
            preparedStatement.setString(7, f_p_save_date.getValue().format(dateTimeFormatter));
            preparedStatement.executeUpdate();

            setNotic setNotic = new setNoticClass();
            setNotic.showNotic("เพิ่มข้อมูลเสินค้าเสร็จสิ้น","Success!");

            Button btnSubToLot = (Button) actionEvent.getSource();
            Stage stage = (Stage) btnSubToLot.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/homePage.fxml"));
            fxmlLoader.load();
            HomeController addLotController = fxmlLoader.getController();
            addLotController.setProduct(product);
            addLotController.setUser(userID);
            stage.close();
        }
        else {
            if (resultSet.next()){
                setNotic setNotic = new setNoticClass();
                setNotic.showNotic("รหัสสินค้าซ้ำ กรุณากรอกใหม่อีกครั้ง","Error");
            } else {
                setNotic setNotic = new setNoticClass();
                setNotic.showNotic("กรุณากรอกข้อมูลให้ครบถ้วน","Error");
            }
        }
    }

    public boolean checkEmpty(){
        if (f_p_name.getText().equals("") || f_p_id.getText().equals("") ||
                f_p_price.getText().equals("") || (checkSelectedInvt()== null) ||(checkSelectedType()==(null)) || (f_p_save_date.getValue() == null) ){
            return false;
        }
        else{
            return true;
        }
    }
}
