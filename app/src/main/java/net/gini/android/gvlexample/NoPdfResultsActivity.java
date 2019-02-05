package net.gini.android.gvlexample;

import static net.gini.android.gvlexample.ActivityHelper.forcePortraitOrientationOnPhones;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class NoPdfResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forcePortraitOrientationOnPhones(this);
        setContentView(R.layout.activity_no_pdf_results);
    }
}
