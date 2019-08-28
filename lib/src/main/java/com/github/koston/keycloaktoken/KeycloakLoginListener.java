package com.github.koston.keycloaktoken;

public interface KeycloakLoginListener {

  void OnLoggedIn(KeycloakToken token);

  void OnLoginError();
}
