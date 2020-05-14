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
import java.io.OutputStream;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Currency;

import model.UserLogin;

public class Login extends AppCompatActivity {

    private EditText emailEdit;
    private EditText passwordEdit;
    private JSONObject mArguments;
    private String TAG = "Login";
    public static String CURRENT_USER;
    private String inputtedEmail = "";

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEdit = (EditText) findViewById(R.id.email);
        passwordEdit = (EditText) findViewById(R.id.password);

        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS)
                , Context.MODE_PRIVATE);
        if(!mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
           // getSupportFragmentManager().beginTransaction()
           //         .add(R.id.sign_in_fragment_id, new LoginFragment())
            //        .commit();
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
                if(CURRENT_USER == null){
                    CURRENT_USER = email;
                }
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

    private class LoginAsyncTask extends AsyncTask<String, Void, String> {
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
                    //Log.e(TAG, resultObject.getString("error"));
                }
            }catch(JSONException e){
                Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class GetUserInfoAsyncTask extends AsyncTask<String, Void, String> {
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
                    CURRENT_USER = username;
                    int userid = jsonObject.getJSONArray("names").getJSONObject(0).getInt("member_id");
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString(getString(R.string.email), inputtedEmail);
                    editor.putString(getString(R.string.username), username);
                    editor.putInt(getString(R.string.member_id), userid);
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
