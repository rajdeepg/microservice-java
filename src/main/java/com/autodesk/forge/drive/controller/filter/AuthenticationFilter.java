/*
 * Copyright 2019 Autodesk, Inc. All Rights Reserved.
 *
 * This computer source code and related instructions and comments are the unpublished confidential
 * and proprietary information of Autodesk, Inc. and are protected under applicable copyright and
 * trade secret law. They may not be disclosed to, copied or used by any third party without the
 * prior written consent of Autodesk, Inc.
 */

package com.autodesk.forge.drive.controller.filter;

import com.autodesk.forge.drive.model.RequestData;
import com.autodesk.forge.drive.model.TokenData;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

@Component public class AuthenticationFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

  private final ApplicationContext applicationContext;
  private final ObjectMapper objectMapper;
  private final Pattern whitelistApiPathRegEx;

  @Autowired public AuthenticationFilter(ApplicationContext applicationContext,
    @Value("${setting.whitelist-api-path}") String whitelistApiPath) {
    this.applicationContext = applicationContext;
    this.whitelistApiPathRegEx = Pattern.compile(whitelistApiPath);
    this.objectMapper =
      new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  @Override public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
    FilterChain chain) throws IOException, ServletException {
    HttpStatus httpStatus = beginRequest(request, response);
    if (HttpStatus.OK == httpStatus) {
      chain.doFilter(request, response);
    } else {
      response.setStatus(httpStatus.value());
    }

    endRequest(request, response);

    if (HttpStatus.OK != httpStatus) {
      response.sendError(httpStatus.value(), httpStatus.getReasonPhrase());
    }
  }

  private HttpStatus beginRequest(HttpServletRequest request, HttpServletResponse response) {

    String requestURI = request.getRequestURI();
    String requestMethod = request.getMethod();
    logger.info("Authorizing request [{}] [{}]", requestMethod, requestURI);

    if (whitelistApiPathRegEx.matcher(requestURI).lookingAt()) {
      return HttpStatus.OK;
    }

    RequestData authenticationRequestData = applicationContext.getBean(RequestData.class);

    // We *always* expect x-ads-token-data
    String xAdsTokenData = request.getHeader("x-ads-token-data");
    if (StringUtils.isEmpty(xAdsTokenData)) {
      logger.error("Missing xAdsTokenData");
      return HttpStatus.UNAUTHORIZED;
    } else {
      try {
        // Attempt to convert to JSON
        TokenData tokenData = objectMapper.readValue(xAdsTokenData, TokenData.class);
        authenticationRequestData.setTokenData(tokenData);
      } catch (java.io.IOException e) {
        logger.error("Invalid xAdsTokenData [{}]", e);
        return HttpStatus.BAD_REQUEST;
      }
    }

    return HttpStatus.OK;
  }

  private void endRequest(HttpServletRequest request, HttpServletResponse response) {
    return;
  }
}
