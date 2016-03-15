# Lunchbus
Decide where the lunchbus goes. Play project to learn Spring Boot + websockets + AngularJS + Groovy + Redis.

My goal is to learn Spring Boot and websockets primarily, and also to see what does an application relying solely on
websockets look like and its strengths/weaknesses.

## Features

- IOU list (done)
- Places list, with tags
- People going
- Chatbox
- Choose a place randomly, restricting by preferences (tags) of those going
- Optionally use Redis (at localhost) as a backing store

Also totally unrelated to the lunchbus functionality, I have in there a log tail demo where a generator generates (at
random intervals) about 10 messages a second and sends them in batches 4 times a second to the client, which displays
them in a scrolling buffer, as a test case for high-throughput situations with websockets, and to make sure even under
load the application still does well. The log generation is only triggered if you click the link to show the logs,
otherwise there is no overhead from it.

## Running

You need Java 8 JDK installed on your system. While the project is built with Gradle, you do NOT need Gradle installed,
as Gradle projects come with a wrapper script that downloads the necessary version of Gradle first if needed.

    gradlew bootRun

Then, go to http://localhost:8080/ to see the application.

Another method:

    gradlew build
    java -jar build\libs\lunchbus-0.1.0.jar

You can also add `--server.port=9000` to change the port used to whatever you want.

If you'd like persistence, you can also use Redis. First, you need to install Redis at localhost and run it. It is
highly recommended you don't expose the server to the Internet, so you should enable the "bind 127.0.0.1" option within
the redis.conf file that you use. You can download Redis from http://redis.io/download (or for Windows users, at
https://github.com/MSOpenTech/redis/releases). For example on Windows no installation is needed, just download the zip
file, updated redis.windows.conf for the bind setting, then run `redis-server redis.windows.conf`.

To run lunchbus in Redis mode, just add `--app.useRedis=true` to the command line.

To run in IDE, just run the main class Application. For example in IntelliJ you open the build.gradle file as a project,
right click Application and select run.

## License

Copyright by Jason Winnebeck, 2016. Licensed under Apache 2.0 License.