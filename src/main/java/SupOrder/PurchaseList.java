package SupOrder;

import Objects.Product;

public class PurchaseList {
    private String id;
    private OrderSupplier orderSupplier;
    private Product product;
    private double pAmount;

    public PurchaseList(String id,OrderSupplier orderSupplier, Product product, double pAmount) {
        this.orderSupplier = orderSupplier;
        this.product = product;
        this.pAmount = pAmount;
        this.id = id;
    }

    public void setOrderSupplier(OrderSupplier orderSupplier) {
        this.orderSupplier = orderSupplier;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setpAmount(double pAmount) {
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

    public double getpAmount() {
        return pAmount;
    }
}
