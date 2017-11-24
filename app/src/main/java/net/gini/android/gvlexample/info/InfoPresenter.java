package net.gini.android.gvlexample.info;

import android.content.Intent;
import android.net.Uri;

import net.gini.android.gvlexample.R;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Alpar Szotyori on 22.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

class InfoPresenter extends InfoContract.Presenter {

    InfoPresenter(final InfoContract.View view) {
        super(view);
    }

    @Override
    void onLinkClicked(final String link) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        getView().getContext().startActivity(intent);
    }

    @Override
    public void start() {
        getView().showVersions(getView().getContext().getString(R.string.info_versions_header), getVersions());
        getView().showLinks(getView().getContext().getString(R.string.info_links_header), getLinks());
    }

    private Map<String, String> getLinks() {
        final Map<String, String> links = new LinkedHashMap<>();
        links.put(getView().getContext().getString(R.string.info_links_changelog),
                getView().getContext().getString(R.string.gvl_changelog_link));
        links.put(getView().getContext().getString(R.string.info_links_documentation),
                getView().getContext().getString(R.string.gvl_documentation_link));
        links.put(getView().getContext().getString(R.string.info_links_github),
                getView().getContext().getString(R.string.gvl_github_link));
        return links;
    }

    private Map<String, String> getVersions() {
        final Map<String, String> versions = new LinkedHashMap<>();
        versions.put(getView().getContext().getString(R.string.gvl_display_name), "2.4.2");
        versions.put(getView().getContext().getString(R.string.api_sdk_display_name), "1.3.92");
        return versions;
    }


}
