# KeycloakToken
[ ![API](https://img.shields.io/badge/API-17%2B-blue.svg?style=flat) ](https://android-arsenal.com/api?level=17)
[![](https://jitpack.io/v/7Koston/KeycloakToken.svg)](https://jitpack.io/#7Koston/KeycloakToken)

Simple Android librirary to obtain, exchange, and do logout via Keycloak token.
Based on [this](https://github.com/maslick/keycloak-android-native) solution. Rewrited on Java, simplified to use as some kind a "Helper".

Library dependencies:
* [OkHttp3](https://github.com/square/okhttp/tree/master/okhttp/src/main/java/okhttp3)
* [RxJava & RxAndroid](https://github.com/ReactiveX/RxJava)
* [Gson](https://github.com/google/gson)

### Including in project

Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
    }
   }
```
Add the dependency:
```gradle
dependencies {
  implementation 'com.github.7Koston:KeycloakToken:1.0.0'
}
```

## Thanks to

[Pavel Maslov](https://github.com/maslick)

## License

This project is licensed under the Apache 2.0 - see the [LICENSE](LICENSE) file for details
