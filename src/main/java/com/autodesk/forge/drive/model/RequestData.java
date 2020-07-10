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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component @Scope("request") public class RequestData {
  @Getter @Setter private String bearerToken;
  @Getter @Setter private TokenData tokenData;
  @Getter @Setter private String trackingId;

  public RequestData() {
    this.trackingId = UUID.randomUUID().toString();
  }
}
