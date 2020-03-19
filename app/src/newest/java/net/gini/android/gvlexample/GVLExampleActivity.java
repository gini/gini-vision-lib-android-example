package net.gini.android.gvlexample;

import android.content.Intent;
import android.os.Bundle;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

import net.gini.android.vision.camera.CameraActivity;

/**
 * Created by Alpar Szotyori on 20.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class GVLExampleActivity extends BaseGVLExampleActivity implements GVLExampleContract.View {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCenter.start(getApplication(), "972db965-d54d-43ce-aa65-09f63e7208c5",
                Analytics.class, Crashes.class);
    }

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
