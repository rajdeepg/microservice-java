package com.abc.drive.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor public class DriveInfo {
  @Getter @Setter private String collectionId;
  @Getter @Setter private String spaceId;
  @Getter @Setter private String rootFolderId;
}
