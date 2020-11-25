package User;

public class UserID {
    private String employeeId;
    private String name;
    private String username;
    private String password;

    public UserID(String employeeId, String name, String username, String password) {
        this.employeeId = employeeId;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void deleteID(){
        //
    }
}
