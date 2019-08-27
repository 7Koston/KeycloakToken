package com.github.koston.keycloaktoken;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface IOAuth2AccessTokenStorage {

  @Nullable
  KeycloakToken getStoredAccessToken();

  void storeAccessToken(@NonNull KeycloakToken token);

  boolean hasAccessToken();

  void removeAccessToken();
}
