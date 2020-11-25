package User;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordBCrypt {
    private UserID userID;
    private String hashed;

    public PasswordBCrypt(UserID userID) {
        this.userID = userID;
    }
    public void encryptPassword() {
        hashed = BCrypt.hashpw(this.userID.getPassword(),BCrypt.gensalt(10));
    }

    public boolean decryptPassword(String password){
        return BCrypt.checkpw(password, userID.getPassword());
    }

    public String getHashed() {
        return hashed;
    }

    //UserID userID = new UserID(123,"Tong","Tonqs","0000");
    //String pass = "0000";
    //PasswordBCrypt passwordBcrypt = new PasswordBCrypt(userID);
    //passwordBcrypt.hashPassword(pass);
}
