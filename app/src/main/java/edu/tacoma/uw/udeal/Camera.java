package edu.tacoma.uw.udeal;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.FileNotFoundException;
import java.io.InputStream;


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
              //  Intent i = new Intent(Intent.ACTION_PICK,
             //           android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != getActivity().getPackageManager().PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                } else {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, PICK_FROM_GALLERY);
                }

                //   final int ACTIVITY_SELECT_IMAGE = 1234;
              //  startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
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

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == TAKE_PHOTO && resultCode == getActivity().RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            myImageView.setImageBitmap(photo);
        }

        else if (reqCode == PICK_FROM_GALLERY && resultCode == getActivity().RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
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
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
    }


}
