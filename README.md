# Bike Rental App

This repository contains source code for the back-end (Spring Boot 3 + PostgreSQL) of the Bike Rental App
project. Recommended to run and inspect with
[IntelliJ IDEA](https://www.jetbrains.com/idea/)

_**Link to repository of the Android app for this project: [bike-rental-app](https://github.com/Thomas-Hoang-04/bike-rental-app)**_

### Contributor

- **Hoàng Minh Hải** - _GitHub:
  [Thomas-Hoang-04](https://github.com/Thomas-Hoang-04)_

### Tools & Techstack

<p>
  <img alt="kotlin" src="https://img.shields.io/badge/-Kotlin-purple?style=for-the-badge&logo=kotlin&logoColor=white"/>
  <img alt="spring-boot" src="https://img.shields.io/badge/-Spring%20Boot-green?style=for-the-badge&logo=spring-boot&logoColor=white"/>
  <img alt="spring" src="https://img.shields.io/badge/-Spring%20Security-green?style=for-the-badge&logo=spring&logoColor=white"/>
  <img alt="auth0" src="https://img.shields.io/badge/-Auth0-black?style=for-the-badge&logo=auth0&logoColor=white"/>
  <img alt="hibernate" src="https://img.shields.io/badge/-Hibernate-gray?style=for-the-badge&logo=hibernate&logoColor=white"/>
  <img alt="postgres" src="https://img.shields.io/badge/-PostgreSQL-blue?style=for-the-badge&logo=postgresql&logoColor=white"/>
  <img alt="twilio" src="https://img.shields.io/badge/-Twilio-red?style=for-the-badge&logo=twilio&logoColor=white"/>
  <img alt="git" src="https://img.shields.io/badge/-Git-gray?style=for-the-badge&logo=git"/>
  <img alt="github" src="https://img.shields.io/badge/-GitHub-black?style=for-the-badge&logo=github&logoColor=white"/>
  <img alt="docker" src="https://img.shields.io/badge/-Docker-blue?style=for-the-badge&logo=docker&logoColor=white"/>
  <img alt="intellij" src="https://img.shields.io/badge/-IntelliJ%20IDEA-orange?style=for-the-badge&logo=intellij-idea&logoColor=white"/>
</p>

### Prerequisites

- PostgreSQL is used as the primary database. Make sure to have
  PostgreSQL server installed locally on your machine or implement a cloud-based service
- Both the `bike` database (for information on bikes and bike stations) and the `user` (user credentials & information) database must be set up
  in order for the app to run as intended. Refer to the respective modules configuration and _**Guidelines - Section 4**_ for more information
- For the OTP verification service, this project uses Twilio to distribute SMS. Register for a free account
  [here](https://www.twilio.com/try-twilio) and obtain the necessary credentials to run the service
- For the HTTPS protocol, this project uses a self-signed SSL certificate. Make sure to have a valid certificate
  or generate a new one. You can refer to [this guide](https://www.youtube.com/watch?v=eBEq0Kv7vsw) for more details
- A secret key is required to facilitate proper password encoding. Recommended to use an SHA-256 or SHA-512 random hash as a key.

Follow instructions in _**Guidelines - Section 4**_ for additional configuration

### Guidelines

1. To run this project, open Terminal, point to a desired directory, and clone
   the project with the provided command

   ```
   git clone https://github.com/Thomas-Hoang-04/bike-rental-app_be.git
   ```

2. Download [IntelliJ IDEA](https://www.jetbrains.com/idea/download)

3. Open the project with IntelliJ IDEA. The IDE will automatically sync the
   project with included build files and import/download necessary dependencies.

   If the project does not sync upon being imported to IntelliJ IDEA, locate the top menu bar and find option _Build > Sync Project with Gradle Files_

4. Set up the prerequisites. Then, create a `secret.yaml` file in the `src/main/resources` directory. 
   The structure of this file will be as followed

    ```
    spring:
      datasource:
        bike:
          url: <your-postgres-bike-database-url>
          driver-class-name: org.postgresql.Driver
          username: <your-postgres-database-username>
          password: <your-postgres-database-password>
    
        user:
          url: <your-postgres-user-database-url>
          driver-class-name: org.postgresql.Driver
          username: <your-postgres-database-username>
          password: <your-postgres-database-password>
    
    twilio:
      account-sid: <your-twilio-id>
      auth-token: <your-twilio-auth-token>
      phone-number: <your-twilio-phone-number>
    
    pwd:
      secret-key: <a-random-SHA-key>
    
    server:
      ssl:
        key-store: <path-to-your-keystore.p12-file>
        key-store-password: <your-keystore-password>
        key-alias: <your-keystore-alias>
        key-store-type: PKCS12
        enabled: true
    ```

    Fill this file with your own database connection information and necessary credentials

5. To run and debug the app, find the section described in the image below and
   press the Play button to build and initiate the back-end server

   ![Run the app here](src/main/resources/Run.png)

   This repository is configured to run the Spring Boot app on an embedded Tomcat server, using port 443 with HTTPS protocol. Once activated, the server will start and listen for incoming requests.

> _**Notes:** This repository also provides a Dockerfile script to generate a Docker image of this application,
which can be used for cloud deployment._    

### Project Structure

- _**General project structure**_
    ```
    ├── src/
        ├── main/ - Main project code
            ├── kotlin/com/cnpm/bikerentalapp - Primary package of this app
                ├── config/ - Application config
                    ├── exception/ - Custom exception models & handlers
                    ├── httpresponse/ - Custom HTTP response model
                    ├── jwt/ - API key (JSON Web Token - JWT) parser & handler
                    ├── security/ - Security configuration for the entire app
                    ├── utility/ - General utilities 
                ├── bike/ - Module "bike" for bike infomation management & communication
                ├── otp/ - Module "otp" for OTP verification service
                ├── station/ - Module "station" for station infomation management & communication
                ├── user/ - Module "station" for authentication & user infomation management
                ├── BikeRentalApp.kt - Entry point of the app
            ├── resources/ - Contain application resources 
                (app properties, environment variables, certificate, ...)   
        ├── test/ - General unit test snippets
    ├── gradle/wrapper/ - Gradle environment variables & metadata
    ├── .gitignore - List of files & directories ignored by Git versioning
    ├── build.gradle.kts - Gradle build file & dependencies manager for the entire project
    ├── Dockerfile - Script to generate a Docker image for this application (deployable to a cloud service)
    ├── LICENSE - Apache-2.0 license for this app
    ├── README.md - This documents
    ├── settings.gradle.kts - Gradle build settings
    ```

- _**Module structure**_

    The backbone of this app comprises 4 module - `bike`, `station`, `otp` and `user`, each with their respective purpose and functionality. 
    These modules implements certain aspects of the MVC (Model-View-Controller) design pattern.
    Therefore, their structure adheres _(either partially or entirely)_ to what is described in this graph below
    
    ```
    ├── <module>
        ├── config/ - Module config (database, connection to external service, ...)
        ├── controller/ - Controller with API endpoints for communication with the Android app
        ├── model/ - Data models & Entities. Structure the data packages according to the database schemas
            and the DTO (Data Transfer Object) pattern. Dictate which data is accessible to the user
        ├── repository/ - Data repository. Contain methods and custom queries to traverse the database
        ├── services/ - Script to set up a Service, a middle-man between Repository and Controller that process 
            and manipulate the provided data to match the user and system requirements
        ├── utility/ - Data processing utilities
    ```
    
    The `user` module contain an additional `principal` package to implement and manage user principals based on Spring Security specification

