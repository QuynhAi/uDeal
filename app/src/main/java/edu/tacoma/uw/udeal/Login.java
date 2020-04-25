package edu.tacoma.uw.udeal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import model.User;
import model.UserLogin;

public class Login extends AppCompatActivity {

    private EditText emailEdit;
    private EditText passwordEdit;
    private JSONObject mArguments;
    private String TAG = "Login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEdit = (EditText) findViewById(R.id.email);
        passwordEdit = (EditText) findViewById(R.id.password);


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

    public void loginMethod(UserLogin userLogin) {
        StringBuilder url = new StringBuilder(getString(R.string.login));
        //Construct a JSONObject to build a formatted message to send.
        mArguments = new JSONObject();
        try {
            mArguments.put(UserLogin.EMAIL, userLogin.getEmail());
            mArguments.put(UserLogin.PASSWORD, userLogin.getPassword());
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
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Credentials did not match or missing information ", Toast.LENGTH_SHORT).show();
                    //Log.e(TAG, resultObject.getString("error"));
                }
            }catch(JSONException e){
                Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_SHORT).show();
            }

        }
    }

}
