package Objects;

public class Shelf {
    private String id;
    private String level;
    private Warehouse warehouse;

    public Shelf(String id, String level,Warehouse warehouse) {
        this.id = id;
        this.level = level;
        this.warehouse = warehouse;
    }

    public String getId() {
        return id;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }
}