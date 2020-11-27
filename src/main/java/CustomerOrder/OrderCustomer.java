package CustomerOrder;

import java.util.List;

public class OrderCustomer {
    private String id;
    private String date;
    private Customer customer;
    private List<SaleList> saleLists;

    public OrderCustomer(String id, String date, Customer customer, List<SaleList> saleLists) {
        this.id = id;
        this.date = date;
        this.customer = customer;
        this.saleLists = saleLists;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<SaleList> getSaleLists() {
        return saleLists;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setSaleLists(List<SaleList> saleLists) {
        this.saleLists = saleLists;
    }
}
