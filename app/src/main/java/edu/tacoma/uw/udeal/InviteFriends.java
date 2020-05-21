package edu.tacoma.uw.udeal;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * This fragment handles inviting friends to the application.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
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
        View view = inflater.inflate(R.layout.invite_friends, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Invite Friends");
        toolbar.setTitleTextColor(Color.WHITE);

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        return view;
    }

    /**
     * Handles the hiding of the menu items.
     *
     * @param menu The menu for this fragment
     */
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
