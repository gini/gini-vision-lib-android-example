package net.gini.android.gvlexample.info;

import net.gini.android.gvlexample.PresenterContract;
import net.gini.android.gvlexample.ViewContract;

import java.util.Map;

/**
 * Created by aszotyori on 22.11.17.
 */

abstract class InfoContract {

    interface View extends ViewContract {

        void showVersions(final Map<String, String> versions);

        void showLinks(final Map<String, String> links);
    }

    static abstract class Presenter extends PresenterContract<View> {

        Presenter(final View view) {
            super(view);
        }

        abstract void onLinkClicked(final String link);
    }
}
