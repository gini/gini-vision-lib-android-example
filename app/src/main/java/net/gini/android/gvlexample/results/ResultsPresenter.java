package net.gini.android.gvlexample.results;

import static net.gini.android.gvlexample.gini.ExtractionUtil.isPay5Extraction;
import static net.gini.android.gvlexample.results.ResultsActivity.EXTRA_IN_EXTRACTIONS;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import net.gini.android.gvlexample.gini.Extraction;
import net.gini.android.models.SpecificExtraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alpar Szotyori on 23.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

class ResultsPresenter extends ResultsContract.Presenter {

    private static final Map<String, Integer> PAY5_INDEX;

    static {
        PAY5_INDEX = new HashMap<>();
        PAY5_INDEX.put(Pay5.AMOUNT_TO_PAY, 0);
        PAY5_INDEX.put(Pay5.PAYMENT_RECIPIENT, 1);
        PAY5_INDEX.put(Pay5.IBAN, 2);
        PAY5_INDEX.put(Pay5.BIC, 3);
        PAY5_INDEX.put(Pay5.PAYMENT_REFERENCE, 4);
    }

    private final SparseArray<Extraction> mExtractions;

    ResultsPresenter(final ResultsContract.View view) {
        super(view);
        mExtractions = new SparseArray<>(20);
        readExtractions();
    }

    private void readExtractions() {
        Bundle extras = getView().getIntent().getExtras();
        if (extras != null) {
            Bundle extractionsBundle = extras.getParcelable(EXTRA_IN_EXTRACTIONS);
            if (extractionsBundle != null) {
                int nonPay5Index = 5;
                for (String key : extractionsBundle.keySet()) {
                    final SpecificExtraction specificExtraction = extractionsBundle.getParcelable(key);
                    if (specificExtraction != null) {
                        if (isPay5Extraction(key)) {
                            final Integer pay5Index = PAY5_INDEX.get(key);
                            mExtractions.append(
                                    pay5Index, new Extraction(key, specificExtraction.getValue()));
                        } else {
                            mExtractions.append(
                                    nonPay5Index, new Extraction(key, specificExtraction.getValue()));
                            nonPay5Index++;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDoneClicked(final View view) {
        logExtractions();
        getView().getActivity().finish();
    }

    private void logExtractions() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[\n");
        boolean first = true;
        for (int i = 0; i < mExtractions.size(); i++) {
            final Extraction extraction = mExtractions.valueAt(i);
            if (!first) {
                stringBuilder.append("  },\n");
            }
            first = false;
            stringBuilder.append("  {\n").append("    \"name\": \"").append(
                    extraction.getName()).append("\"\n").append("    \"value\": \"").append(
                    extraction.getValue()).append("\"\n");
        }
        stringBuilder.append("  }\n");
        stringBuilder.append("]");
        Log.d("extractions", stringBuilder.toString());
    }

    @Override
    public void start() {
        final List<Extraction> extractions = new ArrayList<>(mExtractions.size());
        for (int i = 0; i < mExtractions.size(); i++) {
            extractions.add(mExtractions.valueAt(i));
        }
        getView().showExtractions(extractions);
    }
}
