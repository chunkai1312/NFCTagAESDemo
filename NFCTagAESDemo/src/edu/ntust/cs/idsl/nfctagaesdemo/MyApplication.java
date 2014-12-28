package edu.ntust.cs.idsl.nfctagaesdemo;

import android.app.Application;

public class MyApplication extends Application {

    private SettingsManager settings;

    @Override
    public void onCreate() {
        super.onCreate();
        settings = SettingsManager.getInstance(getApplicationContext());
    }

    public SettingsManager getSettings() {
        return settings;
    }

}
