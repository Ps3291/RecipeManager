# Recipe Manager Web Service
This Web Application contains ReST APIs performing CRUD operations to Get(based on filters), Create, Update and Delete recipes from a database in a JSON response. This is a standalone application without any integration to frontend framework and API endpoints can be called through Tools like PostMan.

### Architecture Design
Recipe Manager Web Service is microservice based layered architectured RESTful Web Service based on SpringBoot Framework and can be deployed independently on premise/cloud.There are 4 layers:
- API Layer
  - Top layer and is main interface available for intgeration and interaction with front-end or end user to consume APIs
  - Contains API end points implementation
  - Springboot-starter-web module used as a framework to implement ReSTful api end points  
- Service Layer
  - Mainly responsible for interacting with Data Access Layer and transferring the recipes data as required by API and Data Access layers
  - Is a module to decouple business logic of recipes data transfer and mapping from/to API layer
- Data Access Layer
  - Springboot-starter-data-JPA  module is used to implement mappings between objects and tables
  - This layer contains recipe entity classes and JPA repositories which implement lower level functionality of storing/retrieving recipes  
- Persistence Layer
  - Bottom most layer, responsible for physically storing the recipes data onto database table
  - H2 In memory table : `RecipeEntity` is used to store the recipes for the application


### Frameworks/Tools Used
Function      | Framework          |
------------- | ------------------ |
ReSTful API   | SpringBoot         |
ORM           | Spring data JPA    |
Logging       | SLF4j              |
Java          | JDK 8              |
Test Framework| SpringBoot Test    |
Unit Tests    | Junit(AssertJ)     |
Build Tool    | Maven              |
Database      | H2                 |


### Steps to execute the application

* Download code as zip / `git clone https://github.com/Ps3291/RecipeManager.git`
* Traverse to `RecipeManager` folder containing pom.xml and import it in your IDE as maven project.
* Run the application as 'Spring Boot App' from 'RecipeManagerApplication' class located in 'com.recipe' package in the IDE. 
* Application.properties conatins the H2 database configuration, credentials and server.port as shown below. So please make sure that the server port 8081 is free or else update the Application.properties file with available port and re-run the application.
 
server.port=8081

spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.defer-datasource-initialization=true
spring.h2.console.enabled=true

* Unit test cases and Integration test cases are available under src/test/java/com/recipe

### ReST API End Points
Recipe manager Webservice has ReST API Ends points for getting all recipes, getting a specific recipe,creating a new recipe,updating a specific recipe, deleting an existing recipe and searching recipes based on one or more parameters. Below table lists and describes on the implemented ReST APIs:

API End Point      | Method| Purpose             | Request                        | Response                                                                                        |
------------------ | ----- | ------------------- | -------------------------------| ------------------------------------------------------------------------------------------------|
/recipe            | GET   | Get all recipes     | No body needed                 | Recipes as JSON list with 200 OK on success, 204 No Content on empty DB                         |/recipe/{id}       | GET   | Get recipe by ID    | Recipe id as path parameter    | Recipe JSON Model with 200 OK on Success, 404 Not Found on unavailable ID                       |
/recipe/create     | POST  | Create a new recipe | Recipe Model in JSON format    | Newly created recipe id with 201 CREATED successfully, 409 Conflict on duplicate primary key    |
/recipe/update     | PUT   | Update recipe by ID | Updated JSON Recipe Model      | Updated recipe id with 200 OK on successfull update,404 Not Found on invalid ID                 |
/recipe/delete/{id}|DELETE | Delete recipe by ID | Recipe id as path parameter    | Deletion message with 200 OK on success, 404 Not Found on invalid ID                            |
/recipe/search     | GET   | Search by parameters| Filtering fields as path params| Recipe Model list with 200 OK on success, 204 No Content for zero search results and 400 Bad request on missing query params from request URL |

*** Note : For the search recipe endpoint, the search parameters are : vegetarian(yes or no), servings, ingredients(included/excluded as mentioned in path parameter : include(true/false) and instructions. It is mandatory to add all the parameters in Request Path even if they are null. Path parameters ingredient and include work in conjuction since value of include defines if a a certain ingredient is to be included in the search or excluded from the search query.


### ReST End Points Models and example API calls
- **RecipeEntity Model**
  - JSON Schema
    ```
    {
	      "id": "recipe Id as integer value",
	      "name": "recipeName as string",
	      "vegeterian": "yes/no as string",
	      "servings": "number of servings as string",
	      "ingredients": "list of ingredient names with quantity as string",
	      "instructions": "Instructions for recipe as string"
    }
    ```
  - JSON Example 
    ```
    {
        "id": 7,
        "name": "Cheese Toast",
        "vegeterian": "yes",
        "servings": "8",
        "ingredients": "1 tbsp butter, 1 cup shredded Cheddar cheese, 1 cup shredded mozzarella cheese, 8 slices Bread",
        "instructions": "Preheat oven to 375 degree ferhenheit. Spread all slices of bread with 1 teaspoon softened butter; sprinkle with Cheddar and mozzarella cheese. Bake on a baking sheet until cheese melts and edges begin to brown."
    }
	```
- ReST API Calls and responses
  - GET request
	URL : http://localhost:8081/recipe/2
	- Response :
  ```
	{
    "id": 2,
    "name": "Coffee",
    "vegeterian": "No",
    "servings": "1",
    "ingredients": "1 Coffee, 1 Milk, 1 Sugar",
    "instructions": "Boil the water and coffee ready in 2 mins."
	}
  ```
  - POST request 
	URL : http://localhost:8081/recipe/create
	- Request Body :
  ```
	{
    "id": 9,
    "name": "Omlette",
    "vegeterian": "No",
    "servings": "1",
    "ingredients": "3 eggs beaten, 2 tsp butter, pinch of salt and pepper",
    "instructions": "SSeason the beaten eggs with salt and pepper. Melt butter in a non-stick frying pan over a low heat. Pour the eggs into the pan and cook till golden brown."
	}
  ```
    Response Body:
```
  Recipe created successfully with Id : 9
  ```
  - PUT request
	URL : http://localhost:8081/recipe/update
	- Request Body :
  ```
	{
    "id": 9,
    "name": "Omlettes",
    "vegeterian": "No",
    "servings": "3",
    "ingredients": "3 eggs beaten, 2 tsp butter, pinch of salt and pepper",
    "instructions": "SSeason the beaten eggs with salt and pepper. Melt butter in a non-stick frying pan over a low heat. Pour the eggs into the pan and cook till golden brown."
	}
  ```
  Response Body :
  ```
   Updated recipe with ID: 9
  ```
  - DELETE request 
	URL : http://localhost:8081/recipe/delete/9
	- Response Body :
  ```
   Deleted Recipe with ID : 9
  ```
  - GET request
	URL : http://localhost:8081/recipe/search?vegeterian=no&servings=1&ingredients=pumpkin&include=false&instructions=
   - Response Body :
  ```
    {
        "id": 6,
        "name": "Chicken Steak",
        "vegeterian": "No",
        "servings": "1",
        "ingredients": "4 chicken pieces, 2 Eggs (slightly whisked),1 tsp ginger garlic paste,1 cup onions, 2 tbsp coriander leaves, 2 green chillies,1 tsp black pepper powder,2 tbsp flour, 1 tbsp vinegar, salt, Oil",
        "instructions": "Flatten the chicken and mix with the other ingredients, except oil and marinate for 2 hours.Heat a non-stick pan with thin layer of oil.When hot, place chicken in it and cook till tender and brown on both sides."
    },
    {
        "id": 9,
        "name": "Omlette",
        "vegeterian": "No",
        "servings": "1",
        "ingredients": "3 eggs beaten, 2 tsp butter, pinch of salt and pepper",
        "instructions": "SSeason the beaten eggs with salt and pepper. Melt butter in a non-stick frying pan over a low heat. Pour the eggs into the pan and cook till golden brown."
    }  
  ```