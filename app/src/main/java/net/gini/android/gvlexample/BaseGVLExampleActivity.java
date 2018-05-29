package net.gini.android.gvlexample;

import static net.gini.android.gvlexample.ActivityHelper.forcePortraitOrientationOnPhones;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.single.PermissionListener;

import net.gini.android.gvlexample.info.InfoActivity;
import net.gini.android.gvlexample.results.ResultsActivity;
import net.gini.android.vision.GiniVisionError;
import net.gini.android.vision.camera.CameraActivity;
import net.gini.android.vision.requirements.RequirementReport;
import net.gini.android.vision.requirements.RequirementsReport;

import java.util.List;

/**
 * Created by Alpar Szotyori on 20.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public abstract class BaseGVLExampleActivity extends AppCompatActivity implements GVLExampleContract.View {

    private static final int REQUEST_GINI_VISION = 1;
    private Hockeyapp mHockeyapp;
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
                        extractionsBundle = getExtractions(data);
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

    abstract Bundle getExtractions(final Intent data);

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        if (isIntentActionViewOrSend(intent)) {
            mPresenter.launchGVLForIntent(intent);
        }
    }

    boolean isIntentActionViewOrSend(final Intent intent) {
        String action = intent.getAction();
        return Intent.ACTION_VIEW.equals(action) || Intent.ACTION_SEND.equals(action);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forcePortraitOrientationOnPhones(this);
        setContentView(R.layout.activity_main);
        mPresenter = new GVLExamplePresenter(this);
        mRestoredInstance = savedInstanceState != null;

        final TextView appVersion = findViewById(R.id.appVersion);
        appVersion.setText(BuildConfig.VERSION_NAME);

        getLifecycle().addObserver(new Hockeyapp(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.start();
        if (!mRestoredInstance) {
            final Intent intent = getIntent();
            if (isIntentActionViewOrSend(intent)) {
                mPresenter.launchGVLForIntent(intent);
            }
        }
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
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.stop();
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
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage(errorMessage)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    final DialogInterface dialog,
                                    final int which) {
                                mPresenter.onImportedFileErrorAcknowledged();
                            }
                        })
                .create();
        alertDialog.show();
    }

    @Override
    public void showUnfulfilledRequirements(final RequirementsReport report) {
        StringBuilder stringBuilder = new StringBuilder();
        List<RequirementReport> requirementReports = report.getRequirementReports();
        for (int i = 0; i < requirementReports.size(); i++) {
            RequirementReport requirementReport = requirementReports.get(i);
            if (!requirementReport.isFulfilled()) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append("\n");
                }
                stringBuilder.append(requirementReport.getRequirementId());
                if (!requirementReport.getDetails().isEmpty()) {
                    stringBuilder.append(": ");
                    stringBuilder.append(requirementReport.getDetails());
                }
            }
        }
        new AlertDialog.Builder(this)
                .setMessage("Requirements not fulfilled:\n" + stringBuilder)
                .setPositiveButton("OK", null)
                .show();
    }

    @Override
    public void requestCameraPermission(final PermissionRequestListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            listener.permissionGranted();
            return;
        }
        Dexter.withActivity(this)
              .withPermission(Manifest.permission.CAMERA)
              .withListener(new PermissionListener() {
                  @Override
                  public void onPermissionGranted(final PermissionGrantedResponse response) {
                      listener.permissionGranted();
                  }

                  @Override
                  public void onPermissionDenied(final PermissionDeniedResponse response) {
                      showCameraPermissionDeniedDialog(listener);
                  }

                  @Override
                  public void onPermissionRationaleShouldBeShown(final PermissionRequest permission,
                          final PermissionToken token) {
                      showCameraPermissionRationale(token);
                  }
              })
              .withErrorListener(new PermissionRequestErrorListener() {
                  @Override
                  public void onError(final DexterError error) {
                      Log.e("dexter", error.name());
                  }
              })
              .check();
    }

    @SuppressLint("InlinedApi")
    @Override
    public void requestStoragePermission(final PermissionRequestListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            listener.permissionGranted();
            return;
        }
        Dexter.withActivity(this)
              .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
              .withListener(new PermissionListener() {
                  @Override
                  public void onPermissionGranted(final PermissionGrantedResponse response) {
                      listener.permissionGranted();
                  }

                  @Override
                  public void onPermissionDenied(final PermissionDeniedResponse response) {
                      showStoragePermissionDeniedDialog(listener);
                  }

                  @Override
                  public void onPermissionRationaleShouldBeShown(final PermissionRequest permission,
                          final PermissionToken token) {
                      showStoragePermissionRationale(token);
                  }
              })
              .check();
    }

    private void showStoragePermissionDeniedDialog(final PermissionRequestListener listener) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.storage_permission_denied_message)
                .setPositiveButton(getString(R.string.grant_access),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int which) {
                                showAppDetailsSettingsScreen();
                            }
                        })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        listener.permissionDenied();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(final DialogInterface dialog) {
                        listener.permissionDenied();
                    }
                })
                .create();
        alertDialog.show();
    }

    private void showStoragePermissionRationale(final PermissionToken token) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.storage_permission_rationale)
                .setPositiveButton(getString(R.string.grant_access),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int which) {
                                token.continuePermissionRequest();
                            }
                        })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(final DialogInterface dialog) {
                        token.cancelPermissionRequest();
                    }
                })
                .create();
        alertDialog.show();
    }

    private void showCameraPermissionDeniedDialog(final PermissionRequestListener listener) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage(
                        R.string.camera_permission_denied_message)
                .setPositiveButton(
                        getString(R.string.grant_access), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int which) {
                                showAppDetailsSettingsScreen();
                            }
                        })
                .setNegativeButton(getString(R.string.cancel), null)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(final DialogInterface dialog) {
                        listener.permissionDenied();
                    }
                })
                .create();
        alertDialog.show();
    }

    private void showAppDetailsSettingsScreen() {
        final Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        final Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void showCameraPermissionRationale(final PermissionToken token) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.camera_permission_rationale)
                .setPositiveButton(getString(R.string.grant_access),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int which) {
                                token.continuePermissionRequest();
                            }
                        })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(final DialogInterface dialog) {
                        token.cancelPermissionRequest();
                    }
                })
                .create();
        alertDialog.show();
    }

    public void launchGiniVision(View view) {
        mPresenter.launchGVL();
    }
}
