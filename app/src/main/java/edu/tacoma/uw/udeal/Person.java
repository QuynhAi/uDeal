package edu.tacoma.uw.udeal;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class Person extends Fragment {

    private ImageView myProfileImage;
    private Button takePicture;
    private static final int TAKE_PHOTO = 0;
    private TextView myName;
    private TextView myLocation;
    private TextView myInvite;

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
        myLocation = (TextView) view.findViewById(R.id.profile_location);
        myInvite = (TextView) view.findViewById(R.id.invitetext);

        //Set invite to open new fragment when clicked
        myInvite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Begin the transaction
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment_container, new InviteFriends()).commit();

            }
        });

        return view;
    }
}
