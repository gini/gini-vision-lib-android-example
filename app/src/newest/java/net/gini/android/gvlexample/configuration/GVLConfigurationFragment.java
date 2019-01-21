package net.gini.android.gvlexample.configuration;

import android.os.Bundle;
import android.preference.Preference;

import net.gini.android.gvlexample.R;

/**
 * Created by Alpar Szotyori on 21.06.2018.
 *
 * Copyright (c) 2018 Gini GmbH.
 */
public class GVLConfigurationFragment extends ConfigurationFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_gvl);

        bindPreferenceSummaryToValue(R.string.pref_key_gvl_document_import_file_types);
        bindPreferenceSummaryToValue(R.string.pref_key_gvl_network_library);

        findPreference(getString(R.string.pref_key_gvl_network_library))
                .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(final Preference preference,
                            final Object newValue) {
                        final String stringValue = newValue.toString();
                        handleNetworkLibraryUpdated(stringValue);
                        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                                newValue);
                        return true;
                    }
                });

        final String networkLibrary = getPreferenceManager().getSharedPreferences().getString(
                getString(R.string.pref_key_gvl_network_library), "Default");
        handleNetworkLibraryUpdated(networkLibrary);
    }

    private void handleNetworkLibraryUpdated(final String networkLibrary) {
        final Preference multiPagePreference = findPreference(
                getString(R.string.pref_key_gvl_enable_multi_page));
        final String baseUrlPrefKey =
                getString(R.string.pref_key_api_sdk_gini_api_base_url);
        String baseUrl;
        if ("Accounting".equals(networkLibrary)) {
            multiPagePreference.setEnabled(false);
            updateSwitchPreference(
                    getString(R.string.pref_key_gvl_enable_multi_page), false);
            baseUrl =
                    getString(R.string.api_accounting_base_url);
        } else {
            multiPagePreference.setEnabled(true);
            baseUrl = getString(R.string.api_base_url);
        }
        getPreferenceManager().getSharedPreferences().edit()
                .putString(baseUrlPrefKey, baseUrl).apply();
    }
}
