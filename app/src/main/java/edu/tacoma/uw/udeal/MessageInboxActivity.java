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

import com.google.android.material.bottomnavigation.BottomNavigationView;

import inbox.InboxListActivity;

public class MessageInboxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_inbox);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_toolbar);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new InboxListActivity()).commit();
        }
        Fragment inboxFragment = new InboxListActivity();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, inboxFragment).commit();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        Fragment inboxfragment = new InboxListActivity();
//        fragmentTransaction.add(R.id.fragment_container, inboxfragment);
//        fragmentTransaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            Intent h = new Intent(MessageInboxActivity.this, MainActivity.class);
                            startActivity(h);
                            break;
                        case R.id.nav_camera:
                            Intent p = new Intent(MessageInboxActivity.this, PostActivity.class);
                            startActivity(p);
                            break;
                        case R.id.nav_inbox:
                            break;
                        case R.id.nav_cart:
                            Intent c = new Intent(MessageInboxActivity.this, CartActivity.class);
                            startActivity(c);
                            break;
                        case R.id.nav_person:
                            Intent np = new Intent(MessageInboxActivity.this, ProfileActivity.class);
                            startActivity(np);
                            break;
                    }
                    return false;
                }
            };
}
