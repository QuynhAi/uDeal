package edu.tacoma.uw.udeal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import inbox.InboxListActivity;

public class PostActivity extends AppCompatActivity {

    private Fragment postFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_toolbar);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            postFragment = new Camera();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    postFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, postFragment).commit();
        }

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        Fragment postfragment = new Camera();
//        fragmentTransaction.add(R.id.fragment_container, postfragment);
//        fragmentTransaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            Intent h = new Intent(PostActivity.this, MainActivity.class);
                            startActivity(h);
                            break;
                        case R.id.nav_camera:
                            break;
                        case R.id.nav_inbox:
                            Intent i = new Intent(PostActivity.this, MessageInboxActivity.class);
                            startActivity(i);
                            break;
                        case R.id.nav_cart:
                            Intent c = new Intent(PostActivity.this, CartActivity.class);
                            startActivity(c);
                            break;
                        case R.id.nav_person:
                            Intent np = new Intent(PostActivity.this, ProfileActivity.class);
                            startActivity(np);
                            break;
                    }
                    return false;
                }
            };
}
