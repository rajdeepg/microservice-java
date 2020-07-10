/*
 * Copyright 2019 Autodesk, Inc. All Rights Reserved.
 *
 * This computer source code and related instructions and comments are the unpublished confidential
 * and proprietary information of Autodesk, Inc. and are protected under applicable copyright and
 * trade secret law. They may not be disclosed to, copied or used by any third party without the
 * prior written consent of Autodesk, Inc.
 */

package com.autodesk.forge.drive.provider;

import com.autodesk.forge.drive.model.RequestData;
import com.autodesk.forge.drive.model.response.DriveInfo;
import com.autodesk.forge.drive.service.DriveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service public class DriveProvider {

  private final TaskExecutor taskExecutor;
  private final DriveService driveService;

  @Autowired public DriveProvider(TaskExecutor taskExecutor, DriveService driveService) {
    this.taskExecutor = taskExecutor;
    this.driveService = driveService;
  }

  public CompletableFuture<DriveInfo> getDriveInfo(RequestData requestData) {
    return CompletableFuture.supplyAsync(
      () -> driveService.getDriveInfo(requestData.getTokenData().getAccess_token().getUserid()),
      taskExecutor);
  }

  public CompletableFuture<DriveInfo> createDriveInfo(RequestData requestData) {
    return CompletableFuture.supplyAsync(
      () -> driveService.createDriveInfo(requestData.getTokenData().getAccess_token().getUserid()),
      taskExecutor);
  }
}
