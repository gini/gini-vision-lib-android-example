package net.gini.android.gvlexample;

import android.content.SharedPreferences;
import android.text.TextUtils;

import net.gini.android.Gini;
import net.gini.android.GiniApiType;
import net.gini.android.SdkBuilder;
import net.gini.android.gvlexample.configuration.ConfigurationManager;
import net.gini.android.gvlexample.gini.SingleDocumentAnalyzer;

/**
 * Created by Alpar Szotyori on 27.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class GVLExampleApp extends BaseGVLExampleApp {

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

    @Override
    public void resetGiniApiInstance() {
        mGiniApi = null;
        mSingleDocumentAnalyzer = null;
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
        final String giniApiTypeIndex = configuration.getString(
                this.getString(R.string.pref_key_api_sdk_api_type), "0");
        final GiniApiType giniApiType =
                GiniApiType.values()[Integer.valueOf(
                        giniApiTypeIndex)];
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
        final boolean enableCertificatePinning = configuration.getBoolean(
                this.getString(R.string.pref_key_api_sdk_enable_certificate_pinning), true);

        SdkBuilder builder = new SdkBuilder(this, clientId, clientSecret, emailDomain)
                .setGiniApiType(giniApiType)
                .setApiBaseUrl(apiBaseUrl)
                .setUserCenterApiBaseUrl(userCenterBaseUrl)
                .setConnectionTimeoutInMs(Integer.parseInt(connectionTimeout))
                .setMaxNumberOfRetries(Integer.parseInt(nrOfRetries))
                .setConnectionBackOffMultiplier(Float.parseFloat(backoffMultiplier));
        if (enableCertificatePinning) {
            builder.setNetworkSecurityConfigResId(R.xml.network_security_config);
        }
        mGiniApi = builder.build();
    }
}
