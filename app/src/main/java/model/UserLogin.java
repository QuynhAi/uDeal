package model;

public class UserLogin {


    private String email;
    private String password;
    private String uniqueId;


    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String UNIQUE_ID = "uniqueID";

    public UserLogin( String email, String password) {

        this.email = email;
        this.password = password;
        this.uniqueId = uniqueId;
    }

    public String getEmail(){return email;}
    public String getPassword(){return password;}
    public String getUniqueId() { return uniqueId; }
}
