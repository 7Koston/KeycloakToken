package com.github.koston.keycloaktoken.demo;

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

public class MainActivity extends AppCompatActivity
    implements KeycloakLoginListener, KeycloakLogoutListener, KeycloakTokenRefreshListener {

  private TextView tvResult;

  private KeycloakTokenExchanger tokenExchanger;

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

    KeycloakTokenRefresher tokenRefresher = new KeycloakTokenRefresher(
        OkHttpModule.getApolloHttpClient(), config, this);
    tokenRefresher.refreshAccessToken(SettingsModule.get().getRefreshToken());

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
    tvResult = findViewById(R.id.tvResult);

    bLogin.setOnClickListener(view -> tokenExchanger.openBrowserForLogin(getApplicationContext()));

    bLogout.setOnClickListener(
        view -> tokenExchanger.logout(SettingsModule.get().getRefreshToken()));

    tvResult.setText(SettingsModule.get().getAccessToken());
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void OnLoggedIn(KeycloakToken token) {
    SettingsModule.get().setAccessToken(token.getAccessToken());
    SettingsModule.get().setRefreshTOken(token.getRefreshToken());

    tvResult.setText(token.getIdToken() + "\n" + token.getAccessToken());

    Toast.makeText(getApplicationContext(), "LOGGED IN", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void OnLoginError() {
    Toast.makeText(getApplicationContext(), "LOGIN ERROR", Toast.LENGTH_SHORT).show();
    Log.e("OnLoginError", "LOGIN ERROR");
  }

  @Override
  public void OnLoggedOut() {
    SettingsModule.get().setAccessToken("");
    SettingsModule.get().setRefreshTOken("");

    Toast.makeText(getApplicationContext(), "LOGGED OUT", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void OnLogoutError() {
    Toast.makeText(getApplicationContext(), "LOGOUT ERROR", Toast.LENGTH_SHORT).show();
    Log.e("OnLogoutError", "LOGOUT ERROR");
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void OnTokenRefreshed(KeycloakToken token) {
    SettingsModule.get().setAccessToken(token.getAccessToken());
    SettingsModule.get().setRefreshTOken(token.getRefreshToken());

    tvResult.setText(token.getIdToken() + "\n" + token.getAccessToken());

    Toast.makeText(getApplicationContext(), "TOKEN REFRESHED", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void OnTokenRefreshError() {
    Toast.makeText(getApplicationContext(), "LOGOUT ERROR", Toast.LENGTH_SHORT).show();
    Log.e("OnLogoutError", "LOGOUT ERROR");
  }
}
