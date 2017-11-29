package net.gini.android.gvlexample.results;

import android.app.Application;

import net.gini.android.gvlexample.PresenterContract;
import net.gini.android.gvlexample.ViewContract;
import net.gini.android.gvlexample.gini.Extraction;

import java.util.List;

/**
 * Created by Alpar Szotyori on 23.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public abstract class ResultsContract {

    interface View extends ViewContract {

        void showExtractions(List<Extraction> extractions);

        void finish();

        Application getApplication();
    }

    public static abstract class Presenter extends PresenterContract<View> {

        Presenter(final View view) {
            super(view);
        }

        public abstract void sendFeedback(final android.view.View view);

        public abstract void updateExtractions(final List<Extraction> extractions);
    }
}
