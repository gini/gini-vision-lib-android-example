package net.gini.android.gvlexample.results;

import android.content.Context;

import net.gini.android.gvlexample.R;

/**
 * Created by aszotyori on 23.11.17.
 */

public class ExtractionLabel {

    public static String forName(final Context context, final String extractionName) {
        if (extractionName == null) {
            return "";
        }
        switch (extractionName) {
            case "amount":
                return context.getString(R.string.amount_label);
            case "paymentRecipient":
                return context.getString(R.string.payment_recipient_label);
            case "iban":
                return context.getString(R.string.iban_label);
            case "bic":
                return context.getString(R.string.bic_label);
            case "paymentReference":
                return context.getString(R.string.payment_reference_label);
        }
        return extractionName;
    }
}
