package com.artzmb.hhkl;

import android.content.Context;
import android.content.Intent;

public class SettingsActivity extends BaseActivity {

    public static Intent createIntent(Context context) {
        Intent i = new Intent(context, SettingsActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    @Override
    protected int provideContentViewLayoutResourceId() {
        return R.layout.activity_settings;
    }
}
