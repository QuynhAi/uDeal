package edu.tacoma.uw.udeal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import model.UserLogin;

/**
 * This activity handles the login for the application.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class Login extends AppCompatActivity {

    /** The email edit text. */
    private EditText emailEdit;

    /** The possword edit text. */
    private EditText passwordEdit;

    /** The arguments for the async task. */
    private JSONObject mArguments;

    /** The login tag. */
    private String TAG = "Login";

    /** The inputted email. */
    private String inputtedEmail = "";

    /** The shared preferences for the application. */
    private SharedPreferences mSharedPreferences;

    /**
     * Sets up the view of the activity and the shared preferences to determine
     * if the user has already logged in.
     *
     * @param savedInstanceState The instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEdit = (EditText) findViewById(R.id.email);
        passwordEdit = (EditText) findViewById(R.id.password);

        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS)
                , Context.MODE_PRIVATE);
        if(!mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
            // Not yet logged in
        } else {
            // Go to main activity if we have already logged in
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        Button loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String email = emailEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                UserLogin userLogin = new UserLogin(email, password);
                loginMethod(userLogin);

            }
        });

        TextView textBtn = (TextView) findViewById(R.id.registerNowText);
        textBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Login.this, AddNewUser.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Logins the user and calls an async task.
     *
     * @param userLogin The user login information
     * @throws JSONException if the JSON cannot be created
     */
    public void loginMethod(UserLogin userLogin) {
        StringBuilder url = new StringBuilder(getString(R.string.login));
        //Construct a JSONObject to build a formatted message to send.
        mArguments = new JSONObject();
        try {
            mArguments.put(UserLogin.EMAIL, userLogin.getEmail());
            mArguments.put(UserLogin.PASSWORD, userLogin.getPassword());
            inputtedEmail = userLogin.getEmail();
            new LoginAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error with JSON creation: " + e.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * The login async task.
     *
     * @author TCSS 450 Team 8
     * @version 1.0
     */
    private class LoginAsyncTask extends AsyncTask<String, Void, String> {
        /**
         * Performs the login operation.
         *
         * @param urls The URl to verify the login information
         * @return The response from the task
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
                    response = e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * If successful, we log in succesfullly and store the information
         * in the shared preferences.
         *
         * @param result The result from the async task
         */
        @Override
        protected void onPostExecute(String result){
            try{
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getBoolean("success") == true){
                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                    mSharedPreferences
                            .edit()
                            .putBoolean(getString(R.string.LOGGEDIN), true)
                            .commit();
                    StringBuilder getUserInfoURL = new StringBuilder(getString(R.string.get_specific_user));
                    getUserInfoURL.append(inputtedEmail);
                    new GetUserInfoAsyncTask().execute(getUserInfoURL.toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Credentials did not match or missing information ", Toast.LENGTH_SHORT).show();
                }
            }catch(JSONException e){
                Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_SHORT).show();
            }

        }
    }

    /**
     * The async task to get user information.
     *
     * @author TCSS 450 Team 8
     * @version 1.0
     */
    private class GetUserInfoAsyncTask extends AsyncTask<String, Void, String> {
        /**
         * Gets the user information.
         *
         * @param urls The URl to get the user information
         * @return The response from the task
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to get user information, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * If successful, we get the user information and add the information to the shared
         * preferences.
         *
         * @param s The result from the async task
         */
        @Override
        protected void onPostExecute(String s) {
            if (s.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), "Unable to get user information: " + s, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("success")) {
                    String username = jsonObject.getJSONArray("names").getJSONObject(0).getString("username");
                    int userid = jsonObject.getJSONArray("names").getJSONObject(0).getInt("member_id");
                    String firstName = jsonObject.getJSONArray("names").getJSONObject(0).getString("first_name");
                    String lastName = jsonObject.getJSONArray("names").getJSONObject(0).getString("last_name");
                    String fullName = firstName + " " + lastName;
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString(getString(R.string.email), inputtedEmail);
                    editor.putString(getString(R.string.username), username);
                    editor.putInt(getString(R.string.member_id), userid);
                    editor.putString(getString(R.string.fullname), fullName);
                    editor.commit();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            } catch (JSONException e) {
                Log.d("myTag","FAILURE");
                Toast.makeText(getApplicationContext(), "JSON Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
