package CustomerOrder;

import Connection.ConnectionHandler;
import Objects.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SaleList {
    private String id;
    private OrderCustomer orderCustomer;
    private Product product;
    private int amount;

    public SaleList(String id, OrderCustomer orderCustomer, int amount) throws SQLException {
        this.id = id;
        this.orderCustomer = orderCustomer;
        this.amount = amount;
        ConnectionHandler connectionHandler = new ConnectionHandler();
        Connection connection = connectionHandler.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM sale_list WHERE pu_id = \"" + id + "\"");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        ResultSet getProduct = connection.createStatement().executeQuery("SELECT * FROM product WHERE pd_id = \""+resultSet.getString(4)+"\"");
        getProduct.next();
        ResultSet getType = connection.createStatement().executeQuery("SELECT t_name FROM type WHERE t_id = \""+getProduct.getString(6)+"\"");
        getType.next();
        this.product = new Product(getProduct.getString(1),getProduct.getString(2),
                Double.parseDouble(getProduct.getString(3)), getType.getString(1),
                Integer.parseInt(getProduct.getString(4)), getProduct.getString(5));
    }

    public void setOrderCustomer(OrderCustomer orderCustomer) {
        this.orderCustomer = orderCustomer;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public OrderCustomer getOrderCustomer() {
        return orderCustomer;
    }

    public Product getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }
}
