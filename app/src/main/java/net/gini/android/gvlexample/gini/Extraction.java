package net.gini.android.gvlexample.gini;

/**
 * Created by Alpar Szotyori on 23.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
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
