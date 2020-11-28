package SupOrder;

public class Supplier {
    private String id;
    private String phone;
    private String address;

    public Supplier(String id, String phone, String address) {
        this.id = id;
        this.phone = phone;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
