package model;

public class UserLogin {


    private String email;
    private String password;
    private String uniqueId;


    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";

    public UserLogin( String email, String password) {

        this.email = email;
        this.password = password;
    }

    public String getEmail(){return email;}
    public String getPassword(){return password;}
}
