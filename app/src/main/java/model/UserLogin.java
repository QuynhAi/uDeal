package model;

/**
 * This class handles the user login information.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class UserLogin {

    /** The email of the user. */
    private String email;

    /** The password of the user. */
    private String password;

    /** Email string. */
    public static final String EMAIL = "email";

    /** Password string. */
    public static final String PASSWORD = "password";

    /**
     * Initializes the email and the password.
     *
     * @param email The email
     * @param password The password
     */
    public UserLogin( String email, String password) {

        this.email = email;
        this.password = password;
    }

    /**
     * Gets the email.
     *
     * @return The email.
     */
    public String getEmail(){
        return email;
    }

    /**
     * Gets the password.
     *
     * @return The password.
     */
    public String getPassword(){
        return password;
    }
}
