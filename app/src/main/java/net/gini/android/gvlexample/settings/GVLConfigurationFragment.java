package net.gini.android.gvlexample.settings;

import android.os.Bundle;

import net.gini.android.gvlexample.R;

/**
 * Created by Alpar Szotyori on 21.06.2018.
 *
 * Copyright (c) 2018 Gini GmbH.
 */
public class GVLConfigurationFragment extends ConfigurationFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_gvl);

        bindPreferenceSummaryToValue(R.string.pref_key_gvl_document_import_file_types);
    }
}
