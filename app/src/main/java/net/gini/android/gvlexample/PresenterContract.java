package net.gini.android.gvlexample;

/**
 * Created by aszotyori on 22.11.17.
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
