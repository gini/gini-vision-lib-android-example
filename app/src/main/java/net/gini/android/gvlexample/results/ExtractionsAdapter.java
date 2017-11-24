package net.gini.android.gvlexample.results;

import android.support.design.widget.TextInputLayout;

import net.gini.android.gvlexample.R;
import net.gini.android.gvlexample.databinding.BindingAdapter;
import net.gini.android.gvlexample.gini.Extraction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alpar Szotyori on 24.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class ExtractionsAdapter extends BindingAdapter {

    private List<ObservableExtraction> mExtractions = new ArrayList<>();

    @Override
    public int getItemCount() {
        return mExtractions.size();
    }

    @Override
    public void onBindViewHolder(final BindingAdapter.BindingViewHolder holder,
            final int position) {
        final TextInputLayout textInputLayout =
                holder.getBinding().getRoot().findViewById(R.id.textInputLayout);
        textInputLayout.setHintAnimationEnabled(false);
        super.onBindViewHolder(holder, position);
        textInputLayout.setHintAnimationEnabled(true);
    }

    @Override
    public int getLayoutIdForPosition(final int position) {
        return R.layout.item_extraction;
    }

    @Override
    public Object getObjForPosition(final int position) {
        return mExtractions.get(position);
    }

    void showExtractions(final List<Extraction> extractions) {
        mExtractions.clear();
        for (final Extraction extraction : extractions) {
            mExtractions.add(new ObservableExtraction(extraction));
        }
        notifyDataSetChanged();
    }
}
