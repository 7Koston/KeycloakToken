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

package com.github.koston.keycloaktoken.demo.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.github.koston.keycloaktoken.KeycloakToken;
import com.github.koston.keycloaktoken.demo.R;
import com.github.koston.keycloaktoken.demo.singltones.SharedPreferencesModule;

public class MainActivity extends AppCompatActivity implements MainView {

  private MainPresenter presenter;

  private TextView tvUserId;
  private TextView tvEmail;
  private TextView tvName;
  private TextView tvSurname;
  private TextView tvRoles;
  private TextView tvTokenType;
  private TextView tvIdToken;
  private TextView tvSessionState;
  private TextView tvNotBeforePolicy;
  private TextView tvExpiresIn;
  private TextView tvRefreshExpiresIn;
  private TextView tvTokenExpirationDate;
  private TextView tvRefreshTokenExpirationDate;
  private TextView tvAccessToken;
  private TextView tvRefreshToken;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    presenter = new MainPresenter(this);

    initViews();

    presenter.onCreate(savedInstanceState);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (presenter != null) presenter.onDestroy();
    presenter = null;
  }

  @Override
  protected void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    if (presenter != null) presenter.onSaveInstanceState(outState);
  }

  private void initViews() {
    Button bLogin = findViewById(R.id.bLogin);
    Button bLogout = findViewById(R.id.bLogout);
    Button bRefresh = findViewById(R.id.bRefresh);
    tvUserId = findViewById(R.id.tvUserId);
    tvEmail = findViewById(R.id.tvEmail);
    tvName = findViewById(R.id.tvName);
    tvSurname = findViewById(R.id.tvSurname);
    tvRoles = findViewById(R.id.tvRoles);

    tvTokenType = findViewById(R.id.tvTokenType);
    tvIdToken = findViewById(R.id.tvIdToken);
    tvSessionState = findViewById(R.id.tvSessionState);
    tvNotBeforePolicy = findViewById(R.id.tvNotBeforePolicy);
    tvExpiresIn = findViewById(R.id.tvExpiresIn);
    tvRefreshExpiresIn = findViewById(R.id.tvRefreshExpiresIn);
    tvTokenExpirationDate = findViewById(R.id.tvTokenExpirationDate);
    tvRefreshTokenExpirationDate = findViewById(R.id.tvRefreshTokenExpirationDate);

    tvAccessToken = findViewById(R.id.tvAccessToken);
    tvRefreshToken = findViewById(R.id.tvRefreshToken);

    bLogin.setOnClickListener(view -> presenter.openBrowserForLogin(this));
    bRefresh.setOnClickListener(
        view -> presenter.refreshAccessToken(SharedPreferencesModule.get().getRefreshToken()));
    bLogout.setOnClickListener(
        view -> presenter.logout(SharedPreferencesModule.get().getRefreshToken()));
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    String data;
    {
      if (intent != null) {
        data = intent.getDataString();
        if (presenter != null) presenter.exchangeCodeForToken(intent.getData());
        if (data != null) {
          Log.d("INTENT", data);
        } else {
          Log.e("INTENT", "DATA IS NULL");
        }
      }
    }
  }

  @Override
  public String onGetAccessToken() {
    return SharedPreferencesModule.get().getAccessToken();
  }

  @Override
  public String onGetRefreshToken() {
    return SharedPreferencesModule.get().getRefreshToken();
  }

  @Override
  public void onShowMainModel(@Nullable MainModel model) {
    if (model == null) return;
    tvUserId.setText(getString(R.string.user_id, model.getUserId()));
    tvEmail.setText(getString(R.string.user_email, model.getEmail()));
    tvName.setText(getString(R.string.user_name, model.getName()));
    tvSurname.setText(getString(R.string.user_surname, model.getSurname()));
    tvRoles.setText(getString(R.string.user_roles, model.getRoles()));
    tvTokenType.setText(getString(R.string.token_type, model.getTokenType()));
    tvIdToken.setText(getString(R.string.token_id, model.getIdToken()));
    tvSessionState.setText(getString(R.string.session_state, model.getSessionState()));
    tvNotBeforePolicy.setText(getString(R.string.not_before_policy, model.getNotBeforePolicy()));
    tvExpiresIn.setText(getString(R.string.expires_in, model.getExpiresIn()));
    tvRefreshExpiresIn.setText(getString(R.string.refresh_expires_in, model.getRefreshExpiresIn()));
    tvTokenExpirationDate.setText(
        getString(R.string.token_expiration_date, model.getTokenExpirationDate()));
    tvRefreshTokenExpirationDate.setText(
        getString(R.string.refresh_token_expiration_date, model.getRefreshTokenExpirationDate()));
    tvAccessToken.setText(getString(R.string.access_token, model.getAccessToken()));
    tvRefreshToken.setText(getString(R.string.refresh_token, model.getRefreshToken()));
  }

  @Override
  public void onKeycloakTokenGained(KeycloakToken token) {
    SharedPreferencesModule.get().setAccessToken(token.getAccessToken());
    SharedPreferencesModule.get().setRefreshTOken(token.getRefreshToken());

    Toast.makeText(getApplicationContext(), "LOGGED IN", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onKeycloakTokenRefreshed(KeycloakToken token) {
    SharedPreferencesModule.get().setAccessToken(token.getAccessToken());
    SharedPreferencesModule.get().setRefreshTOken(token.getRefreshToken());

    Toast.makeText(getApplicationContext(), "TOKEN REFRESHED", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onKeycloakLoggedOut() {
    SharedPreferencesModule.get().setAccessToken("");
    SharedPreferencesModule.get().setRefreshTOken("");

    Toast.makeText(getApplicationContext(), "LOGGED OUT", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onKeycloakTokenGainingError() {
    Toast.makeText(getApplicationContext(), "LOGIN ERROR", Toast.LENGTH_SHORT).show();
    Log.e("onLoginError", "LOGIN ERROR");
  }

  @Override
  public void onKeycloakTokenRefreshingError() {
    Toast.makeText(getApplicationContext(), "LOGOUT ERROR", Toast.LENGTH_SHORT).show();
    Log.e("onLogoutError", "LOGOUT ERROR");
  }

  @Override
  public void onKeycloakTokenLoggingOutError() {
    Toast.makeText(getApplicationContext(), "LOGOUT ERROR", Toast.LENGTH_SHORT).show();
    Log.e("onLogoutError", "LOGOUT ERROR");
  }
}
