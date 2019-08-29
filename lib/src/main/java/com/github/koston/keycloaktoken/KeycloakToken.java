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

import com.google.gson.annotations.SerializedName;
import io.reactivex.annotations.Nullable;
import java.util.Calendar;

public final class KeycloakToken {

  @SerializedName("access_token")
  @Nullable
  private String accessToken;

  @SerializedName("expires_in")
  @Nullable
  private Integer expiresIn;

  @SerializedName("refresh_expires_in")
  @Nullable
  private Integer refreshExpiresIn;

  @SerializedName("refresh_token")
  @Nullable
  private String refreshToken;

  @SerializedName("token_type")
  @Nullable
  private String tokenType;

  @SerializedName("id_token")
  @Nullable
  private String idToken;

  @SerializedName("not-before-policy")
  @Nullable
  private Integer notBeforePolicy;

  @SerializedName("session_state")
  @Nullable
  private String sessionState;

  @SerializedName("token_expiration_date")
  @Nullable
  private Calendar tokenExpirationDate;

  @SerializedName("refresh_expiration_date")
  @Nullable
  private Calendar refreshTokenExpirationDate;

  public KeycloakToken(
      @Nullable String accessToken,
      @Nullable Integer expiresIn,
      @Nullable Integer refreshExpiresIn,
      @Nullable String refreshToken,
      @Nullable String tokenType,
      @Nullable String idToken,
      @Nullable Integer notBeforePolicy,
      @Nullable String sessionState,
      @Nullable Calendar tokenExpirationDate,
      @Nullable Calendar refreshTokenExpirationDate) {
    this.accessToken = accessToken;
    this.expiresIn = expiresIn;
    this.refreshExpiresIn = refreshExpiresIn;
    this.refreshToken = refreshToken;
    this.tokenType = tokenType;
    this.idToken = idToken;
    this.notBeforePolicy = notBeforePolicy;
    this.sessionState = sessionState;
    this.tokenExpirationDate = tokenExpirationDate;
    this.refreshTokenExpirationDate = refreshTokenExpirationDate;
  }

  @Nullable
  public final String getAccessToken() {
    return this.accessToken;
  }

  public final void setAccessToken(@Nullable String var1) {
    this.accessToken = var1;
  }

  @Nullable
  public final Integer getExpiresIn() {
    return this.expiresIn;
  }

  public final void setExpiresIn(@Nullable Integer var1) {
    this.expiresIn = var1;
  }

  @Nullable
  public final Integer getRefreshExpiresIn() {
    return this.refreshExpiresIn;
  }

  public final void setRefreshExpiresIn(@Nullable Integer var1) {
    this.refreshExpiresIn = var1;
  }

  @Nullable
  public final String getRefreshToken() {
    return this.refreshToken;
  }

  public final void setRefreshToken(@Nullable String var1) {
    this.refreshToken = var1;
  }

  @Nullable
  public final String getTokenType() {
    return this.tokenType;
  }

  public final void setTokenType(@Nullable String var1) {
    this.tokenType = var1;
  }

  @Nullable
  public final String getIdToken() {
    return this.idToken;
  }

  public final void setIdToken(@Nullable String var1) {
    this.idToken = var1;
  }

  @Nullable
  public final Integer getNotBeforePolicy() {
    return this.notBeforePolicy;
  }

  public final void setNotBeforePolicy(@Nullable Integer var1) {
    this.notBeforePolicy = var1;
  }

  @Nullable
  public final String getSessionState() {
    return this.sessionState;
  }

  public final void setSessionState(@Nullable String var1) {
    this.sessionState = var1;
  }

  @Nullable
  public final Calendar getTokenExpirationDate() {
    return this.tokenExpirationDate;
  }

  public final void setTokenExpirationDate(@Nullable Calendar var1) {
    this.tokenExpirationDate = var1;
  }

  @Nullable
  public final Calendar getRefreshTokenExpirationDate() {
    return this.refreshTokenExpirationDate;
  }

  public final void setRefreshTokenExpirationDate(@Nullable Calendar var1) {
    this.refreshTokenExpirationDate = var1;
  }
}
