## This repo contains the SpringBoot Github Review Karma App we've made during our Java 21-3 course.

* This project requires __Java 16__ , __MAVEN__ and __DOCKER_COMPOSE__ build by execute in the project root:

* Clone project by execute:

`git clone git@github.com:neuefische/rem-21-3-github.git`
         
* Startup local database in docker container (startup local [Docker](https://www.docker.com/products/docker-desktop) before)

`docker-compose up -d`

* Build the backend with profile 'local' by

`mvn clean package -Plocal`

* Run the backend with profile 'local' by

`mvn spring-boot:run -Dspring.profiles.active=local
`

* This app will be automatically deployed at Heroku on pushes to the main branch. API available at

[Hosted at Heroku](https://rem-21-3-github.herokuapp.com/api/rem213)

* Swagger is available at:

[Local](http://localhost:8080/api/rem213/swagger-ui/)
