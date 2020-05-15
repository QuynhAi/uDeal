package edu.tacoma.uw.udeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import inbox.MessageInboxActivity;

/**
 * The activity that holds the fragment for the user profile.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class ProfileActivity extends AppCompatActivity {

    /**
     * Creates the activity and sets up the profile fragment.
     *
     * @param savedInstanceState The saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_toolbar);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Person()).commit();
        }
        Fragment personfragment = new Person();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, personfragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_toolbar,menu);
        return true;
    }

    /**
     * Removes the shared preferences on logout and sets up the actions for the menu items.
     *
     * @param item The menu item
     * @return Boolean value for options item selected
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.invite) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, new InviteFriends()).addToBackStack(null).commit();
        } else if (item.getItemId() == R.id.settings){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, new SettingsFrag()).addToBackStack(null).commit();
        } else if (item.getItemId() == R.id.action_logout) {
            SharedPreferences sharedPreferences =
                    getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), false)
                    .commit();

            sharedPreferences.edit().remove(getString(R.string.email))
                    .commit();

            sharedPreferences.edit().remove(getString(R.string.username))
                    .commit();

            sharedPreferences.edit().remove(getString(R.string.member_id))
                    .commit();

            Intent i = new Intent(this, Login.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /** The bottom navigation view for the application. */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            Intent h = new Intent(ProfileActivity.this, MainActivity.class);
                            h.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(h);
                            overridePendingTransition(0,0);
                            break;
                        case R.id.nav_camera:
                            Intent p = new Intent(ProfileActivity.this, PostActivity.class);
                            p.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(p);
                            overridePendingTransition(0,0);
                            break;
                        case R.id.nav_inbox:
                            Intent i = new Intent(ProfileActivity.this, MessageInboxActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(i);
                            overridePendingTransition(0,0);
                            break;
                        case R.id.nav_cart:
                            Intent c = new Intent(ProfileActivity.this, CartActivity.class);
                            c.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(c);
                            overridePendingTransition(0,0);
                            break;
                        case R.id.nav_person:
                            break;
                    }
                    return false;
                }
            };
}