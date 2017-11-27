package net.gini.android.gvlexample.results;

import static net.gini.android.gvlexample.results.ResultsActivity.EXTRA_IN_EXTRACTIONS;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import net.gini.android.gvlexample.gini.Extraction;
import net.gini.android.models.SpecificExtraction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alpar Szotyori on 23.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

class ResultsPresenter extends ResultsContract.Presenter {

    private final List<Extraction> mExtractions;

    ResultsPresenter(final ResultsContract.View view) {
        super(view);
        mExtractions = new ArrayList<>();
        readExtractions();
    }

    private void readExtractions() {
        Bundle extras = getView().getIntent().getExtras();
        if (extras != null) {
            Bundle extractionsBundle = extras.getParcelable(EXTRA_IN_EXTRACTIONS);
            if (extractionsBundle != null) {
                for (String key : extractionsBundle.keySet()) {
                    final SpecificExtraction specificExtraction = extractionsBundle.getParcelable(key);
                    if (specificExtraction != null) {
                        mExtractions.add(new Extraction(key, specificExtraction.getValue()));
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
        for (final Extraction extraction : mExtractions) {
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
        getView().showExtractions(mExtractions);
    }
}
