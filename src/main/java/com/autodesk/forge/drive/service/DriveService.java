/*
 * Copyright 2019 Autodesk, Inc. All Rights Reserved.
 *
 * This computer source code and related instructions and comments are the unpublished confidential
 * and proprietary information of Autodesk, Inc. and are protected under applicable copyright and
 * trade secret law. They may not be disclosed to, copied or used by any third party without the
 * prior written consent of Autodesk, Inc.
 */

package com.autodesk.forge.drive.service;

import com.autodesk.forge.drive.model.response.DriveInfo;
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
