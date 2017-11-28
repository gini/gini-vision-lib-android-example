package net.gini.android.gvlexample;

import static net.gini.android.gvlexample.ActivityHelper.forcePortraitOrientationOnPhones;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NoPdfResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forcePortraitOrientationOnPhones(this);
        setContentView(R.layout.activity_no_pdf_results);
    }
}
