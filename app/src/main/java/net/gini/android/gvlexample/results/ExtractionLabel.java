package net.gini.android.gvlexample.results;

import android.content.Context;

import net.gini.android.gvlexample.R;

/**
 * Created by Alpar Szotyori on 23.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class ExtractionLabel {

    public static String forName(final Context context, final String extractionName) {
        if (extractionName == null) {
            return "";
        }
        switch (extractionName) {
            case Pay5.AMOUNT_TO_PAY:
                return context.getString(R.string.amount_label);
            case Pay5.PAYMENT_RECIPIENT:
                return context.getString(R.string.payment_recipient_label);
            case Pay5.IBAN:
                return context.getString(R.string.iban_label);
            case Pay5.BIC:
                return context.getString(R.string.bic_label);
            case Pay5.PAYMENT_REFERENCE:
                return context.getString(R.string.payment_reference_label);
        }
        return extractionName;
    }
}
