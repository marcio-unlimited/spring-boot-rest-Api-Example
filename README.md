# Example Rest API - Spring

This is a restful API from Jobs and Tasks, built using Spring Framework, Spring boot, Spring Rest and an in memory database HSQLDB.
The project consists of Jobs, included in an unary tree structure, and task list that are related to a job.  It is possible to manipulate both Taks and Jobs using the HTTP verbs PUT, GET, POST, DELETE. 

# How to build this project

In the root directory using a command line tool execute the folowing commands

- To execute the test suit.
 `mvn test`


- To execute the application
`mvn run:spring-boot:run`

# How to use this application.

To consume all the endpoints and use the application there is an json import file from postman in the artifacts folder in the project root folder.

You can access it using the Postman app by importing the provided json file or accessing the url: [Postman Inter Test API](https://documenter.getpostman.com/view/4049196/S1TR5LCm)

You also can access all the endpoints using this curl commands:

# Create a new Job:
`curl --location --request POST "localhost:8080/jobs" \
  --header "Content-Type: application/json" \
  --data "{
  \"id\": 2,
  \"name\": \"Job 2\",
  \"active\": true,
  \"parentJob\": {
	  \"id\": 1,
	  \"name\": \"first job\",
	  \"active\": true,
	  \"parentJob\" : null
  },
  \"tasks\": [
    {
      \"id\": 3,
      \"name\": \"Third task\",
      \"weight\": 3,
      \"completed\": true,
      \"createdAt\": \"2015-05-23\"
    },
    {
      \"id\": 4,
      \"name\": \"Fourth task\",
      \"weight\": 1,
      \"completed\": false,
      \"createdAt\": \"2017-05-23\"
    }
  ]
}"
`
### Find a specific job:

`curl --location --request GET "localhost:8080/jobs/2" \
  --header "Content-Type: application/json"`
  
### List the Jobs sorting by task weight

`curl --location --request GET "localhost:8080/jobs?sortByWeight=true" \
  --header "Content-Type: application/json"`
  
### Change a Job
 
 `curl --location --request PUT "localhost:8080/jobs/2" \
  --header "Content-Type: application/json" \
  --data "{
  \"id\": 2,
  \"name\": \"Job 2 - 1\",
  \"active\": true,
  \"parentJob\": {
	  \"id\": 1,
	  \"name\": \"first job\",
	  \"active\": true,
	  \"parentJob\" : null
  },
  \"tasks\": [
    {
      \"id\": 3,
      \"name\": \"Third task\",
      \"weight\": 3,
      \"completed\": true,
      \"createdAt\": \"2015-05-23\"
    },
    {
      \"id\": 4,
      \"name\": \"Fourth task\",
      \"weight\": 1,
      \"completed\": false,
      \"createdAt\": \"2017-05-23\"
    }
  ]
}""`
  
### Remove a job

`curl --location --request DELETE "localhost:8080/jobs/2" \
  --header "Content-Type: application/json"`

### Create a new task

`curl --location --request POST "localhost:8080/tasks" \
  --header "Content-Type: application/json" \
  --data "{
  \"id\": 1,
  \"name\": \"First task\",
  \"weight\": 3,
  \"completed\": true,
  \"createdAt\": \"2018-05-23\"
}"`

### Find all tasks from a certain data onwwards.

`curl --location --request GET "localhost:8080/tasks?createdAt=05-05-2010" \
  --header "Content-Type: application/json"`

### Change a task

`curl --location --request PUT "localhost:8080/tasks/1" \
  --header "Content-Type: application/json" \
  --data "{
  \"id\": 2,
  \"name\": \"second task\",
  \"weight\": 10,
  \"completed\": true,
  \"createdAt\": \"2018-05-23\"
}"`

### Remove a task

`curl --location --request PUT "localhost:8080/tasks/1" \
  --header "Content-Type: application/json" \
  --data "{
  \"id\": 2,
  \"name\": \"second task\",
  \"weight\": 10,
  \"completed\": true,
  \"createdAt\": \"2018-05-23\"
}"`


