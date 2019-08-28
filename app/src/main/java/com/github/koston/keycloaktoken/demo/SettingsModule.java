package com.github.koston.keycloaktoken.demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingsModule {

  private static SettingsModule mInstance;

  private final SharedPreferences mPreferences;

  private SettingsModule(Context context) {
    mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
  }

  public static void initialize(Context context) {
    if (mInstance == null) {
      mInstance = new SettingsModule(context);
    }
  }

  public static SettingsModule get() {
    return mInstance;
  }

  public String getRefreshToken() {
    return mPreferences.getString("refresh_token", "");
  }

  public String getAccessToken() {
    return mPreferences.getString("access_token", "");
  }


  public void setAccessToken(String token) {
    mPreferences.edit().putString("access_token", token).apply();
  }

  public void setRefreshTOken(String token) {
    mPreferences.edit().putString("refresh_token", token).apply();
  }
}

