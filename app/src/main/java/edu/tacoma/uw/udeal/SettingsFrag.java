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
public class SettingsFrag extends Fragment {

    /** The TextView for updating profile photo. */
    private TextView updateProfilePhoto;

    /** The TextView for changing the password. */
    private TextView changePassword;

    /** The TextView for inviting others. */
    private TextView inviteOthers;

    /** The TextView for signing out. */
    private TextView signOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        updateProfilePhoto = (TextView) view.findViewById(R.id.updateprofilephoto);
        changePassword = (TextView) view.findViewById(R.id.changepassword);
        inviteOthers = (TextView) view.findViewById(R.id.inviteothers);
        signOut = (TextView) view.findViewById(R.id.signout);

        updateProfilePhoto.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ChangePasswordFragment()).addToBackStack(null).commit();
            }
        });

        inviteOthers.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new InviteFriends()).addToBackStack(null).commit();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences sharedPreferences =
                        getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
                sharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), false)
                        .commit();

                sharedPreferences.edit().remove(getString(R.string.email))
                        .commit();

                sharedPreferences.edit().remove(getString(R.string.username))
                        .commit();

                sharedPreferences.edit().remove(getString(R.string.member_id))
                        .commit();

                sharedPreferences.edit().remove(getString(R.string.fullname))
                        .commit();

                Intent i = new Intent(getActivity(), Login.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                getActivity().finish();
            }
        });
        return view;
    }

}
