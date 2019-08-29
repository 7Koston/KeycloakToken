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

package com.github.koston.keycloaktoken.demo.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.github.koston.keycloaktoken.Config;
import com.github.koston.keycloaktoken.KeycloakLoginListener;
import com.github.koston.keycloaktoken.KeycloakLogoutListener;
import com.github.koston.keycloaktoken.KeycloakToken;
import com.github.koston.keycloaktoken.KeycloakTokenExchanger;
import com.github.koston.keycloaktoken.KeycloakTokenRefreshListener;
import com.github.koston.keycloaktoken.KeycloakTokenRefresher;
import com.github.koston.keycloaktoken.demo.R;
import com.github.koston.keycloaktoken.demo.singltones.OkHttpModule;
import com.github.koston.keycloaktoken.demo.singltones.SharedPreferencesModule;
import com.github.koston.keycloaktoken.demo.utils.Helper;
import com.github.koston.keycloaktoken.demo.utils.Principal;

public class MainActivity extends AppCompatActivity
    implements KeycloakLoginListener, KeycloakLogoutListener, KeycloakTokenRefreshListener {

  private TextView tvToken;
  private TextView tvUserId;
  private TextView tvEmail;
  private TextView tvName;
  private TextView tvRoles;
  private TextView tvRefreshDates;
  private TextView tvAccessDates;

  private KeycloakTokenExchanger tokenExchanger;
  private KeycloakTokenRefresher tokenRefresher;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Config config =
        new Config(
            "android-app",
            "https://auth.novel.tl/auth/realms/site/protocol/openid-connect",
            "https://auth.novel.tl/auth/realms/site/protocol/openid-connect/auth",
            "app://android.novel.tl");

    tokenRefresher = new KeycloakTokenRefresher(OkHttpModule.getApolloHttpClient(), config, this);

    tokenExchanger =
        new KeycloakTokenExchanger(OkHttpModule.getApolloHttpClient(), config, this, this);

    initViews();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    tokenExchanger.onDestroy();
    tokenExchanger = null;
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    String data;
    {
      if (intent != null) {
        data = intent.getDataString();
        tokenExchanger.exchangeCodeForToken(intent.getData());
        if (data != null) {
          Log.d("INTENT", data);
        } else {
          Log.e("INTENT", "DATA IS NULL");
        }
      }
    }
  }

  private void initViews() {
    Button bLogin = findViewById(R.id.bLogin);
    Button bLogout = findViewById(R.id.bLogout);
    Button bRefresh = findViewById(R.id.bRefresh);
    tvToken = findViewById(R.id.tvToken);
    tvUserId = findViewById(R.id.tvUserId);
    tvEmail = findViewById(R.id.tvEmail);
    tvName = findViewById(R.id.tvName);
    tvRoles = findViewById(R.id.tvRoles);
    tvRefreshDates = findViewById(R.id.tvRefreshDates);
    tvAccessDates = findViewById(R.id.tvAccessDates);

    bLogin.setOnClickListener(view -> tokenExchanger.openBrowserForLogin(getApplicationContext()));
    bRefresh.setOnClickListener(
        view -> tokenRefresher.refreshAccessToken(SharedPreferencesModule.get().getRefreshToken()));
    bLogout.setOnClickListener(
        view -> tokenExchanger.logout(SharedPreferencesModule.get().getRefreshToken()));

    tvToken.setText(SharedPreferencesModule.get().getAccessToken());
  }

  private void showInfo(KeycloakToken token) {
    String access = token.getAccessToken();
    Helper helper = new Helper();
    Principal principal = helper.parseJwtToken(access);

    tvToken.setText(access);
    tvUserId.setText(principal.getUserId());
    tvEmail.setText(principal.getEmail());
    tvName.setText(principal.getName());
    tvRoles.setText(principal.getRoles());
    tvRefreshDates.setText(helper.formatDate(token.getTokenExpirationDate()));
    tvAccessDates.setText(helper.formatDate(token.getRefreshTokenExpirationDate()));
  }

  private void clearInfo() {
    String empty = "";
    tvToken.setText(empty);
    tvUserId.setText(empty);
    tvEmail.setText(empty);
    tvName.setText(empty);
    tvRoles.setText(empty);
    tvRefreshDates.setText(empty);
    tvAccessDates.setText(empty);
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void OnLoggedIn(KeycloakToken token) {
    SharedPreferencesModule.get().setAccessToken(token.getAccessToken());
    SharedPreferencesModule.get().setRefreshTOken(token.getRefreshToken());

    Toast.makeText(getApplicationContext(), "LOGGED IN", Toast.LENGTH_SHORT).show();

    showInfo(token);
  }

  @Override
  public void OnLoginError() {
    Toast.makeText(getApplicationContext(), "LOGIN ERROR", Toast.LENGTH_SHORT).show();
    Log.e("OnLoginError", "LOGIN ERROR");

    clearInfo();
  }

  @Override
  public void OnLoggedOut() {
    SharedPreferencesModule.get().setAccessToken("");
    SharedPreferencesModule.get().setRefreshTOken("");

    Toast.makeText(getApplicationContext(), "LOGGED OUT", Toast.LENGTH_SHORT).show();

    clearInfo();
  }

  @Override
  public void OnLogoutError() {
    Toast.makeText(getApplicationContext(), "LOGOUT ERROR", Toast.LENGTH_SHORT).show();
    Log.e("OnLogoutError", "LOGOUT ERROR");

    clearInfo();
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void OnTokenRefreshed(KeycloakToken token) {
    SharedPreferencesModule.get().setAccessToken(token.getAccessToken());
    SharedPreferencesModule.get().setRefreshTOken(token.getRefreshToken());

    Toast.makeText(getApplicationContext(), "TOKEN REFRESHED", Toast.LENGTH_SHORT).show();

    showInfo(token);
  }

  @Override
  public void OnTokenRefreshError() {
    Toast.makeText(getApplicationContext(), "LOGOUT ERROR", Toast.LENGTH_SHORT).show();
    Log.e("OnLogoutError", "LOGOUT ERROR");

    clearInfo();
  }
}
