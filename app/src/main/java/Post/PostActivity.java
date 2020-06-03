package Post;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import Cart.CartActivity;
import MyAccount.ProfileActivity;
import edu.tacoma.uw.udeal.MainActivity;
import edu.tacoma.uw.udeal.R;
import inbox.MessageInboxActivity;

/**
 * The activity that holds the fragment for posting an item.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class PostActivity extends AppCompatActivity {

    /** The post fragment. */
    private Fragment postFragment;

    /**
     * Creates the activity and sets up the post fragment.
     *
     * @param savedInstanceState The saved instance state
     */
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
    }

    /** The bottom navigation view for the application. */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            Intent h = new Intent(PostActivity.this, MainActivity.class);
                            h.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(h);
                            overridePendingTransition(0,0);
                            break;
                        case R.id.nav_camera:
                            break;
                        case R.id.nav_inbox:
                            Intent i = new Intent(PostActivity.this, MessageInboxActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(i);
                            overridePendingTransition(0,0);
                            break;
                        case R.id.nav_cart:
                            Intent c = new Intent(PostActivity.this, CartActivity.class);
                            c.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(c);
                            overridePendingTransition(0,0);
                            break;
                        case R.id.nav_person:
                            Intent np = new Intent(PostActivity.this, ProfileActivity.class);
                            np.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(np);
                            overridePendingTransition(0,0);
                            break;
                    }
                    return false;
                }
            };
}
