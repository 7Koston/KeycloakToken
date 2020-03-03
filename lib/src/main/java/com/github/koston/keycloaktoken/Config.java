/*
 * Copyright 2019 7Koston
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.koston.keycloaktoken;

import androidx.annotation.NonNull;

public final class Config {

  @NonNull private String protocol;
  @NonNull private String clientId;
  @NonNull private String baseUrl;
  @NonNull private String authenticationCodeUrl;
  @NonNull private String redirectUri;
  @NonNull private String tokenRequestUrl;

  public Config(
      @NonNull String protocol,
      @NonNull String baseUrl,
      @NonNull String authenticationCodeUrl,
      @NonNull String clientId,
      @NonNull String redirectUri) {
    this.protocol = protocol;
    this.baseUrl = protocol + baseUrl;
    this.authenticationCodeUrl = protocol + authenticationCodeUrl;
    this.clientId = clientId;
    this.redirectUri = redirectUri;
    this.tokenRequestUrl =
        protocol
            + authenticationCodeUrl
            + "?"
            + "client_id="
            + clientId
            + "&redirect_uri="
            + redirectUri
            + "&response_type=code";
  }

  private void buildTokenRequestUrl() {
    ;
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

  @NonNull
  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(@NonNull String protocol) {
    this.protocol = protocol;
  }

  @NonNull
  public String getTokenRequestUrl() {
    return tokenRequestUrl;
  }

  public void setTokenRequestUrl(@NonNull String tokenRequestUrl) {
    this.tokenRequestUrl = tokenRequestUrl;
  }
}
