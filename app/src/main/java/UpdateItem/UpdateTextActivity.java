package UpdateItem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import Cart.CartActivity;
import edu.tacoma.uw.udeal.R;
import model.Item;

/**
 * This activity gives the user the ability to update the text fields of their item.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class UpdateTextActivity extends AppCompatActivity {

    /** The post item button. */
    private Button postItem;

    /** The arguments for the async task. */
    private JSONObject  mArguments;

    /** The arguments for the async task for Inbox. */
    private JSONObject  mArgumentsInbox;

    /** The tag. */
    private String TAG = "addNewItem";

    /** THe item ID. */
    private int itemID;

    /** The member ID. */
    private int memberID;

    /** The title. */
    private EditText mytitle;

    /** The price. */
    private EditText myprice;

    /** The description. */
    private EditText mydescription;

    /** The location. */
    private EditText mylocation;

    /** Dropdown for category. */
    private Spinner mycategory;

    /** The dropdown menu. */
    private Spinner dropdown;

    /** The current category. */
    private int currentCategory;

    /** The category string. */
    private String categoryString;

    /** The title. */
    private String title;

    /** The location. */
    private String location;

    /** The category. */
    private String category;

    /** The description. */
    private String description;

    /** The price. */
    private double price;

    /**
     * Sets up the ability to update the text of an item.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_text);

        setTitle("Edit Your Item");

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        itemID = b.getInt("ItemID");
        memberID = b.getInt("MemberID");
        title = b.getString("Title");
        location = b.getString("Location");
        price = b.getDouble("Price");
        category = b.getString("Category");
        description = b.getString("Description");

        //Spinner for the dropdown menu
        dropdown = findViewById(R.id.spinner);
        final String[] items = new String[]{"Select a category", "Appliances", "Auto Parts", "Books and Magazines", "Cars and Trucks",
                "Cell Phones", "Clothing and Shoes",  "Computer Equipment", "Electronics", "Furniture", "General", "Home and Garden",
                "Musical Instruments", "Photography", "Sports and Outdoors", "Tickets", "Tools and Machinery"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        currentCategory = 0;
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (currentCategory != position){
                    categoryString = items[position];
                } else {
                    ((TextView) view).setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing selected
            }
        });
        // Get the text from inputs
        mytitle = (EditText) findViewById(R.id.title);
        mytitle.setText(title);
        myprice = (EditText) findViewById(R.id.price);
        myprice.setText(price + "");
        mydescription = (EditText) findViewById(R.id.description);
        mydescription.setText(description);
        mylocation = (EditText) findViewById(R.id.location);
        mylocation.setText(location);
        mycategory = (Spinner) findViewById(R.id.spinner);
        for(int i =0; i < items.length; i++) {
            if (category.equals(items[i])) {
                mycategory.setSelection(i);
            }
        }
        postItem = (Button) findViewById(R.id.post);
        postItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int id = memberID;
                String title = mytitle.getText().toString();
                double price = Double.parseDouble( myprice.getText().toString());
                String desc = mydescription.getText().toString();
                String loc = mylocation.getText().toString();
                String cat = mycategory.getSelectedItem().toString();
                Item item = new Item(id, title, loc, desc, cat, price);
                onAddItem(item);
            }
        });
    }

    /**
     * Support for navigating up. Finishes current activity.
     *
     * @return True to support navigation up.
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    /**
     * Adds the item and creates async task to handle the upload to the database.
     *
     * @param item The item to be added to the database
     */
    public void onAddItem(Item item) {
        StringBuilder urlTextFields = new StringBuilder(getString(R.string.update_item_text));
        StringBuilder inboxurl = new StringBuilder(getString(R.string.user_inbox));
        inboxurl.append("/updateTitle");
        mArguments = new JSONObject();
        mArgumentsInbox = new JSONObject();
        try {
            mArguments.put(Item.ITEM_ID, itemID);
            mArguments.put(Item.MEMBER_ID, item.getmMemberID());
            mArguments.put(Item.TITLE, item.getmTitle());
            mArguments.put(Item.LOCATION, item.getmLocation());
            mArguments.put(Item.DESCRIPTION, item.getmDescription());
            mArguments.put(Item.CATEGORY, item.getmCategory());
            mArguments.put(Item.PRICE, item.getmPrice());

            SharedPreferences settings = getSharedPreferences((getString(R.string.LOGIN_PREFS)), Context.MODE_PRIVATE);
            String current = settings.getString(getString(R.string.username), "");
            mArgumentsInbox.put("seller", current);
            mArgumentsInbox.put("itemid", Integer.toString(itemID));
            mArgumentsInbox.put("newtitle", item.getmTitle());
            new UpdateItemInboxAsyncTask().execute(inboxurl.toString());
            new UpdateItemAsyncTask().execute(urlTextFields.toString());
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error with JSON creation" +
                    e.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * The async task to update the item text fields.
     *
     * @author TCSS 450 Team 8
     * @version 1.0
     */
    private class UpdateItemAsyncTask extends AsyncTask<String, Void, String> {
        /**
         * Handles the post of the image URL to the photo database
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
                    urlConnection.setRequestMethod("PUT");
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
         * If successful, the photo URl is successfully added to the database.
         *
         * @param result The result of the async task
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject resultObject = new JSONObject(result);
                if (resultObject.getBoolean("success") == true) {
                    Intent np = new Intent(getApplicationContext(), CartActivity.class);
                    np.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(np);
                    Toast.makeText(getApplicationContext(), "Successfully updated item", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Missing information", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * The async task to update the item for the inbox page
     *
     * @author TCSS 450 Team 8
     * @version 1.0
     */
    private class UpdateItemInboxAsyncTask extends AsyncTask<String, Void, String> {
        /**
         * Handles the post of the image URL to the photo database
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
                    urlConnection.setRequestMethod("PUT");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setDoOutput(true);
                    OutputStreamWriter wr =
                            new OutputStreamWriter(urlConnection.getOutputStream());
                    Log.e(TAG, mArgumentsInbox.toString());
                    wr.write(mArgumentsInbox.toString());
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
         * If successful, the photo URl is successfully added to the database.
         *
         * @param result The result of the async task
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject resultObject = new JSONObject(result);
                if (resultObject.getBoolean("success") == true) {
                    Toast.makeText(getApplicationContext(), "Successfully updated item", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Missing information", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
