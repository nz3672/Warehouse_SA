package Objects;

public class Product {
    private String productId;
    private String name;
    private double price;
    private String type;
    private Warehouse Warehouse;
    private int quantity;
    private String saveDate;

    public Product(String productId, String name, double price, String type, Warehouse Warehouse, String saveDate) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.type = type;
        this.Warehouse = Warehouse;
        this.saveDate = saveDate;
    }

    public Product(String productId, int quantity, String name, Warehouse Warehouse, String type, double price, String saveDate) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.type = type;
        this.Warehouse = Warehouse;
        this.saveDate = saveDate;
        this.quantity = quantity;
    }

    public Product(String productId, String name, double price, String type, int quantity, String saveDate) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.type = type;
        this.quantity = quantity;
        this.saveDate = saveDate;
    }

    public Product(String productId, String name, int quantity, String saveDate) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.saveDate = saveDate;
    }

    public Product(String productId, String name, double price, String type, String saveDate) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.type = type;
        this.saveDate = saveDate;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.Warehouse = warehouse;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public Warehouse getWarehouse() {
        return Warehouse;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(String saveDate) {
        this.saveDate = saveDate;
    }
}