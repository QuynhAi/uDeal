package edu.tacoma.uw.udeal;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import model.Item;
import model.UserRegister;


public class Camera extends Fragment {
    private Button takePicture;
    private Button selectPicture;
    private Button postItem;
    private EditText mytitle;
    private EditText myprice;
    private EditText mydescription;
    private static final int PICK_FROM_GALLERY = 1;
    private static final int TAKE_PHOTO = 0;
    private ImageView myImageView;
    private JSONObject  mArguments;
    private String TAG = "addNewItem";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Post an Item");
        View view = inflater.inflate(R.layout.activity_camera, container, false);

        myImageView = (ImageView) view.findViewById(R.id.selectedphoto);

        // Take picture button to open up the camera
        takePicture = (Button) view.findViewById(R.id.takePicture);
        takePicture.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                //startActivity(intent);
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, TAKE_PHOTO);
            }
        });

        // Select picture button opens up folder
        selectPicture = (Button) view.findViewById(R.id.selectPicture);
        selectPicture.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
              //  Intent i = new Intent(Intent.ACTION_PICK,
             //           android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != getActivity().getPackageManager().PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                } else {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, PICK_FROM_GALLERY);
                }

                //   final int ACTIVITY_SELECT_IMAGE = 1234;
              //  startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
            }
        });

        // Get the text from inputs
        mytitle = (EditText) view.findViewById(R.id.title);
        myprice = (EditText) view.findViewById(R.id.price);
        mydescription = (EditText) view.findViewById(R.id.description);
        postItem = (Button) view.findViewById(R.id.post);
        postItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int id = 2; // REPLACE THIS WITH USER ID OF CURRENT USER
                String title = mytitle.getText().toString();
                double price = Double.parseDouble( myprice.getText().toString()); // FIND BETTER WAY TO CONVERT TO DOUBLE
                String desc = mydescription.getText().toString();
                String loc = "Seattle, WA"; // REPLACE THIS WITH LOCATION IN FIELD
                String cat = "Test Category"; // REPLACE THIS WITH CATEGORY FROM DROPDOWN MENU

                Item item = new Item(id, title, loc, desc, cat, price);
                onAddItem(item);
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == TAKE_PHOTO && resultCode == getActivity().RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            myImageView.setImageBitmap(photo);
        }

        else if (reqCode == PICK_FROM_GALLERY && resultCode == getActivity().RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                myImageView.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(getActivity(), "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case PICK_FROM_GALLERY:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == getActivity().getPackageManager().PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
    }

    public void onAddItem(Item item) {
        StringBuilder url = new StringBuilder(getString(R.string.additem));
        mArguments = new JSONObject();
        try {
            mArguments.put(Item.MEMBER_ID, item.getmMemberID());
            mArguments.put(Item.TITLE, item.getmTitle());
            mArguments.put(Item.LOCATION, item.getmLocation());
            mArguments.put(Item.DESCRIPTION, item.getmDescription());
            mArguments.put(Item.CATEGORY, item.getmCategory());
            mArguments.put(Item.PRICE, item.getmPrice());
            new AddItemAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(getActivity().getApplicationContext(), "Error with JSON creation: " +
                    e.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }

    private class AddItemAsyncTask extends AsyncTask<String, Void, String> {
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
                    response = "Unable to add the new item, Reason: "
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
                    Toast.makeText(getActivity().getApplicationContext(), "Successfully posted item", Toast.LENGTH_SHORT).show();
                  //  Intent intent = new Intent(AddNewUser.this, MainActivity.class);
                  //  startActivity(intent);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Missing information", Toast.LENGTH_SHORT).show();
                    //Log.e(TAG, resultObject.getString("error"));
                }
            }catch(JSONException e){
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage() , Toast.LENGTH_SHORT).show();
            }

        }
    }


}
