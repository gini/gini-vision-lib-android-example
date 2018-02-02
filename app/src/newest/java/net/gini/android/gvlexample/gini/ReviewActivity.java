package net.gini.android.gvlexample.gini;

import android.content.Intent;
import android.support.annotation.NonNull;

import net.gini.android.vision.Document;

public class ReviewActivity extends net.gini.android.vision.review.ReviewActivity {

    @Override
    public void onAddDataToResult(@NonNull Intent result) {
    }

    @Override
    public void onShouldAnalyzeDocument(@NonNull Document document) {
    }

    @Override
    public void onDocumentWasRotated(@NonNull Document document, int oldRotation, int newRotation) {
        super.onDocumentWasRotated(document, oldRotation, newRotation);
    }

    @Override
    public void onProceedToAnalysisScreen(@NonNull Document document) {
        super.onProceedToAnalysisScreen(document);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

