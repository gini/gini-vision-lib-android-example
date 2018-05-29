package net.gini.android.gvlexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import net.gini.android.vision.GiniVisionError;
import net.gini.android.vision.requirements.RequirementsReport;

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

        void showUnfulfilledRequirements(final RequirementsReport report);

        void finish();

        void requestCameraPermission(final PermissionRequestListener listener);

        void requestStoragePermission(final PermissionRequestListener listener);

        interface PermissionRequestListener {
            void permissionGranted();
            void permissionDenied();
        }
    }

    abstract static class Presenter extends PresenterContract<View> {

        protected Presenter(final View view) {
            super(view);
        }

        abstract void stop();

        abstract void presentInfos();

        abstract void launchGVL();

        abstract void launchGVLForIntent(final Intent intent);

        abstract void onGVLResultsReceived(@Nullable final Bundle extractions);

        abstract void onGVLErrorReceived(final GiniVisionError error);

        abstract void onGVLWasCanceled();

        abstract void onImportedFileErrorAcknowledged();
    }
}
