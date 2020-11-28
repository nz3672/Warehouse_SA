package SupOrder;

import java.sql.Connection;
import Connection.ConnectionHandler;
import Objects.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    public void setDateRecieve(String dateRecieve) {
        this.dateRecieve = dateRecieve;
    }

    public String getDateSave() {
        return dateSave;
    }

    public void setDateSave(String dateSave) {
        this.dateSave = dateSave;
    }

    public OrderSupplier(String lotId, Supplier supplier, String dateRecieve, String dateSave) throws SQLException {
        this.Id = lotId;
        this.supplier = supplier;
        this.dateRecieve = dateRecieve;
        this.dateSave = dateSave;
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
}
