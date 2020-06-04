package Settings;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
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
import java.util.UUID;

import edu.tacoma.uw.udeal.R;

/**
 * The profile picture fragment gives the user the ability update their profile picture. This allows the user
 * to select, or upload a photo.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class ProfilePictureFragment extends Fragment {

    /** The url tag. */
    public static final String MY_URL_TAG = "url";

    /** The filename. */
    public static final String MY_FILE_NAME = "myfilename";

    /** The file. */
    public static final String MY_FILE = "myfile";

    /** Integer for picking photo from gallery. */
    private static final int PICK_FROM_GALLERY = 1;

    /** Integer for taking photo. */
    private static final int TAKE_PHOTO = 0;

    /** The take picture button. */
    private Button takePicture;

    /** The select picture button. */
    private Button selectPicture;

    /** The post item button. */
    private Button postItem;

    /** Image view for the selected photo. */
    private ImageView myImageView;

    /** The arguments for the async task. */
    private JSONObject  mArguments;

    /** The tag. */
    private String TAG = "addNewItem";

    /** The compressed photo file. */
    private File compressedFile;

    /** The photo bitmap. */
    private Bitmap tempPhotoStorage;

    /** The filename of the photo. */
    private String imageUploadName;

    /**
     * Creates the view by setting up the text fields, the dropdown menu, and the ability to
     * select a photo and take a photo of the item.
     *
     * @param inflater The layout inflater
     * @param container The view group
     * @param savedInstanceState The saved instance state
     * @return The view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_picture, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Update Profile Photo");
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
        myImageView = (ImageView) view.findViewById(R.id.selectedphoto);
        // Take picture button to open up the camera
        takePicture = (Button) view.findViewById(R.id.takePicture);
        takePicture.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
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

        postItem = (Button) view.findViewById(R.id.post);
        postItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS),
                        Context.MODE_PRIVATE);
                String uName = settings.getString(getString(R.string.username), "");
                onAddProfilePhoto(uName);
            }
        });
        return view;
    }

    /**
     * Handles the result of selecting a photo from the gallery or take a photo.
     *
     * @param reqCode The request integer
     * @param resultCode The result integer
     * @param data The data
     */
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
        } else {
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
                    Log.d(TAG, "onRequestPermissionsResult: User did not allow permission");
                }
                break;
        }
    }

    /**
     * Handles the adding of the image of the item.
     *
     * @param theUsername
     */
    public void onAddProfilePhoto(String theUsername) {
        // Handle unique key of image (put in database)
        StringBuilder url = new StringBuilder(getString(R.string.add_profile_photo));
        mArguments = new JSONObject();
        // Create the unique URL extension
        String tempPhotoID = generatePhotoID();
        try {
            mArguments.put("username", theUsername);
            mArguments.put("url", tempPhotoID);
            new AddPhotoAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(getActivity().getApplicationContext(), "URL Error with JSON creation: " +
                    e.getMessage() , Toast.LENGTH_SHORT).show();
        }
        // Posts image and puts it into S3
        processImage();
        new MultipartUtility().execute(getString(R.string.upload));
    }

    /**
     * Generates a random photo ID for storing into S3.
     *
     * @return A unique photo ID.
     */
    private String generatePhotoID() {
        String temp = UUID.randomUUID().toString();
        imageUploadName = temp + ".jpg";
        return temp + ".jpg";
    }

    /**
     * Processes the image.
     */
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

    /**
     * The async task to add a photo.
     *
     * @author TCSS 450 Team 8
     * @version 1.0
     */
    private class AddPhotoAsyncTask extends AsyncTask<String, Void, String> {
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
                    //Toast.makeText(getActivity().getApplicationContext(), "Successfully posted item", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Missing information", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * The async task to upload the photo to S3.
     *
     * @author TCSS 450 Team 8
     * @version 1.0
     */
    public class MultipartUtility extends AsyncTask<String, Void, String> {
        /**
         * Handles the upload of the photo to S3 using a multipart form data.
         *
         * @param urls The URL to establish the connection
         * @return The result of the async task
         */
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
                } catch (Exception e) {
                    response = "Unable to add the image to AWS server, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * If successful, the photo is successful added to S3.
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
                    Toast.makeText(getActivity().getApplicationContext(), "Successfully updated profile photo", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Error uploading image to server", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
