package net.gini.android.gvlexample.info;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import net.gini.android.gvlexample.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.gini.android.gvlexample.ActivityHelper.forcePortraitOrientationOnPhones;

/**
 * Created by Alpar Szotyori on 22.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class InfoActivity extends AppCompatActivity implements InfoContract.View {

    private RecyclerView mInfoItemsRecycler;
    private InfoContract.Presenter mPresenter;

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forcePortraitOrientationOnPhones(this);
        setContentView(R.layout.activity_info);
        mPresenter = new InfoPresenter(this);
        mInfoItemsRecycler = findViewById(R.id.infoItemsRecycler);

        mInfoItemsRecycler.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        final InfoAdapter adapter = new InfoAdapter();
        adapter.setItemClickListener(new InfoAdapter.ItemClickListener() {
            @Override
            public void onLinkClicked(final String link) {
                mPresenter.onLinkClicked(link);
            }

            @Override
            public void onConfigurationItemClicked(final String activityName) {
                mPresenter.onConfigurationItemClicked(activityName);
            }
        });
        mInfoItemsRecycler.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void showVersions(final String header, final Map<String, String> versions) {
        showSection(header, versions, InfoAdapter.Cell.Type.VERSION);
    }

    @Override
    public void showLinks(final String header, final Map<String, String> links) {
        showSection(header, links, InfoAdapter.Cell.Type.LINK);
    }

    @Override
    public void showConfigurationItems(final String header,
            final Map<String, String> configurationItems) {
        showSection(header, configurationItems, InfoAdapter.Cell.Type.ACTIVITY);
    }

    private void showSection(final String header, final Map<String, String> configurationLinks,
            final InfoAdapter.Cell.Type cellType) {
        final InfoAdapter adapter = (InfoAdapter) mInfoItemsRecycler.getAdapter();
        adapter.getTable().putSection(new InfoAdapter.Table.Section(
                new InfoAdapter.Cell(header, InfoAdapter.Cell.Type.HEADER),
                toCellList(configurationLinks, cellType)
        ));
        adapter.notifyDataSetChanged();
    }

    @NonNull
    private List<InfoAdapter.Cell> toCellList(final Map<String, String> map,
            final InfoAdapter.Cell.Type cellType) {
        List<InfoAdapter.Cell> items = new ArrayList<>(map.size());
        for (final Map.Entry<String, String> labelAndValue : map.entrySet()) {
            items.add(
                    new InfoAdapter.Cell(labelAndValue.getKey(), labelAndValue.getValue(),
                            cellType));
        }
        return items;
    }

}
