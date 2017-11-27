package net.gini.android.gvlexample;

import static net.gini.android.gvlexample.gini.ExtractionUtil.isPay5Extraction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import net.gini.android.gvlexample.gini.AnalysisActivity;
import net.gini.android.gvlexample.gini.ReviewActivity;
import net.gini.android.vision.DocumentImportEnabledFileTypes;
import net.gini.android.vision.GiniVisionError;
import net.gini.android.vision.GiniVisionFeatureConfiguration;
import net.gini.android.vision.camera.CameraActivity;

/**
 * Created by Alpar Szotyori on 27.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

class GVLExamplePresenter extends GVLExampleContract.Presenter {

    GVLExamplePresenter(final GVLExampleContract.View view) {
        super(view);
    }

    @Override
    void presentInfos() {
        getView().showInfos();
    }

    @Override
    void launchGVL() {
// Uncomment to enable requirements check.
        // NOTE: on Android 6.0 and later the camera permission is required before checking the requirements
//        RequirementsReport report = GiniVisionRequirements.checkRequirements(this);
//        if (!report.isFulfilled()) {
//            showUnfulfilledRequirementsToast(report);
//            return;
//        }

        final Context context = getView().getContext();

        Intent intent = new Intent(context, CameraActivity.class);

        // Uncomment to add an extra page to the Onboarding pages
//        intent.putParcelableArrayListExtra(CameraActivity.EXTRA_IN_ONBOARDING_PAGES, getOnboardingPages());

        // Set EXTRA_IN_SHOW_ONBOARDING_AT_FIRST_RUN to false to disable automatically showing the OnboardingActivity the
        // first time the CameraActivity is launched - we highly recommend letting the Gini Vision Library show the
        // OnboardingActivity at first run
        //intent.putExtra(CameraActivity.EXTRA_IN_SHOW_ONBOARDING_AT_FIRST_RUN, false);

        // Set EXTRA_IN_SHOW_ONBOARDING to true, to show the OnboardingActivity when the CameraActivity starts
        //intent.putExtra(CameraActivity.EXTRA_IN_SHOW_ONBOARDING, true);

        // Set EXTRA_IN_BACK_BUTTON_SHOULD_CLOSE_LIBRARY to true, to close library on pressing the back
        // button from any Activity in the library
        //intent.putExtra(CameraActivity.EXTRA_IN_BACK_BUTTON_SHOULD_CLOSE_LIBRARY, true);

        // Configure the features you would like to use
        final GiniVisionFeatureConfiguration giniVisionFeatureConfiguration =
                GiniVisionFeatureConfiguration.buildNewConfiguration().setDocumentImportEnabledFileTypes(
                        DocumentImportEnabledFileTypes.PDF_AND_IMAGES).setFileImportEnabled(
                        true).build();

        intent.putExtra(CameraActivity.EXTRA_IN_GINI_VISION_FEATURE_CONFIGURATION,
                giniVisionFeatureConfiguration);

        // Set your ReviewActivity subclass
        CameraActivity.setReviewActivityExtra(intent, context, ReviewActivity.class);

        // Set your AnalysisActivity subclass
        CameraActivity.setAnalysisActivityExtra(intent, context, AnalysisActivity.class);

        // Start for result in order to receive the error result, in case something went wrong, or the extractions
        // To receive the extractions add it to the result Intent in ReviewActivity#onAddDataToResult(Intent) or
        // AnalysisActivity#onAddDataToResult(Intent) and retrieve them here in onActivityResult()

        getView().showGVL(intent);
    }

    @Override
    void onGVLResultsReceived(final Bundle extractions) {
        if (extractions == null) {
//                if (isIntentActionViewOrSend(getIntent())) {
//                    finish();
//                }
            return;
        }
        // Retrieve the extra we set in our ReviewActivity or AnalysisActivity subclasses' onAddDataToResult()
        // method
        // The payload format is up to you. For the example we added all the extractions as key-value pairs to
        // a Bundle.
        if (pay5ExtractionsAvailable(extractions)) {
            getView().showResults(extractions);
        } else {
            getView().showNoResults();
        }
    }

    @Override
    void onGVLErrorReceived(final GiniVisionError error) {
        String errorMessage = getView().getContext().getString(R.string.gvl_general_error);
        if (error != null) {
            errorMessage = "Error: " + error.getErrorCode() + " - " + error.getMessage();
        }
        getView().showError(errorMessage);
    }

    private boolean pay5ExtractionsAvailable(Bundle extractionsBundle) {
        for (String key : extractionsBundle.keySet()) {
            if (isPay5Extraction(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void start() {

    }
}
