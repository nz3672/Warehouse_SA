package SupOrder;

import java.sql.Connection;
import Connection.ConnectionHandler;
import Objects.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderSupplier {
    private String Id;
    private Supplier supplier;
    private String dateRecieve;
    private String dateSave;
    private String Employee_name;
    private String status;
    private CheckBox checkBox;

    public void setDateRecieve(String dateRecieve) {
        this.dateRecieve = dateRecieve;
    }

    public String getDateSave() {
        return dateSave;
    }

    public void setDateSave(String dateSave) {
        this.dateSave = dateSave;
    }

    public OrderSupplier(String lotId, Supplier supplier, String employee_name, String dateRecieve, String dateSave, String status) throws SQLException {
        this.Id = lotId;
        this.supplier = supplier;
        this.Employee_name = employee_name;
        this.dateRecieve = dateRecieve;
        this.dateSave = dateSave;
        this.status = status;
        this.checkBox = new CheckBox();
    }
    public String getLotId() {
        return Id;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public String getDateRecieve() {
        return dateRecieve;
    }

    public void setLotId(String lotId) {
        this.Id = lotId;
    }

    public void setEmployee_name(String employee_name) {
        Employee_name = employee_name;
    }

    public String getEmployee_name() {
        return Employee_name;
    }

    public String getStatus() {
        return status;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }
}
