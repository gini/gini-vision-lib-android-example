package net.gini.android.gvlexample.configuration;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import net.gini.android.gvlexample.R;

/**
 * Created by Alpar Szotyori on 22.06.2018.
 * <p>
 * Copyright (c) 2018 Gini GmbH.
 */
public final class ConfigurationManager {

    public static void initDefaultValues(@NonNull final Context context) {
        PreferenceManager.setDefaultValues(context, R.xml.pref_api_sdk, true);
        PreferenceManager.setDefaultValues(context, R.xml.pref_gvl, true);
    }

    public static void resetDefaultValues(@NonNull final Context context) {
        SharedPreferences configuration = PreferenceManager.getDefaultSharedPreferences(context);
        configuration.edit().clear().apply();
        initDefaultValues(context);
    }

    public static SharedPreferences getConfigurationPreferences(@NonNull final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private ConfigurationManager() {
    }
}
