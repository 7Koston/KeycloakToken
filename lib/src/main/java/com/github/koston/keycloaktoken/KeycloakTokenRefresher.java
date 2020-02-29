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

import com.google.gson.Gson;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.Calendar;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class KeycloakTokenRefresher extends DisposableContainer {

  private final Config config;
  private OkHttpClient okHttpClient;
  private KeycloakTokenRefreshListener tokenRefreshListener;

  public KeycloakTokenRefresher(
      OkHttpClient client, Config config, KeycloakTokenRefreshListener refreshListener) {
    this.config = config;
    okHttpClient = client;
    tokenRefreshListener = refreshListener;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    okHttpClient = null;
    tokenRefreshListener = null;
  }

  public void refreshAccessToken(String refreshToken) {
    addSubscription(
        doRefreshAccessToken(refreshToken)
            .subscribeWith(
                new DisposableSingleObserver<KeycloakToken>() {
                  @Override
                  public void onSuccess(KeycloakToken token) {
                    if (token == null) {
                      onError(new NullPointerException("KeycloakToken is NULL"));
                      return;
                    }

                    Calendar expirationDate = Calendar.getInstance();
                    Calendar refreshExpirationDate = Calendar.getInstance();
                    Integer tokenExpiresIn = token.getExpiresIn();
                    Integer refreshExpiresIn = token.getRefreshExpiresIn();

                    if (tokenExpiresIn != null) {
                      expirationDate.add(java.util.Calendar.SECOND, tokenExpiresIn);
                    }
                    if (refreshExpiresIn != null) {
                      refreshExpirationDate.add(java.util.Calendar.SECOND, refreshExpiresIn);
                    }

                    token.setTokenExpirationDate(expirationDate);
                    token.setRefreshTokenExpirationDate(refreshExpirationDate);

                    if (tokenRefreshListener != null) {
                      tokenRefreshListener.onTokenRefreshed(token);
                    }
                  }

                  @Override
                  public void onError(Throwable e) {
                    e.printStackTrace();
                    if (tokenRefreshListener != null) {
                      tokenRefreshListener.onTokenRefreshError();
                    }
                  }
                }));
  }

  private Single<KeycloakToken> doRefreshAccessToken(String refreshToken) {
    return Single.fromCallable(
        () -> {
          ResponseBody body =
              okHttpClient
                  .newCall(
                      new Request.Builder()
                          .url(config.getBaseUrl() + "/token")
                          .post(
                              new FormBody.Builder()
                                  .add("refresh_token", refreshToken)
                                  .add("client_id", config.getClientId())
                                  .add("grant_type", "refresh_token")
                                  .build())
                          .build())
                  .execute()
                  .body();
          if (body != null) {
            return new Gson().fromJson(body.string(), KeycloakToken.class);
          }
          return null;
        })
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread());
  }
}
