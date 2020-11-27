package CustomerOrder;

public class Customer {
    private String id;
    private String address;
    private String phone;

    public Customer(String id, String address, String phone) {
        this.id = id;
        this.address = address;
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getId() {
        return id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
