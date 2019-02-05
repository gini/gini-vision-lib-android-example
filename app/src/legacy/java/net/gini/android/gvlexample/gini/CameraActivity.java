package net.gini.android.gvlexample.gini;

import static net.gini.android.gvlexample.gini.ExtractionUtil.getExtractionsBundle;

import android.content.Intent;
import android.os.Bundle;

import net.gini.android.gvlexample.GVLExampleActivity;
import net.gini.android.gvlexample.GVLExampleApp;
import net.gini.android.gvlexample.R;
import net.gini.android.models.Box;
import net.gini.android.models.SpecificExtraction;
import net.gini.android.vision.document.QRCodeDocument;

import java.util.Collections;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * Created by Alpar Szotyori on 14.12.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class CameraActivity extends net.gini.android.vision.camera.CameraActivity {

    private SingleDocumentAnalyzer mSingleDocumentAnalyzer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSingleDocumentAnalyzer = ((GVLExampleApp) getApplication()).getSingleDocumentAnalyzer();
    }

    @Override
    public void onQRCodeAvailable(@NonNull final QRCodeDocument qrCodeDocument) {
        showActivityIndicatorAndDisableInteraction();
        mSingleDocumentAnalyzer.cancelAnalysis();
        mSingleDocumentAnalyzer.analyzeDocument(qrCodeDocument,
                new DocumentAnalyzer.Listener() {
                    @Override
                    public void onException(final Exception exception) {
                        hideActivityIndicatorAndEnableInteraction();
                        showError(getString(R.string.qrcode_error), 4000);
                    }

                    @Override
                    public void onExtractionsReceived(
                            final Map<String, SpecificExtraction> extractions) {
                        hideActivityIndicatorAndEnableInteraction();
                        final Intent result = new Intent();
                        final Bundle extractionsBundle = getExtractionsBundle(extractions);
                        result.putExtra(GVLExampleActivity.EXTRA_OUT_EXTRACTIONS, extractionsBundle);
                        setResult(RESULT_OK, result);
                        finish();
                    }
                });
    }

    @NonNull
    private static SpecificExtraction createSpecificExtraction(
            final @NonNull String name, final @NonNull String value) {
        return new SpecificExtraction(name, value, "", new Box(0, 0, 0, 0, 0),
                Collections.<net.gini.android.models.Extraction>emptyList());
    }
}
