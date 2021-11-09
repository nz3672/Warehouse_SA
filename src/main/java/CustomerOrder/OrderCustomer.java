package CustomerOrder;

import javafx.scene.control.CheckBox;

import java.util.List;

public class OrderCustomer {
    private String id;
    private String save_date;
    private String pay_date;
    private Customer customer;
    private List<SaleList> saleLists;
    private String employee_name;
    private double total_price;
    private String status;
    private CheckBox checkBox;


    public OrderCustomer(String id, String save_date, Customer customer, List<SaleList> saleLists) {
        this.id = id;
        this.save_date = save_date;
        this.customer = customer;
        this.saleLists = saleLists;
    }

    public OrderCustomer(String id, Customer customer, String save_date) {
        this.id = id;
        this.save_date = save_date;
        this.customer = customer;
    }

    public OrderCustomer(String id, Customer customer, String employee_name, String pay_date, String save_date, double total_price, String status) {
        this.id = id;
        this.save_date = save_date;
        this.pay_date = pay_date;
        this.customer = customer;
        this.employee_name = employee_name;
        this.total_price = total_price;
        this.status = status;
        this.checkBox = new CheckBox();
    }

    public String getPay_date() {
        return pay_date;
    }

    public void setPay_date(String pay_date) {
        this.pay_date = pay_date;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getSave_date() {
        return save_date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<SaleList> getSaleLists() {
        return saleLists;
    }

    public void setSave_date(String save_date) {
        this.save_date = save_date;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setSaleLists(List<SaleList> saleLists) {
        this.saleLists = saleLists;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }
}