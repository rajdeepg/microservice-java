# Microservice - Java Architecture 

## A simple/consistent Java archtiecture for building scalable microservices.

Java
Spring/Spring Boot
Spring Fox - For Swagger
Spring MVC
Separation of Concerns

## We want to follow a design pattern where we have 3 layers of responsibilities.

* **Controllers** - This provides the REST API Implementation.  Its primary purpose to generate the REST API, validate the incoming input, call the appropriate provider classes to perform the operation, and format the output response.
* **Providers** - This is where all the Business logic of the system will reside.  It should use the appropriate services when performing an operation.  Its main purpose is to orchestrate the different services to perform the given operation.
* **Services** - This is where a specific component exposes its APIs.

## Singleton Architecture
We have decided to use a Singleton architecture for all business objects.  By doing this, when requests come to the service, it will not require these complex business objects to be created since they would have already been created resulting in better performance and reduction in garbage collection cycles.  Spring by default creates all beans as Singleton unless explicitly specified otherwise.  All business classes should be registered with Spring.

Business objects are expensive to create.  They tend to have lots of dependent business objects.  With singletons, these business objects only need to be created once.
Forces developers to not store state in Business Objects so the service can better horizontally scale.

## No State on Web Servers
We will be storing no state on the Web Servers.  This will provide the following benefits.

Fault Tolerant - If a particular EC2 instance goes down, another can handle the user's request without any loss of functionality.
No Session Affinity - User requests can be routed to any Web Server that is available
No Custom Load Balancer - We do not have to write any custom load balancer to maintain session affinity.

## Swagger Documentation - Spring Fox
We have decided to use Spring Fox.  Spring Fox allows you to annotate your controller API and generate swagger documentation dynamically from code.  Here is an example of a project that uses Spring Fox.  With Spring Fox, we can easily configure swagger not just to provide documentation but as a mechanism to test the REST APIs.  

## Lombok
We have decided to use Lombok that automatically plugs into your editor and build tools so that you write less code.

 Never write another getter or equals method again.
Supports Spring writing constructor injection (i.e. @RequiredArgsConstructor(onConstructor = @__(@Autowired))).

## Spring MVC vs. Spring Flux
We have decided to use Spring MVC instead of Spring Flux.  The main reason for choosing Spring MVC is that we have better control of the thread pool and the fact that there numerous third part dependency currently do not support a Flux based implementation.  After talking with some folks in Pivotal they also recommended to use a Spring MVC approach for what we are doing.

Spring MVC - Based on thread pool
Spring Flux - Based on event pool


## Completable Futrues
CompletableFuture is used for asynchronous programming in Java. Asynchronous programming is a means of writing non-blocking code by running a task on a separate thread than the main application thread and notifying the main thread about its progress, completion or failure.  For our providers we should implement them as Completable Futures.  This way the callers (i.e. Controllers) can properly orchestirate the Providers in what makes sense to them.  In some cases the controller can call multiple Provider APIs in parallel or in other cases the controller can call the provider APIs in sequence.

## Aspect-Oriented Programming
We should use AOP (Aspect Oriented Programming) when it makes sense.  A good example of when to use AOP is for logging.  We do not have to write log statements for all our methods and will be automatically taken care of if we create a Logging AOP class.  This class can handle all uncaught exception, log arguments for methods (excluding PII info), and will keep logging very consistent.  The rest of the code does not have to be sprinkled with logging statements.

## Exception/Error Handling
We need a generic way to handle exceptions and error handling.  We can create a generic Exception handler class that maps all unhandled exceptions to user facing HTTP Status codes.  If the Exception Handler gets an unhandled exception it returns (418 - I am a Teapot) to signify that we have not mapped this exception to a user facing HTTP Status code.  The default response code should be 418, so we can track all that pass through, and assign them the correct response code. 418's indicate a condition we have not expected.  The JSON response also includes a tracking id so we can use that to check Splunk for more detailed information regarding the error.  All Splunk logs will include this tracking id.

