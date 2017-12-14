package net.gini.android.gvlexample.gini;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import net.gini.android.gvlexample.GVLExampleActivity;
import net.gini.android.models.Box;
import net.gini.android.models.SpecificExtraction;
import net.gini.android.vision.PaymentData;

import java.util.Collections;

/**
 * Created by Alpar Szotyori on 14.12.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class CameraActivity extends net.gini.android.vision.camera.CameraActivity {

    @Override
    public void onPaymentDataAvailable(@NonNull final PaymentData paymentData) {
        super.onPaymentDataAvailable(paymentData);
        final Intent result = new Intent();
        final Bundle extractionsBundle = getExtractionsBundle(paymentData);
        result.putExtra(GVLExampleActivity.EXTRA_OUT_EXTRACTIONS, extractionsBundle);
        setResult(RESULT_OK, result);
        finish();
    }

    private Bundle getExtractionsBundle(@NonNull final PaymentData paymentData) {
        final Bundle extractionsBundle = new Bundle();
        extractionsBundle.putParcelable("paymentReference",
                createSpecificExtraction("paymentReference",
                        paymentData.getPaymentReference()));
        extractionsBundle.putParcelable("paymentRecipient",
                createSpecificExtraction("paymentRecipient",
                        paymentData.getPaymentRecipient()));
        extractionsBundle.putParcelable("amountToPay",
                createSpecificExtraction("amountToPay",
                        paymentData.getAmount()));
        extractionsBundle.putParcelable("iban",
                createSpecificExtraction("iban",
                        paymentData.getIBAN()));
        extractionsBundle.putParcelable("bic",
                createSpecificExtraction("bic",
                        paymentData.getBIC()));
        return extractionsBundle;
    }

    @NonNull
    private static SpecificExtraction createSpecificExtraction(
            final @NonNull String name, final @NonNull String value) {
        return new SpecificExtraction(name, value, "", new Box(0, 0, 0, 0, 0),
                Collections.<net.gini.android.models.Extraction>emptyList());
    }
}
