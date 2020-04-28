package inbox;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;

import edu.tacoma.uw.udeal.Camera;
import edu.tacoma.uw.udeal.Cart;
import edu.tacoma.uw.udeal.Home;
import edu.tacoma.uw.udeal.Person;
import edu.tacoma.uw.udeal.R;

/**
 * An activity representing a single Inbox detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link InboxListActivity}.
 */
public class InboxDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_detail);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_toolbar);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Home()).commit();
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();

            if(getIntent().getSerializableExtra(InboxDetailFragment.ARG_ITEM_ID) != null){
                arguments.putSerializable(InboxDetailFragment.ARG_ITEM_ID,
                        getIntent().getSerializableExtra(InboxDetailFragment.ARG_ITEM_ID));
                InboxDetailFragment fragment = new InboxDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.inbox_detail_container, fragment)
                        .commit();
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new Home();
                            break;
                        case R.id.nav_camera:
                            selectedFragment = new Camera();
                            break;
                        case R.id.nav_inbox:
                            selectedFragment = new InboxListActivity();
                            break;
                        case R.id.nav_cart:
                            selectedFragment = new Cart();
                            break;
                        case R.id.nav_person:
                            selectedFragment = new Person();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            navigateUpTo(new Intent(this, InboxListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
