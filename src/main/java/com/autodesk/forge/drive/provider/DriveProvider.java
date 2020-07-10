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
