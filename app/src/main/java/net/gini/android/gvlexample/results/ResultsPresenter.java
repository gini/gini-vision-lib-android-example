package net.gini.android.gvlexample.results;

import android.util.Log;
import android.view.View;

import net.gini.android.gvlexample.gini.Extraction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alpar Szotyori on 23.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

class ResultsPresenter extends ResultsContract.Presenter {

    private List<Extraction> mExtractions;

    ResultsPresenter(final ResultsContract.View view) {
        super(view);
        mExtractions = new ArrayList<>();
        mExtractions.add(new Extraction("amount", "19.99:EUR"));
        mExtractions.add(new Extraction("paymentRecipient", "Etepetete"));
        mExtractions.add(new Extraction("iban", "DE123124123123124"));
        mExtractions.add(new Extraction("bic", "COBADEXXX"));
        mExtractions.add(new Extraction("paymentReference", "RefNr 94839"));
        mExtractions.add(new Extraction("docType", "invoice"));
        mExtractions.add(new Extraction("payed", "false"));
        mExtractions.add(new Extraction("date", "2017.01.30"));
    }

    @Override
    public void start() {
        getView().showExtractions(mExtractions);
    }

    @Override
    public void onDoneClicked(final View view) {
        logExtractions();
    }

    private void logExtractions() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[\n");
        boolean first = true;
        for (final Extraction extraction : mExtractions) {
            if (!first) {
                stringBuilder.append("  },\n");
            }
            first = false;
            stringBuilder.append("  {\n")
                    .append("    \"name\": \"").append(extraction.getName()).append("\"\n")
                    .append("    \"value\": \"").append(extraction.getValue()).append("\"\n");
        }
        stringBuilder.append("  }\n");
        stringBuilder.append("]");
        Log.d("extractions", stringBuilder.toString());
    }
}
