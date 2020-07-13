#### This is WIP 

### What is done

- setting the docker for postgresql with initial database schema
- generate password method based based user input, namely 'author'.
  
### What was being done at this moment
- adding database access layer

### What is left to do
- add other methods like `delete`, `create` cards

### How to run

- run the application by writing on your console `sbt runMain`
- curl the endpoint `curl localhost:9000/generatepassword?author=love`
