package net.gini.android.gvlexample.results;

import static net.gini.android.gvlexample.ActivityHelper.forcePortraitOrientationOnPhones;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import net.gini.android.gvlexample.R;
import net.gini.android.gvlexample.databinding.ActivityResultsBinding;
import net.gini.android.gvlexample.gini.Extraction;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Alpar Szotyori on 23.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class ResultsActivity extends AppCompatActivity implements ResultsContract.View {

    public static final String EXTRA_IN_EXTRACTIONS = "EXTRA_IN_EXTRACTIONS";
    private static final String EXTRACTION_NAMES_SAVED_STATE = "EXTRACTION_NAMES_SAVED_STATE";
    private static final String EXTRACTION_VALUES_SAVED_STATE = "EXTRACTION_VALUES_SAVED_STATE";
    private List<Extraction> mExtractions;

    private RecyclerView mExtractionsRecycler;
    private ResultsContract.Presenter mPresenter;

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forcePortraitOrientationOnPhones(this);
        ActivityResultsBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_results);
        mPresenter = new ResultsPresenter(this,
                (Bundle) getIntent().getParcelableExtra(EXTRA_IN_EXTRACTIONS));
        binding.setPresenter(mPresenter);

        mExtractionsRecycler = findViewById(R.id.extractionsRecycler);
        mExtractionsRecycler.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mExtractionsRecycler.setAdapter(new ExtractionsAdapter());

        if (savedInstanceState != null) {
            final ArrayList<String> extractionNames = savedInstanceState.getStringArrayList(EXTRACTION_NAMES_SAVED_STATE);
            final ArrayList<String> extractionValues = savedInstanceState.getStringArrayList(EXTRACTION_VALUES_SAVED_STATE);
            if (extractionNames != null && extractionValues != null) {
                mExtractions = new ArrayList<>(extractionNames.size());
                for (int i = 0; i < extractionNames.size(); i++) {
                    mExtractions.add(new Extraction(extractionNames.get(i), extractionValues.get(i)));
                }
                mPresenter.updateExtractions(mExtractions);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        final ArrayList<String> extractionNames = new ArrayList<>();
        final ArrayList<String> extractionValues = new ArrayList<>();
        for (final Extraction extraction : mExtractions) {
            extractionNames.add(extraction.getName());
            extractionValues.add(extraction.getValue());
        }
        outState.putStringArrayList(EXTRACTION_NAMES_SAVED_STATE, extractionNames);
        outState.putStringArrayList(EXTRACTION_VALUES_SAVED_STATE, extractionValues);
    }

    @Override
    public void showExtractions(final List<Extraction> extractions) {
        final ExtractionsAdapter adapter = (ExtractionsAdapter) mExtractionsRecycler.getAdapter();
        mExtractions = extractions;
        adapter.showExtractions(extractions);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.stop();
    }
}
