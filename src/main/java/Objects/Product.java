package Objects;

public class Product {
    private String productId;
    private String name;
    private double price;
    private String type;
    private String inventoryName;
    private double amount;
    private String saveDate;

    public Product(String productId, String name, double price, String type, String inventoryName, String saveDate) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.type = type;
        this.inventoryName = inventoryName;
        this.saveDate = saveDate;
    }

    public Product(String productId,double amount,String name,  String inventoryName,String type, double price, String saveDate) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.type = type;
        this.inventoryName = inventoryName;
        this.saveDate = saveDate;
        this.amount = amount;
    }

    public Product(String productId, String name, double amount, String saveDate) {
        this.productId = productId;
        this.name = name;
        this.amount = amount;
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

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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

    public String getInventoryName() {
        return inventoryName;
    }

    public double getAmount() {
        return amount;
    }

    public String getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(String saveDate) {
        this.saveDate = saveDate;
    }
}
