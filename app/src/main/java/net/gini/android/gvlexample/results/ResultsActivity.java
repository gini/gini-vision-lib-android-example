package net.gini.android.gvlexample.results;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.gini.android.gvlexample.R;
import net.gini.android.gvlexample.databinding.ActivityResultsBinding;
import net.gini.android.gvlexample.gini.Extraction;

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


}
