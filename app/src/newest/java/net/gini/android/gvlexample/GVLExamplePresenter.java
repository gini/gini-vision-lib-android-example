package net.gini.android.gvlexample;

import android.content.Context;
import android.content.Intent;

import net.gini.android.vision.DocumentImportEnabledFileTypes;
import net.gini.android.vision.GiniVision;
import net.gini.android.vision.camera.CameraActivity;
import net.gini.android.vision.network.GiniVisionDefaultNetworkApi;
import net.gini.android.vision.network.GiniVisionDefaultNetworkService;
import net.gini.android.vision.requirements.GiniVisionRequirements;
import net.gini.android.vision.requirements.RequirementsReport;

/**
 * Created by Alpar Szotyori on 22.02.2018.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class GVLExamplePresenter extends BaseGVLExamplePresenter {

    private final GiniVisionDefaultNetworkApi mGiniVisionNetworkApi;
    private final GiniVisionDefaultNetworkService mGiniVisionNetworkService;

    GVLExamplePresenter(final GVLExampleContract.View view) {
        super(view);

        final Context context = view.getContext();
        final String clientId = context.getString(R.string.gini_api_client_id);
        final String clientSecret = context.getString(R.string.gini_api_client_secret);

        mGiniVisionNetworkService = GiniVisionDefaultNetworkService
                .builder(context)
                .setClientCredentials(clientId, clientSecret, "gvlexample.net")
                .setCertificateAssetPaths(new String[]{"*.gini.net.cer"})
                .build();

        mGiniVisionNetworkApi = GiniVisionDefaultNetworkApi
                .builder()
                .withGiniVisionDefaultNetworkService(mGiniVisionNetworkService)
                .build();
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

        Intent intent = new Intent(context, CameraActivity.class);

        getView().showGVL(intent);
    }

    @Override
    protected void initGiniVision() {
        GiniVision.cleanup();
        GiniVision.newInstance()
                  .setGiniVisionNetworkService(mGiniVisionNetworkService)
                  .setGiniVisionNetworkApi(mGiniVisionNetworkApi)
                  .setQRCodeScanningEnabled(true)
                  .setFileImportEnabled(true)
                  .setDocumentImportEnabledFileTypes(DocumentImportEnabledFileTypes.PDF_AND_IMAGES)
                  .build();
    }
}
