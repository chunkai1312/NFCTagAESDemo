package edu.ntust.cs.idsl.nfctagaesdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SettingsManager {

    private static SettingsManager instance;

    private Context context;
    private SharedPreferences pref;
    private Editor editor;

    public static final String PREF_NAME = "SettingsPref";
    public static final String KEY = "key";

    private SettingsManager(Context context) {
        this.context = context;
        pref = this.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static synchronized SettingsManager getInstance(Context context) {
        if (instance == null) {
            instance = new SettingsManager(context);
        }
        return instance;
    }

    public void setKey(String value) {
        editor.putString(KEY, value);
        editor.commit();
    }

    public String getKey() {
        return pref.getString(KEY, "");
    }

}
