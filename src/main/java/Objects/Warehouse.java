package Objects;

public class Warehouse {
    private String id;
    private String level;

    public Warehouse(String id, String level) {
        this.id = id;
        this.level = level;
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
}
