package net.gini.android.gvlexample.gini;

import java.util.Set;

public final class ExtractionUtil {

    public static boolean hasNoPay5Extractions(final Set<String> extractionNames) {
        for (String extractionName : extractionNames) {
            if (isPay5Extraction(extractionName)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isPay5Extraction(String extractionName) {
        return extractionName.equals("amountToPay") ||
                extractionName.equals("bic") ||
                extractionName.equals("iban") ||
                extractionName.equals("paymentReference") ||
                extractionName.equals("paymentRecipient");
    }
}
