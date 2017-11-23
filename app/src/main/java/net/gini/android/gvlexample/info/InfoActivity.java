package net.gini.android.gvlexample.info;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.gini.android.gvlexample.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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

    static class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.InfoHolder> {

        private LinkClickedListener mLinkClickedListener = new LinkClickedListener() {
            @Override
            public void onLinkClicked(final String link) {

            }
        };
        private Table mTable = new Table();

        @Override
        public InfoHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            final Cell.Type cellType = Cell.Type.values()[viewType];
            @LayoutRes
            int layoutId = 0;
            switch (cellType) {
                case HEADER:
                    layoutId = R.layout.item_info_header;
                    break;
                case VERSION:
                    layoutId = R.layout.item_info_label_and_value;
                    break;
                case LINK:
                    layoutId = R.layout.item_info_label;
                    break;
            }
            final View view =
                    LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            return new InfoHolder(view, cellType);
        }

        @Override
        public void onBindViewHolder(final InfoHolder holder, final int position) {
            final Cell item = mTable.get(position);
            if (holder.label != null) {
                holder.label.setText(item.label);
            }
            if (holder.value != null) {
                holder.value.setText(item.value);
            }
            holder.itemView.setOnClickListener(null);
            if (holder.cellType == Cell.Type.LINK) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        final int actualPosition = holder.getAdapterPosition();
                        mLinkClickedListener.onLinkClicked(mTable.get(actualPosition).value);
                    }
                });
            }
        }

        @Override
        public int getItemViewType(final int position) {
            return mTable.get(position).type.ordinal();
        }

        @Override
        public int getItemCount() {
            return mTable.getSize();
        }

        Table getTable() {
            return mTable;
        }

        void setLinkClickedListener(final LinkClickedListener listener) {
            mLinkClickedListener = listener;
        }

        static class Cell {

            final String label;
            final Type type;
            final String value;
            Cell(final String label, final String value, final Type type) {
                this.label = label;
                this.value = value;
                this.type = type;
            }

            Cell(final String label, final Type type) {
                this.label = label;
                this.value = "";
                this.type = type;
            }

            enum Type {
                HEADER,
                VERSION,
                LINK
            }
        }

        static class InfoHolder extends RecyclerView.ViewHolder {

            final Cell.Type cellType;
            final TextView label;
            final TextView value;

            InfoHolder(final View itemView, final Cell.Type cellType) {
                super(itemView);
                label = itemView.findViewById(R.id.label);
                value = itemView.findViewById(R.id.value);
                this.cellType = cellType;
            }
        }

        static class Table {
            private final List<Cell> index = new ArrayList<>();
            private final Map<String, Section> sections = new LinkedHashMap<>();

            Cell get(final int position) {
                return index.get(position);
            }

            int getSize() {
                return index.size();
            }

            void putSection(final Section section) {
                sections.put(section.header.label, section);
                updateIndex();
            }

            private void updateIndex() {
                index.clear();
                for (final Map.Entry<String, Section> sectionEntry : sections.entrySet()) {
                    final Section section = sectionEntry.getValue();
                    index.add(section.header);
                    index.addAll(section.items);
                }
            }

            static class Section {
                final Cell header;
                final List<Cell> items;

                Section(final Cell header,
                        final List<Cell> items) {
                    this.header = header;
                    this.items = items;
                }
            }
        }

        interface LinkClickedListener {

            void onLinkClicked(final String link);
        }
    }


}
