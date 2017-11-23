package net.gini.android.gvlexample.info;

import android.content.Intent;
import android.net.Uri;

import net.gini.android.gvlexample.R;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by aszotyori on 22.11.17.
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
        getView().showVersions(getView().getContext().getString(R.string.info_links_header), getVersions());
        getView().showLinks(getView().getContext().getString(R.string.info_versions_header), getLinks());
    }

    private Map<String, String> getLinks() {
        final Map<String, String> links = new LinkedHashMap<>();
        links.put(getView().getContext().getString(R.string.info_links_changelog),
                "http://developer.gini.net/gini-vision-lib-android/html/changelog.html");
        links.put(getView().getContext().getString(R.string.info_links_documentation),
                "http://developer.gini.net/gini-vision-lib-android/html/index.html");
        links.put(getView().getContext().getString(R.string.info_links_github),
                "https://github.com/gini/gini-vision-lib-android");
        return links;
    }

    private Map<String, String> getVersions() {
        final Map<String, String> versions = new LinkedHashMap<>();
        versions.put("Gini Vision Library", "2.4.2");
        versions.put("Gini API SDK", "1.3.92");
        return versions;
    }


}
