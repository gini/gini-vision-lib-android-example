package net.gini.android.gvlexample;

import android.content.Context;
import android.content.Intent;

import net.gini.android.gvlexample.gini.FakeGiniVisionNetworkService;
import net.gini.android.gvlexample.gini.StubGiniVisionNetworkApi;
import net.gini.android.vision.DocumentImportEnabledFileTypes;
import net.gini.android.vision.GiniVision;
import net.gini.android.vision.camera.CameraActivity;
import net.gini.android.vision.network.GiniVisionNetworkApi;
import net.gini.android.vision.network.GiniVisionNetworkService;
import net.gini.android.vision.requirements.GiniVisionRequirements;
import net.gini.android.vision.requirements.RequirementsReport;

/**
 * Created by Alpar Szotyori on 22.02.2018.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class GVLExamplePresenter extends BaseGVLExamplePresenter {

    private final GiniVisionNetworkApi mGiniVisionNetworkApi;
    private final GiniVisionNetworkService mGiniVisionNetworkService;

    GVLExamplePresenter(final GVLExampleContract.View view) {
        super(view);

        final Context context = view.getContext();
        final String clientId = context.getString(R.string.gini_api_client_id);
        final String clientSecret = context.getString(R.string.gini_api_client_secret);

//        mGiniVisionNetworkService = GiniVisionDefaultNetworkService
//                .builder(context)
//                .setClientCredentials(clientId, clientSecret, "gvlexample.net")
//                .build();
        mGiniVisionNetworkService = new FakeGiniVisionNetworkService(context);

//        mGiniVisionNetworkApi = GiniVisionDefaultNetworkApi
//                .builder()
//                .withGiniVisionDefaultNetworkService(mGiniVisionNetworkService)
//                .build();
        mGiniVisionNetworkApi = new StubGiniVisionNetworkApi();
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

        getView().showGVL(intent);
    }

    @Override
    protected void initGiniVision() {
        GiniVision.cleanup(getView().getContext());
        GiniVision.newInstance()
                  .setGiniVisionNetworkService(mGiniVisionNetworkService)
                  .setGiniVisionNetworkApi(mGiniVisionNetworkApi)
                  .setQRCodeScanningEnabled(true)
                  .setFileImportEnabled(true)
                  .setMultiPageEnabled(true)
                  .setDocumentImportEnabledFileTypes(DocumentImportEnabledFileTypes.PDF_AND_IMAGES)
                  .build();
    }
}
