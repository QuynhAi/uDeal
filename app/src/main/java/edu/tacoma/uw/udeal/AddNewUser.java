package edu.tacoma.uw.udeal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import model.UserRegister;

/**
 * This activity handles the addition of a new user.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class AddNewUser extends AppCompatActivity {

    /** The first name edit text. */
    private EditText firstNameEdit;

    /** The last name edit text. */
    private EditText lastNameEdit;

    /** The username edit text. */
    private EditText usernameEdit;

    /** The email edit text. */
    private EditText emailEdit;

    /** The password edit text. */
    private EditText passwordEdit;

    /** The JSON object for the arguments. */
    private JSONObject  mArguments;

    /** The add new user tag. */
    private String TAG = "addNewUser";

    /** The register button. */
    private Button registerBtn;

    /**
     * Initalizes all of the edit text fields and sets up the on click
     * listener of the button.
     *
     * @param savedInstanceState The saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        firstNameEdit = (EditText)findViewById(R.id.firstName);
        lastNameEdit = (EditText)findViewById(R.id.lastName);
        usernameEdit = (EditText) findViewById(R.id.username);
        emailEdit = (EditText) findViewById(R.id.email);
        passwordEdit = (EditText) findViewById(R.id.password);

        registerBtn = (Button)findViewById(R.id.registerButton);
        registerBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String firstName = firstNameEdit.getText().toString();
                String lastname = lastNameEdit.getText().toString();
                String username = usernameEdit.getText().toString();
                String email = emailEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                UserRegister user = new UserRegister(firstName, lastname, email, username, password);
                onAddUser(user);
            }
        });
    }

    /**
     * Method that adds the user. Calls an async task and passes the appropriate arguments.
     *
     * @param user The user that is registering
     */
    public void onAddUser(UserRegister user) {
        StringBuilder url = new StringBuilder(getString(R.string.register));
        mArguments = new JSONObject();
        try {
            mArguments.put(UserRegister.FIRST_NAME, user.getFirstName());
            mArguments.put(UserRegister.LAST_NAME, user.getLastName());
            mArguments.put(UserRegister.EMAIL, user.getEmail());
            mArguments.put(UserRegister.USER_NAME, user.getUserName());
            mArguments.put(UserRegister.PASSWORD, user.getPassword());
            new AddUserAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error with JSON creation: " + e.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Async task that adds the user to the database.
     *
     * @author TCSS 450 Team 8
     * @version 1.0
     */
    private class AddUserAsyncTask extends AsyncTask<String, Void, String> {
        /**
         * Performs the async task to register the user in the database.
         *
         * @param urls The url endpoint for the registration
         * @return The response from the async task
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setDoOutput(true);
                    OutputStreamWriter wr =
                            new OutputStreamWriter(urlConnection.getOutputStream());
                    Log.i(TAG, mArguments.toString());
                    wr.write(mArguments.toString());
                    wr.flush();
                    wr.close();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to add the new user, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * If registration is successful, user is redirected to the login activity. If the account
         * already exists are information is mission, toast is displayed on screen.
         *
         * @param result The result from the async task
         */
        @Override
        protected void onPostExecute(String result){
            try{
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getBoolean("success") == true){
                    Toast.makeText(getApplicationContext(), "Registration successful. Please login with your newly registered account.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddNewUser.this, Login.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Account already exists or missing information", Toast.LENGTH_SHORT).show();
                }
            }catch(JSONException e){
                Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_SHORT).show();
            }
        }
    }
}
