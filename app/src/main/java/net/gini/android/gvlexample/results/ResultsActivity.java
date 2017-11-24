package net.gini.android.gvlexample.results;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.gini.android.gvlexample.R;
import net.gini.android.gvlexample.databinding.ActivityResultsBinding;
import net.gini.android.gvlexample.databinding.BindingAdapter;
import net.gini.android.gvlexample.gini.Extraction;

import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity implements ResultsContract.View {

    private RecyclerView mExtractionsRecycler;
    private ResultsContract.Presenter mPresenter;

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityResultsBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_results);
        mPresenter = new ResultsPresenter(this);
        binding.setPresenter(mPresenter);

        mExtractionsRecycler = findViewById(R.id.extractionsRecycler);
        mExtractionsRecycler.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mExtractionsRecycler.setAdapter(new ExtractionsAdapter());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void showExtractions(final List<Extraction> extractions) {
        final ExtractionsAdapter adapter = (ExtractionsAdapter) mExtractionsRecycler.getAdapter();
        adapter.showExtractions(extractions);
    }

    static class ExtractionsAdapter extends BindingAdapter {

        private List<Extraction> mExtractions = new ArrayList<>();

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
            mExtractions = extractions;
            notifyDataSetChanged();
        }
    }
}
