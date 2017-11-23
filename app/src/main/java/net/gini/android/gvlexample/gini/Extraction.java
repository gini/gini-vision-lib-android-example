package net.gini.android.gvlexample.gini;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import net.gini.android.gvlexample.BR;

/**
 * Created by aszotyori on 23.11.17.
 */

public class Extraction extends BaseObservable {

    private final String mName;
    private String mValue;
    private TextWatcher mValueWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(final CharSequence s, final int start, final int count,
                final int after) {

        }

        @Override
        public void onTextChanged(final CharSequence s, final int start, final int before,
                final int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.equals(mValue, s.toString())) {
                setValue(s.toString());
            }
        }
    };

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

    public TextWatcher getValueWatcher() {
        return mValueWatcher;
    }
}
