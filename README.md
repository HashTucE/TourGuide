# TourGuide

<p align="center">
  <img src="https://imagizer.imageshack.com/img923/5501/26aD16.png">
</p>

TourGuide is an application for tourists. It takes advantage of GPS location to allow its users to discover nearby attractions. It also incorporates a system that rewards visitors with points redeemable for discounts on tourism players.

Touristic API with the following features :
- Show user location
- Establish the list of the 5 closest attractions to the user
- Show the rewards list of a user
- Show users location
- Establish the list of 5 provider offers taking into account user preferences

# Prerequisites

- Java 19
- Gradle 7.6


# Run the Application

- Open a command prompt, once located to the root of the project, run the following command : 

Runs this project as a Spring Boot application
```
gradle bootRun
```

- Stop the application in the command prompt with : `CTRL + C`

This are additional tasks runnable from root project :

**Build tasks**

- assemble - Assembles the outputs of this project.
- bootJar - Assembles an executable jar archive containing the main classes and their dependencies.
- build - Assembles and tests this project.
- clean - Deletes the build directory.
- jar - Assembles a jar archive containing the main classes.

**Verification tasks**

- check - Runs all checks.
- jacocoTestCoverageVerification - Verifies code coverage metrics based on specified rules for the test task.
- jacocoTestReport - Generates code coverage report for the test task.
- test - Runs the test suite.


# Requests

- Install [Postman](https://www.postman.com/downloads/)

**Endpoints availables**

- GET : http://localhost:8080/
- GET : http://localhost:8080/getLocation?userName=
- GET : http://localhost:8080/getNearbyAttractions?userName=
- GET : http://localhost:8080/getRewards?userName=
- GET : http://localhost:8080/getAllCurrentLocations
- GET : http://localhost:8080/getTripDeals?userName=&attractionName=
- GET : http://localhost:8080/getPreferences?userName=
- PUT : http://localhost:8080/updatePreferences?userName=



# UML Diagram
![uml](https://imagizer.imageshack.com/img924/1951/HO29eq.png)

# Tests Report
![report](https://imagizer.imageshack.com/img923/3599/Y1sEkz.png)

# JaCoCo Code Coverage
![coverage](https://imagizer.imageshack.com/img923/108/tD7ULZ.png)

# Technology Stack
![stack](https://imagizer.imageshack.com/img922/1429/n2bAFu.png)
