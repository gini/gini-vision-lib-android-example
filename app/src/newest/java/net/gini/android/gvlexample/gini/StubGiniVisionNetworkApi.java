package net.gini.android.gvlexample.gini;

import android.support.annotation.NonNull;

import net.gini.android.vision.network.Error;
import net.gini.android.vision.network.GiniVisionNetworkApi;
import net.gini.android.vision.network.GiniVisionNetworkCallback;
import net.gini.android.vision.network.model.GiniVisionSpecificExtraction;

import java.util.Map;

/**
 * Created by Alpar Szotyori on 14.05.2018.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class StubGiniVisionNetworkApi implements GiniVisionNetworkApi {
    @Override
    public void sendFeedback(@NonNull final Map<String, GiniVisionSpecificExtraction> extractions,
            @NonNull final GiniVisionNetworkCallback<Void, Error> callback) {
        callback.success(null);
    }
}
