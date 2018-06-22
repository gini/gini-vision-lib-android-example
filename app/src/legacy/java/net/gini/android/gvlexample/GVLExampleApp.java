package net.gini.android.gvlexample;

import android.app.Application;
import android.content.SharedPreferences;
import android.text.TextUtils;

import net.gini.android.Gini;
import net.gini.android.SdkBuilder;
import net.gini.android.gvlexample.configuration.ConfigurationManager;
import net.gini.android.gvlexample.gini.SingleDocumentAnalyzer;

/**
 * Created by Alpar Szotyori on 27.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class GVLExampleApp extends Application {

    private Gini mGiniApi;
    private SingleDocumentAnalyzer mSingleDocumentAnalyzer;

    @Override
    public void onCreate() {
        super.onCreate();
        ConfigurationManager.initDefaultValues(this);
    }

    public SingleDocumentAnalyzer getSingleDocumentAnalyzer() {
        if (mSingleDocumentAnalyzer == null) {
            mSingleDocumentAnalyzer = new SingleDocumentAnalyzer(getGiniApi());
        }
        return mSingleDocumentAnalyzer;
    }

    public Gini getGiniApi() {
        if (mGiniApi == null) {
            createGiniApi();
        }
        return mGiniApi;
    }

    private void createGiniApi() {
        SharedPreferences configuration = ConfigurationManager.getConfigurationPreferences(this);
        final String clientId = configuration.getString(
                this.getString(R.string.pref_key_api_sdk_client_id), "");
        String clientSecret = configuration.getString(
                this.getString(R.string.pref_key_api_sdk_client_secret), "");
        clientSecret = TextUtils.isEmpty(clientSecret) ? this.getString(
                R.string.gini_api_client_secret) : clientSecret;
        final String emailDomain = configuration.getString(
                this.getString(R.string.pref_key_api_sdk_email_domain), "");
        final String apiBaseUrl = configuration.getString(
                this.getString(R.string.pref_key_api_sdk_gini_api_base_url), "");
        final String userCenterBaseUrl = configuration.getString(
                this.getString(R.string.pref_key_api_sdk_user_center_base_url), "");
        final String connectionTimeout = configuration.getString(
                this.getString(R.string.pref_key_api_sdk_connection_timeout), "60");
        final String nrOfRetries = configuration.getString(
                this.getString(R.string.pref_key_api_sdk_nr_of_retries), "3");
        final String backoffMultiplier = configuration.getString(
                this.getString(R.string.pref_key_api_sdk_backoff_multiplier), "1");

        SdkBuilder builder = new SdkBuilder(this, clientId, clientSecret, emailDomain)
                .setApiBaseUrl(apiBaseUrl)
                .setUserCenterApiBaseUrl(userCenterBaseUrl)
                .setConnectionTimeoutInMs(Integer.parseInt(connectionTimeout))
                .setMaxNumberOfRetries(Integer.parseInt(nrOfRetries))
                .setConnectionBackOffMultiplier(Float.parseFloat(backoffMultiplier));
        mGiniApi = builder.build();
    }
}
