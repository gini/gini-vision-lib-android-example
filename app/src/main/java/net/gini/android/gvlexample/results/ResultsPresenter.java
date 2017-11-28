package net.gini.android.gvlexample.results;

import static net.gini.android.gvlexample.gini.ExtractionUtil.isPay5Extraction;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import net.gini.android.DocumentTaskManager;
import net.gini.android.gvlexample.GVLExampleApp;
import net.gini.android.gvlexample.ThreadHelper;
import net.gini.android.gvlexample.gini.Extraction;
import net.gini.android.models.Document;
import net.gini.android.models.SpecificExtraction;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bolts.Continuation;
import bolts.Task;

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

    private final SparseArray<ExtractionContainer> mExtractions;
    private final ThreadHelper mThreadHelper = new ThreadHelper();

    ResultsPresenter(final ResultsContract.View view, final Bundle extractionsBundle) {
        super(view);
        mExtractions = new SparseArray<>(20);
        readExtractions(extractionsBundle);
    }

    private void readExtractions(final Bundle extractionsBundle) {
        if (extractionsBundle != null) {
            int nonPay5Index = 5;
            for (String key : extractionsBundle.keySet()) {
                final SpecificExtraction specificExtraction = extractionsBundle.getParcelable(key);
                if (specificExtraction != null) {
                    if (isPay5Extraction(key)) {
                        final Integer pay5Index = PAY5_INDEX.get(key);
                        mExtractions.append(pay5Index,
                                ExtractionContainer.from(specificExtraction));
                    } else {
                        mExtractions.append(nonPay5Index,
                                ExtractionContainer.from(specificExtraction));
                        nonPay5Index++;
                    }
                }
            }
        }
    }

    @Override
    public void sendFeedback(final View view) {
        logExtractions();
        final GVLExampleApp app = (GVLExampleApp) getView().getApplication();
        final DocumentTaskManager documentTaskManager = app.getGiniApi().getDocumentTaskManager();
        final Document document = app.getSingleDocumentAnalyzer().getGiniApiDocument();

        // We require the Gini API SDK's net.gini.android.models.Document for sending the feedback
        if (document != null) {
            syncSpecificExtractions();
            final Map<String, SpecificExtraction> extractionsMap = createExtractionsMap();
            try {
                documentTaskManager.sendFeedbackForExtractions(document, extractionsMap)
                                   .continueWith(new Continuation<Document, Object>() {
                                       @Override
                                       public Object then(final Task<Document> task)
                                               throws Exception {
                                           mThreadHelper.runOnUiThread(new Runnable() {
                                               @Override
                                               public void run() {
                                                   if (task.isFaulted()) {
                                                       String message = "unknown";
                                                       if (task.getError() != null) {
                                                           message = task.getError().getMessage();
                                                       }
                                                       Log.e("feedback", message);
                                                   }
                                                   getView().finish();
                                               }
                                           });
                                           return null;
                                       }
                                   });
            } catch (JSONException e) {
                Log.e("feedback", "Feedback not sent", e);
                getView().finish();
            }
        } else {
            Log.e("feedback", "Feedback not sent: no Gini Api Document available");
            getView().finish();
        }
    }

    @Override
    public void updateExtractions(final List<Extraction> extractions) {
        for (int i = 0; i < mExtractions.size(); i++) {
            final ExtractionContainer extractionContainer = mExtractions.valueAt(i);
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
            final ExtractionContainer extraction = mExtractions.valueAt(i);
            extraction.sync();
        }
    }

    private Map<String, SpecificExtraction> createExtractionsMap() {
        final Map<String, SpecificExtraction> extractionsMap = new HashMap<>(mExtractions.size());
        for (int i = 0; i < mExtractions.size(); i++) {
            final ExtractionContainer extractionContainer = mExtractions.valueAt(i);
            extractionsMap.put(extractionContainer.specificExtraction.getName(),
                    extractionContainer.specificExtraction);
        }
        return extractionsMap;
    }

    private void logExtractions() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[\n");
        boolean first = true;
        for (int i = 0; i < mExtractions.size(); i++) {
            final ExtractionContainer extraction = mExtractions.valueAt(i);
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
                         .append(extraction.specificExtraction.getValue())
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

    static class ExtractionContainer {
        final Extraction extraction;
        final SpecificExtraction specificExtraction;

        ExtractionContainer(final Extraction extraction,
                final SpecificExtraction specificExtraction) {
            this.extraction = extraction;
            this.specificExtraction = specificExtraction;
        }

        static ExtractionContainer from(final SpecificExtraction specificExtraction) {
            final Extraction extraction =
                    new Extraction(specificExtraction.getName(), specificExtraction.getValue());
            return new ExtractionContainer(extraction, specificExtraction);
        }

        void sync() {
            if (!extraction.getValue().equals(specificExtraction.getValue())) {
                specificExtraction.setValue(extraction.getValue());
            }
        }
    }
}
