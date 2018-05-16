package net.gini.android.gvlexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import net.gini.android.vision.GiniVisionError;
import net.gini.android.vision.GiniVisionFileImport;
import net.gini.android.vision.ImportedFileValidationException;

/**
 * Created by Alpar Szotyori on 27.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

abstract class BaseGVLExamplePresenter extends GVLExampleContract.Presenter {

    private boolean mGVLLaunchedForImportedFile = false;

    BaseGVLExamplePresenter(final GVLExampleContract.View view) {
        super(view);
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
    void launchGVLForImportedFile(final Intent intent) {
        getView().requestStoragePermission(new GVLExampleContract.View.PermissionRequestListener() {
            @Override
            public void permissionGranted() {
                initGiniVision();
                doLaunchGVLForImportedFile(intent);
            }

            @Override
            public void permissionDenied() {
                getView().finish();
            }
        });
    }

    private void doLaunchGVLForImportedFile(final Intent intent) {
        try {
            final Intent giniVisionIntent =
                    GiniVisionFileImport.createIntentForImportedFile(intent, getView().getContext());
            getView().showGVL(giniVisionIntent);
            mGVLLaunchedForImportedFile = true;
        } catch (ImportedFileValidationException e) {
            e.printStackTrace();
            final Context context = getView().getContext();
            String errorMessage = context.getString(R.string.imported_file_cannot_analyze_error);
            if (e.getValidationError() != null) {
                switch (e.getValidationError()) {
                    case TYPE_NOT_SUPPORTED:
                        errorMessage =
                                context.getString(R.string.imported_file_type_not_supported_error);
                        break;
                    case SIZE_TOO_LARGE:
                        errorMessage = context.getString(R.string.imported_file_too_large_error);
                        break;
                    case TOO_MANY_PDF_PAGES:
                        errorMessage =
                                context.getString(R.string.imported_file_pdf_too_many_pages_error);
                        break;
                }
            }
            getView().showImportedFileError(errorMessage);
        }
    }

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
