package com.github.koston.keycloaktoken;

import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.Gson;

public final class SharedPreferencesOAuth2Storage implements IOAuth2AccessTokenStorage {

  @NonNull
  private final String ACCESS_TOKEN_PREFERENCES_KEY;
  @NonNull
  private final SharedPreferences prefs;
  @NonNull
  private final Gson gson;

  public SharedPreferencesOAuth2Storage(@NonNull SharedPreferences prefs, @NonNull Gson gson) {
    super();
    this.prefs = prefs;
    this.gson = gson;
    this.ACCESS_TOKEN_PREFERENCES_KEY = "OAuth2AccessToken";
  }

  @NonNull
  public final String getACCESS_TOKEN_PREFERENCES_KEY() {
    return this.ACCESS_TOKEN_PREFERENCES_KEY;
  }

  @Nullable
  public KeycloakToken getStoredAccessToken() {
    String tokenStr = this.prefs.getString(this.ACCESS_TOKEN_PREFERENCES_KEY, null);
    return tokenStr == null ? null : this.gson.fromJson(tokenStr, KeycloakToken.class);
  }

  public void storeAccessToken(@NonNull KeycloakToken token) {
    this.prefs.edit().putString(this.ACCESS_TOKEN_PREFERENCES_KEY, this.gson.toJson(token)).apply();
  }

  public boolean hasAccessToken() {
    return this.prefs.contains(this.ACCESS_TOKEN_PREFERENCES_KEY);
  }

  public void removeAccessToken() {
    this.prefs.edit().remove(this.ACCESS_TOKEN_PREFERENCES_KEY).apply();
  }

  @NonNull
  public final SharedPreferences getPrefs() {
    return this.prefs;
  }

  @NonNull
  public final Gson getGson() {
    return this.gson;
  }
}