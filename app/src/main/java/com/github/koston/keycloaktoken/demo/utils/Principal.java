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

package com.github.koston.keycloaktoken.demo.utils;

import androidx.annotation.Nullable;

public final class Principal {

  @Nullable private final String userId;
  @Nullable private final String email;
  @Nullable private final String name;
  @Nullable private final String surname;
  @Nullable private final String roles;

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
