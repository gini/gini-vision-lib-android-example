package net.gini.android.gvlexample.results;

import android.util.Log;

import net.gini.android.DocumentTaskManager;
import net.gini.android.gvlexample.GVLExampleApp;
import net.gini.android.gvlexample.ThreadHelper;
import net.gini.android.models.Document;
import net.gini.android.models.SpecificExtraction;

import org.json.JSONException;

import java.util.Map;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by Alpar Szotyori on 01.02.2018.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

class FeedbackSender extends BaseFeedbackSender<SpecificExtraction> {

    private final ThreadHelper mThreadHelper = new ThreadHelper();

    @Override
    void doSendFeedback(final Map<String, SpecificExtraction> extractionsMap,
            final ResultsContract.View view) {
        final GVLExampleApp app = (GVLExampleApp) view.getApplication();
        final DocumentTaskManager documentTaskManager = app.getGiniApi().getDocumentTaskManager();
        final Document document = app.getSingleDocumentAnalyzer().getGiniApiDocument();

        // We require the Gini API SDK's net.gini.android.models.Document for sending the feedback
        if (document != null) {
            try {
                documentTaskManager.sendFeedbackForExtractions(document, extractionsMap)
                                   .continueWith(new Continuation<Document, Object>() {
                                       @Override
                                       public Object then(final Task<Document> task)
                                               throws Exception {
                                           mThreadHelper.runOnUiThread(new Runnable() {
                                               @Override
                                               public void run() {
                                                   if (task.isFaulted()) {
                                                       String message = "unknown";
                                                       if (task.getError() != null) {
                                                           message = task.getError().getMessage();
                                                       }
                                                       Log.e("feedback", message);
                                                   }
                                                   view.finish();
                                               }
                                           });
                                           return null;
                                       }
                                   });
            } catch (JSONException e) {
                Log.e("feedback", "Feedback not sent", e);
                view.finish();
            }
        } else {
            Log.e("feedback", "Feedback not sent: no Gini Api Document available");
            view.finish();
        }
    }
}
