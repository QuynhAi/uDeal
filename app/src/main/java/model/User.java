package model;

import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String uniqueId;

    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String USER_NAME = "userName";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String UNIQUE_ID = "uniqueID";

    public User(String firstName, String lastName, String username, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.uniqueId = uniqueId;
    }

    public String getFirstName(){return firstName;}
    public String getLastName() {
        return lastName;
    }
    public String getUserName() {
        return username;
    }
    public String getEmail(){return email;}
    public String getPassword(){return password;}
    public String getUniqueId() { return uniqueId; }

}
