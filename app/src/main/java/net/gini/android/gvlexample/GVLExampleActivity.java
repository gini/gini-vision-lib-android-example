package net.gini.android.gvlexample;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import net.gini.android.gvlexample.info.InfoActivity;
import net.gini.android.gvlexample.results.ResultsActivity;
import net.gini.android.vision.GiniVisionError;
import net.gini.android.vision.camera.CameraActivity;

/**
 * Created by Alpar Szotyori on 20.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class GVLExampleActivity extends AppCompatActivity implements GVLExampleContract.View {

    public static final String EXTRA_OUT_EXTRACTIONS = "EXTRA_OUT_EXTRACTIONS";
    private static final int REQUEST_GINI_VISION = 1;
    private GVLExamplePresenter mPresenter;
    private boolean mRestoredInstance;

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
                    Bundle extractionsBundle = null;
                    if (data != null) {
                        extractionsBundle = data.getBundleExtra(EXTRA_OUT_EXTRACTIONS);
                    }
                    mPresenter.onGVLResultsReceived(extractionsBundle);
                    break;
                case RESULT_CANCELED:
                    mPresenter.onGVLWasCanceled();
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
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        if (isIntentActionViewOrSend(intent)) {
            mPresenter.launchGVLForImportedFile(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
        if (!mRestoredInstance) {
            final Intent intent = getIntent();
            if (isIntentActionViewOrSend(intent)) {
                mPresenter.launchGVLForImportedFile(intent);
            }
        }
    }

    private boolean isIntentActionViewOrSend(final Intent intent) {
        String action = intent.getAction();
        return Intent.ACTION_VIEW.equals(action) || Intent.ACTION_SEND.equals(action);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new GVLExamplePresenter(this);
        mRestoredInstance = savedInstanceState != null;
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
    public void showNoPdfResults() {
        final Intent intent = new Intent(this, NoPdfResultsActivity.class);
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

    @Override
    public void showImportedFileError(final String errorMessage) {
        new AlertDialog.Builder(this).setMessage(errorMessage).setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {
                        mPresenter.onImportedFileErrorAcknowledged();
                    }
                }).show();
    }

    public void launchGiniVision(View view) {
        mPresenter.launchGVL();
    }
}
