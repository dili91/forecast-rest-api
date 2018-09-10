## Forecast application 

### Objectives
Aim of this Java based application is to retrieve average forecast weather metrics of a specific city. The application interacts with OpenWeather REST API to fetch raw data and compute the following metrics:

* Average daily (6h-18h) temperature in Celsius for the 3 days following the user's request;
* Average nightly (18h-6h) temperature in Celsius for the 3 days following the user's request;
* Average pressure for the 3 days following the user's request.

The above metrics are exposed through a REST API available at ${endpoint}/weather/data endpoint. A detailed documentation of the API is provided through Swagger v2 specification, which is available at ${endpoint}/swagger-ui.html. 

### Assumptions

**TO DO TO DO**

### Technology choice
I decided to use Spring framework to build the application as it's an opinionated and widely used framework which comes with a rich set of tools and pluggable integrations for RESTful webservices development, validation, testing, documenting rest apis and caching. The project is built starting from a very minimal Spring boot project Maven scheleton. By default it comes with:

* Spring libraries for RESTful services development;
* Java Bean Validation;
* JUnit unit testing framework;
* Spring caching framework **TO DO TO DO**


Other than the above technologies I decided to plugin Springfox library to provide an embedded and meaningful Swagger v2 documentation of REST apis exposed. 

The project requires Java >= 8 runtime as it leverages on lambda expressions to compute metrics averages. I used Eclipse Photon EE as IDE and Maven to manage all project's dependecies.

Finally I used Postman client and its CLI runner Newman for Integration testing.

The above technology ecosystem made it possibile to implement the application requirements with a quick minimal effort, without affecting code's quality and readability.


### Running the application
The application can be executed in different ways: from a Java IDE, with Maven , using java CLI or deploying a WAR file into an existing application server. 

One of the first 3 options is suggested for development/testing purposes. In such cases Spring boot project will startup an embeded Tomcat server to publish the application on the default 8080 port. If required, it's possible to change the default port adding the following line to the application.properties file contained in the resources folder of the project: 

`server.port = ${custom port}`

#### Java IDE
The simplest and quickest approach is to import project's root folder into a Java IDE, like Eclipse IDE or IntelliJ IDEA. This approach is valid only for development/testing purposes. 

To import and run the project into Eclipse IDE: 

1. Import the project's root folder as Existing Maven project;
2. Run the project as Java application.

An embedded Tomcat server will start and logs will be available inside Eclipse Console panel.

#### Maven 
Spring boot project comes with a mvnw utility file that is available in the root folder of the project. The utility simply wraps Maven commands to package the application and runs it using Spring Boot maven plugin.

To run the application on Unix systems run the below command: 

`./mvnw spring-boot:run`

If you're running on Windows you can find a similar mvnw.mcd file at the same location.

#### Java CLI
Maven is required to build the application. In order to do so please run the following: 

`./mvnw clean package`

Once built, a jar containing the application will be available inside ./target folder. To run the application using the Java CLI please run the following: 

`java -jar target/weather-0.0.1-SNAPSHOT.jar` 

#### Deploying to an existing application server
Please refer to [this](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#build-tool-plugins-maven-packaging) Spring resource for reference
 
### Testing the application
#### Manual checks
**TO DO TO DO**
#### Unit testing 
**TO DO TO DO**
#### Integration testing
**TO DO TO DO**

