package com.github.koston.keycloaktoken.demo.main;

import androidx.annotation.Nullable;
import com.github.koston.keycloaktoken.KeycloakToken;

public interface MainView {

  String onGetAccessToken();

  String onGetRefreshToken();

  void onShowMainModel(@Nullable MainModel model);

  void onKeycloakTokenGained(KeycloakToken token);

  void onKeycloakTokenRefreshed(KeycloakToken token);

  void onKeycloakLoggedOut();

  void onKeycloakTokenGainingError();

  void onKeycloakTokenRefreshingError();

  void onKeycloakTokenLoggingOutError();
}
