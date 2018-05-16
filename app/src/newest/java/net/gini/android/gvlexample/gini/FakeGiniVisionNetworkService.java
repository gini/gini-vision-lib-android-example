package net.gini.android.gvlexample.gini;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import net.gini.android.Gini;
import net.gini.android.SdkBuilder;
import net.gini.android.gvlexample.R;
import net.gini.android.models.SpecificExtraction;
import net.gini.android.vision.Document;
import net.gini.android.vision.network.AnalysisResult;
import net.gini.android.vision.network.CancellationToken;
import net.gini.android.vision.network.Error;
import net.gini.android.vision.network.GiniVisionNetworkCallback;
import net.gini.android.vision.network.GiniVisionNetworkService;
import net.gini.android.vision.network.Result;
import net.gini.android.vision.network.model.GiniVisionSpecificExtraction;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Alpar Szotyori on 14.05.2018.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class FakeGiniVisionNetworkService implements GiniVisionNetworkService {

    private final SingleDocumentAnalyzer mSingleDocumentAnalyzer;
    private final Map<String, Document> mGiniDocuments;

    public FakeGiniVisionNetworkService(@NonNull final Context context) {
        final String clientId = context.getString(R.string.gini_api_client_id);
        final String clientSecret = context.getString(R.string.gini_api_client_secret);
        SdkBuilder builder = new SdkBuilder(context, clientId,
                clientSecret,
                "gvlexample.net");
        final Gini giniApi = builder.build();

        mSingleDocumentAnalyzer = new SingleDocumentAnalyzer(giniApi);
        mGiniDocuments = new HashMap<>();
    }

    @Override
    public CancellationToken upload(@NonNull final Document document,
            @NonNull final GiniVisionNetworkCallback<Result, Error> callback) {

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                final net.gini.android.models.Document apiDocument =
                        new net.gini.android.models.Document(
                                UUID.randomUUID().toString(),
                                net.gini.android.models.Document.ProcessingState.PENDING, "", 1,
                                new Date(),
                                net.gini.android.models.Document.SourceClassification.SCANNED);
                mGiniDocuments.put(apiDocument.getId(), document);
                callback.success(new Result(apiDocument.getId()));
            }
        }, 500 + (Math.round(Math.random() * 1000)));

        return new CancellationToken() {
            @Override
            public void cancel() {
            }
        };
    }

    @Override
    public CancellationToken delete(@NonNull final String giniApiDocumentId,
            @NonNull final GiniVisionNetworkCallback<Result, Error> callback) {
        callback.success(new Result(giniApiDocumentId));
        return new CancellationToken() {
            @Override
            public void cancel() {
            }
        };
    }

    @Override
    public CancellationToken analyze(
            @NonNull final LinkedHashMap<String, Integer> giniApiDocumentIdRotationMap,
            @NonNull final GiniVisionNetworkCallback<AnalysisResult, Error> callback) {
        final String giniApiDocumentId = giniApiDocumentIdRotationMap.keySet().iterator().next();
        final Document document = mGiniDocuments.get(giniApiDocumentId);
        if (document == null) {
            callback.failure(new Error("No document to analyze."));
        }
        mSingleDocumentAnalyzer.cancelAnalysis();
        mSingleDocumentAnalyzer.analyzeDocument(document, new DocumentAnalyzer.Listener() {
            @Override
            public void onException(final Exception exception) {
                callback.failure(new Error(exception.getMessage()));
            }

            @Override
            public void onExtractionsReceived(final Map<String, SpecificExtraction> extractions) {
                final Map<String, GiniVisionSpecificExtraction> gvExtractions =
                        SpecificExtractionMapper.mapToGVL(extractions);
                callback.success(
                        new AnalysisResult(mSingleDocumentAnalyzer.getGiniApiDocument().getId(),
                                gvExtractions));
            }
        });

        return new CancellationToken() {
            @Override
            public void cancel() {
                mSingleDocumentAnalyzer.cancelAnalysis();
            }
        };
    }

    @Override
    public void cleanup() {
        mSingleDocumentAnalyzer.cancelAnalysis();
        mGiniDocuments.clear();
    }
}
