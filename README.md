# Lunchbus
Decide where the lunchbus goes. Play project to learn Spring Boot + websockets + AngularJS + Groovy.

My goal is to learn Spring Boot and websockets primarily, and also to see what does an application relying solely on
websockets look like and its strengths/weaknesses.

## Features

- [x] IOU list (done)
- [ ] Places list
- [ ] People going
- [ ] Choose a place randomly, restricting by preferences of those going

## Running

    gradlew bootRun

Then, go to http://localhost:8080/ to see the application.

Another method:

    gradlew build
    java -jar build\libs\lunchbus-0.1.0.jar

## License

Copyright by Jason Winnebeck, 2016. Licensed under Apache 2.0 License.