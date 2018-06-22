package net.gini.android.gvlexample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import net.gini.android.gvlexample.configuration.ConfigurationManager;
import net.gini.android.gvlexample.gini.AnalysisActivity;
import net.gini.android.gvlexample.gini.CameraActivity;
import net.gini.android.gvlexample.gini.ReviewActivity;
import net.gini.android.vision.DocumentImportEnabledFileTypes;
import net.gini.android.vision.GiniVisionDebug;
import net.gini.android.vision.GiniVisionFeatureConfiguration;
import net.gini.android.vision.GiniVisionFileImport;
import net.gini.android.vision.ImportedFileValidationException;
import net.gini.android.vision.requirements.GiniVisionRequirements;
import net.gini.android.vision.requirements.RequirementsReport;

/**
 * Created by Alpar Szotyori on 22.02.2018.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class GVLExamplePresenter extends BaseGVLExamplePresenter {

    GVLExamplePresenter(final GVLExampleContract.View view) {
        super(view);
    }

    @Override
    protected void doLaunchGVL() {
        // NOTE: on Android 6.0 and later the camera permission is required before checking the requirements
        RequirementsReport report =
                GiniVisionRequirements.checkRequirements(getView().getContext());
        if (!report.isFulfilled()) {
            getView().showUnfulfilledRequirements(report);
            return;
        }

        final Context context = getView().getContext();
        SharedPreferences configuration = ConfigurationManager.getConfigurationPreferences(context);
        final boolean showOnboardingOnFirstRun = configuration.getBoolean(
                context.getString(R.string.pref_key_gvl_show_onboarding_on_first_run), true);
        final boolean alwaysShowOnboarding = configuration.getBoolean(
                context.getString(R.string.pref_key_gvl_always_show_onboarding), true);
        final boolean enableQRCodeScanning = configuration.getBoolean(
                context.getString(R.string.pref_key_gvl_enable_qr_code_scanning), true);
        final boolean enableFileImport = configuration.getBoolean(
                context.getString(R.string.pref_key_gvl_enable_file_import), true);
        final String documentImportFileTypesIndex = configuration.getString(
                context.getString(R.string.pref_key_gvl_document_import_file_types), "0");
        final DocumentImportEnabledFileTypes documentImportFileTypes =
                DocumentImportEnabledFileTypes.values()[Integer.valueOf(
                        documentImportFileTypesIndex)];
        final boolean enableDebugging = configuration.getBoolean(
                context.getString(R.string.pref_key_gvl_enable_debugging), true);

        if (enableDebugging) {
            GiniVisionDebug.enable();
        } else {
            GiniVisionDebug.disable();
        }

        Intent intent = new Intent(context, CameraActivity.class);

        final GiniVisionFeatureConfiguration giniVisionFeatureConfiguration =
                GiniVisionFeatureConfiguration.buildNewConfiguration()
                                              .setDocumentImportEnabledFileTypes(
                                                      documentImportFileTypes)
                                              .setFileImportEnabled(enableFileImport)
                                              .setQRCodeScanningEnabled(enableQRCodeScanning)
                                              .build();

        intent.putExtra(CameraActivity.EXTRA_IN_GINI_VISION_FEATURE_CONFIGURATION,
                giniVisionFeatureConfiguration);
        intent.putExtra(CameraActivity.EXTRA_IN_SHOW_ONBOARDING_AT_FIRST_RUN, showOnboardingOnFirstRun);
        intent.putExtra(CameraActivity.EXTRA_IN_SHOW_ONBOARDING, alwaysShowOnboarding);

        CameraActivity.setReviewActivityExtra(intent, context, ReviewActivity.class);
        CameraActivity.setAnalysisActivityExtra(intent, context, AnalysisActivity.class);

        getView().showGVL(intent);
    }

    @Override
    protected void initGiniVision() {
    }

    @Override
    void doLaunchGVLForIntent(@NonNull final Intent intent) {
        try {
            final Intent giniVisionIntent =
                    GiniVisionFileImport.createIntentForImportedFile(intent, getView().getContext(),
                            ReviewActivity.class, AnalysisActivity.class);
            getView().showGVL(giniVisionIntent);
            setGVLLaunchedForImportedFile(true);
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
    void stop() {

    }
}
