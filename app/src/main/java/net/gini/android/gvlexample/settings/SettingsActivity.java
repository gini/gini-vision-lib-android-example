package net.gini.android.gvlexample.settings;

import static net.gini.android.gvlexample.ActivityHelper.forcePortraitOrientationOnPhones;

import android.content.Context;
import android.os.Bundle;

import net.gini.android.gvlexample.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Alpar Szotyori on 22.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class SettingsActivity extends AppCompatActivity implements SettingsContract.View {

    private RecyclerView mSettingsItemsRecycler;
    private SettingsContract.Presenter mPresenter;

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forcePortraitOrientationOnPhones(this);
        setupActionBar();
        setContentView(R.layout.activity_settings);
        mPresenter = new SettingsPresenter(this);
        mSettingsItemsRecycler = findViewById(R.id.settingsItemsRecycler);

        mSettingsItemsRecycler.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        final SettingsAdapter adapter = new SettingsAdapter();
        adapter.setItemClickListener(new SettingsAdapter.ItemClickListener() {
            @Override
            public void onLinkClicked(final String link) {
                mPresenter.onLinkClicked(link);
            }

            @Override
            public void onConfigurationItemClicked(final String activityName) {
                mPresenter.onConfigurationItemClicked(activityName);
            }
        });
        mSettingsItemsRecycler.setAdapter(adapter);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void showVersions(final String header, final Map<String, String> versions) {
        showSection(header, versions, SettingsAdapter.Cell.Type.VERSION);
    }

    @Override
    public void showLinks(final String header, final Map<String, String> links) {
        showSection(header, links, SettingsAdapter.Cell.Type.LINK);
    }

    @Override
    public void showConfigurationItems(final String header,
            final Map<String, String> configurationItems) {
        showSection(header, configurationItems, SettingsAdapter.Cell.Type.ACTIVITY);
    }

    private void showSection(final String header, final Map<String, String> configurationLinks,
            final SettingsAdapter.Cell.Type cellType) {
        final SettingsAdapter adapter = (SettingsAdapter) mSettingsItemsRecycler.getAdapter();
        adapter.getTable().putSection(new SettingsAdapter.Table.Section(
                new SettingsAdapter.Cell(header, SettingsAdapter.Cell.Type.HEADER),
                toCellList(configurationLinks, cellType)
        ));
        adapter.notifyDataSetChanged();
    }

    @NonNull
    private List<SettingsAdapter.Cell> toCellList(final Map<String, String> map,
                                                  final SettingsAdapter.Cell.Type cellType) {
        List<SettingsAdapter.Cell> items = new ArrayList<>(map.size());
        for (final Map.Entry<String, String> labelAndValue : map.entrySet()) {
            items.add(
                    new SettingsAdapter.Cell(labelAndValue.getKey(), labelAndValue.getValue(),
                            cellType));
        }
        return items;
    }

}
