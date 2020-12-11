# Home Improvement - Auth-Service
This project ist part of the HomeImprovement application and provides a backend service to authenticate and authorize users. This service provides a RESTful API to save users in a database. Furthermore, the API can be used to authenticate and authorize users. After a sucessfull authorization, the service returns a JSON Web Token (JWT) that can be used to use protected services.

## How to configure the project
To run the project, you need to install the following technologies:
- Java
- Maven
- Postgreß
- Application Server (Payara Micro is recommended)

After the sucessfull installation of these technologies, you can clone this project and open it in your preferred IDE (e.g. Apache NetBeans). As a first step you have to create a database in postgreß. Then you have to open the postboot file in the root directory of this project and change the database name to the name of the database you just created and the postgreß username to the username of your postgreß user.

## How to run the project
To run the project you need an application server. For this purpose, Payara Micro is recommended. Before you are able to deploy the application, make sure to create a .war file with the command `mvn package`. This package then can be deployed to the application server. In the case that the this project and the payara micro jar file are in the same folder you can deploy the application with the following command: `java -jar payara-micro-5.2020.6.jar --postbootcommandfile authservice/postboot --addlibs authservice/postgresql.jar --deploy authservice/target/authservice.war`. Make sure that the path and the payara micro jar file are written correctly.

## Documentation
When the service is running, you can use the REST-Endpoints to use the functionality.
1. http://localhost:8080/authservice/data/auth/singup with http method POST and the body:
`{
    "username": "",
    "email": "",
    "password": ""
}`. After a successfull registration of the user the endpoint returns an Account object with http status 200. Otherwise you will get http status 409 (Conflict).
2. http://localhost:8080/authservice/data/auth/login with http method POST and the body:
`{
    "email": "",
    "password": ""
}`. After providing the correct credentials a jwt is returned.
