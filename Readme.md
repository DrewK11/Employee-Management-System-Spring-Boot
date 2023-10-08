// README.md
# Employee Management System as a Spring Boot exercise
### Introduction
This REST API was made with Spring Boot 3.0, and features CRUD operations to create, read, update, and delete employees. Also included are unit tests for the Service and Controller layer, as well as integration tests. The Builder pattern was used for the Employee DTO, and Jib was used to containerize everything.
### Building & Running 
* Clone this repository [here](https://github.com/DrewK11/Spring-Boot-REST-api.git).
* Cd into the project root folder.
* Then we can use Spring Boot & Maven to run the app like so:
```
mvn clean spring-boot:run
```
### Usage
* Once the app is running, you can make requests to the API. Here are some examples:
### API Endpoints
| HTTP Verbs | Endpoints | Action |
| --- | --- | --- |
| GET | /api/employees/{id} | To retrieve an employee with the specified {id} |
| GET | /api/employees | To get all non-deleted employees |
| POST | /api/employees | To create a new employee |
| PUT | /api/employees/{id} | To update an employee with the specified {id} |
| DELETE | /api/employees/{id} | To delete an employee with the specified {id} |

* Please include a JSON body when creating or updating an employee. Please see below for an example:
``` 
{
    "firstName": "John",
    "lastName": "Doe",
    "email": "John@gmail.com",
    "age": 20,
    "phone": "012345678910"
}
```
### Technologies Used
* Spring Boot 3.0
* Java 17
* MySQL
* JUnit & Mockito
* Jib
### Structure
The basic structure for the project is as follows:
- **controller** : REST controller
- **service** : Service classes containing business logic
- **repository** : JPARepository (implemented by Spring Boot)
- **entity**: Data model mapped to database table
- **dto**: Data transmission model