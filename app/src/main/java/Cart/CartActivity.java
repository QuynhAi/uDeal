package Cart;

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
import com.google.android.material.tabs.TabLayout;

import edu.tacoma.uw.udeal.MainActivity;
import Post.PostActivity;
import MyAccount.ProfileActivity;
import edu.tacoma.uw.udeal.R;
import inbox.MessageInboxActivity;

/**
 * The activity that holds the fragments for the liked items and posted items.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class CartActivity extends AppCompatActivity {

    /** The tab layout for the activity. */
    private TabLayout tabLayout;

    /**
     * Sets up the tabbed layout for the activity.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartstart);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_toolbar);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        setTitle("My Cart");

        //Adding bottom nav to the activity
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        //Adding tabbed fragments to the activity
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        TabLayout.Tab sellTab = tabLayout.newTab();
        TabLayout.Tab buyTab = tabLayout.newTab();
        sellTab.setText("Posted Items");
        buyTab.setText("Saved Items");
        tabLayout.addTab(sellTab, 0, true);
        tabLayout.addTab(buyTab, 1, false);

        tabLayout.setTabTextColors(getResources().getColor(R.color.colorWhite), getResources().getColor(R.color.colorAccent));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new SellingFrag();
                        break;
                    case 1:
                        fragment = new BuyingFrag();
                        break;
                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new SellingFrag()).commit();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SellingFrag())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
    }

    /** The bottom navigation view for the application. */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            Intent h = new Intent(CartActivity.this, MainActivity.class);
                            h.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(h);
                            overridePendingTransition(0,0);
                            break;
                        case R.id.nav_camera:
                            Intent p = new Intent(CartActivity.this, PostActivity.class);
                            p.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(p);
                            overridePendingTransition(0,0);
                            break;
                        case R.id.nav_inbox:
                            Intent i = new Intent(CartActivity.this, MessageInboxActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(i);
                            overridePendingTransition(0,0);
                            break;
                        case R.id.nav_cart:
                            break;
                        case R.id.nav_person:
                            Intent np = new Intent(CartActivity.this, ProfileActivity.class);
                            np.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(np);
                            overridePendingTransition(0,0);
                            break;
                    }
                    return false;
                }
            };
}
