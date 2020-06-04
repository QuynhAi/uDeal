package Settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import Authentication.Login;
import MyAccount.ProfileActivity;
import edu.tacoma.uw.udeal.R;


/**
 * A fragment for the settings of the application.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class SettingsFrag extends Fragment {

    /** The TextView for updating profile photo. */
    private TextView updateProfilePhoto;

    /** The TextView for changing the password. */
    private TextView changePassword;

    /** The TextView for inviting others. */
    private TextView inviteOthers;

    /** The TextView for writing a review. */
    private TextView writeReview;

    /** The TextView for viewing my reviews. */
    private TextView myReviews;

    /** The TextView for signing out. */
    private TextView signOut;

    /**
     * Sets up the fragment for the settings.
     *
     * @param inflater The layout inflater
     * @param container The view group container
     * @param savedInstanceState The bundle saved instance
     * @return The view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        writeReview = (TextView) view.findViewById(R.id.writereview);
        myReviews = (TextView) view.findViewById(R.id.myreviews);
        updateProfilePhoto = (TextView) view.findViewById(R.id.updateprofilephoto);
        changePassword = (TextView) view.findViewById(R.id.changepassword);
        inviteOthers = (TextView) view.findViewById(R.id.inviteothers);
        signOut = (TextView) view.findViewById(R.id.signout);

        updateProfilePhoto.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfilePictureFragment()).addToBackStack(null).commit();
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ChangePasswordFragment()).addToBackStack(null).commit();
            }
        });

        inviteOthers.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new InviteFriends()).addToBackStack(null).commit();
            }
        });
        writeReview.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new WriteReview()).addToBackStack(null).commit();
            }
        });
        myReviews.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ReviewsWrittenFragment()).addToBackStack(null).commit();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences sharedPreferences =
                        getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
                sharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), false)
                        .commit();

                sharedPreferences.edit().remove(getString(R.string.email))
                        .commit();

                sharedPreferences.edit().remove(getString(R.string.username))
                        .commit();

                sharedPreferences.edit().remove(getString(R.string.member_id))
                        .commit();

                sharedPreferences.edit().remove(getString(R.string.fullname))
                        .commit();

                Intent i = new Intent(getActivity(), Login.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                getActivity().finish();
            }
        });
        return view;
    }

}
