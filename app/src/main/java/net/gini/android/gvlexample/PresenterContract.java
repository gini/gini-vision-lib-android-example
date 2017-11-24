package net.gini.android.gvlexample;

/**
 * Created by Alpar Szotyori on 22.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public abstract class PresenterContract<V extends ViewContract> {

    private final V mView;

    protected PresenterContract(final V view) {
        mView = view;
    }

    protected V getView() {
        return mView;
    }

    public abstract void start();
}
