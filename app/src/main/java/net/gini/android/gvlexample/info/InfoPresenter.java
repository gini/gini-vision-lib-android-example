package net.gini.android.gvlexample.info;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import net.gini.android.gvlexample.R;
import net.gini.android.gvlexample.configuration.ConfigurationActivity;
import net.gini.android.gvlexample.configuration.ConfigurationManager;
import net.gini.android.vision.BuildConfig;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Alpar Szotyori on 22.11.2017.
 * <p>
 * Copyright (c) 2017 Gini GmbH.
 */

class InfoPresenter extends InfoContract.Presenter {

    private static final String GVL_CONFIGURATION_ITEM = "GVL_CONFIGURATION_ITEM";
    private static final String API_SDK_CONFIGURATION_ITEM = "API_SDK_CONFIGURATION_ITEM";
    private static final String RESET_ALL_CONFIGURATION_ITEM = "RESET_ALL_CONFIGURATION_ITEM";

    InfoPresenter(final InfoContract.View view) {
        super(view);
    }

    @Override
    void onLinkClicked(final String link) {
        openLink(link);
    }

    @Override
    void onConfigurationItemClicked(final String configurationItem) {
        switch (configurationItem) {
            case GVL_CONFIGURATION_ITEM:
                launchConfigurationActivity(
                        ConfigurationActivity.ConfigurationSubject.VISION_LIBRARY);
                break;
            case API_SDK_CONFIGURATION_ITEM:
                launchConfigurationActivity(ConfigurationActivity.ConfigurationSubject.API_SDK);
                break;
            case RESET_ALL_CONFIGURATION_ITEM:
                confirmResetConfiguration();
                break;
            default:
                throw new UnsupportedOperationException(
                        "Unknown configuration item: " + configurationItem);
        }
    }

    private void confirmResetConfiguration() {
        final Context context = getView().getContext();
        new AlertDialog.Builder(context)
                .setMessage("Alle Anpassungen werden zurückgesetzt.")
                .setPositiveButton("Fortfahren", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ConfigurationManager.resetDefaultValues(context);
                        Toast.makeText(context, "Konfiguration wurde zurückgesetzt", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Abbrechen", null)
                .create()
                .show();
    }

    private void launchConfigurationActivity(
            final ConfigurationActivity.ConfigurationSubject configurationSubject) {
        final Context context = getView().getContext();
        final Intent intent = ConfigurationActivity.newIntent(getView().getContext(),
                configurationSubject);
        context.startActivity(intent);
    }

    private void openLink(final String link) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        getView().getContext().startActivity(intent);
    }

    @Override
    public void start() {
        getView().showConfigurationItems("Konfiguration", getConfigurationItems());
        getView().showVersions(getView().getContext().getString(R.string.info_versions_header),
                getVersions());
        getView().showLinks(getView().getContext().getString(R.string.info_links_header),
                getLinks());
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

        versions.put(getView().getContext().getString(R.string.gvl_display_name),
                BuildConfig.VERSION_NAME);
        versions.put(getView().getContext().getString(R.string.api_sdk_display_name),
                net.gini.android.BuildConfig.VERSION_NAME);
        return versions;
    }

    private Map<String, String> getConfigurationItems() {
        final Map<String, String> links = new LinkedHashMap<>();
        links.put("Gini Vision Library", GVL_CONFIGURATION_ITEM);
        links.put("Gini API SDK", API_SDK_CONFIGURATION_ITEM);
        links.put("Alles zurücksetzen", RESET_ALL_CONFIGURATION_ITEM);
        return links;
    }
}
