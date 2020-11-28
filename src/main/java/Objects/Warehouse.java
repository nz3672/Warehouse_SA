package Objects;

public class Warehouse {
    private String id;
    private String level;
    private String shelf;
    private String shelfLevel;

    public Warehouse(String id, String level, String shelf, String shelfLevel) {
        this.id = id;
        this.level = level;
        this.shelf = shelf;
        this.shelfLevel = shelfLevel;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    public void setShelfLevel(String shelfLevel) {
        this.shelfLevel = shelfLevel;
    }

    public String getId() {
        return id;
    }

    public String getLevel() {
        return level;
    }

    public String getShelf() {
        return shelf;
    }

    public String getShelfLevel() {
        return shelfLevel;
    }

    @Override
    public String toString() {
        return "id= " + id + ' ' +
                ",level= " + level + ' ' +
                ",shelf= " + shelf + ' ' +
                ",shelfLevel= " + shelfLevel + ' ' +
                '}';
    }
}
