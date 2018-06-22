package net.gini.android.gvlexample.settings;

import android.os.Bundle;

import net.gini.android.gvlexample.R;

/**
 * Created by Alpar Szotyori on 21.06.2018.
 *
 * Copyright (c) 2018 Gini GmbH.
 */
public class APISDKConfigurationFragment extends ConfigurationFragment {

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
    }
}
