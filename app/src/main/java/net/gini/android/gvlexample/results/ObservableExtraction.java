package net.gini.android.gvlexample.results;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import net.gini.android.gvlexample.BR;
import net.gini.android.gvlexample.gini.Extraction;

/**
 * Created by Alpar Szotyori on 24.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class ObservableExtraction extends BaseObservable {

    private final Extraction mExtraction;

    public ObservableExtraction(final Extraction extraction) {
        mExtraction = extraction;
    }

    @Bindable
    public String getName() {
        return mExtraction.getName();
    }

    @Bindable
    public String getValue() {
        return mExtraction.getValue();
    }

    public void setValue(final String value) {
        mExtraction.setValue(value);
        notifyPropertyChanged(BR.value);
    }
}
