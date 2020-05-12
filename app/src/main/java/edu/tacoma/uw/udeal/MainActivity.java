package edu.tacoma.uw.udeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import inbox.MessageInboxActivity;


public class MainActivity extends AppCompatActivity {

    private Fragment homefragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_toolbar);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            homefragment = new Home();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    homefragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    homefragment).commit();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                homefragment).commit();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        Fragment homefragment = new Home();
//        fragmentTransaction.add(R.id.fragment_container, homefragment);
//        fragmentTransaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            break;
                        case R.id.nav_camera:
                            Intent p = new Intent(MainActivity.this, PostActivity.class);
                            startActivity(p);
                            break;
                        case R.id.nav_inbox:
                            Intent i = new Intent(MainActivity.this, MessageInboxActivity.class);
                            startActivity(i);
                            break;
                        case R.id.nav_cart:
                            Intent c = new Intent(MainActivity.this, CartActivity.class);
                            startActivity(c);
                            break;
                        case R.id.nav_person:
                            Intent np = new Intent(MainActivity.this, ProfileActivity.class);
                            startActivity(np);
                            break;
                    }
                    return false;
                }
            };
}
