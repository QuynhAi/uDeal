package Settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.tacoma.uw.udeal.R;


/**
 * A fragment for writing a review about a transaction.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class WriteReview extends Fragment {

    /** The update password button. */
    private Button postReviewButton;

    /** The JSON object for the arguments. */
    private JSONObject mArguments;

    /** The dropdown menu. */
    private Spinner dropdown;

    /** The current category. */
    private int currentCategory;

    /** The category string. */
    private String categoryString;

    /** The review edit text. */
    private EditText review;

    /** The username edit text. */
    private EditText username;

    /** The rating bar. */
    private RatingBar ratingBar;

    /**
     * Sets up the fragment for writing a review.
     *
     * @param inflater The layout inflater
     * @param container The view group container
     * @param savedInstanceState The bundle saved instance
     * @return The view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.write_review, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Write a Review");
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

        dropdown = view.findViewById(R.id.spinner);
        final String[] items = new String[]{"Select your role", "Buyer", "Seller"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        currentCategory = 0;
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (currentCategory != position){
                    categoryString = items[position];
                } else {
                    //top is correct way but doesn't work. second way works
//                    ((TextView) view).setTextColor(getResources().getColor(R.color.colorPrimary));
                    ((TextView) view).setTextColor(R.color.colorPrimary);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing selected
            }
        });

        postReviewButton = (Button) view.findViewById(R.id.postreview);
        review = (EditText) view.findViewById(R.id.review);
        username = (EditText) view.findViewById(R.id.username);
        ratingBar = (RatingBar) view.findViewById(R.id.stars);

        postReviewButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String myReview = review.getText().toString();
                String myUsername = username.getText().toString();
                String myRole = dropdown.getSelectedItem().toString();
                double myRating = ratingBar.getRating();
                addReview(myReview, myUsername, myRole, myRating);
            }
        });
        return view;
    }

    /**
     * Adds a review by executing the async task.
     *
     * @param theReview The review
     * @param theUsername The username of the one to be reviewed
     * @param theRole The role of the user
     * @param theRating The rating of the transaction
     */
    private void addReview(String theReview, String theUsername, String theRole, double theRating) {
        SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);
        mArguments = new JSONObject();
        try {
            mArguments.put("reviewed", theUsername);
            mArguments.put("reviewer", settings.getString(getString(R.string.username), "" ));
            mArguments.put("rating", theRating);
            mArguments.put("review", theReview);
            mArguments.put("role", theRole);
            new AddReviewAsyncTask().execute(getString(R.string.add_review));
        } catch (JSONException e) {
            Toast.makeText(getActivity().getApplicationContext(), "Error with JSON creation" +
                    e.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * The async task to add a review.
     *
     * @author TCSS 450 Team 8
     * @version 1.0
     */
    private class AddReviewAsyncTask extends AsyncTask<String, Void, String> {
        /**
         * Handles the post of the review's text fields to the database.
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
                    response = "Unable to add the new item, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * If successful, the review is successfully posted.
         *
         * @param result The result of the async task
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject resultObject = new JSONObject(result);
                if (resultObject.getBoolean("success") == true) {
                    Intent intent = new Intent(getActivity(),SettingsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    getActivity().overridePendingTransition(0,0);
                    Toast.makeText(getActivity().getApplicationContext(), "Successfully posted the review", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Missing information", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
