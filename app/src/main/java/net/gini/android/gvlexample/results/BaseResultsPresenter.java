package net.gini.android.gvlexample.results;

import static net.gini.android.gvlexample.gini.ExtractionUtil.isPay5Extraction;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import net.gini.android.gvlexample.gini.Extraction;
import net.gini.android.vision.network.Error;
import net.gini.android.vision.network.GiniVisionNetworkCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alpar Szotyori on 23.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

abstract class BaseResultsPresenter extends ResultsContract.Presenter {

    private static final Map<String, Integer> PAY5_INDEX;

    static {
        PAY5_INDEX = new HashMap<>();
        PAY5_INDEX.put(Pay5.AMOUNT_TO_PAY, 0);
        PAY5_INDEX.put(Pay5.PAYMENT_RECIPIENT, 1);
        PAY5_INDEX.put(Pay5.IBAN, 2);
        PAY5_INDEX.put(Pay5.BIC, 3);
        PAY5_INDEX.put(Pay5.PAYMENT_REFERENCE, 4);
    }

    private final SparseArray<BaseExtractionContainer> mExtractions;
    private final BaseFeedbackSender mFeedbackSender;
    private final ExtractionContainerFactory mExtractionContainerFactory;

    BaseResultsPresenter(final ResultsContract.View view, final Bundle extractionsBundle) {
        super(view);
        mExtractions = new SparseArray<>(20);
        mFeedbackSender = new FeedbackSender();
        mExtractionContainerFactory = new ExtractionContainerFactory();
        readExtractions(extractionsBundle);
    }

    private void readExtractions(final Bundle extractionsBundle) {
        if (extractionsBundle != null) {
            int nonPay5Index = 5;
            for (String key : extractionsBundle.keySet()) {
                final BaseExtractionContainer extractionContainer =
                        mExtractionContainerFactory.createExtractionContainer(extractionsBundle,
                                key);
                if (extractionContainer != null) {
                    if (isPay5Extraction(key)) {
                        final Integer pay5Index = PAY5_INDEX.get(key);
                        mExtractions.append(pay5Index, extractionContainer);
                    } else {
                        mExtractions.append(nonPay5Index, extractionContainer);
                        nonPay5Index++;
                    }
                }
            }
        }
    }

    @Override
    public void sendFeedback(final View view) {
        logExtractions();
        syncSpecificExtractions();
        //noinspection unchecked
        mFeedbackSender.sendFeedback(mExtractions, getView());
    }

    @Override
    public void updateExtractions(final List<Extraction> extractions) {
        for (int i = 0; i < mExtractions.size(); i++) {
            final BaseExtractionContainer extractionContainer = mExtractions.valueAt(i);
            for (final Extraction updatedExtraction : extractions) {
                final Extraction extraction = extractionContainer.extraction;
                if (extraction.getName().equals(updatedExtraction.getName())) {
                    extraction.setValue(updatedExtraction.getValue());
                    break;
                }
            }
        }
    }

    private void syncSpecificExtractions() {
        for (int i = 0; i < mExtractions.size(); i++) {
            final BaseExtractionContainer extraction = mExtractions.valueAt(i);
            extraction.sync();
        }
    }

    private void logExtractions() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[\n");
        boolean first = true;
        for (int i = 0; i < mExtractions.size(); i++) {
            final BaseExtractionContainer extraction = mExtractions.valueAt(i);
            if (!first) {
                stringBuilder.append("  },\n");
            }
            first = false;
            stringBuilder.append("  {\n")
                         .append("    \"name\": \"")
                         .append(extraction.extraction.getName())
                         .append("\"\n")
                         .append("    \"value\": \"")
                         .append(extraction.extraction.getValue())
                         .append("\"\n")
                         .append("    \"specificExtractionValue\": \"")
                         .append(extraction.getSpecificExtractionValue())
                         .append("\"\n");
        }
        stringBuilder.append("  }\n");
        stringBuilder.append("]");
        Log.d("extractions", stringBuilder.toString());
    }

    @Override
    public void start() {
        getView().showExtractions(createExtractionsList());
    }

    @NonNull
    private List<Extraction> createExtractionsList() {
        final List<Extraction> extractions = new ArrayList<>(mExtractions.size());
        for (int i = 0; i < mExtractions.size(); i++) {
            extractions.add(mExtractions.valueAt(i).extraction);
        }
        return extractions;
    }
}
