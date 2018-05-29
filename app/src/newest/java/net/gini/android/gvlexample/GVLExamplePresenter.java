package net.gini.android.gvlexample;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import net.gini.android.gvlexample.gini.FakeGiniVisionNetworkService;
import net.gini.android.gvlexample.gini.StubGiniVisionNetworkApi;
import net.gini.android.vision.AsyncCallback;
import net.gini.android.vision.DocumentImportEnabledFileTypes;
import net.gini.android.vision.GiniVision;
import net.gini.android.vision.ImportedFileValidationException;
import net.gini.android.vision.analysis.AnalysisActivity;
import net.gini.android.vision.camera.CameraActivity;
import net.gini.android.vision.network.GiniVisionNetworkApi;
import net.gini.android.vision.network.GiniVisionNetworkService;
import net.gini.android.vision.requirements.GiniVisionRequirements;
import net.gini.android.vision.requirements.RequirementsReport;
import net.gini.android.vision.review.ReviewActivity;
import net.gini.android.vision.util.CancellationToken;

/**
 * Created by Alpar Szotyori on 22.02.2018.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class GVLExamplePresenter extends BaseGVLExamplePresenter {

    private final GiniVisionNetworkApi mGiniVisionNetworkApi;
    private final GiniVisionNetworkService mGiniVisionNetworkService;
    private CancellationToken mFileImportCancellationToken;

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
    void stop() {
        if (mFileImportCancellationToken != null) {
            mFileImportCancellationToken.cancel();
            mFileImportCancellationToken = null;
        }
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

    @Override
    void doLaunchGVLForIntent(@NonNull final Intent intent) {
        final Context context = getView().getContext();
        if (GiniVision.hasInstance() && GiniVision.getInstance().isMultiPageEnabled()) {
            mFileImportCancellationToken = GiniVision.getInstance().createIntentForImportedFiles(
                    intent, context,
                    new AsyncCallback<Intent, ImportedFileValidationException>() {
                        @Override
                        public void onSuccess(final Intent result) {
                            mFileImportCancellationToken = null;
                            getView().showGVL(result);
                            setGVLLaunchedForImportedFile(true);
                        }

                        @Override
                        public void onError(final ImportedFileValidationException exception) {
                            mFileImportCancellationToken = null;
                            String errorMessage = context.getString(R.string.imported_file_cannot_analyze_error);
                            if (exception.getValidationError() != null) {
                                switch (exception.getValidationError()) {
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

                        @Override
                        public void onCancelled() {
                            mFileImportCancellationToken = null;
                        }
                    });
        } else {
            try {
                final Intent giniVisionIntent =
                        GiniVision.createIntentForImportedFile(intent,
                                context,
                                ReviewActivity.class,
                                AnalysisActivity.class);
                getView().showGVL(giniVisionIntent);
                setGVLLaunchedForImportedFile(true);
            } catch (ImportedFileValidationException e) {
                e.printStackTrace();
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
    }
}
