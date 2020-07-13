#### This is WIP 
This is continuation work from the point I delivered (check `unfinished` branch)

I wanted to improve because it was not finished yet

### What is done

- `sign up` and `create cards` features
- `postgres` and `mini-blog` are dockerized
 
### What is left to do
- add other methods like `delete` cards

### How to run

- Execute `./runDockerCompose.sh`, it will start the docker with two containers(postgres, mini blog app)

- Sign up a new `author`
`curl --request POST "localhost:9000/signup?author=yourname"`

- Create a new card  
`curl --request POST "localhost:9000/create?name=name&content=content&category=category&author=yourname&password=generatedpassword"`

### stack

- sbt
- scala
- akka http
- postgresql
- docker
