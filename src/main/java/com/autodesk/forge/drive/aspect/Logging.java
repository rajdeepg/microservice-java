package com.autodesk.forge.drive.aspect;

import com.autodesk.forge.drive.controller.filter.AuthenticationFilter;
import com.autodesk.forge.drive.model.RequestData;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Aspect @Configuration public class Logging {
  // Information
  private static final org.slf4j.Logger logger =
    LoggerFactory.getLogger(AuthenticationFilter.class);
  private final ApplicationContext applicationContext;
  private final Set<String> argumentsIgnored = new HashSet<>(); // arguments ignored due to PII

  @Autowired public Logging(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    argumentsIgnored.add("password");
    argumentsIgnored.add("username");
    argumentsIgnored.add("address");
    argumentsIgnored.add("phonenumber");
    argumentsIgnored.add("mobilenumber");
    argumentsIgnored.add("firstname");
    argumentsIgnored.add("lastname");
    argumentsIgnored.add("middlename");
  }

  @Before("execution(* com.autodesk.forge.drive.controller.*.*(..))")
  public void beforeRequest(JoinPoint joinPoint) {
    before("Before.Request", joinPoint);
  }

  @AfterReturning(value = "execution(* com.autodesk.forge.drive.controller.*.*(..))", returning = "result")
  public void afterRequest(JoinPoint joinPoint, Object result) {
    after("After.Request", joinPoint, result);
  }

  @AfterThrowing(value = "execution(* com.autodesk.forge.drive.controller.*.*(..))", throwing = "ex")
  public void throwingRequest(JoinPoint joinPoint, Throwable ex) {
    throwing("Throwing.Request", joinPoint, ex);
  }

  private List<Object> argumentsAsArray(JoinPoint joinPoint) {
    List<Object> arguments = new ArrayList<>();

    String[] names = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
    Object[] values = joinPoint.getArgs();

    Boolean haveNames = (null != names);
    for (int i = 0; i < values.length; i++) {
      if (haveNames && argumentsIgnored.stream().anyMatch(names[i]::equalsIgnoreCase)) {
        arguments.add("<redacted>");
      } else {
        arguments.add((null == values[i]) ? "null" : values[i].toString()); // Simplify the logging
      }
    }
    return arguments;
  }

  // RequestData has the TrackingId as well as Logging Level (by endpoint)
  private RequestData getRequestData(JoinPoint joinPoint) {
    return applicationContext.getBean(RequestData.class);
  }

  private void before(String event, JoinPoint joinPoint) {
    Map<String, Object> map = new LinkedHashMap<>(); // We like order
    map.put(joinPoint.getSignature().toShortString(), argumentsAsArray(joinPoint));
    logger.info("Logging [{}] TrackingId [{}] Map [{}]", event,
      getRequestData(joinPoint).getTrackingId(), map);
  }

  private void after(String event, JoinPoint joinPoint, Object result) {
    Object value = (result == null) ? "null" : result.toString(); // Simplify the logging
    Map<String, Object> map = new LinkedHashMap<>(); // We like order
    map.put(joinPoint.getSignature().toShortString(), value);
    logger.info("Logging [{}] TrackingId [{}] Map [{}]", event,
      getRequestData(joinPoint).getTrackingId(), map);
  }

  private void throwing(String event, JoinPoint joinPoint, Throwable ex) {
    Map<String, Object> map = new LinkedHashMap<>(); // We like order
    map.put(joinPoint.getSignature().toShortString(), ex.toString());
    logger.info("Logging [{}] TrackingId [{}] Map [{}]", event,
      getRequestData(joinPoint).getTrackingId(), map);
  }
}
