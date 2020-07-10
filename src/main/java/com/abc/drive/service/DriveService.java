package com.abc.drive.service;

import com.abc.drive.model.response.DriveInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service public class DriveService {

  @Autowired public DriveService() {
  }

  public DriveInfo getDriveInfo(String userId) {
    DriveInfo driveInfo = new DriveInfo();
    driveInfo.setCollectionId("CollectionId1");
    driveInfo.setSpaceId("SpaceId1");
    driveInfo.setRootFolderId("RootFolderId1");
    return driveInfo;
  }

  public DriveInfo createDriveInfo(String userId) {
    DriveInfo driveInfo = new DriveInfo();
    driveInfo.setCollectionId("CollectionId2");
    driveInfo.setSpaceId("SpaceId2");
    driveInfo.setRootFolderId("RootFolderId2");
    return driveInfo;
  }
}
