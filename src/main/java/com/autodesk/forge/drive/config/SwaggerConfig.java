/*
 * Copyright 2018 Autodesk, Inc. All Rights Reserved.
 *
 * This computer source code and related instructions and comments are the unpublished confidential
 * and proprietary information of Autodesk, Inc. and are protected under applicable copyright and
 * trade secret law. They may not be disclosed to, copied or used by any third party without the
 * prior written consent of Autodesk, Inc.
 */

package com.autodesk.forge.drive.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

@Service @EnableSwagger2 public class SwaggerConfig {
  private final String swaggerUrlBase;
  private final String swaggerUrlProtocol;
  private final String apigeeBasePath;
  private final ServletContext servletContext;
  private final Pattern apigeeEndpointRegEx;
  private final boolean apigeeEndpoint;


  public SwaggerConfig(@Value("${swagger.url.base}") String swaggerUrlBase,
    @Value("${swagger.url.protocol}") String swaggerUrlProtocol, ServletContext servletContext) {
    this.swaggerUrlBase = swaggerUrlBase;
    this.swaggerUrlProtocol = swaggerUrlProtocol;
    this.servletContext = servletContext;
    this.apigeeEndpointRegEx = Pattern.compile("developer(|-dev|-stg).api.autodesk.com");
    this.apigeeEndpoint = apigeeEndpointRegEx.matcher(swaggerUrlBase).matches();
    this.apigeeBasePath = "drive";
  }

  @Bean public Docket driveApi() {
    Docket docket =
      new Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false).apiInfo(apiInfo())
        .select().apis(RequestHandlerSelectors.basePackage("com.autodesk.forge.drive.controller"))
        .build();
    docket.host(swaggerUrlBase);

    HashSet<String> protocols = new HashSet<>();
    protocols.add(swaggerUrlProtocol);
    docket.protocols(protocols);
    docket.groupName("drive");

    // add base url/base path
    if (apigeeEndpoint) {
      docket.pathProvider(new RelativePathProvider(servletContext) {
        @Override public String getApplicationBasePath() {
          return "/" + apigeeBasePath;
        }
      });
    }

    // add headers
    List<Parameter> headerParameters = new ArrayList<>();
    headerParameters.add(new ParameterBuilder().name("If-None-Match").description("E-Tag value")
      .modelRef(new ModelRef("string")).parameterType("header").required(false).build());
    if (apigeeEndpoint) {
      headerParameters.add(new ParameterBuilder().name("Authorization")
        .description("Forge Authorization Header (i.e. Bearer <<AccessToken>>)")
        .modelRef(new ModelRef("string")).parameterType("header").required(true).build());
    } else {
      headerParameters.add(
        new ParameterBuilder().name("x-ads-token-data").description("x-ads-token-data Header")
          .modelRef(new ModelRef("string")).parameterType("header").required(true).build());
    }
    headerParameters.add(new ParameterBuilder().name("Content-Type").description("Content Type")
      .modelRef(new ModelRef("string")).parameterType("header").required(false).build());

    docket.globalOperationParameters(headerParameters);
    return docket;
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder().title("CP Drive Service API")
      .description("REST API for CP Drive Service API").termsOfServiceUrl("http://springfox.io")
      .contact(new springfox.documentation.service.Contact("CP Drive", "https://wiki.autodesk.com",
        "<<drive>>@autodesk.com")).license("Apache License Version 2.0")
      .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0").version("1.0").build();
  }
}
