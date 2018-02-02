package net.gini.android.gvlexample.results;

import android.os.Bundle;
import android.support.annotation.Nullable;

import net.gini.android.models.SpecificExtraction;

/**
 * Created by Alpar Szotyori on 01.02.2018.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

class ExtractionContainerFactory {

    @Nullable
    BaseExtractionContainer createExtractionContainer(final Bundle extractionsBundle, final String key) {
        final SpecificExtraction specificExtraction =
                extractionsBundle.getParcelable(key);
        if (specificExtraction != null) {
            return new ExtractionContainer(specificExtraction);
        }
        return null;
    }
}
