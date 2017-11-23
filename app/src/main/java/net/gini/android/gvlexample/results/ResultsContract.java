package net.gini.android.gvlexample.results;

import net.gini.android.gvlexample.PresenterContract;
import net.gini.android.gvlexample.ViewContract;
import net.gini.android.gvlexample.gini.Extraction;

import java.util.List;

/**
 * Created by aszotyori on 23.11.17.
 */

public abstract class ResultsContract {

    interface View extends ViewContract {

        void showExtractions(List<Extraction> extractions);
    }

    public static abstract class Presenter extends PresenterContract<View> {

        Presenter(final View view) {
            super(view);
        }

        public abstract void onDoneClicked(final android.view.View view);
    }
}
