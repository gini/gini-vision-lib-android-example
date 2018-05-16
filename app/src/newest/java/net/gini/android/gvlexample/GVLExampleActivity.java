package net.gini.android.gvlexample;

import android.content.Intent;
import android.os.Bundle;

import net.gini.android.vision.camera.CameraActivity;

/**
 * Created by Alpar Szotyori on 20.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class GVLExampleActivity extends BaseGVLExampleActivity implements GVLExampleContract.View {

    @Override
    Bundle getExtractions(final Intent data) {
        return data.getBundleExtra(CameraActivity.EXTRA_OUT_EXTRACTIONS);
    }

    boolean isIntentActionViewOrSend(final Intent intent) {
        String action = intent.getAction();
        return Intent.ACTION_VIEW.equals(action)
                || Intent.ACTION_SEND.equals(action)
                || Intent.ACTION_SEND_MULTIPLE.equals(action);
    }
}
