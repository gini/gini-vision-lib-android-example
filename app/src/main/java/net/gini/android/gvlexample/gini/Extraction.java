package net.gini.android.gvlexample.gini;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import net.gini.android.gvlexample.BR;

/**
 * Created by aszotyori on 23.11.17.
 */

public class Extraction extends BaseObservable {

    private final String mName;
    private String mValue;

    public Extraction(final String name, final String value) {
        this.mName = name;
        this.mValue = value;
    }

    @Bindable
    public String getName() {
        return mName;
    }

    @Bindable
    public String getValue() {
        return mValue;
    }

    public void setValue(final String value) {
        this.mValue = value;
        notifyPropertyChanged(BR.value);
    }
}
