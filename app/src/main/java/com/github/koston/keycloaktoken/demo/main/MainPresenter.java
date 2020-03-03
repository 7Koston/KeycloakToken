package com.github.koston.keycloaktoken.demo.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.github.koston.keycloaktoken.Config;
import com.github.koston.keycloaktoken.KeycloakToken;
import com.github.koston.keycloaktoken.KeycloakTokenAPI;
import com.github.koston.keycloaktoken.demo.basic.BasicPresenter;
import com.github.koston.keycloaktoken.demo.singltones.OkHttpModule;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import java.util.Date;

public class MainPresenter extends BasicPresenter {

  private static final String BUNDLE_MAIN_MODEL = "BUNDLE_MAIN_MODEL";

  private final MainView view;
  private final Config config;
  private final MainRequests requests;

  private MainModel model;

  public MainPresenter(MainView view) {
    this.view = view;
    config =
        new Config(
            "https",
            "://auth.novel.tl/auth/realms/site/protocol/openid-connect",
            "://auth.novel.tl/auth/realms/site/protocol/openid-connect/auth",
            "android-app",
            "app://android.novel.tl");
    requests = new MainRequests(new KeycloakTokenAPI(config, OkHttpModule.getHttpClient()));
  }

  public void onCreate(Bundle savedState) {
    String accessToken = view.onGetAccessToken();
    String refreshToken = view.onGetRefreshToken();
    if (savedState == null && accessToken != null && !accessToken.isEmpty())
      model = new MainModel(accessToken, refreshToken);
    if (savedState != null) model = savedState.getParcelable(BUNDLE_MAIN_MODEL);
    if (model != null) view.onShowMainModel(model);
  }

  public void onSaveInstanceState(Bundle outState) {
    if (model != null) outState.putParcelable("model", model);
  }

  public void exchangeCodeForToken(Uri intentData) {
    if (intentData == null || !intentData.toString().startsWith(config.getRedirectUri())) {
      view.onKeycloakTokenGainingError();
      return;
    }

    String code = intentData.getQueryParameter("code");
    if (code == null || code.isEmpty()) {
      view.onKeycloakTokenGainingError();
      return;
    }

    addSubscription(
        requests
            .keycloakTokenRequest(code)
            .subscribeWith(
                new DisposableSingleObserver<KeycloakToken>() {
                  @Override
                  public void onSuccess(KeycloakToken token) {
                    if (token == null) {
                      onError(new NullPointerException("KeycloakToken is NULL"));
                      return;
                    }

                    Date expirationDate = new Date();
                    Date refreshExpirationDate = new Date();
                    Integer tokenExpiresIn = token.getExpiresIn();
                    Integer refreshExpiresIn = token.getRefreshExpiresIn();

                    if (tokenExpiresIn != null) {
                      expirationDate = new Date(expirationDate.getTime() + (tokenExpiresIn * 1000));
                    }
                    if (refreshExpiresIn != null) {
                      refreshExpirationDate =
                          new Date(refreshExpirationDate.getTime() + (refreshExpiresIn * 1000));
                    }

                    token.setTokenExpirationDate(expirationDate);
                    token.setRefreshTokenExpirationDate(refreshExpirationDate);

                    initModel(token);

                    view.onKeycloakTokenGained(token);
                    view.onShowMainModel(model);
                  }

                  @Override
                  public void onError(Throwable e) {
                    e.printStackTrace();
                    view.onKeycloakTokenGainingError();
                  }
                }));
  }

  private void initModel(KeycloakToken token) {
    model = new MainModel(token);
  }

  public void logout(String refreshToken) {
    addSubscription(
        requests
            .keycloakLogoutRequest(refreshToken)
            .subscribeWith(
                new DisposableSingleObserver<Boolean>() {
                  @Override
                  public void onSuccess(Boolean response) {
                    if (response != null && response) {
                      view.onKeycloakLoggedOut();
                    } else {
                      view.onKeycloakTokenLoggingOutError();
                    }
                  }

                  @Override
                  public void onError(Throwable e) {
                    e.printStackTrace();
                    view.onKeycloakTokenLoggingOutError();
                  }
                }));
  }

  public void refreshAccessToken(String refreshToken) {
    addSubscription(
        requests
            .keycloakTokenRefreshRequest(refreshToken)
            .subscribeWith(
                new DisposableSingleObserver<KeycloakToken>() {
                  @Override
                  public void onSuccess(KeycloakToken token) {
                    if (token == null) {
                      onError(new NullPointerException("KeycloakToken is NULL"));
                      return;
                    }

                    Date expirationDate = new Date();
                    Date refreshExpirationDate = new Date();
                    Integer tokenExpiresIn = token.getExpiresIn();
                    Integer refreshExpiresIn = token.getRefreshExpiresIn();

                    if (tokenExpiresIn != null) {
                      expirationDate = new Date(expirationDate.getTime() + (tokenExpiresIn * 1000));
                    }
                    if (refreshExpiresIn != null) {
                      refreshExpirationDate =
                          new Date(refreshExpirationDate.getTime() + (refreshExpiresIn * 1000));
                    }

                    token.setTokenExpirationDate(expirationDate);
                    token.setRefreshTokenExpirationDate(refreshExpirationDate);

                    initModel(token);

                    view.onKeycloakTokenRefreshed(token);
                    view.onShowMainModel(model);
                  }

                  @Override
                  public void onError(Throwable e) {
                    e.printStackTrace();
                    view.onKeycloakTokenRefreshingError();
                  }
                }));
  }

  public void openBrowserForLogin(Context context) {
    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(config.getTokenRequestUrl())));
  }
}
