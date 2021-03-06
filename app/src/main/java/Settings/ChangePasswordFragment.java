package Settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import edu.tacoma.uw.udeal.R;

import model.EmailItem;
import model.NewPassEmail;
import model.UserRegister;


/**
 * A fragment for changing the password of the user.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class ChangePasswordFragment extends Fragment {

    /** The edit text for the password. */
    private EditText myPassword;

    /** The update password button. */
    private Button updateBtn;

    /** The JSON object for the arguments. update pass */
    private JSONObject mArguments;

    /** The JSON object for the arguments. email update*/
    private JSONObject mArguments2;

    /**
     * Sets up the fragment for changing the password.
     *
     * @param inflater The layout inflater
     * @param container The view group container
     * @param savedInstanceState The bundle saved instance
     * @return The view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_password, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Change Password");
        toolbar.setTitleTextColor(Color.WHITE);

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                getActivity().overridePendingTransition(0,0);
            }
        });
        myPassword = (EditText) view.findViewById(R.id.newpw);
        updateBtn = (Button) view.findViewById(R.id.updatepw);

        updateBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String password = myPassword.getText().toString();
                if (TextUtils.isEmpty(password) || password.length() < 6){
                    Toast.makeText(v.getContext(), "Enter valid password (at least 6 characters)",
                            Toast.LENGTH_LONG).show();
                    myPassword.requestFocus();
                } else {
                    onUpdatePassword(password);
                }
            }
        });
        return view;
    }

    /**
     * Updates the password by executing an async task.
     *
     * @param password The new password
     */
    private void onUpdatePassword(String password) {
        StringBuilder url = new StringBuilder(getString(R.string.change_password));
        SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        String username = settings.getString(getString(R.string.username), "");
        mArguments = new JSONObject();
        try {
            mArguments.put("username", username);
            mArguments.put("password", password);
            new UpdatePasswordAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(getActivity(), "Error with JSON creation: " + e.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Async task that adds the user to the database.
     *
     * @author TCSS 450 Team 8
     * @version 1.0
     */
    private class UpdatePasswordAsyncTask extends AsyncTask<String, Void, String> {
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
                    urlConnection.setRequestMethod("PUT");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setDoOutput(true);
                    OutputStreamWriter wr =
                            new OutputStreamWriter(urlConnection.getOutputStream());
                    Log.i("myTag", mArguments.toString());
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
                    response = "Unable to update the password, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * If password update is successful, a toast is displayed saying it was successful. If the
         * update is unsuccessful, the toast is displayed saying it was unsuccessful.
         *
         * @param result The result from the async task
         */
        @Override
        protected void onPostExecute(String result){
            try{
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getBoolean("success") == true){
                    SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS),
                            Context.MODE_PRIVATE);
                    String name = settings.getString(getString(R.string.fullname), "");
                    String email = settings.getString(getString(R.string.email), "");
                    NewPassEmail item = new NewPassEmail(name, email);
                    emailNotice(item);
                    myPassword.setText("");
                    Toast.makeText(getActivity(), "Password updated successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Password update was unsuccessful.", Toast.LENGTH_SHORT).show();
                }
            }catch(JSONException e){
                Toast.makeText(getActivity(), e.getMessage() , Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Adds the item and creates async task to handle the upload to the database.
     *
     * @param item The item to be added to the database
     */
    public void emailNotice(NewPassEmail item) {
        StringBuilder urlTextFields = new StringBuilder(getString(R.string.sendpwupdate));
        mArguments2 = new JSONObject();
        try {
            mArguments2.put(NewPassEmail.USER_NAME, item.getmUserName());
            mArguments2.put(NewPassEmail.USER_EMAIL, item.getmEmail());


            new ChangePassAsyncTask().execute(urlTextFields.toString());
        } catch (JSONException e) {
            Toast.makeText(getActivity().getApplicationContext(), "Error with JSON creation" +
                    e.getMessage() , Toast.LENGTH_LONG).show();
        }
    }

    /**
     * The async task to send password update email.
     *
     * @author TCSS 450 Team 8
     * @version 1.0
     */
    private class ChangePassAsyncTask extends AsyncTask<String, Void, String> {
        /**
         * Handles the post of the emails text fields to the database.
         *
         * @param urls The URL to establish the connection
         * @return The result of the async task
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
                    Log.i("argument2", mArguments2.toString());
                    wr.write(mArguments2.toString());
                    wr.flush();
                    wr.close();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to send email, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * If successful, the invite is successfully sent.
         *
         * @param result The result of the async task
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject resultObject = new JSONObject(result);
                if (resultObject.getBoolean("success") == true) {

                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Missing information", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
