package com.github.koston.keycloaktoken.demo;

import android.app.Application;
import com.github.koston.keycloaktoken.demo.singltones.OkHttpModule;
import com.github.koston.keycloaktoken.demo.singltones.SharedPreferencesModule;

public class App extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    OkHttpModule.initialize();
    SharedPreferencesModule.initialize(getApplicationContext());
  }
}
