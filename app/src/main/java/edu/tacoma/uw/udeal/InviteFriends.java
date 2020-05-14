package edu.tacoma.uw.udeal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;


/**
 * A simple {@link Fragment} subclass.
 */
public class InviteFriends extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Invite Friends");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.invite_friends, container, false);
    }

    //method overrides to hide the menu buttons for this fragment
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem invite=menu.findItem(R.id.invite);
        if(invite!=null)
            invite.setVisible(false);

        MenuItem settings=menu.findItem(R.id.settings);
        if(settings!=null)
            settings.setVisible(false);

        MenuItem login = menu.findItem(R.id.action_logout);
        if(login!=null)
            login.setVisible(false);
    }

}
