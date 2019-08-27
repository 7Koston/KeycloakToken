package com.github.koston.keycloaktoken;

import androidx.annotation.NonNull;

public final class Config {

  @NonNull
  public static final String clientId = "site";
  @NonNull
  public static final String baseUrl = "https://auth.novel.tl/auth/realms/site/protocol/openid-connect";
  @NonNull
  public static final String authenticationCodeUrl = "https://auth.novel.tl/auth/realms/site/protocol/openid-connect/auth";
  @NonNull
  public static final String redirectUri = "test://urlmobile.com";

  private Config() {
  }


}
