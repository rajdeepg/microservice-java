/*
 * Copyright 2018 Autodesk, Inc. All Rights Reserved.
 *
 * This computer source code and related instructions and comments are the unpublished confidential
 * and proprietary information of Autodesk, Inc. and are protected under applicable copyright and
 * trade secret law. They may not be disclosed to, copied or used by any third party without the
 * prior written consent of Autodesk, Inc.
 */

package com.autodesk.forge.drive.model;

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
