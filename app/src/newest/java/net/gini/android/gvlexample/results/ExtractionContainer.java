package net.gini.android.gvlexample.results;

import net.gini.android.vision.network.model.GiniVisionSpecificExtraction;

/**
 * Created by Alpar Szotyori on 01.02.2018.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

class ExtractionContainer extends
        BaseExtractionContainer<GiniVisionSpecificExtraction> {

    ExtractionContainer(
            final GiniVisionSpecificExtraction specificExtraction) {
        super(specificExtraction);
    }

    @Override
    String getSpecificExtractionName() {
        return specificExtraction.getName();
    }

    @Override
    String getSpecificExtractionValue() {
        return specificExtraction.getValue();
    }

    @Override
    void setSpecificExtractionValue(final String value) {
        specificExtraction.setValue(value);
    }
}
