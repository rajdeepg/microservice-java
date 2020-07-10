package com.abc.drive.model;

import lombok.Getter;
import lombok.Setter;

public class TokenData {
  @Getter @Setter private AccessToken access_token;
  @Getter @Setter private String client_id;
  @Getter @Setter private String scope;
  @Getter @Setter private String expires_in;


  public class AccessToken {
    @Getter @Setter private String client_id;
    @Getter @Setter private String userid;
    @Getter @Setter private String oxygenid;
    @Getter @Setter private String firstname;
    @Getter @Setter private String lastname;
    @Getter @Setter private String username;
    @Getter @Setter private String email;
    @Getter @Setter private String absolute_expiry;
    @Getter @Setter private String entitlements;
  }
}
