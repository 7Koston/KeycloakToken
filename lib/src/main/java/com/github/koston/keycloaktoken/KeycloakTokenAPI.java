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

package com.github.koston.keycloaktoken;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class KeycloakTokenAPI {

  private final @NonNull Config config;
  private final @NonNull OkHttpClient okHttpClient;

  public KeycloakTokenAPI(@NonNull Config config, @NonNull OkHttpClient okHttpClient) {
    this.config = config;
    this.okHttpClient = okHttpClient;
  }

  public @NonNull Call requestGrantAccessToken(@NonNull String responseCode) {
    return okHttpClient.newCall(
        new Request.Builder()
            .url(config.getBaseUrl() + "/token")
            .post(
                new FormBody.Builder()
                    .add("code", responseCode)
                    .add("client_id", config.getClientId())
                    .add("redirect_uri", config.getRedirectUri())
                    .add("grant_type", "authorization_code")
                    .build())
            .build());
  }

  public @NonNull Call requestLogout(@NonNull String refreshToken) {
    return okHttpClient.newCall(
        new Request.Builder()
            .url(config.getBaseUrl() + "/logout")
            .post(
                new FormBody.Builder()
                    .add("client_id", config.getClientId())
                    .add("refresh_token", refreshToken)
                    .build())
            .build());
  }

  public @NonNull Call requestRefreshAccessToken(@NonNull String refreshToken) {
    return okHttpClient.newCall(
        new Request.Builder()
            .url(config.getBaseUrl() + "/token")
            .post(
                new FormBody.Builder()
                    .add("refresh_token", refreshToken)
                    .add("client_id", config.getClientId())
                    .add("grant_type", "refresh_token")
                    .build())
            .build());
  }
}
