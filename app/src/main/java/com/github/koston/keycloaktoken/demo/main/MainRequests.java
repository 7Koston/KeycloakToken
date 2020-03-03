package com.github.koston.keycloaktoken.demo.main;

import com.github.koston.keycloaktoken.KeycloakToken;
import com.github.koston.keycloaktoken.KeycloakTokenAPI;
import com.google.gson.Gson;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;

public final class MainRequests {

  private final KeycloakTokenAPI api;

  public MainRequests(KeycloakTokenAPI api) {
    this.api = api;
  }

  Single<KeycloakToken> keycloakTokenRequest(final String responseCode) {
    return Single.fromCallable(
            () -> {
              ResponseBody body = api.requestGrantAccessToken(responseCode).execute().body();
              if (body != null) {
                return new Gson().fromJson(body.string(), KeycloakToken.class);
              }
              return null;
            })
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread());
  }

  Single<Boolean> keycloakLogoutRequest(final String refreshToken) {
    return Single.fromCallable(
            () -> {
              api.requestLogout(refreshToken).execute();
              return true;
            })
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread());
  }

  Single<KeycloakToken> keycloakTokenRefreshRequest(final String refreshToken) {
    return Single.fromCallable(
            () -> {
              ResponseBody body = api.requestRefreshAccessToken(refreshToken).execute().body();
              if (body != null) {
                return new Gson().fromJson(body.string(), KeycloakToken.class);
              }
              return null;
            })
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread());
  }
}
