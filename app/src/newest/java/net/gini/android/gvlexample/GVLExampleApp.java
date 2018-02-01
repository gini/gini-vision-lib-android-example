package net.gini.android.gvlexample;

import android.app.Application;
import android.support.annotation.NonNull;

import net.gini.android.vision.GiniVisionApplication;
import net.gini.android.vision.network.GiniVisionNetwork;
import net.gini.android.vision.network.GiniVisionNetworkHandler;

/**
 * Created by Alpar Szotyori on 27.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class GVLExampleApp extends Application implements GiniVisionApplication {

    private GiniVisionNetworkHandler mGiniVisionNetworkHandler;

    @NonNull
    @Override
    public GiniVisionNetwork getGiniVisionNetwork() {
        if (mGiniVisionNetworkHandler == null) {
            final String clientId = this.getString(R.string.gini_api_client_id);
            final String clientSecret = this.getString(R.string.gini_api_client_secret);
            mGiniVisionNetworkHandler = GiniVisionNetworkHandler
                    .builder(this)
                    .setClientCredentials(clientId, clientSecret, "gvlexample.net")
                    .setCertificateAssetPaths(new String[]{"*.gini.net.cer"})
                    .build();
        }
        return mGiniVisionNetworkHandler;
    }

}
