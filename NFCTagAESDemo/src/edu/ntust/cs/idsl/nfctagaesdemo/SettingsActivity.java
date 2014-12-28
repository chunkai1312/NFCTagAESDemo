package edu.ntust.cs.idsl.nfctagaesdemo;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SettingsActivity extends PreferenceActivity implements OnPreferenceClickListener {

    private static final String ACTION = "edu.ntust.cs.idsl.nfctagaesdemo.action.SETTINGS";
    private MyApplication app;
    private Preference prefKey;
    private String key;
    private static final String KEY = "key";

    public static void startAction(Context context) {
        context.startActivity(getAction(context));
    }

    public static Intent getAction(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        intent.setAction(ACTION);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        app = (MyApplication) getApplicationContext();
        setPrefKey();
    }

    private void setPrefKey() {
        prefKey = (Preference) findPreference(KEY);
        key = app.getSettings().getKey();
        if (key.isEmpty()) {
            prefKey.setSummary(R.string.empty);
        } else {
            prefKey.setSummary(key);
        }
        prefKey.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            return true;
        case R.id.action_save:
            saveSettings();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (prefKey == preference) {
            showGeneratingKeyDialog();
        }
        return true;
    }

    private void showGeneratingKeyDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.generate_key)
                .setPositiveButton(android.R.string.ok, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            byte[] newKey = AESCoder.generateKey();
                            key = AESCoder.base64(newKey);
                            prefKey.setSummary(key);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel,
                        new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create().show();
    }

    private void saveSettings() {
        app.getSettings().setKey(key);
        ToastMaker.toast(this, R.string.your_settings_have_been_saved);
    }

}
