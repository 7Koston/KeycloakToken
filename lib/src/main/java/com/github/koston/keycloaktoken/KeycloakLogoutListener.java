package com.github.koston.keycloaktoken;

public interface KeycloakLogoutListener {

  void OnLoggedOut();

  void OnLogoutError();
}
