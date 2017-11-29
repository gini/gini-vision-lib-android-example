package net.gini.android.gvlexample;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Alpar Szotyori on 28.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class ActivityHelper {

    private static void lockToPortraitOrientation(@Nullable final Activity activity) {
        if (activity == null) {
            return;
        }
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public static void forcePortraitOrientationOnPhones(@Nullable final Activity activity) {
        if (activity == null) {
            return;
        }
        if (!isTablet(activity)) {
            lockToPortraitOrientation(activity);
        }
    }

    private static boolean isTablet(@NonNull final Activity activity) {
        return activity.getResources().getBoolean(R.bool.is_tablet);
    }
}
