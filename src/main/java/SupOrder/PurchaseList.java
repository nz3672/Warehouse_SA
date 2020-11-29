package SupOrder;

import Connection.ConnectionHandler;
import Objects.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PurchaseList {
    private String id;
    private OrderSupplier orderSupplier;
    private Product product;
    private int pAmount;

    public PurchaseList(String id,OrderSupplier orderSupplier, int pAmount) throws SQLException {
        this.orderSupplier = orderSupplier;
        this.pAmount = pAmount;
        this.id = id;
        ConnectionHandler connectionHandler = new ConnectionHandler();
        Connection connection = connectionHandler.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM purchase_list WHERE pu_id = \"" + id + "\"");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        ResultSet getProduct = connection.createStatement().executeQuery("SELECT * FROM product WHERE pd_id = \""+resultSet.getString(4)+"\"");
        getProduct.next();
        ResultSet getType = connection.createStatement().executeQuery("SELECT t_name FROM type WHERE t_id = \""+getProduct.getString(6)+"\"");
        getType.next();
        //this.product = new Product(getProduct.getString(1),getProduct.getString(2), Double.parseDouble(getProduct.getString(3)), getType.getString(1),  Integer.parseInt(getProduct.getString(4)), getProduct.getString(5));
    }

    public PurchaseList(String id, Product product, int pAmount) {
        this.id = id;
        this.product = product;
        this.pAmount = pAmount;
    }

    public void setOrderSupplier(OrderSupplier orderSupplier) {
        this.orderSupplier = orderSupplier;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setpAmount(int pAmount) {
        this.pAmount = pAmount;
    }

    public String getId() {
        return id;
    }

    public OrderSupplier getOrderSupplier() {
        return orderSupplier;
    }

    public Product getProduct() {
        return product;
    }

    public int getpAmount() {
        return pAmount;
    }
}
