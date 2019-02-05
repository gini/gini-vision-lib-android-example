package net.gini.android.gvlexample;

import android.app.Activity;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;
import net.hockeyapp.android.metrics.MetricsManager;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * Created by Alpar Szotyori on 29.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

class Hockeyapp implements LifecycleObserver {

    private final Activity mActivity;
    private final boolean mDisabled;

    Hockeyapp(final Activity activity) {
        this.mActivity = activity;
        mDisabled = BuildConfig.DEBUG;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void checkForCrashes() {
        if (mDisabled) {
            return;
        }
        CrashManager.register(mActivity);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void checkForUpdates() {
        if (mDisabled) {
            return;
        }
        UpdateManager.register(mActivity);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void disableUpdateCheck() {
        if (mDisabled) {
            return;
        }
        UpdateManager.unregister();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void enableUserMetrics() {
        if (mDisabled) {
            return;
        }
        MetricsManager.register(mActivity.getApplication());
    }

}
