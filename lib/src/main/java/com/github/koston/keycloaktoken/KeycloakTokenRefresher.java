package com.github.koston.keycloaktoken;

import com.google.gson.Gson;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.Calendar;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class KeycloakTokenRefresher {

  private CompositeDisposable compositeDisposable;

  private OkHttpClient okHttpClient;
  private KeycloakTokenRefreshListener tokenRefreshListener;

  private Config config;

  public KeycloakTokenRefresher(
      OkHttpClient client, Config config, KeycloakTokenRefreshListener refreshListener) {
    this.config = config;
    okHttpClient = client;
    tokenRefreshListener = refreshListener;
    compositeDisposable = new CompositeDisposable();
  }

  private void addSubscription(Disposable subscription) {
    compositeDisposable.add(subscription);
  }

  private void disposeAll() {
    if (!compositeDisposable.isDisposed()) {
      compositeDisposable.clear();
    }
  }

  public void onDestroy() {
    okHttpClient = null;
    disposeAll();
    compositeDisposable = null;
    tokenRefreshListener = null;
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
                      tokenRefreshListener.OnTokenRefreshed(token);
                    }
                  }

                  @Override
                  public void onError(Throwable e) {
                    e.printStackTrace();
                    if (tokenRefreshListener != null) {
                      tokenRefreshListener.OnTokenRefreshError();
                    }
                  }
                }));
  }
}
