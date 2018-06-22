package net.gini.android.gvlexample.configuration;

import static net.gini.android.gvlexample.ActivityHelper.forcePortraitOrientationOnPhones;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import net.gini.android.gvlexample.R;

public class ConfigurationActivity extends AppCompatActivity {

    public static final String EXTRA_IN_CONFIGURATION_SUBJECT = "EXTRA_IN_CONFIGURATION_SUBJECT";

    private ConfigurationSubject mConfigurationSubject;

    public static Intent newIntent(@NonNull final Context context,
            @NonNull final ConfigurationSubject configurationSubject) {
        final Intent intent = new Intent(context, ConfigurationActivity.class);
        intent.putExtra(EXTRA_IN_CONFIGURATION_SUBJECT, configurationSubject);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        forcePortraitOrientationOnPhones(this);
        setupActionBar();
        readExtras();
        validateExtras();
        showConfigurationFragment();
    }

    private void showConfigurationFragment() {
        final Fragment configurationFragment;
        switch (mConfigurationSubject) {
            case VISION_LIBRARY:
                setTitle(R.string.gvl_configuration_title);
                configurationFragment = new GVLConfigurationFragment();
                break;
            case API_SDK:
                setTitle(R.string.api_sdk_configuration_title);
                configurationFragment = new APISDKConfigurationFragment();
                break;
            default:
                throw new UnsupportedOperationException(
                        "Unknown configuration subject: " + mConfigurationSubject);
        }
        getFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, configurationFragment)
                .commit();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void readExtras() {
        mConfigurationSubject =
                (ConfigurationSubject) getIntent().getSerializableExtra(
                        EXTRA_IN_CONFIGURATION_SUBJECT);
    }

    private void validateExtras() {
        if (mConfigurationSubject == null) {
            throw new IllegalStateException("Missing configuration subject.");
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public enum ConfigurationSubject {
        VISION_LIBRARY,
        API_SDK
    }
}
