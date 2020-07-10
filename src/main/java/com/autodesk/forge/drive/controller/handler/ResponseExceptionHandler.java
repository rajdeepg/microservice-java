/*
 * Copyright 2018 Autodesk, Inc. All Rights Reserved.
 *
 * This computer source code and related instructions and comments are the unpublished confidential
 * and proprietary information of Autodesk, Inc. and are protected under applicable copyright and
 * trade secret law. They may not be disclosed to, copied or used by any third party without the
 * prior written consent of Autodesk, Inc.
 */

package com.autodesk.forge.drive.controller.handler;

import com.autodesk.forge.drive.model.ErrorData;
import com.autodesk.forge.drive.model.RequestData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;

@ControllerAdvice public class ResponseExceptionHandler extends ExceptionHandlerExceptionResolver {

  private static final Logger logger = LoggerFactory.getLogger(ResponseExceptionHandler.class);
  private final ApplicationContext applicationContext;
  private final ObjectMapper objectMapper;

  @Autowired public ResponseExceptionHandler(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    this.objectMapper =
      new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  // Common handler
  private ResponseEntity<ErrorData> InternalExceptionHandler(Exception ex, HttpStatus httpStatus,
    WebRequest request) {
    // Construct a response similar to that of HttpServletResponse.sendError()
    // Ours has 'timestamp' formatted as "2018-04-19T01:10:12.389Z" so we can
    // differentiate the two
    RequestData requestData = applicationContext.getBean(RequestData.class);
    ErrorData errorData = new ErrorData();
    errorData.setTrackingId(requestData.getTrackingId());
    errorData.setStatus(httpStatus.value());
    errorData.setStatusMessage(httpStatus.getReasonPhrase());
    String message = ex.getMessage();

    // If not a Server error, try to provide more details
    if (!httpStatus.is5xxServerError() && (null != ex.getCause())) {
      message += " " + ex.getCause().getMessage();
    }
    errorData.setMessage(message);

    // Include the StackTrace in our logs
    errorData.setStackTrace(ex.getStackTrace().toString());

    // Useful to help correlate reported failures to our logs
    try {
      logger.warn("End Rest API Exception [{}]", objectMapper.writeValueAsString(errorData));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    // Remove the StackTrace before we return this to callers.
    errorData.setStackTrace(null);

    return new ResponseEntity<>(errorData, httpStatus);
  }

  @ExceptionHandler(value = {Exception.class})
  protected ResponseEntity<ErrorData> GlobalExceptionHandler(Exception ex, WebRequest request) {

    HttpStatus status = null;

    // If exception is a CompletionException, then extract the actual exception
    // since it is wrapped.
    if (ex instanceof CompletionException) {
      ex = (Exception) ex.getCause();
    }

    // These first set of exceptions might be thrown by the Springframework *before*
    // the Controllers are called
    if (ex instanceof HttpMediaTypeNotAcceptableException) {
      status = HttpStatus.NOT_ACCEPTABLE;
    } else if (ex instanceof HttpMediaTypeNotSupportedException) {
      status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
    } else if (ex instanceof HttpMessageConversionException) {
      status = HttpStatus.BAD_REQUEST;
    } else if (ex instanceof HttpRequestMethodNotSupportedException) {
      status = HttpStatus.METHOD_NOT_ALLOWED;
    } else if (ex instanceof MissingServletRequestPartException) {
      status = HttpStatus.BAD_REQUEST;
    } else if (ex instanceof MissingServletRequestParameterException) {
      // "Required Boolean parameter 'PaginationV2' is not present"
      status = HttpStatus.BAD_REQUEST;
    } else if (ex instanceof MethodArgumentTypeMismatchException) {
      // "Failed to convert value of type 'java.lang.String' to required type
      // 'java.lang.Boolean'"
      status = HttpStatus.BAD_REQUEST;
    } else if (ex instanceof MultipartException) {
      status = HttpStatus.BAD_REQUEST;
    } else if (ex instanceof SecurityException) {
      status = HttpStatus.UNAUTHORIZED;
      // Lastly, we have our own errors...
    } else if (ex instanceof CancellationException) {
      // Completable Future might have been cancelled before completion
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    if (null == status) {
      // We refuse to brew coffee because ...
      status = HttpStatus.I_AM_A_TEAPOT; // So we can collect these
    }

    return InternalExceptionHandler(ex, status, request);
  }
}
