package edu.tacoma.uw.udeal;

import androidx.annotation.NonNull;
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

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartstart);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_toolbar);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Cart()).commit();
        }
        Fragment cartfragment = new Cart();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, cartfragment).commit();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        Fragment cartfragment = new Cart();
//        fragmentTransaction.add(R.id.fragment_container, cartfragment);
//        fragmentTransaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            Intent h = new Intent(CartActivity.this, MainActivity.class);
                            startActivity(h);
                            break;
                        case R.id.nav_camera:
                            Intent p = new Intent(CartActivity.this, PostActivity.class);
                            startActivity(p);
                            break;
                        case R.id.nav_inbox:
                            Intent i = new Intent(CartActivity.this, MessageInboxActivity.class);
                            startActivity(i);
                            break;
                        case R.id.nav_cart:
                            break;
                        case R.id.nav_person:
                            Intent np = new Intent(CartActivity.this, ProfileActivity.class);
                            startActivity(np);
                            break;
                    }
                    return false;
                }
            };
}
