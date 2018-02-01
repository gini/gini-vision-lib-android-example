package net.gini.android.gvlexample;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Alpar Szotyori on 20.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class GVLExampleActivity extends BaseGVLExampleActivity implements GVLExampleContract.View {

    public static final String EXTRA_OUT_EXTRACTIONS = "EXTRA_OUT_EXTRACTIONS";

    @Override
    Bundle getExtractions(final Intent data) {
        return data.getBundleExtra(EXTRA_OUT_EXTRACTIONS);
    }
}
