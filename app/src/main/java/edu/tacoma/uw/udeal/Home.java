package edu.tacoma.uw.udeal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


public class Home extends Fragment {

    private ImageView myImageView;
    private JSONArray values;
    private View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Home");
        View view = inflater.inflate(R.layout.activity_home, container, false);
        myView = view;
        myImageView = (ImageView) view.findViewById(R.id.displayphoto);
        loadPreferences();
        return view;
    }

    @Override
    public void onResume() {
        StringBuilder urlTextFields = new StringBuilder(getString(R.string.download));
        urlTextFields.append("?myfilename=f93fda4f-5cfc-4faf-8744-c2fdfb8d7e74.jpg");
        super.onResume();
        //if (myImageView == null) {
          //  Log.d("myTag", "imageview is null");
        //    new ImageTask().execute(urlTextFields.toString());
      //  }
    }

    private void loadPreferences() {

        SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);

        // Get value
  //      String temp = settings.getString(getString(R.string.username), "");

        TextView tempTextView = myView.findViewById(R.id.username_display);
//        tempTextView.setText("Hello" + temp);
        tempTextView.setText("Hello" + settings.getInt(getString(R.string.member_id), 0));
    }

    private class ImageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to download the image, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(), "Unable to download" + s, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            try {
                Log.d("myTag","WE SUCCESSFULLY ESTABLISHED URL CONNECTION");
                JSONObject jsonObject = new JSONObject(s);
                Log.d("myTag", jsonObject.toString());
                if (jsonObject.getBoolean("success")) {
                    Log.d("myTag","We are inside the success feature");
                    values = jsonObject.getJSONObject("values").getJSONObject("Body").getJSONArray("data");
                    Bitmap bitmap = null;
                    byte[] tmp= new byte[values.length()];
                    for(int i=0;i< values.length(); i++){
                        tmp[i]=(byte)(((int) values.get(i)) & 0xFF);
                    }
                    bitmap = BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
                    myImageView.setImageBitmap(bitmap);
              /*      Log.d("myTag","We have gotten the values");
                    Log.d("myTag",values.toString());
                    byte[] data = values.toString().getBytes("UTF-8");
                    Log.d("myTag", "WE HAVE SUCCESSFULLY GOTTEN THE BITES");
                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                    if(bmp == null) {
                        Log.d("myTag", "BMP IS NULL");
                    }
                    myImageView.setImageBitmap(bmp);
                    Log.d("myTag", "IMAGE IS SET"); */
                //    if (!mCourseList.isEmpty()) {
                //        setupRecyclerView((RecyclerView) mRecyclerView);
               //     }
                }

            } catch (JSONException e) {
                Log.d("myTag","FAILURE");
                Toast.makeText(getActivity().getApplicationContext(), "JSON Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

}
