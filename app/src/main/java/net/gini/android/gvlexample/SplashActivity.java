package net.gini.android.gvlexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Alpar Szotyori on 21.11.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = new Intent(this, GVLExampleActivity.class);
        startActivity(intent);
        finish();
    }
}
