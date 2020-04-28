package edu.tacoma.uw.udeal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class Camera extends Fragment {
    private Button takePicture;
    private Button selectPicture;
    private Button postItem;
    private EditText mytitle;
    private EditText myprice;
    private EditText mydescription;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Post an Item");
        View view = inflater.inflate(R.layout.activity_camera, container, false);

        // Take picture button to open up the camera
        takePicture = (Button) view.findViewById(R.id.takePicture);
        takePicture.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);
            }
        });

        // Select picture button opens up folder
        selectPicture = (Button) view.findViewById(R.id.selectPicture);
        selectPicture.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                final int ACTIVITY_SELECT_IMAGE = 1234;
                startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
            }
        });

        // Get the text from inputs
        mytitle = (EditText) view.findViewById(R.id.title);
        myprice = (EditText) view.findViewById(R.id.price);
        mydescription = (EditText) view.findViewById(R.id.description);
        postItem = (Button) view.findViewById(R.id.post);
        postItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String stitle = mytitle.getText().toString();
                String sprice = myprice.getText().toString();
                String sdescription = mydescription.getText().toString();
            }
        });


        return view;
    }


}
