package com.github.koston.keycloaktoken;

import androidx.annotation.NonNull;

public final class Config {

  @NonNull
  private String clientId;

  @NonNull
  private String baseUrl;

  @NonNull
  private String authenticationCodeUrl;

  @NonNull
  private String redirectUri;

  public Config(
      @NonNull String clientId,
      @NonNull String baseUrl,
      @NonNull String authenticationCodeUrl,
      @NonNull String redirectUri) {
    this.clientId = clientId;
    this.baseUrl = baseUrl;
    this.authenticationCodeUrl = authenticationCodeUrl;
    this.redirectUri = redirectUri;
  }

  @NonNull
  public String getClientId() {
    return clientId;
  }

  public void setClientId(@NonNull String clientId) {
    this.clientId = clientId;
  }

  @NonNull
  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(@NonNull String baseUrl) {
    this.baseUrl = baseUrl;
  }

  @NonNull
  public String getAuthenticationCodeUrl() {
    return authenticationCodeUrl;
  }

  public void setAuthenticationCodeUrl(@NonNull String authenticationCodeUrl) {
    this.authenticationCodeUrl = authenticationCodeUrl;
  }

  @NonNull
  public String getRedirectUri() {
    return redirectUri;
  }

  public void setRedirectUri(@NonNull String redirectUri) {
    this.redirectUri = redirectUri;
  }
}
