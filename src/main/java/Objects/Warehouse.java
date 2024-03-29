package Objects;

import Connection.ConnectionHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Warehouse {
    private String id;
    private String name;
    private String level;
    private String shelf;
    private String shelfLevel;

    public Warehouse(String name, String level, String shelf, String shelfLevel) throws SQLException {
        this.name = name;
        this.level = level;
        this.shelf = shelf;
        this.shelfLevel = shelfLevel;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return  id + ' ' + name+ "," + level +
                "," + shelf + ',' + shelfLevel ;
    }
}
