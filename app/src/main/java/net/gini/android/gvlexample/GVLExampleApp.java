package net.gini.android.gvlexample;

import android.app.Application;

import net.gini.android.Gini;
import net.gini.android.SdkBuilder;
import net.gini.android.gvlexample.gini.SingleDocumentAnalyzer;

/**
 * Created by Alpar Szotyori on 27.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class GVLExampleApp extends Application {

    private Gini mGiniApi;
    private SingleDocumentAnalyzer mSingleDocumentAnalyzer;

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
        final String clientId = this.getString(R.string.gini_api_client_id);
        final String clientSecret = this.getString(R.string.gini_api_client_secret);
        mGiniApi = new SdkBuilder(this, clientId, clientSecret, "gvlexample.net")
                .setNetworkSecurityConfigResId(R.xml.network_security_config)
                .build();
    }
}
