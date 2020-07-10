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
