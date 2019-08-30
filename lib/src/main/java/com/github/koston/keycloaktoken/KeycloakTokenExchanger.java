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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.google.gson.Gson;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.Calendar;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class KeycloakTokenExchanger extends DisposableContainer {

  private OkHttpClient okHttpClient;
  private KeycloakLoginListener loginListener;
  private KeycloakLogoutListener logoutListener;

  private final Config config;

  public KeycloakTokenExchanger(
      OkHttpClient client,
      Config config,
      KeycloakLoginListener loginListener,
      KeycloakLogoutListener logoutListener) {
    this.config = config;
    this.loginListener = loginListener;
    this.logoutListener = logoutListener;
    okHttpClient = client;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    okHttpClient = null;
    loginListener = null;
    logoutListener = null;
  }

  private Single<KeycloakToken> grantNewAccessToken(String code) {
    return Single.fromCallable(
        () -> {
          ResponseBody body =
              okHttpClient
                  .newCall(
                      new Request.Builder()
                          .url(config.getBaseUrl() + "/token")
                          .post(
                              new FormBody.Builder()
                                  .add("code", code)
                                  .add("client_id", config.getClientId())
                                  .add("redirect_uri", config.getRedirectUri())
                                  .add("grant_type", "authorization_code")
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

  private Single<Response> doLogout(String refreshToken) {
    return Single.fromCallable(
        () ->
            okHttpClient
                .newCall(
                    new Request.Builder()
                        .url(config.getBaseUrl() + "/logout")
                        .post(
                            new FormBody.Builder()
                                .add("client_id", config.getClientId())
                                .add("refresh_token", refreshToken)
                                .build())
                        .build())
                .execute())
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread());
  }

  public void openBrowserForLogin(Context context) {
    context.startActivity(
        new Intent(
            Intent.ACTION_VIEW,
            Uri.parse(config.getAuthenticationCodeUrl())
                .buildUpon()
                .appendQueryParameter("client_id", config.getClientId())
                .appendQueryParameter("redirect_uri", config.getRedirectUri())
                .appendQueryParameter("response_type", "code")
                .build()));
  }

  public void exchangeCodeForToken(Uri intentData) {
    if (intentData != null && intentData.toString().startsWith(config.getRedirectUri())) {
      String code = intentData.getQueryParameter("code");
      if (code != null && !code.isEmpty()) {
        addSubscription(
            grantNewAccessToken(code)
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

                        if (loginListener != null) {
                          loginListener.onLoggedIn(token);
                        }
                      }

                      @Override
                      public void onError(Throwable e) {
                        e.printStackTrace();
                        if (loginListener != null) {
                          loginListener.onLoginError();
                        }
                      }
                    }));
      } else {
        if (loginListener != null) {
          loginListener.onLoginError();
        }
      }
    } else {

      if (loginListener != null) {
        loginListener.onLoginError();
      }
    }
  }

  public void logout(String refreshToken) {
    addSubscription(
        doLogout(refreshToken)
            .subscribeWith(
                new DisposableSingleObserver<Response>() {
                  @Override
                  public void onSuccess(Response response) {
                    if (response != null) {
                      if (logoutListener != null) {
                        logoutListener.onLoggedOut();
                      }
                    } else {
                      onError(new NullPointerException());
                    }
                  }

                  @Override
                  public void onError(Throwable e) {
                    e.printStackTrace();
                    if (logoutListener != null) {
                      logoutListener.onLogoutError();
                    }
                  }
                }));
  }
}
