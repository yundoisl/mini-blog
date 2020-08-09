### Introduction

This is mini blog app which performs CRUD operation with authentication via JWT by consuming REST APIs exposed by the back-end server. 

It consists of two services (`postgresql` and `mini-blog`) which run on docker container.

### Features

All functional requirement are satisfied:

- Sign up
- Authentication via JWT
- Login
- Create
- Update
- Delete

### Workflow showcase

This workflow briefly describes how `John`(author) uses the app.

1. `John` wants to `signup` to the app. So he sends `POST` request to `/signup` with his name on request body. The app will return the generated password once `John` is registered with success.

    Request
    ```
    curl --request POST 'localhost:9000/signup' \
    --header 'Content-Type: application/json' \
    --data-raw '{ "author" : "John"}'
    ```
    Response
    ```
    Your password has been generated: 851186384
    ```

2. `John` wants to `login` to the app, so he sends `POST` request to `/login` with login credentials. The app will return the response with JWT inside `X-Access-Token` header.

    Request
    ```
    curl --request POST 'http://localhost:9000/login' \
    --header 'Content-Type: application/json' \
    --data-raw '{ "author" : "John", "password": "851186384"}'
    ```   
    Response
    ```
    Successfully logged in
    header // X-Access-Token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huIiwiZXhwIjoxNTk3MDc4ODU2LCJpYXQiOjE1OTY5OTI0NTZ9.eLGPITDQg4gtzDdiuV0jkZAKNFmDJW33_5UBXtcY-Yg
    ```

3. `John` wants to `create` a new card, so he sends `POST` request to `/create` attaching JWT from the login response to `Authorization` header.
    Request
    ```
    curl --request POST 'localhost:9000/create' \
    --header 'Authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huIiwiZXhwIjoxNTk3MDc4ODU2LCJpYXQiOjE1OTY5OTI0NTZ9.eLGPITDQg4gtzDdiuV0jkZAKNFmDJW33_5UBXtcY-Yg' \
    --header 'Content-Type: application/json' \
    --data-raw '{"name": "My First Card", "content": "Hello, world", "category": "Books", "status": "Draft"}'
    ```
    Response
    ```
    Your card has been created with id : 1
    ```
   
4. `John` wants to `update` previously created card, so he sends `POST` request to `/update` attaching JWT to `Authorization` header and request body as below.
    
    Request
    ```
    curl --request POST 'localhost:9000/update' \
    --header 'Authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huIiwiZXhwIjoxNTk3MDc4ODU2LCJpYXQiOjE1OTY5OTI0NTZ9.eLGPITDQg4gtzDdiuV0jkZAKNFmDJW33_5UBXtcY-Yg' \
    --header 'Content-Type: application/json' \
    --data-raw '{"id": 1, "name": "Updated Card", "content": "Updated", "category": "Updated", "status": "Updated"}'
    ```
    Response
    ```
    Your card id: 1 has been updated
    ```

5. `John` wants to `delete` the card he just updated, so he sends `POST` request to `/delete` attaching JWT to `Authorization` header and request body as below.
   
    Request
    ```
    curl --request POST 'localhost:9000/delete' \
    --header 'Authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huIiwiZXhwIjoxNTk3MDc4ODU2LCJpYXQiOjE1OTY5OTI0NTZ9.eLGPITDQg4gtzDdiuV0jkZAKNFmDJW33_5UBXtcY-Yg' \
    --header 'Content-Type: application/json' \
    --data-raw '{"id": 1}'
    ```
    Response
    ```
    Your card id: 1 has been deleted
    ```
   
### How to run
    
- Execute `./runDockerCompose.sh`, it will start the docker with two containers(postgres, mini blog app)

### Tests

Integration test: It is done via docker
- BlogIntegrationTest
    
Unit tests:
- JwtServiceTest
- BlogControllerTest
    
### Stack

- scala 2.13.3
- akka http 10.2.0
- postgresql 11.7
- sbt 
- docker
