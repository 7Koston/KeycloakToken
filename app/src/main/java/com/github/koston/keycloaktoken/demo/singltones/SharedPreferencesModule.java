/*
 * Copyright 2019 7Koston
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.koston.keycloaktoken.demo.singltones;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesModule {

  private static SharedPreferencesModule mInstance;

  private final SharedPreferences mPreferences;

  private SharedPreferencesModule(Context context) {
    mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
  }

  public static void initialize(Context context) {
    if (mInstance == null) {
      mInstance = new SharedPreferencesModule(context);
    }
  }

  public static SharedPreferencesModule get() {
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
