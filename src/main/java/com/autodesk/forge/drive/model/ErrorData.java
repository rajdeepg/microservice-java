/*
 * Copyright 2019 Autodesk, Inc. All Rights Reserved.
 *
 * This computer source code and related instructions and comments are the unpublished confidential
 * and proprietary information of Autodesk, Inc. and are protected under applicable copyright and
 * trade secret law. They may not be disclosed to, copied or used by any third party without the
 * prior written consent of Autodesk, Inc.
 */

package com.autodesk.forge.drive.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class ErrorData {
  private static final Integer MAX_STACKTRACE_SIZE = 1024;
  @Getter @Setter private String trackingId;
  @Getter @Setter private String statusMessage;
  @Getter @Setter private String message;
  @Getter @Setter private String timestamp;
  @Getter @Setter private int status;
  @Getter private List<String> stackTrace;

  public ErrorData() {
    timestamp = Instant.now().toString();
  }

  public void setStackTrace(String stackTrace) {
    if (null != stackTrace) {
      Integer stackTraceSize = Math.min(stackTrace.length(), MAX_STACKTRACE_SIZE);
      this.stackTrace = Arrays.asList(stackTrace.substring(0, stackTraceSize).split("\n"));
    } else {
      this.stackTrace = null;
    }
  }
}
