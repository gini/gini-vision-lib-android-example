package net.gini.android.gvlexample.results;

import android.util.SparseArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alpar Szotyori on 01.02.2018.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

abstract class BaseFeedbackSender<T> {

    void sendFeedback(final
    SparseArray<BaseExtractionContainer<T>> extractions, final ResultsContract.View view) {
        final Map<String, T> extractionsMap = createExtractionsMap(extractions);
        doSendFeedback(extractionsMap, view);
    }

    abstract void doSendFeedback(final Map<String, T> extractionsMap, final ResultsContract.View view);

    private Map<String, T> createExtractionsMap(final
    SparseArray<BaseExtractionContainer<T>> extractions) {
        final Map<String, T> extractionsMap =
                new HashMap<>(extractions.size());
        for (int i = 0; i < extractions.size(); i++) {
            final BaseExtractionContainer<T> extractionContainer = extractions.valueAt(i);
            extractionsMap.put(extractionContainer.getSpecificExtractionName(),
                    extractionContainer.specificExtraction);
        }
        return extractionsMap;
    }

}
