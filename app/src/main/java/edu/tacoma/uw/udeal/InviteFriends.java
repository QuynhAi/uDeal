package edu.tacoma.uw.udeal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class InviteFriends extends Fragment {

    public InviteFriends() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Invite Friends");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.invite_friends, container, false);
    }
}
