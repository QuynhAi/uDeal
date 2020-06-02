package edu.tacoma.uw.udeal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
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

import model.EmailItem;


/**
 * This fragment handles inviting friends to the application.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class InviteFriends extends Fragment {
    private Button invite;
    private EditText firstName;
    private EditText lastName;
    private EditText emailo;
    private EmailItem addThisItem;

    /** The tag. */
    private String TAG = "addNewItem";
    /** The arguments for the async task. */
    private JSONObject  mArguments;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.invite_friends, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Invite Friends");
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

        invite = (Button) view.findViewById(R.id.invitebutton);
        firstName = (EditText) view.findViewById(R.id.firstName);
        lastName = (EditText) view.findViewById(R.id.lastName);
        emailo = (EditText) view.findViewById(R.id.emailout);
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS),
                        Context.MODE_PRIVATE);
                int id = settings.getInt(getString(R.string.member_id), 0);
                String fName = firstName.getText().toString();
                String lName = lastName.getText().toString();
                String oemail = emailo.getText().toString();
                if (!oemail.contains("@") || !oemail.contains(".edu")) {
                    Toast.makeText(v.getContext(), "Enter valid .edu email address",
                            Toast.LENGTH_LONG).show();
                    emailo.requestFocus();
                } else {
                    EmailItem item = new EmailItem(id, fName, lName, oemail);
                    addThisItem = item;
                    onAddItem(item);
                }
            }
        });
        return view;
    }

    /**
     * Adds the item and creates async task to handle the upload to the database.
     *
     * @param item The item to be added to the database
     */
    public void onAddItem(EmailItem item) {
        StringBuilder urlTextFields = new StringBuilder(getString(R.string.sendinvite));
        mArguments = new JSONObject();
        try {
            mArguments.put(EmailItem.USER_ID, item.getmUserID());
            mArguments.put(EmailItem.FNAME_ID, item.getmFName());
            mArguments.put(EmailItem.LNAME_ID, item.getmLName());
            mArguments.put(EmailItem.EMAIL_ID, item.getmEmail());

            new AddItemAsyncTask().execute(urlTextFields.toString());
        } catch (JSONException e) {
            Toast.makeText(getActivity().getApplicationContext(), "Error with JSON creation" +
                    e.getMessage() , Toast.LENGTH_LONG).show();
        }
    }

    /**
     * The async task to send invite.
     *
     * @author TCSS 450 Team 8
     * @version 1.0
     */
    private class AddItemAsyncTask extends AsyncTask<String, Void, String> {
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
                    Toast.makeText(getActivity().getApplicationContext(), "Successfully sent invite", Toast.LENGTH_LONG).show();
                    firstName.setText("");
                    lastName.setText("");
                    emailo.setText("");
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Missing information", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

}
