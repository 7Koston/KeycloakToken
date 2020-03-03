package com.github.koston.keycloaktoken.demo.main;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.github.koston.keycloaktoken.KeycloakToken;
import com.github.koston.keycloaktoken.demo.utils.Principal;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.migcomponents.migbase64.Base64;
import java.nio.charset.Charset;
import java.util.Date;

public class MainModel implements Parcelable {

  public static final Creator<MainModel> CREATOR =
      new Creator<MainModel>() {
        @Override
        public MainModel createFromParcel(Parcel in) {
          return new MainModel(in);
        }

        @Override
        public MainModel[] newArray(int size) {
          return new MainModel[size];
        }
      };
  // response
  @Nullable private Integer expiresIn;
  @Nullable private Integer refreshExpiresIn;
  @Nullable private Integer notBeforePolicy;
  @Nullable private String accessToken;
  @Nullable private String refreshToken;
  @Nullable private String tokenType;
  @Nullable private String idToken;
  @Nullable private String sessionState;
  @Nullable private Date tokenExpirationDate;
  @Nullable private Date refreshTokenExpirationDate;

  // token decode
  @Nullable private String userId;
  @Nullable private String email;
  @Nullable private String name;
  @Nullable private String surname;
  @Nullable private String roles;

  public MainModel(@Nullable KeycloakToken token) {
    if (token != null) {
      expiresIn = token.getExpiresIn();
      refreshExpiresIn = token.getRefreshExpiresIn();
      notBeforePolicy = token.getNotBeforePolicy();
      accessToken = token.getAccessToken();
      refreshToken = token.getRefreshToken();
      tokenType = token.getTokenType();
      idToken = token.getIdToken();
      sessionState = token.getSessionState();
      tokenExpirationDate = token.getTokenExpirationDate();
      refreshTokenExpirationDate = token.getRefreshTokenExpirationDate();

      Principal principal = parseJwtToken(accessToken);
      userId = principal.getUserId();
      email = principal.getEmail();
      name = principal.getName();
      surname = principal.getSurname();
      roles = principal.getRoles();
    }
  }

  @NonNull
  public final Principal parseJwtToken(@Nullable String jwtToken) {
    if (jwtToken == null) {
      return new Principal(null, null, null, null, null);
    }

    String[] base64 = jwtToken.split("\\.");
    String base64EncodedBody = base64[1];

    String body = new String(Base64.decodeFast(base64EncodedBody), Charset.forName("UTF-8"));
    JsonObject jsonBody = new Gson().fromJson(body, JsonObject.class);

    String userId = jsonBody.get("sub").getAsString();
    String email = jsonBody.get("email").getAsString();
    String name = jsonBody.get("given_name").getAsString();
    String surname = jsonBody.get("family_name").getAsString();
    String roles =
        jsonBody.get("realm_access").getAsJsonObject().getAsJsonArray("roles").toString();

    return new Principal(userId, email, name, surname, roles);
  }

  public MainModel(@Nullable String accessToken, @Nullable String refreshToken) {
    if (accessToken != null) {
      this.accessToken = accessToken;
      Principal principal = parseJwtToken(accessToken);
      userId = principal.getUserId();
      email = principal.getEmail();
      name = principal.getName();
      surname = principal.getSurname();
      roles = principal.getRoles();
    }
    if (refreshToken != null) this.refreshToken = refreshToken;
  }

  private MainModel(Parcel in) {
    int _expiresIn = in.readInt();
    if (_expiresIn == 0) expiresIn = null;
    else expiresIn = _expiresIn;

    int _refreshExpiresIn = in.readInt();
    if (_refreshExpiresIn == 0) refreshExpiresIn = null;
    else refreshExpiresIn = _refreshExpiresIn;

    int _notBeforePolicy = in.readInt();
    if (_notBeforePolicy == 0) notBeforePolicy = null;
    else notBeforePolicy = _notBeforePolicy;

    long _tokenExpirationDate = in.readLong();
    if (_tokenExpirationDate == 0) tokenExpirationDate = null;
    else tokenExpirationDate = new Date(_tokenExpirationDate);

    long _refreshTokenExpirationDate = in.readLong();
    if (_refreshTokenExpirationDate == 0) refreshTokenExpirationDate = null;
    else refreshTokenExpirationDate = new Date(_refreshTokenExpirationDate);

    accessToken = in.readString();
    refreshToken = in.readString();
    tokenType = in.readString();
    idToken = in.readString();
    sessionState = in.readString();
    userId = in.readString();
    email = in.readString();
    name = in.readString();
    surname = in.readString();
    roles = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(expiresIn != null ? expiresIn : 0);
    dest.writeInt(refreshExpiresIn != null ? refreshExpiresIn : 0);
    dest.writeInt(notBeforePolicy != null ? notBeforePolicy : 0);
    dest.writeLong(tokenExpirationDate != null ? tokenExpirationDate.getTime() : 0);
    dest.writeLong(refreshTokenExpirationDate != null ? refreshTokenExpirationDate.getTime() : 0);
    dest.writeString(accessToken);
    dest.writeString(refreshToken);
    dest.writeString(tokenType);
    dest.writeString(idToken);
    dest.writeString(sessionState);
    dest.writeString(userId);
    dest.writeString(email);
    dest.writeString(name);
    dest.writeString(surname);
    dest.writeString(roles);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Nullable
  public Integer getExpiresIn() {
    return expiresIn;
  }

  @Nullable
  public Integer getRefreshExpiresIn() {
    return refreshExpiresIn;
  }

  @Nullable
  public Integer getNotBeforePolicy() {
    return notBeforePolicy;
  }

  @Nullable
  public String getAccessToken() {
    return accessToken;
  }

  @Nullable
  public String getRefreshToken() {
    return refreshToken;
  }

  @Nullable
  public String getTokenType() {
    return tokenType;
  }

  @Nullable
  public String getIdToken() {
    return idToken;
  }

  @Nullable
  public String getSessionState() {
    return sessionState;
  }

  @Nullable
  public Date getTokenExpirationDate() {
    return tokenExpirationDate;
  }

  @Nullable
  public Date getRefreshTokenExpirationDate() {
    return refreshTokenExpirationDate;
  }

  @Nullable
  public String getUserId() {
    return userId;
  }

  @Nullable
  public String getEmail() {
    return email;
  }

  @Nullable
  public String getName() {
    return name;
  }

  @Nullable
  public String getSurname() {
    return surname;
  }

  @Nullable
  public String getRoles() {
    return roles;
  }
}
