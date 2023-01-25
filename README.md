# TourGuide

<p align="center">
  <img src=https://user-images.githubusercontent.com/95872501/208905875-1a8da33a-dc57-4ddc-9342-e7e6ab9bbb8d.png>
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

# Prepare the Database

- Install [MySQL](https://dev.mysql.com/downloads/mysql/)

- Open a command prompt to start MySQL server :
  - Enter this command replacing the `???` by your username : 
    ```
    mysql -u ??? -p
    ```
  - Type your password and press `enter` to start the server.
  
- Copy/paste SQL scripts :

  - Use this [script](https://github.com/HashTucE/Poseidon-Inc/blob/develop/src/main/resources/doc/schema.sql) to create the datatbase.

  - Use this [script](https://github.com/HashTucE/Poseidon-Inc/blob/develop/src/main/resources/doc/data.sql) to create an admin and a user.

- Additional informations :
  - Then to sign in with any of these 2 first account, use the password `&&`.
  - I recommend to use first the account of `admin` because his role give him access to user management.

# Run the Application

- The datasource is set to `src/main/resources/application.properties` : 
```
spring.datasource.url=jdbc:mysql://localhost:3306/paymybuddy?serverTimezone=UTC
```

- Open a command prompt, once located to the root of the project, run the following command replacing `???` by your username and your password of datasource : 
```
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.datasource.username=??? --spring.datasource.password=???"
```
- And finally open your browser to access to this URL : http://localhost:8080/
- Then you can create a new account or use an existing from data script, ENJOY !
- Stop the application in the command prompt with : `CTRL + C`

# UML Diagram
![uml](https://user-images.githubusercontent.com/95872501/209088546-ebcaa663-b75a-46ae-bece-fe31cd995b37.png)

# JaCoCo Code Coverage
![Capture d’écran 2022-12-22 à 09 19 43](https://user-images.githubusercontent.com/95872501/209089621-20ac00aa-46ea-466f-98a9-ac8d7be3ecbf.png)

# Technology Stack
![Capture d’écran 2022-12-05 à 01 19 56](https://user-images.githubusercontent.com/95872501/205524881-6a809029-414e-4a1f-b339-15154421f01a.png)
