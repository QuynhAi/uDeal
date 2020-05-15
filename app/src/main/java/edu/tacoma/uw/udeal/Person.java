package edu.tacoma.uw.udeal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class Person extends Fragment {

    private ImageView myProfileImage;
    private Button takePicture;
    private static final int TAKE_PHOTO = 0;
    private TextView myName;
    private TextView myUsername;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("My Account");

        View view = inflater.inflate(R.layout.activity_person, container, false);

        myProfileImage = (ImageView) view.findViewById(R.id.profile_pic);

        // Take picture button to open up the camera
        takePicture = (Button) view.findViewById(R.id.add_profile_pic);
        takePicture.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                //startActivity(intent);
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, TAKE_PHOTO);
            }
        });

        // Get the text from inputs
        myName = (TextView) view.findViewById(R.id.profile_name);
        SharedPreferences settings = getActivity().getSharedPreferences((getString(R.string.LOGIN_PREFS)), Context.MODE_PRIVATE);
        String fullName = settings.getString(getString(R.string.fullname), "");
        myName.setText(fullName);
        String userName = settings.getString(getString(R.string.username), "");
        myUsername = (TextView) view.findViewById(R.id.profile_location);
        myUsername.setText("@" +  userName);



        return view;
    }

    //method overrides to hide the menu buttons for this fragment
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem invite=menu.findItem(R.id.action_logout);
        if(invite!=null)
            invite.setVisible(false);
    }

}
