package edu.tacoma.uw.udeal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Fragment for display the user person.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class Person extends Fragment {

    /** The profile image for the user. */
    private ImageView myProfileImage;

    /** Button to take a picture. */
    private Button takePicture;

    /** The take photo integer. */
    private static final int TAKE_PHOTO = 0;

    /** The text view for the name. */
    private TextView myName;

    /** The text view for the username. */
    private TextView myUsername;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * Creates the view and sets up the image capture ability for the user photo.
     *
     * @param inflater The layout inflater
     * @param container The view group container
     * @param savedInstanceState The saved instance state
     * @return The view
     */
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

    /**
     * Handles the hiding of the menu items.
     *
     * @param menu The menu for this fragment
     */
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem invite=menu.findItem(R.id.action_logout);
        if(invite!=null)
            invite.setVisible(false);
    }
}
