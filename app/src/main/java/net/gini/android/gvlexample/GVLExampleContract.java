package net.gini.android.gvlexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

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

        void showNoPdfResults();

        void showInfos();

        void showError(final String errorMessage);

        void showImportedFileError(final String errorMessage);

        void finish();
    }

    abstract static class Presenter extends PresenterContract<View> {

        protected Presenter(final View view) {
            super(view);
        }

        abstract void presentInfos();

        abstract void launchGVL();

        abstract void launchGVLForImportedFile(final Intent intent);

        abstract void onGVLResultsReceived(@Nullable final Bundle extractions);

        abstract void onGVLErrorReceived(final GiniVisionError error);

        abstract void onGVLWasCanceled();

        abstract void onImportedFileErrorAcknowledged();
    }
}
