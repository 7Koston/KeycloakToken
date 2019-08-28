package com.github.koston.keycloaktoken;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import okhttp3.Response;
import okhttp3.ResponseBody;

public class KeycloakTokenExchanger {

  private CompositeDisposable compositeDisposable;

  private OkHttpClient okHttpClient;
  private KeycloakLoginListener loginListener;
  private KeycloakLogoutListener logoutListener;

  private Config config;

  public KeycloakTokenExchanger(
      OkHttpClient client,
      Config config,
      KeycloakLoginListener loginListener,
      KeycloakLogoutListener logoutListener) {
    this.config = config;
    this.loginListener = loginListener;
    this.logoutListener = logoutListener;
    okHttpClient = client;
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

                        loginListener.OnLoggedIn(token);
                      }

                      @Override
                      public void onError(Throwable e) {
                        e.printStackTrace();
                        loginListener.OnLoginError();
                      }
                    }));
      } else {
        loginListener.OnLoginError();
      }
    } else {
      loginListener.OnLoginError();
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
                      logoutListener.OnLoggedOut();
                    } else {
                      onError(new NullPointerException());
                    }
                  }

                  @Override
                  public void onError(Throwable e) {
                    e.printStackTrace();
                    logoutListener.OnLogoutError();
                  }
                }));
  }
}
