package edu.tacoma.uw.udeal;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;



public class Chat extends Fragment {
    private EditText myMessageTextField;
    private ImageButton sendButton;

    public static final String TAG  = "Chat";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Inbox");

        final View rootview = inflater.inflate(R.layout.chat, container, false);

        return rootview;

    }
}
