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
