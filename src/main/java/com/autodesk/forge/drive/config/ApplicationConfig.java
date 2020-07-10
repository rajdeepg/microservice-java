/*
 * Copyright 2019 Autodesk, Inc. All Rights Reserved.
 *
 * This computer source code and related instructions and comments are the unpublished confidential
 * and proprietary information of Autodesk, Inc. and are protected under applicable copyright and
 * trade secret law. They may not be disclosed to, copied or used by any third party without the
 * prior written consent of Autodesk, Inc.
 */

package com.autodesk.forge.drive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Asset-Graph Spring Boot application configuration.
 *
 * @author lopezm
 */
@EnableAsync public class ApplicationConfig {
  @Bean public TaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    //TODO: Configure pool size properly
    executor.setCorePoolSize(5);
    executor.setMaxPoolSize(10);
    executor.setQueueCapacity(25);
    return executor;
  }
}
