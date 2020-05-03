package edu.tacoma.uw.udeal;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private JSONObject mArguments2;
    private String TAG = "addNewItem";
    public static final String MY_URL_TAG = "url";
    public static final String MY_FILE_NAME = "myfilename";
    public static final String MY_FILE = "myfile";
    public File compressedFile;
    private Bitmap tempPhotoStorage;
    private String imageUploadName;

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
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != getActivity().getPackageManager().PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                } else {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, PICK_FROM_GALLERY);
                }
            }
        });

        // Get the text from inputs
        mytitle = (EditText) view.findViewById(R.id.title);
        myprice = (EditText) view.findViewById(R.id.price);
        mydescription = (EditText) view.findViewById(R.id.description);
        postItem = (Button) view.findViewById(R.id.post);
        postItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int id = 3; // TODO: REPLACE THIS WITH USER ID OF CURRENT USER
                String title = mytitle.getText().toString();
                double price = Double.parseDouble( myprice.getText().toString()); // TODO: FIND BETTER WAY TO CONVERT TO DOUBLE
                String desc = mydescription.getText().toString();
                String loc = "Bellevue, WA"; // TODO: REPLACE THIS WITH LOCATION IN FIELD
                String cat = "Automobiles"; // TODO: REPLACE THIS WITH CATEGORY FROM DROPDOWN MENU
                // TODO: POSSIBLY INCLUDE TIMESTAMP COLUMN IN DATABASE WHEN ITEM WAS ADDED
                Item item = new Item(id, title, loc, desc, cat, price);
                onAddItem(item);
                onAddImage(item);
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
            tempPhotoStorage = photo;
            myImageView.setImageBitmap(photo);
        }

        else if (reqCode == PICK_FROM_GALLERY && resultCode == getActivity().RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                tempPhotoStorage = selectedImage;
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
                    //TODO: something like displaying a message that user didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
    }

    public void onAddItem(Item item) {
        StringBuilder urlTextFields = new StringBuilder(getString(R.string.additem));
        mArguments = new JSONObject();
        // HANDLE ALL THE TEXT FIELDS (PUT IN POSTGRESSQL DATABASE)
        try {
            mArguments.put(Item.MEMBER_ID, item.getmMemberID());
            mArguments.put(Item.TITLE, item.getmTitle());
            mArguments.put(Item.LOCATION, item.getmLocation());
            mArguments.put(Item.DESCRIPTION, item.getmDescription());
            mArguments.put(Item.CATEGORY, item.getmCategory());
            mArguments.put(Item.PRICE, item.getmPrice());
            new AddItemAsyncTask().execute(urlTextFields.toString());
        } catch (JSONException e) {
            Toast.makeText(getActivity().getApplicationContext(), "TEXT FIELDS Error with JSON creation: " +
                    e.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }

    public void onAddImage(Item item) {
        // Handle unique key of image (put in database)
        StringBuilder urlURL = new StringBuilder(getString(R.string.addphoto));
        mArguments2 = new JSONObject();
        // First create the unique URL extension
        String tempPhotoID = generatePhotoID();
        try {
            mArguments2.put(Item.MEMBER_ID, item.getmMemberID());
            // TODO: CHANGE THIS TO GET ITEM ID. Possibly put this in the AddItem class once
            // TODO: it is done uploading, so you can then retrieve the item ID to execute this
            mArguments2.put(Item.ITEM_ID, 8);
            mArguments2.put(MY_URL_TAG, tempPhotoID);
            new AddPhotoAsyncTask().execute(urlURL.toString());
        } catch (JSONException e) {
            Toast.makeText(getActivity().getApplicationContext(), "URL Error with JSON creation: " +
                    e.getMessage() , Toast.LENGTH_SHORT).show();
        }
        // HANDLE THE IMAGE ITSELF (PUT IN S3)
        processImage();
        new MultipartUtility().execute(getString(R.string.upload));
    }

    private String generatePhotoID() {
        String temp = UUID.randomUUID().toString();
        imageUploadName = temp + ".jpg";
        return temp + ".jpg";
    }

    private void processImage() {
        try {
            File f = new File(getActivity().getApplicationContext().getCacheDir(), "tempimage.jpg");
            f.createNewFile();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            tempPhotoStorage.compress(Bitmap.CompressFormat.JPEG, 100, out);
            byte[] bitmapdata = out.toByteArray();
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            compressedFile = f;
        } catch (IOException e) {
            Log.d("myTag", "Compressed file creation unsuccessful");
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
        protected void onPostExecute(String result) {
            try {
                JSONObject resultObject = new JSONObject(result);
                if (resultObject.getBoolean("success") == true) {
                    Toast.makeText(getActivity().getApplicationContext(), "Successfully posted item", Toast.LENGTH_SHORT).show();
                    //  Intent intent = new Intent(AddNewUser.this, MainActivity.class);
                    //  startActivity(intent);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Missing information", Toast.LENGTH_SHORT).show();
                    //Log.e(TAG, resultObject.getString("error"));
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class AddPhotoAsyncTask extends AsyncTask<String, Void, String> {
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
                    Log.i(TAG, mArguments2.toString());
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
        protected void onPostExecute(String result) {
            try {
                JSONObject resultObject = new JSONObject(result);
                if (resultObject.getBoolean("success") == true) {
                    Toast.makeText(getActivity().getApplicationContext(), "Successfully posted item", Toast.LENGTH_SHORT).show();
                    //  Intent intent = new Intent(AddNewUser.this, MainActivity.class);
                    //  startActivity(intent);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Missing information", Toast.LENGTH_SHORT).show();
                    //Log.e(TAG, resultObject.getString("error"));
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }


    public class MultipartUtility extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            String response = "";
            String boundary = "*****" + System.currentTimeMillis() + "*****";
            String LINE_FEED = "\r\n";
            HttpURLConnection urlConnection = null;
            Log.d("myTag", MY_FILE_NAME + " "  + imageUploadName);
            if(compressedFile != null) {
                Log.d("myTag",  MY_FILE + " image not null");
            }
            for (String url : urls) {
                Log.d("myTag",  url);
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    urlConnection.setUseCaches(false);
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    urlConnection.setRequestProperty("Content-Type",
                            "multipart/form-data; boundary=" + boundary);
                    OutputStream outputStream = urlConnection.getOutputStream();
                    OutputStreamWriter wr = new OutputStreamWriter(outputStream);//CHARSET
                    PrintWriter writer = new PrintWriter(wr, true);
                    writer.append("--" + boundary).append(LINE_FEED);
                    writer.append("Content-Disposition: form-data; name=\"" + MY_FILE_NAME + "\"")
                            .append(LINE_FEED);
                    writer.append("Content-Type: text/plain").append(
                            LINE_FEED);
                    writer.append(LINE_FEED);
                    writer.append(imageUploadName);
                    writer.flush();
                    writer.append(LINE_FEED);
                    String fileName = compressedFile.getName();
                    writer.append("--" + boundary).append(LINE_FEED);
                    writer.append(
                            "Content-Disposition: form-data; name=\"" + MY_FILE
                                    + "\"; filename=\"" + fileName + "\"")
                            .append(LINE_FEED);
                    writer.append(
                            "Content-Type: "
                                    + URLConnection.guessContentTypeFromName(fileName))
                            .append(LINE_FEED);
                    writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
                    writer.append(LINE_FEED);
                    writer.flush();

                    FileInputStream inputStream = new FileInputStream(compressedFile);
                    byte[] buffer = new byte[4096];
                    int bytesRead = -1;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.flush();
                    inputStream.close();
                    writer.append(LINE_FEED);
                    writer.flush();

                    writer.append(LINE_FEED).flush();
                    writer.append("--" + boundary + "--").append(LINE_FEED);
                    writer.close();
                    BufferedReader anotherBuffer = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()));
                    String s = "";
                    while ((s = anotherBuffer.readLine()) != null) {
                        response += s;
                    }
                //    reader.close();
                //    urlConnection.disconnect();
                //    } else {
                //        throw new IOException("Server returned non-OK status: " + status);
                 //   }
             //       Log.i(TAG, mArguments2.toString());
              //      wr.write(mArguments2.toString());
               //     wr.flush();
               //     wr.close();
                } catch (Exception e) {
                    response = "Unable to add the image to AWS server, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            Log.d("myTag", "We made it here before return response");
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                Log.d("myTag", "We made it here inside the onPostExecute response");
                Log.d("myTag", result);
                JSONObject resultObject = new JSONObject(result);
                if (resultObject.getBoolean("success") == true) {
                    Toast.makeText(getActivity().getApplicationContext(), "Successfully uploaded image", Toast.LENGTH_SHORT).show();
                    //  Intent intent = new Intent(AddNewUser.this, MainActivity.class);
                    //  startActivity(intent);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Error uploading image to server", Toast.LENGTH_SHORT).show();
                    //Log.e(TAG, resultObject.getString("error"));
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

}
