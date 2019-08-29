package com.github.koston.keycloaktoken.demo.singltones;

import java.util.Arrays;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpModule {

  private static OkHttpClient apolloHttpClient;

  public static void initialize() {
    if (apolloHttpClient == null) {
      buildApolloHttpClient();
    }
  }

  private static void buildApolloHttpClient() {
    OkHttpClient.Builder builder =
        new OkHttpClient.Builder()
            .protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1))
            .addNetworkInterceptor(
                chain -> {
                  Request request = chain.request();
                  request =
                      request
                          .newBuilder()
                          .addHeader("Content-Type", "application/x-www-form-urlencoded")
                          .header("Cache-Control", "no-cache, no-store, must-revalidate")
                          .build();
                  return chain.proceed(request);
                })
            .hostnameVerifier((hostname, session) -> true);

    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    builder.addNetworkInterceptor(logging);

    apolloHttpClient = builder.build();
  }

  public static OkHttpClient getApolloHttpClient() {
    return apolloHttpClient;
  }
}
