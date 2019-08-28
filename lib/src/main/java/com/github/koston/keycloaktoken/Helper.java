package com.github.koston.keycloaktoken;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.migcomponents.migbase64.Base64;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public final class Helper {

  public final boolean isTokenExpired(@Nullable KeycloakToken token) {
    if (token != null) {
      Calendar tokenExpirationDate = token.getTokenExpirationDate();
      if (tokenExpirationDate == null) {
        return true;
      }
      return Calendar.getInstance().after(tokenExpirationDate);
    }
    return true;
  }

  public final boolean isRefreshTokenExpired(@Nullable KeycloakToken token) {
    if (token != null) {
      Calendar tokenExpirationDate = token.getRefreshTokenExpirationDate();
      if (tokenExpirationDate == null) {
        return true;
      }
      return Calendar.getInstance().after(tokenExpirationDate);
    }
    return true;
  }

  @NonNull
  public final String formatDate(@NonNull Calendar formatDate) {
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    return formatter.format(formatDate.getTime());
  }

  @NonNull
  public final Principal parseJwtToken(@Nullable String jwtToken) {
    if (jwtToken == null) {
      return new Principal(null, null, null, null, null);
    }

    String[] base64 = jwtToken.split(".");
    String base64EncodedBody = base64[1];

    String body = Arrays.toString(Base64.decodeFast(base64EncodedBody));
    JsonObject jsonBody = new Gson().fromJson(body, JsonObject.class);

    String userId = jsonBody.get("sub").getAsString();
    String email = jsonBody.get("email").getAsString();
    String name = jsonBody.get("given_name").getAsString();
    String surname = jsonBody.get("family_name").getAsString();
    String roles =
        jsonBody.get("realm_access").getAsJsonObject().getAsJsonArray("roles").getAsString();

    return new Principal(userId, email, name, surname, roles);
  }
}
