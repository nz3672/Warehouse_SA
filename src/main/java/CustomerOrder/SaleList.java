package CustomerOrder;

import Objects.Product;

public class SaleList {
    private String id;
    private OrderCustomer orderCustomer;
    private Product product;
    private int amount;

    public SaleList(String id, Product product, OrderCustomer orderCustomer, int amount) {
        this.id = id;
        this.orderCustomer = orderCustomer;
        this.product = product;
        this.amount = amount;
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
