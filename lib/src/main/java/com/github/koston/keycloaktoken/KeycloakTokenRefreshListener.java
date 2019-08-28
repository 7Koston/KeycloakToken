package com.github.koston.keycloaktoken;

public interface KeycloakTokenRefreshListener {

  void OnTokenRefreshed(KeycloakToken token);

  void OnTokenRefreshError();
}
