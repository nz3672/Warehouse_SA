package Objects;

public class WarehouseList {
    private String id;
    private Warehouse warehouse;
    private Product product;

    public WarehouseList(String id, Warehouse warehouse, Product product) {
        this.warehouse = warehouse;
        this.product = product;
    }

    public WarehouseList(Warehouse warehouse, Product product) {
        this.warehouse = warehouse;
        this.product = product;
    }

    public String getId() {
        return id;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public Product getProduct() {
        return product;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
