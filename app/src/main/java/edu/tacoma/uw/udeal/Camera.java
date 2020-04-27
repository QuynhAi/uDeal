package edu.tacoma.uw.udeal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class Camera extends Fragment {
    private Button takePicture;
    private Button selectPicture;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Post an Item");
        View view = inflater.inflate(R.layout.activity_camera, container, false);

        takePicture = (Button) view.findViewById(R.id.takePicture);
        takePicture.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);
            }
        });

        selectPicture = (Button) view.findViewById(R.id.selectPicture);
        selectPicture.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                final int ACTIVITY_SELECT_IMAGE = 1234;
                startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
            }
        });



        return view;
    }


}
