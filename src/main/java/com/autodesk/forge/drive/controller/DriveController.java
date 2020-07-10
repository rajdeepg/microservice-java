package com.autodesk.forge.drive.controller;


import com.autodesk.forge.drive.model.RequestData;
import com.autodesk.forge.drive.model.response.DriveInfo;
import com.autodesk.forge.drive.provider.DriveProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController public class DriveController {

  private final static String BASE_URL = "/v1";
  private final DriveProvider driveProvider;
  private final ApplicationContext applicationContext;

  @Autowired
  public DriveController(DriveProvider driveProvider, ApplicationContext applicationContext) {
    this.driveProvider = driveProvider;
    this.applicationContext = applicationContext;
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = BASE_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<DriveInfo> getDriveInfo() {
    RequestData requestData = applicationContext.getBean(RequestData.class);
    return driveProvider.getDriveInfo(requestData);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(value = BASE_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<DriveInfo> createDriveInfo() {
    RequestData requestData = applicationContext.getBean(RequestData.class);
    return driveProvider.createDriveInfo(requestData);
  }
}
