package net.gini.android.gvlexample.results;

import net.gini.android.gvlexample.gini.Extraction;

/**
 * Created by Alpar Szotyori on 01.02.2018.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

abstract class BaseExtractionContainer<T> {
    final Extraction extraction;
    final T specificExtraction;

    BaseExtractionContainer(final T specificExtraction) {
        this.specificExtraction = specificExtraction;
        this.extraction =
                new Extraction(getSpecificExtractionName(),
                        getSpecificExtractionValue());
    }

    abstract String getSpecificExtractionName();

    abstract String getSpecificExtractionValue();

    abstract void setSpecificExtractionValue(final String value);

    void sync() {
        if (!extraction.getValue().equals(getSpecificExtractionValue())) {
            setSpecificExtractionValue(extraction.getValue());
        }
    }
}
