package edu.tacoma.uw.udeal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.TextView;
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
public class SettingsActivity extends AppCompatActivity {

   /** The TextView for updating profile photo. */
   private TextView updateProfilePhoto;

    /** The TextView for changing the password. */
    private TextView changePassword;

    /** The TextView for inviting others. */
    private TextView inviteOthers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFrag()).commit();
    }
}
