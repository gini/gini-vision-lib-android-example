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

        final RecyclerAdapter adapter =
                new RecyclerAdapter(getString(R.string.info_versions_header),
                        getString(R.string.info_links_header));
        adapter.setLinkClickedListener(new RecyclerAdapter.LinkClickedListener() {
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
    public void showVersions(final Map<String, String> versions) {
        final RecyclerAdapter adapter = (RecyclerAdapter) mInfoItemsRecycler.getAdapter();
        adapter.setVersions(versions);
    }

    @Override
    public void showLinks(final Map<String, String> links) {
        final RecyclerAdapter adapter = (RecyclerAdapter) mInfoItemsRecycler.getAdapter();
        adapter.setLinks(links);
    }

    static class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

        private ItemList mItems = new ItemList();
        private LinkClickedListener mLinkClickedListener = new LinkClickedListener() {
            @Override
            public void onLinkClicked(final String link) {

            }
        };

        RecyclerAdapter(final String versionsHeader, final String linksHeader) {
            mItems.setVersionsHeader(versionsHeader);
            mItems.setLinksHeader(linksHeader);
        }

        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            final ViewType viewTypeValue = ViewType.values()[viewType];
            @LayoutRes
            int layoutId = 0;
            switch (viewTypeValue) {
                case HEADER:
                    layoutId = R.layout.item_info_header;
                    break;
                case ITEM_VERSION:
                    layoutId = R.layout.item_info_label_and_value;
                    break;
                case ITEM_LINK:
                    layoutId = R.layout.item_info_label;
                    break;
            }
            final View view =
                    LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            return new ViewHolder(view, viewTypeValue);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final Item item = mItems.get(position);
            if (holder.label != null) {
                holder.label.setText(item.label);
            }
            if (holder.value != null) {
                holder.value.setText(item.value);
            }
            holder.itemView.setOnClickListener(null);
            if (holder.viewType == ViewType.ITEM_LINK) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        final int actualPosition = holder.getAdapterPosition();
                        mLinkClickedListener.onLinkClicked(mItems.get(actualPosition).value);
                    }
                });
            }
        }

        @Override
        public int getItemViewType(final int position) {
            return mItems.get(position).type.ordinal();
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        void setLinkClickedListener(final LinkClickedListener listener) {
            mLinkClickedListener = listener;
        }

        void setLinks(Map<String, String> links) {
            mItems.setLinks(toItemList(links, ViewType.ITEM_LINK));
            notifyDataSetChanged();
        }

        @NonNull
        private List<Item> toItemList(final Map<String, String> map, final ViewType viewType) {
            List<Item> items = new ArrayList<>(map.size());
            for (final Map.Entry<String, String> labelAndValue : map.entrySet()) {
                items.add(
                        new Item(labelAndValue.getKey(), labelAndValue.getValue(), viewType));
            }
            return items;
        }

        void setVersions(Map<String, String> versions) {
            mItems.setVersions(toItemList(versions, ViewType.ITEM_VERSION));
            notifyDataSetChanged();
        }

        enum ViewType {
            HEADER,
            ITEM_VERSION,
            ITEM_LINK
        }

        static class Item {
            final String label;
            final ViewType type;
            final String value;

            Item(final String label, final String value, final ViewType type) {
                this.label = label;
                this.value = value;
                this.type = type;
            }

            Item(final String label, final ViewType type) {
                this.label = label;
                this.value = "";
                this.type = type;
            }
        }

        static class ItemList {
            private List<Item> allItems = new ArrayList<>();
            private List<Item> links = new ArrayList<>();
            private Item linksHeader = new Item("", ViewType.HEADER);
            private List<Item> versions = new ArrayList<>();
            private Item versionsHeader = new Item("", ViewType.HEADER);

            ItemList() {
                createAllItemsList();
            }

            private void createAllItemsList() {
                allItems = new ArrayList<>();
                allItems.add(versionsHeader);
                allItems.addAll(versions);
                allItems.add(linksHeader);
                allItems.addAll(links);
            }

            Item get(final int position) {
                return allItems.get(position);
            }

            void setLinks(final List<Item> links) {
                this.links = links;
                createAllItemsList();
            }

            void setLinksHeader(final String header) {
                linksHeader = new Item(header, ViewType.HEADER);
                createAllItemsList();
            }

            void setVersions(final List<Item> versions) {
                this.versions = versions;
                createAllItemsList();
            }

            void setVersionsHeader(final String header) {
                versionsHeader = new Item(header, ViewType.HEADER);
                createAllItemsList();
            }

            int size() {
                return allItems.size();
            }
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            final TextView label;
            final TextView value;
            final ViewType viewType;

            ViewHolder(final View itemView, final ViewType viewType) {
                super(itemView);
                label = itemView.findViewById(R.id.label);
                value = itemView.findViewById(R.id.value);
                this.viewType = viewType;
            }
        }

        interface LinkClickedListener {

            void onLinkClicked(final String link);
        }
    }


}
