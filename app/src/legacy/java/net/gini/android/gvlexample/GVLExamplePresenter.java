package net.gini.android.gvlexample;

import android.content.Context;
import android.content.Intent;

import net.gini.android.vision.DocumentImportEnabledFileTypes;
import net.gini.android.vision.GiniVisionFeatureConfiguration;
import net.gini.android.vision.requirements.GiniVisionRequirements;
import net.gini.android.vision.requirements.RequirementsReport;
import net.gini.android.gvlexample.gini.ReviewActivity;
import net.gini.android.gvlexample.gini.CameraActivity;
import net.gini.android.gvlexample.gini.AnalysisActivity;

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
//        if (!report.isFulfilled()) {
//            getView().showUnfulfilledRequirements(report);
//            return;
//        }

        final Context context = getView().getContext();

        Intent intent = new Intent(context, CameraActivity.class);

        final GiniVisionFeatureConfiguration giniVisionFeatureConfiguration =
                GiniVisionFeatureConfiguration.buildNewConfiguration()
                                              .setDocumentImportEnabledFileTypes(
                                                      DocumentImportEnabledFileTypes.PDF_AND_IMAGES)
                                              .setFileImportEnabled(true)
                                              .setQRCodeScanningEnabled(true)
                                              .build();

        intent.putExtra(CameraActivity.EXTRA_IN_GINI_VISION_FEATURE_CONFIGURATION,
                giniVisionFeatureConfiguration);

        CameraActivity.setReviewActivityExtra(intent, context, ReviewActivity.class);
        CameraActivity.setAnalysisActivityExtra(intent, context, AnalysisActivity.class);

        getView().showGVL(intent);
    }

    @Override
    protected void initGiniVision() {
    }
}
