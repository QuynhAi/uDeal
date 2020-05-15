package model;

/**
 * This class handles the registration for the user.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class UserRegister {

    /** The first string. */
    public static final String FIRST_NAME = "first";

    /** The last string. */
    public static final String LAST_NAME = "last";

    /** The email string. */
    public static final String EMAIL = "email";

    /** The username. */
    public static final String USER_NAME = "username";

    /** The password. */
    public static final String PASSWORD = "password";

    /** The first name. */
    private String first;

    /** The last name. */
    private String last;

    /** The username. */
    private String username;

    /** The email. */
    private String email;

    /** The password. */
    private String password;

    /**
     * Initializes the user registration fields.
     *
     * @param first The first name
     * @param last The last name
     * @param email The email
     * @param username The username
     * @param password The password
     */
    public UserRegister(String first, String last, String email, String username, String password) {
        this.first = first;
        this.last = last;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * Gets the first name.
     *
     * @return The first name
     */
    public String getFirstName(){
        return first;
    }

    /**
     * Gets the last name.
     *
     * @return The last name.
     */
    public String getLastName() {
        return last;
    }

    /**
     * Gets the username.
     *
     * @return The username.
     */
    public String getUserName() {
        return username;
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
