package net.gini.android.gvlexample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import net.gini.android.DocumentMetadata;
import net.gini.android.gvlexample.configuration.ConfigurationManager;
import net.gini.android.gvlexample.gini.ExampleEventTracker;
import net.gini.android.vision.AsyncCallback;
import net.gini.android.vision.DocumentImportEnabledFileTypes;
import net.gini.android.vision.GiniVision;
import net.gini.android.vision.GiniVisionDebug;
import net.gini.android.vision.ImportedFileValidationException;
import net.gini.android.vision.accounting.network.GiniVisionAccountingNetworkApi;
import net.gini.android.vision.accounting.network.GiniVisionAccountingNetworkService;
import net.gini.android.vision.analysis.AnalysisActivity;
import net.gini.android.vision.camera.CameraActivity;
import net.gini.android.vision.network.GiniVisionDefaultNetworkApi;
import net.gini.android.vision.network.GiniVisionDefaultNetworkService;
import net.gini.android.vision.network.GiniVisionNetworkApi;
import net.gini.android.vision.network.GiniVisionNetworkService;
import net.gini.android.vision.requirements.GiniVisionRequirements;
import net.gini.android.vision.requirements.RequirementsReport;
import net.gini.android.vision.review.ReviewActivity;
import net.gini.android.vision.util.CancellationToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.android.LogcatAppender;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;

/**
 * Created by Alpar Szotyori on 22.02.2018.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class GVLExamplePresenter extends BaseGVLExamplePresenter {

    private GiniVisionNetworkApi mGiniVisionNetworkApi;
    private GiniVisionNetworkService mGiniVisionNetworkService;
    private CancellationToken mFileImportCancellationToken;

    GVLExamplePresenter(final GVLExampleContract.View view) {
        super(view);
    }

    @Override
    void stop() {
        if (mFileImportCancellationToken != null) {
            mFileImportCancellationToken.cancel();
            mFileImportCancellationToken = null;
        }
    }

    @Override
    protected void doLaunchGVL() {
        // NOTE: on Android 6.0 and later the camera permission is required before checking the requirements
        RequirementsReport report =
                GiniVisionRequirements.checkRequirements(getView().getContext());
        if (!report.isFulfilled()) {
            getView().showUnfulfilledRequirements(report);
            return;
        }

        final Context context = getView().getContext();

        Intent intent = new Intent(context, CameraActivity.class);

        getView().showGVL(intent);
    }

    @Override
    protected void initGiniVision() {
        final Context context = getView().getContext();
        GiniVision.cleanup(context);

        initNetworking();

        SharedPreferences configuration = ConfigurationManager.getConfigurationPreferences(context);
        final boolean showOnboardingOnFirstRun = configuration.getBoolean(
                context.getString(R.string.pref_key_gvl_show_onboarding_on_first_run), true);
        final boolean alwaysShowOnboarding = configuration.getBoolean(
                context.getString(R.string.pref_key_gvl_always_show_onboarding), true);
        final boolean enableQRCodeScanning = configuration.getBoolean(
                context.getString(R.string.pref_key_gvl_enable_qr_code_scanning), true);
        final boolean enableFileImport = configuration.getBoolean(
                context.getString(R.string.pref_key_gvl_enable_file_import), true);
        final String documentImportFileTypesIndex = configuration.getString(
                context.getString(R.string.pref_key_gvl_document_import_file_types), "0");
        final DocumentImportEnabledFileTypes documentImportFileTypes =
                DocumentImportEnabledFileTypes.values()[Integer.valueOf(
                        documentImportFileTypesIndex)];
        final boolean enableMultiPage = configuration.getBoolean(
                context.getString(R.string.pref_key_gvl_enable_multi_page), true);
        final boolean enableDebugging = configuration.getBoolean(
                context.getString(R.string.pref_key_gvl_enable_debugging), true);
        final boolean enableSupportedFormatsHelpScreen = configuration.getBoolean(
                context.getString(R.string.pref_key_gvl_supported_formats_help_screen), true);
        final boolean enableFlashToggle = configuration.getBoolean(
                context.getString(R.string.pref_key_gvl_enable_flash_toggle), true);
        final boolean flashOnByDefault = configuration.getBoolean(
                context.getString(R.string.pref_key_gvl_flash_on_by_default), true);
        final boolean enableBackButtons = configuration.getBoolean(
                context.getString(R.string.pref_key_gvl_enable_back_buttons), true);

        if (enableDebugging) {
            GiniVisionDebug.enable();
            enableGVLLoggin();
        } else {
            GiniVisionDebug.disable();
            disableGVLLogging();
        }

        GiniVision.newInstance()
                .setEventTracker(new ExampleEventTracker())
                .setGiniVisionNetworkService(mGiniVisionNetworkService)
                .setGiniVisionNetworkApi(mGiniVisionNetworkApi)
                .setShouldShowOnboardingAtFirstRun(showOnboardingOnFirstRun)
                .setShouldShowOnboarding(alwaysShowOnboarding)
                .setQRCodeScanningEnabled(enableQRCodeScanning)
                .setFileImportEnabled(enableFileImport)
                .setMultiPageEnabled(enableMultiPage)
                .setDocumentImportEnabledFileTypes(documentImportFileTypes)
                .setSupportedFormatsHelpScreenEnabled(enableSupportedFormatsHelpScreen)
                .setFlashButtonEnabled(enableFlashToggle)
                .setBackButtonsEnabled(enableBackButtons)
                .setFlashOnByDefault(flashOnByDefault)
                .build();
    }

    @NonNull
    private void initNetworking() {
        final Context context = getView().getContext();
        SharedPreferences configuration = ConfigurationManager.getConfigurationPreferences(context);
        final String clientId = configuration.getString(
                context.getString(R.string.pref_key_api_sdk_client_id), "");
        String clientSecret = configuration.getString(
                context.getString(R.string.pref_key_api_sdk_client_secret), "");
        clientSecret = TextUtils.isEmpty(clientSecret) ? context.getString(
                R.string.gini_api_client_secret) : clientSecret;
        final String emailDomain = configuration.getString(
                context.getString(R.string.pref_key_api_sdk_email_domain), "");
        final String apiBaseUrl = configuration.getString(
                context.getString(R.string.pref_key_api_sdk_gini_api_base_url), "");
        final String userCenterBaseUrl = configuration.getString(
                context.getString(R.string.pref_key_api_sdk_user_center_base_url), "");
        final String connectionTimeout = configuration.getString(
                context.getString(R.string.pref_key_api_sdk_connection_timeout), "60");
        final String nrOfRetries = configuration.getString(
                context.getString(R.string.pref_key_api_sdk_nr_of_retries), "3");
        final String backoffMultiplier = configuration.getString(
                context.getString(R.string.pref_key_api_sdk_backoff_multiplier), "1");
        final String networkLibrary = configuration.getString(
                context.getString(R.string.pref_key_gvl_network_library), "Default");

        final DocumentMetadata documentMetadata = new DocumentMetadata();
        documentMetadata.setBranchId("GVLShowcaseAndroid");
        documentMetadata.add("AppFlow", "ScreenAPI");

        if ("Accounting".equals(networkLibrary)) {
            buildAccountingNetworkLibrary(context, clientId, clientSecret, emailDomain, apiBaseUrl,
                    userCenterBaseUrl, connectionTimeout, nrOfRetries, backoffMultiplier,
                    documentMetadata);
        } else {
            buildDefaultNetworkLibrary(context, clientId, clientSecret, emailDomain, apiBaseUrl,
                    userCenterBaseUrl, connectionTimeout, nrOfRetries, backoffMultiplier,
                    documentMetadata);
        }
    }

    private void buildDefaultNetworkLibrary(final Context context, final String clientId,
            final String clientSecret, final String emailDomain, final String apiBaseUrl,
            final String userCenterBaseUrl, final String connectionTimeout,
            final String nrOfRetries, final String backoffMultiplier,
            final DocumentMetadata documentMetadata) {
        final GiniVisionDefaultNetworkService.Builder builder = GiniVisionDefaultNetworkService
                .builder(context);
        builder.setBaseUrl(apiBaseUrl)
                .setUserCenterBaseUrl(userCenterBaseUrl)
                .setClientCredentials(clientId, clientSecret, emailDomain)
                .setConnectionTimeout(Long.parseLong(connectionTimeout))
                .setConnectionTimeoutUnit(TimeUnit.MILLISECONDS)
                .setMaxNumberOfRetries(Integer.parseInt(nrOfRetries))
                .setBackoffMultiplier(Float.parseFloat(backoffMultiplier))
                .setDocumentMetadata(documentMetadata)
                .setNetworkSecurityConfigResId(R.xml.network_security_config);

        mGiniVisionNetworkService = builder.build();

        mGiniVisionNetworkApi = GiniVisionDefaultNetworkApi
                .builder()
                .withGiniVisionDefaultNetworkService(
                        (GiniVisionDefaultNetworkService) mGiniVisionNetworkService)
                .build();
    }

    private void buildAccountingNetworkLibrary(final Context context, final String clientId,
            final String clientSecret, final String emailDomain, final String apiBaseUrl,
            final String userCenterBaseUrl, final String connectionTimeout,
            final String nrOfRetries, final String backoffMultiplier,
            final DocumentMetadata documentMetadata) {
        final GiniVisionAccountingNetworkService.Builder builder = GiniVisionAccountingNetworkService
                .builder(context);
        builder.setBaseUrl(apiBaseUrl)
                .setUserCenterBaseUrl(userCenterBaseUrl)
                .setClientCredentials(clientId, clientSecret, emailDomain)
                .setConnectionTimeout(Long.parseLong(connectionTimeout))
                .setConnectionTimeoutUnit(TimeUnit.MILLISECONDS)
                .setMaxNumberOfRetries(Integer.parseInt(nrOfRetries))
                .setBackoffMultiplier(Float.parseFloat(backoffMultiplier))
                .setDocumentMetadata(documentMetadata)
                .setNetworkSecurityConfigResId(R.xml.network_security_config);

        mGiniVisionNetworkService = builder.build();

        mGiniVisionNetworkApi = GiniVisionAccountingNetworkApi
                .builder()
                .withGiniVisionAccountingNetworkService(
                        (GiniVisionAccountingNetworkService) mGiniVisionNetworkService)
                .build();
    }

    @Override
    void doLaunchGVLForIntent(@NonNull final Intent intent) {
        final Context context = getView().getContext();
        if (GiniVision.hasInstance() && GiniVision.getInstance().isMultiPageEnabled()) {
            mFileImportCancellationToken = GiniVision.getInstance().createIntentForImportedFiles(
                    intent, context,
                    new AsyncCallback<Intent, ImportedFileValidationException>() {
                        @Override
                        public void onSuccess(final Intent result) {
                            mFileImportCancellationToken = null;
                            getView().showGVL(result);
                            setGVLLaunchedForImportedFile(true);
                        }

                        @Override
                        public void onError(final ImportedFileValidationException exception) {
                            mFileImportCancellationToken = null;
                            String errorMessage = context.getString(
                                    R.string.imported_file_cannot_analyze_error);
                            if (exception.getValidationError() != null) {
                                switch (exception.getValidationError()) {
                                    case TYPE_NOT_SUPPORTED:
                                        errorMessage =
                                                context.getString(
                                                        R.string.imported_file_type_not_supported_error);
                                        break;
                                    case SIZE_TOO_LARGE:
                                        errorMessage = context.getString(
                                                R.string.imported_file_too_large_error);
                                        break;
                                    case TOO_MANY_PDF_PAGES:
                                        errorMessage =
                                                context.getString(
                                                        R.string.imported_file_pdf_too_many_pages_error);
                                        break;
                                }
                            }
                            getView().showImportedFileError(errorMessage);
                        }

                        @Override
                        public void onCancelled() {
                            mFileImportCancellationToken = null;
                        }
                    });
        } else {
            try {
                final Intent giniVisionIntent =
                        GiniVision.createIntentForImportedFile(intent,
                                context,
                                ReviewActivity.class,
                                AnalysisActivity.class);
                getView().showGVL(giniVisionIntent);
                setGVLLaunchedForImportedFile(true);
            } catch (ImportedFileValidationException e) {
                e.printStackTrace();
                String errorMessage = context.getString(
                        R.string.imported_file_cannot_analyze_error);
                if (e.getValidationError() != null) {
                    switch (e.getValidationError()) {
                        case TYPE_NOT_SUPPORTED:
                            errorMessage =
                                    context.getString(
                                            R.string.imported_file_type_not_supported_error);
                            break;
                        case SIZE_TOO_LARGE:
                            errorMessage = context.getString(
                                    R.string.imported_file_too_large_error);
                            break;
                        case TOO_MANY_PDF_PAGES:
                            errorMessage =
                                    context.getString(
                                            R.string.imported_file_pdf_too_many_pages_error);
                            break;
                    }
                }
                getView().showImportedFileError(errorMessage);
            }
        }
    }

    private void enableGVLLoggin() {
        final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.reset();

        final PatternLayoutEncoder layoutEncoder = new PatternLayoutEncoder();
        layoutEncoder.setContext(lc);
        layoutEncoder.setPattern("%-5level %file:%line [%thread] - %msg%n");
        layoutEncoder.start();

        final LogcatAppender logcatAppender = new LogcatAppender();
        logcatAppender.setContext(lc);
        logcatAppender.setEncoder(layoutEncoder);
        logcatAppender.start();

        final ch.qos.logback.classic.Logger root =
                (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.addAppender(logcatAppender);
    }

    private void disableGVLLogging() {
        final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.reset();
    }
}
