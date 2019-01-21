package net.gini.android.gvlexample.configuration;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;

import net.gini.android.gvlexample.GVLExampleApp;
import net.gini.android.gvlexample.R;

/**
 * Created by Alpar Szotyori on 21.06.2018.
 *
 * Copyright (c) 2018 Gini GmbH.
 */
public class APISDKConfigurationFragment extends ConfigurationFragment implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_api_sdk);

        bindPreferenceSummaryToValue(R.string.pref_key_api_sdk_client_id);
        bindPreferenceSummaryToValue(R.string.pref_key_api_sdk_email_domain);
        bindPreferenceSummaryToValue(R.string.pref_key_api_sdk_gini_api_base_url);
        bindPreferenceSummaryToValue(R.string.pref_key_api_sdk_user_center_base_url);
        bindPreferenceSummaryToValue(R.string.pref_key_api_sdk_connection_timeout);
        bindPreferenceSummaryToValue(R.string.pref_key_api_sdk_nr_of_retries);
        bindPreferenceSummaryToValue(R.string.pref_key_api_sdk_backoff_multiplier);

        final Preference apiTypePreference = findPreference(getString(R.string.pref_key_api_sdk_api_type));
        if (apiTypePreference != null) {
            bindPreferenceSummaryToValue(R.string.pref_key_api_sdk_api_type);
            apiTypePreference
                    .setOnPreferenceChangeListener(
                            new Preference.OnPreferenceChangeListener() {
                                @Override
                                public boolean onPreferenceChange(final Preference preference,
                                        final Object newValue) {
                                    final String stringValue = newValue.toString();
                                    final String baseUrlPrefKey =
                                            getString(R.string.pref_key_api_sdk_gini_api_base_url);
                                    if ("1".equals(stringValue)) {
                                        final String baseUrl =
                                                getString(R.string.api_accounting_base_url);
                                        updateEditTextPreference(baseUrlPrefKey, baseUrl);
                                    } else {
                                        final String baseUrl = getString(R.string.api_base_url);
                                        updateEditTextPreference(baseUrlPrefKey, baseUrl);
                                    }

                                    sBindPreferenceSummaryToValueListener.onPreferenceChange(
                                            preference, newValue);
                                    return true;
                                }
                            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences,
            final String key) {
        final GVLExampleApp app = (GVLExampleApp) getActivity().getApplicationContext();
        app.resetGiniApiInstance();
    }
}
