package net.gini.android.gvlexample;

import android.content.Intent;
import android.os.Bundle;

import net.gini.android.vision.GiniVisionError;

/**
 * Created by Alpar Szotyori on 27.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

class GVLExampleContract {

    interface View extends ViewContract {

        void showGVL(final Intent gvlIntent);

        void showResults(final Bundle extractions);

        void showNoResults();

        void showInfos();

        void showError(final String errorMessage);
    }

    abstract static class Presenter extends PresenterContract<View> {

        protected Presenter(final View view) {
            super(view);
        }

        abstract void presentInfos();

        abstract void launchGVL();

        abstract void onGVLResultsReceived(final Bundle extractions);

        abstract void onGVLErrorReceived(final GiniVisionError error);
    }
}
