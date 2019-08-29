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
