package Controller;

import Connection.ConnectionHandler;
import Objects.Product;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tool.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EditProductController { //หน้านี้จะถูกกดแก้ไขจากหน้า homepage ปุ่มกดแก้ไขเพื่อเข้าหน้านี้จะอยู่ตรงรายละเอียดใน table ของสินค้านั้น
    // เพราะฉะนั้นพอกดเข้ามา จะต้องนำพา product ของหน้านั้นที่เรากดมาด้วย เพื่อจะได้รู้ว่าจะแก้ไขอะไร is setProduct()
    @FXML
    TextField f_p_name, f_p_id, f_p_price, f_p_amount;
    @FXML
    RadioButton f_p_inventory1, f_p_inventory2, f_p_inventory3;
    @FXML
    RadioButton f_p_type1, f_p_type2, f_p_type3, f_p_type4, f_p_type5, f_p_type6, f_p_type7;
    @FXML
    DatePicker f_p_save_date;
    private Product product;

    public void initialize() { //set every field to it's details
        Platform.runLater(new Runnable() {
            public void run() {
                f_p_save_date.setValue(LocalDate.now());
                for (Toggle r : setGroupInvt().getToggles()) {
                    if (product.getInventoryName().equals(r.getUserData())) {
                        r.setSelected(true);
                    }
                }
                for (Toggle r : setGroupType().getToggles()) {
                    if (product.getType().equals(r.getUserData())) {
                        r.setSelected(true);
                    }
                }
                f_p_id.setText(product.getProductId());
                f_p_name.setText(product.getName());
                f_p_price.setText(String.valueOf(product.getPrice()));
                f_p_amount.setText(String.valueOf(product.getAmount()));
                f_p_save_date.setValue(LocalDate.now());
            }
        });
    }


    public void btnEditProduct(ActionEvent actionEvent) throws IOException, SQLException {
        checkEmpty checkTextfieldEmpty = new CheckEmptyClass();
        setNotic setNotic = new setNoticClass();
        if (setNotic.showComfirm("ต้องการแก้ไขสินค้าหรือไม่","Confirmation")) {

            ConnectionHandler connectionHandler = new ConnectionHandler();
            Connection connection = connectionHandler.getConnection();

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            String sql = "UPDATE product SET P_amount = ?, P_name = ?, P_inventory = ?, P_type = ?, P_price = ?, P_edit_date = ? WHERE P_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(f_p_amount.getText()));
            preparedStatement.setString(2, f_p_name.getText());
            preparedStatement.setString(3, checkSelectedInvt());
            preparedStatement.setString(4, checkSelectedType());
            preparedStatement.setString(5, String.valueOf(f_p_price.getText()));
            preparedStatement.setString(6, f_p_save_date.getValue().format(dateTimeFormatter));
            preparedStatement.setString(7, f_p_id.getText());
            preparedStatement.executeUpdate();
            product = new Product(f_p_id.getText(), f_p_name.getText(), Double.parseDouble(f_p_price.getText()), checkSelectedType(), checkSelectedInvt(), f_p_save_date.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            Button btn = (Button) actionEvent.getSource();
            Stage stage = (Stage) btn.getScene().getWindow();
            stage.close();
        }
    }

    public void setProduct(Product product){
        this.product = product;
    }

    public void btnDeleteProduct(ActionEvent actionEvent) throws IOException, SQLException {

        ConnectionHandler connectionHandler = new ConnectionHandler();
        Connection connection = connectionHandler.getConnection();
        setNotic setNotic = new setNoticClass();
        if (setNotic.showComfirm("ต้องการลบหรือไม่","Confirmation")) {
        String sql = "DELETE FROM product WHERE P_id=?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, f_p_id.getText());
        preparedStatement.executeUpdate();
        Button btn = (Button) actionEvent.getSource();
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.close();}

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
        return  checkEmpty.checkSelectedRiobtn(setGroupInvt()); // string ของชื่อโกดัง ก็เอาบันทึกลง db ได้เลย return ไปให้แล้วใน btnSubmit
        //ลองปริ้น บรรทัด return ก่อนนะว่าเป็นชื่อโกดังจริงไหม
    }

    public String checkSelectedType(){
        checkEmpty checkEmpty = new CheckEmptyClass();
        return checkEmpty.checkSelectedRiobtn(setGroupType()); // string ของชื่อโกดัง ก็เอาบันทึกลง db ได้เลย retrun ไปให้แล้วใน btnSubmit
        //ลองปริ้น บรรทัด return ก่อนนะว่าเป็นชื่ออประเภทจริงไหม
    }


}
