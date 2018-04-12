package net.gini.android.gvlexample.results;

import android.os.Bundle;

import net.gini.android.vision.network.Error;
import net.gini.android.vision.network.GiniVisionNetworkCallback;

/**
 * Created by Alpar Szotyori on 12.04.2018.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class ResultsPresenter extends BaseResultsPresenter {

    ResultsPresenter(final ResultsContract.View view, final Bundle extractionsBundle) {
        super(view, extractionsBundle);
    }

    @Override
    public void stop() {

    }
}
