package model;

import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserRegister {

    private String first;
    private String last;
    private String username;
    private String email;
    private String password;

    public static final String FIRST_NAME = "first";
    public static final String LAST_NAME = "last";
    public static final String EMAIL = "email";
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";

    public UserRegister(String first, String last, String email, String username, String password) {
        this.first = first;
        this.last = last;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getFirstName(){return first;}
    public String getLastName() {
        return last;
    }
    public String getUserName() {
        return username;
    }
    public String getEmail(){return email;}
    public String getPassword(){return password;}

}
