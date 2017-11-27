package net.gini.android.gvlexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import net.gini.android.gvlexample.info.InfoActivity;
import net.gini.android.gvlexample.results.ResultsActivity;
import net.gini.android.vision.GiniVisionError;
import net.gini.android.vision.camera.CameraActivity;
import net.gini.android.vision.noresults.NoResultsActivity;

/**
 * Created by Alpar Szotyori on 20.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class GVLExampleActivity extends AppCompatActivity implements GVLExampleContract.View {

    public static final String EXTRA_OUT_EXTRACTIONS = "EXTRA_OUT_EXTRACTIONS";
    private static final int REQUEST_GINI_VISION = 1;
    private GVLExamplePresenter mPresenter;

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GINI_VISION) {
            switch (resultCode) {
                case RESULT_OK:
                    final Bundle extractionsBundle = data.getBundleExtra(EXTRA_OUT_EXTRACTIONS);
                    mPresenter.onGVLResultsReceived(extractionsBundle);
                    break;
                case CameraActivity.RESULT_ERROR:
                    final GiniVisionError error =
                            data.getParcelableExtra(CameraActivity.EXTRA_OUT_ERROR);
                    mPresenter.onGVLErrorReceived(error);
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new GVLExamplePresenter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.infoItem) {
            mPresenter.presentInfos();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showGVL(final Intent gvlIntent) {
        startActivityForResult(gvlIntent, REQUEST_GINI_VISION);
    }

    @Override
    public void showResults(final Bundle extractions) {
        final Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra(ResultsActivity.EXTRA_IN_EXTRACTIONS, extractions);
        startActivity(intent);
    }

    @Override
    public void showNoResults() {
        final Intent intent = new Intent(this, NoResultsActivity.class);
        startActivity(intent);
    }

    @Override
    public void showInfos() {
        final Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }

    @Override
    public void showError(final String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    public void launchGiniVision(View view) {
        mPresenter.launchGVL();
    }
}
