package net.gini.android.gvlexample.configuration;

import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

/**
 * Created by Alpar Szotyori on 20.06.2018.
 *
 * Copyright (c) 2018 Gini GmbH.
 */
public class ConfigurationFragment extends PreferenceFragment {

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    protected static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener =
            new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object value) {
                    String stringValue = value.toString();

                    if (preference instanceof ListPreference) {
                        // For list preferences, look up the correct display value in
                        // the preference's 'entries' list.
                        ListPreference listPreference = (ListPreference) preference;
                        int index = listPreference.findIndexOfValue(stringValue);

                        // Set the summary to reflect the new value.
                        preference.setSummary(
                                index >= 0
                                        ? listPreference.getEntries()[index]
                                        : null);

                    } else {
                        // For all other preferences, set the summary to the value's
                        // simple string representation.
                        preference.setSummary(stringValue);
                    }
                    return true;
                }
            };

    protected void bindPreferenceSummaryToValue(@StringRes final int pref_key) {
        bindPreferenceSummaryToValue(findPreference(getString(pref_key)));
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    protected void updateEditTextPreference(@NonNull final String prefKey, @NonNull final String prefValue) {
        getPreferenceManager().getSharedPreferences().edit().putString(prefKey,prefValue).apply();
        EditTextPreference preference = (EditTextPreference) findPreference(prefKey);
        preference.setText(prefValue);
        preference.setSummary(prefValue);
    }

    protected void updateSwitchPreference(@NonNull final String prefKey, @NonNull final boolean prefValue) {
        getPreferenceManager().getSharedPreferences().edit().putBoolean(prefKey,prefValue).apply();
        SwitchPreference preference = (SwitchPreference) findPreference(prefKey);
        preference.setChecked(prefValue);
    }
}
