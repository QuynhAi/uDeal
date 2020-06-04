package Settings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;

import edu.tacoma.uw.udeal.R;

/**
 * A fragment for the settings of the application.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class SettingsActivity extends AppCompatActivity {

   /** The TextView for updating profile photo. */
   private TextView updateProfilePhoto;

    /** The TextView for changing the password. */
    private TextView changePassword;

    /** The TextView for inviting others. */
    private TextView inviteOthers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFrag()).commit();
    }
}
