package net.gini.android.gvlexample.settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.gini.android.gvlexample.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Alpar Szotyori on 24.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsItemHolder> {

    private ItemClickListener mItemClickListener = new ItemClickListener() {
        @Override
        public void onLinkClicked(final String link) {

        }

        @Override
        public void onConfigurationItemClicked(final String activityName) {

        }
    };
    private Table mTable = new Table();

    @Override
    public SettingsItemHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final Cell.Type cellType = Cell.Type.values()[viewType];
        @LayoutRes
        int layoutId = 0;
        switch (cellType) {
            case HEADER:
                layoutId = R.layout.item_setttings_header;
                break;
            case VERSION:
                layoutId = R.layout.item_settings_label_and_value;
                break;
            case ACTIVITY:
            case LINK:
                layoutId = R.layout.item_settings_label;
                break;
        }
        final View view =
                LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new SettingsItemHolder(view, cellType);
    }

    @Override
    public void onBindViewHolder(final SettingsItemHolder holder, final int position) {
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
                    mItemClickListener.onLinkClicked(mTable.get(actualPosition).value);
                }
            });
        }
        if (holder.cellType == Cell.Type.ACTIVITY) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final int actualPosition = holder.getAdapterPosition();
                    mItemClickListener.onConfigurationItemClicked(mTable.get(actualPosition).value);
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

    void setItemClickListener(final ItemClickListener listener) {
        mItemClickListener = listener;
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
            LINK,
            ACTIVITY
        }
    }

    static class SettingsItemHolder extends RecyclerView.ViewHolder {

        final Cell.Type cellType;
        final TextView label;
        final TextView value;

        SettingsItemHolder(final View itemView, final Cell.Type cellType) {
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

    interface ItemClickListener {

        void onLinkClicked(final String link);

        void onConfigurationItemClicked(final String activityName);
    }
}
