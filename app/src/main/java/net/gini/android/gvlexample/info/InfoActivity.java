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
        setContentView(R.layout.activity_info);
        mPresenter = new InfoPresenter(this);
        mInfoItemsRecycler = findViewById(R.id.infoItemsRecycler);

        mInfoItemsRecycler.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        final InfoAdapter adapter = new InfoAdapter();
        adapter.setLinkClickedListener(new InfoAdapter.LinkClickedListener() {
            @Override
            public void onLinkClicked(final String link) {
                mPresenter.onLinkClicked(link);
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
        final InfoAdapter adapter = (InfoAdapter) mInfoItemsRecycler.getAdapter();
        adapter.getTable().putSection(new InfoAdapter.Table.Section(
                new InfoAdapter.Cell(header, InfoAdapter.Cell.Type.HEADER),
                toCellList(versions, InfoAdapter.Cell.Type.VERSION)
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

    @Override
    public void showLinks(final String header, final Map<String, String> links) {
        final InfoAdapter adapter = (InfoAdapter) mInfoItemsRecycler.getAdapter();
        adapter.getTable().putSection(new InfoAdapter.Table.Section(
                new InfoAdapter.Cell(header, InfoAdapter.Cell.Type.HEADER),
                toCellList(links, InfoAdapter.Cell.Type.LINK)
        ));
        adapter.notifyDataSetChanged();
    }




}
