package com.github.koston.keycloaktoken.demo;

import android.app.Application;

public class App extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    OkHttpModule.initialize();
    SettingsModule.initialize(getApplicationContext());
  }
}
