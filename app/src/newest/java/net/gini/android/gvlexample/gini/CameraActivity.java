package net.gini.android.gvlexample.gini;

import android.support.annotation.NonNull;

import net.gini.android.vision.document.QRCodeDocument;

/**
 * Created by Alpar Szotyori on 14.12.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class CameraActivity extends net.gini.android.vision.camera.CameraActivity {

    @Override
    public void onQRCodeAvailable(@NonNull final QRCodeDocument qrCodeDocument) {

    }

}
