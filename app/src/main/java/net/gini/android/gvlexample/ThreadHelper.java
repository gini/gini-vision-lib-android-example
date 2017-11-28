package net.gini.android.gvlexample;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by Alpar Szotyori on 28.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class ThreadHelper {

    private final Thread mUiThread;
    private final Handler mUiHandler;

    public ThreadHelper() {
        final Looper mainLooper = Looper.getMainLooper();
        mUiThread = mainLooper.getThread();
        mUiHandler = new Handler(mainLooper);
    }

    public void runOnUiThread(Runnable action) {
        if (Thread.currentThread() != mUiThread) {
            mUiHandler.post(action);
        } else {
            action.run();
        }
    }
}
