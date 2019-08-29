package com.github.koston.keycloaktoken.demo.utils;

import androidx.annotation.Nullable;

public final class Principal {

  @Nullable
  private final String userId;
  @Nullable
  private final String email;
  @Nullable
  private final String name;
  @Nullable
  private final String surname;
  @Nullable
  private final String roles;

  public Principal(
      @Nullable String userId,
      @Nullable String email,
      @Nullable String name,
      @Nullable String surname,
      @Nullable String roles) {
    super();
    this.userId = userId;
    this.email = email;
    this.name = name;
    this.surname = surname;
    this.roles = roles;
  }

  @Nullable
  public final String getUserId() {
    return this.userId;
  }

  @Nullable
  public final String getEmail() {
    return this.email;
  }

  @Nullable
  public final String getName() {
    return this.name;
  }

  @Nullable
  public final String getSurname() {
    return this.surname;
  }

  @Nullable
  public final String getRoles() {
    return this.roles;
  }
}
