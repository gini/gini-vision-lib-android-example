package net.gini.android.gvlexample.gini;

import android.util.Log;

import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.analytics.EventProperties;

import net.gini.android.vision.tracking.AnalysisScreenEvent;
import net.gini.android.vision.tracking.CameraScreenEvent;
import net.gini.android.vision.tracking.Event;
import net.gini.android.vision.tracking.EventTracker;
import net.gini.android.vision.tracking.OnboardingScreenEvent;
import net.gini.android.vision.tracking.ReviewScreenEvent;

import java.util.Map;

import androidx.annotation.NonNull;

/**
 * Created by Alpar Szotyori on 06.03.2020.
 *
 * Copyright (c) 2020 Gini GmbH.
 */
public class ExampleEventTracker implements EventTracker {

    @Override
    public void onOnboardingScreenEvent(final Event<OnboardingScreenEvent> event) {
        logEvent(event);
        trackEvent(event);
    }

    @Override
    public void onCameraScreenEvent(final Event<CameraScreenEvent> event) {
        logEvent(event);
        trackEvent(event);
    }

    @Override
    public void onReviewScreenEvent(final Event<ReviewScreenEvent> event) {
        logEvent(event);
        trackEvent(event);
    }

    @Override
    public void onAnalysisScreenEvent(final Event<AnalysisScreenEvent> event) {
        logEvent(event);
        trackEvent(event);
    }

    private void logEvent(@NonNull final Event<?> event) {
        //noinspection StringBufferReplaceableByString
        final StringBuilder message = new StringBuilder();
        message.append(event.getType().getClass().getSimpleName())
                .append(".")
                .append(event.getType().name())
                .append(": { ")
                .append(printEventDetails(event.getDetails()))
                .append(" }");
        Log.i("GVL Event Tracker", message.toString());
    }

    private String printEventDetails(@NonNull final Map<String, Object> details) {
        final StringBuilder output = new StringBuilder();
        for (final Map.Entry<String, Object> detail : details.entrySet()) {
            output.append(detail.getKey())
                    .append(": ")
                    .append(detail.getValue())
                    .append("; ");
        }
        return output.toString();
    }

    private void trackEvent(@NonNull final Event<?> event) {
        final String name =
                event.getType().getClass().getSimpleName() + "." + event.getType().name();
        Analytics.trackEvent(name, (EventProperties) event.getDetails());
    }
}
