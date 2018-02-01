package net.gini.android.gvlexample.results;

import android.util.Log;

import net.gini.android.vision.GiniVisionApplication;
import net.gini.android.vision.network.Error;
import net.gini.android.vision.network.GiniVisionNetwork;
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
        final GiniVisionApplication app = (GiniVisionApplication) view.getApplication();
        final GiniVisionNetwork giniVisionNetwork = app.getGiniVisionNetwork();

        giniVisionNetwork.sendFeedback(extractionsMap,
                new GiniVisionNetwork.Callback<Void, Error>() {
                    @Override
                    public void failure(final Error error) {
                        Log.e("feedback", error.getMessage());
                        view.finish();
                    }

                    @Override
                    public void success(final Void result) {
                        view.finish();
                    }

                    @Override
                    public void cancelled() {
                        view.finish();
                    }
                });
    }
}
