package com.artzmb.hhkl.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesUtils {

    private final static String KEY_PREFERRED_LEAGUE = "pref_preferred_league";
    private final static String KEY_API_URL = "pref_api_url";

    public static int getPreferredLeague(Context context) {
        return Integer.parseInt(getSharedPreferences(context).getString(KEY_PREFERRED_LEAGUE, "1"));
    }

    public static void setPreferredLeague(Context context, int leagueLevel) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(KEY_PREFERRED_LEAGUE, String.valueOf(leagueLevel));
        editor.apply();
    }

    public static String getApiUrl(Context context) {
        return getSharedPreferences(context).getString(KEY_API_URL, Config.API_URL);
    }

    public static void setApiUrl(Context context, String apiUrl) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(KEY_API_URL, apiUrl);
        editor.apply();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.edit();
    }
}
