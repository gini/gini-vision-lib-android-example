package net.gini.android.gvlexample.gini;

/**
 * Created by aszotyori on 23.11.17.
 */

public class Extraction {

    private final String mName;
    private String mValue;

    public Extraction(final String name, final String value) {
        this.mName = name;
        this.mValue = value;
    }

    public String getName() {
        return mName;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(final String value) {
        this.mValue = value;
    }
}
