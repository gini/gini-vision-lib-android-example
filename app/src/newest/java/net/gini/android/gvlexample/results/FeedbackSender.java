package net.gini.android.gvlexample.results;

import android.util.Log;

import net.gini.android.vision.GiniVision;
import net.gini.android.vision.network.Error;
import net.gini.android.vision.network.GiniVisionNetworkCallback;
import net.gini.android.vision.network.model.GiniVisionSpecificExtraction;

import java.util.Map;

/**
 * Created by Alpar Szotyori on 01.02.2018.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

class FeedbackSender extends BaseFeedbackSender<GiniVisionSpecificExtraction> {

    @Override
    void doSendFeedback(final Map<String, GiniVisionSpecificExtraction> extractionsMap,
            final ResultsContract.View view) {
        GiniVision.getInstance().getGiniVisionNetworkApi().sendFeedback(extractionsMap,
                new GiniVisionNetworkCallback<Void, Error>() {
                    @Override
                    public void failure(final Error error) {
                        Log.e("feedback", error.getMessage());
                        GiniVision.cleanup();
                        view.finish();
                    }

                    @Override
                    public void success(final Void result) {
                        GiniVision.cleanup();
                        view.finish();
                    }

                    @Override
                    public void cancelled() {
                        GiniVision.cleanup();
                        view.finish();
                    }
                });
    }
}
