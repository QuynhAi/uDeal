package edu.tacoma.uw.udeal;

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


/**
 * A fragment for the settings of the application.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class ChangePasswordFragment extends Fragment {

    /** The edit text for the password. */
    private EditText myPassword;

    /** The update password button. */
    private Button updateBtn;

    /** The JSON object for the arguments. */
    private JSONObject mArguments;

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
                Intent intent = new Intent(getActivity(),SettingsActivity.class);
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
                onUpdatePassword(password);
            }
        });
        return view;
    }

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
                    Toast.makeText(getActivity(), "Password updated successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Password update was unsuccessful.", Toast.LENGTH_SHORT).show();
                }
            }catch(JSONException e){
                Toast.makeText(getActivity(), e.getMessage() , Toast.LENGTH_SHORT).show();
            }
        }
    }
}
