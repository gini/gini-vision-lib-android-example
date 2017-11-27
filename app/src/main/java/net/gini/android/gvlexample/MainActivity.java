package net.gini.android.gvlexample;

import static net.gini.android.gvlexample.gini.ExtractionUtil.isPay5Extraction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import net.gini.android.gvlexample.gini.AnalysisActivity;
import net.gini.android.gvlexample.gini.ReviewActivity;
import net.gini.android.gvlexample.info.InfoActivity;
import net.gini.android.gvlexample.results.ResultsActivity;
import net.gini.android.vision.DocumentImportEnabledFileTypes;
import net.gini.android.vision.GiniVisionError;
import net.gini.android.vision.GiniVisionFeatureConfiguration;
import net.gini.android.vision.camera.CameraActivity;
import net.gini.android.vision.noresults.NoResultsActivity;

/**
 * Created by Alpar Szotyori on 20.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_OUT_EXTRACTIONS = "EXTRA_OUT_EXTRACTIONS";
    private static final int REQUEST_GINI_VISION = 1;

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GINI_VISION) {
            if (data == null) {
//                if (isIntentActionViewOrSend(getIntent())) {
//                    finish();
//                }
                return;
            }
            switch (resultCode) {
                case RESULT_OK:
                    // Retrieve the extra we set in our ReviewActivity or AnalysisActivity subclasses' onAddDataToResult()
                    // method
                    // The payload format is up to you. For the example we added all the extractions as key-value pairs to
                    // a Bundle.
                    Bundle extractionsBundle = data.getBundleExtra(EXTRA_OUT_EXTRACTIONS);
                    if (extractionsBundle != null && pay5ExtractionsAvailable(extractionsBundle)) {
                        startResultsActivity(extractionsBundle);
                    } else {
                        startNoResultsActivity();
                    }
//                    if (isIntentActionViewOrSend(getIntent())) {
//                        finish();
//                    }
                    break;
                case CameraActivity.RESULT_ERROR:
                    // Something went wrong, retrieve and show the error
                    GiniVisionError error = data.getParcelableExtra(CameraActivity.EXTRA_OUT_ERROR);
                    if (error != null) {
                        Toast.makeText(this,
                                "Error: " + error.getErrorCode() + " - " + error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    }

    private void startNoResultsActivity() {
        final Intent intent = new Intent(this, NoResultsActivity.class);
        startActivity(intent);
    }

    private void startResultsActivity(final Bundle extractionsBundle) {
        final Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra(ResultsActivity.EXTRA_IN_EXTRACTIONS, extractionsBundle);
        startActivity(intent);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.infoItem) {
            final Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void launchGiniVision(final View view) {
        // Uncomment to enable requirements check.
        // NOTE: on Android 6.0 and later the camera permission is required before checking the requirements
//        RequirementsReport report = GiniVisionRequirements.checkRequirements(this);
//        if (!report.isFulfilled()) {
//            showUnfulfilledRequirementsToast(report);
//            return;
//        }

        Intent intent = new Intent(this, CameraActivity.class);

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
        CameraActivity.setReviewActivityExtra(intent, this, ReviewActivity.class);

        // Set your AnalysisActivity subclass
        CameraActivity.setAnalysisActivityExtra(intent, this, AnalysisActivity.class);

        // Start for result in order to receive the error result, in case something went wrong, or the extractions
        // To receive the extractions add it to the result Intent in ReviewActivity#onAddDataToResult(Intent) or
        // AnalysisActivity#onAddDataToResult(Intent) and retrieve them here in onActivityResult()
        startActivityForResult(intent, REQUEST_GINI_VISION);
    }
}
