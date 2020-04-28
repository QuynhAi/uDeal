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


public class AddNewUser extends AppCompatActivity {
    private EditText firstNameEdit;
    private EditText lastNameEdit;
    private EditText usernameEdit;
    private EditText emailEdit;
    private EditText passwordEdit;

    private JSONObject  mArguments;
    private String TAG = "addNewUser";
    private Button registerBtn;
    private boolean success = false;
    //private final onAddUser mListener;
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
    private class AddUserAsyncTask extends AsyncTask<String, Void, String> {
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
        @Override
        protected void onPostExecute(String result){
            try{
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getBoolean("success") == true){
                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddNewUser.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Account already exists or missing information", Toast.LENGTH_SHORT).show();
                    //Log.e(TAG, resultObject.getString("error"));
                }
            }catch(JSONException e){
                Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_SHORT).show();
            }

        }
    }
}
