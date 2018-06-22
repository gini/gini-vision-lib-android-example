package net.gini.android.gvlexample;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import net.gini.android.vision.GiniVisionError;

/**
 * Created by Alpar Szotyori on 27.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

abstract class BaseGVLExamplePresenter extends GVLExampleContract.Presenter {

    private boolean mGVLLaunchedForImportedFile = false;

    BaseGVLExamplePresenter(final GVLExampleContract.View view) {
        super(view);
        PreferenceManager.setDefaultValues(view.getContext(), R.xml.pref_api_sdk, false);
        PreferenceManager.setDefaultValues(view.getContext(), R.xml.pref_gvl, false);
    }

    @Override
    void presentInfos() {
        getView().showInfos();
    }

    @Override
    void launchGVL() {
        getView().requestCameraPermission(new GVLExampleContract.View.PermissionRequestListener() {
            @Override
            public void permissionGranted() {
                initGiniVision();
                doLaunchGVL();
            }

            @Override
            public void permissionDenied() {
            }
        });
    }

    protected abstract void doLaunchGVL();

    protected abstract void initGiniVision();

    @Override
    void launchGVLForIntent(final Intent intent) {
        getView().requestStoragePermission(new GVLExampleContract.View.PermissionRequestListener() {
            @Override
            public void permissionGranted() {
                initGiniVision();
                doLaunchGVLForIntent(intent);
            }

            @Override
            public void permissionDenied() {
                getView().finish();
            }
        });
    }

    abstract void doLaunchGVLForIntent(@NonNull final Intent intent);

    @Override
    void onGVLResultsReceived(@Nullable final Bundle extractions) {
        if (extractions != null) {
            getView().showResults(extractions);
        } else {
            getView().showNoPdfResults();
        }
        if (mGVLLaunchedForImportedFile) {
            getView().finish();
        }
        mGVLLaunchedForImportedFile = false;
    }

    BaseGVLExamplePresenter setGVLLaunchedForImportedFile(
            final boolean GVLLaunchedForImportedFile) {
        mGVLLaunchedForImportedFile = GVLLaunchedForImportedFile;
        return this;
    }

    @Override
    void onGVLErrorReceived(final GiniVisionError error) {
        String errorMessage = getView().getContext().getString(R.string.gvl_general_error);
        if (error != null) {
            errorMessage = "Error: " + error.getErrorCode() + " - " + error.getMessage();
        }
        getView().showError(errorMessage);
        mGVLLaunchedForImportedFile = false;
    }

    @Override
    void onGVLWasCanceled() {
        if (mGVLLaunchedForImportedFile) {
            getView().finish();
        }
        mGVLLaunchedForImportedFile = false;
    }

    @Override
    void onImportedFileErrorAcknowledged() {
        getView().finish();
    }

    @Override
    public void start() {

    }
}
