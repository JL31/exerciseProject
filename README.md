# Exercise project

## Intro
A simple project to test REST API development with Java. The idea is to develop (CRUD) tools handle sport exercises.

## Configuration
* Language : Java version 21
* Framework : Spring Boot
* Build tool : Maven

## Test
### Setup
To test this project simply download it and use the classic command (in a CLI) : `docker-compose up --build`<br/>
The prerequisites is for you to have Docker and docker-compose (in a recent version) installed on your machine. Then you can use a REST client (such as Postman) to test the developed APIS.

### Test
Once the server is up you can test the APIs. For now only two have been implemented : create an exercise and get an existing exercise. Here are some commands to test them :
* Create an exercise (it will return the UUID of the created exercise) :
```
curl --location 'http://localhost:8080/api/exercise' \
  --header 'Content-Type: application/json' \
  --data '{
  "name": "push-ups",
  "category": "big muscles !",
  "duration": 0,
  "numberOfSeries": 10,
  "restDurationBetweenSeries": 30,
  "numberOfRepetitions": 0,
  "distance": 0
  }
  '
```
* Get an existing exercise :
`̀``
curl --location 'http://localhost:8080/api/exercise/{exerciseUuid}'
```
Replace the `{exerciseUuid}` with an UUID fetched after an exercise creation.
