# Messenger

This is a simple chatting system that allows multiple clients to chat among themselves. Clients can send message to a particular client (unicast) or to all clients (broadcast) that are connected to the server.

## Project Structure

General gradle project structure is followed. Check [this](http://gradle.org/docs/current/userguide/multi_project_builds.html) for more information.

Main class of a project should be named as `Main.java` and should reside in `$project-name/main/src/java/io/github/halfo/` directory (because, that's how `build.grade` is configured!). Add your project name to `settings.gradle`.


## Building Projects

Fire the following command to build the projects:
```
gradle assemble
```

The jars are in `$project-name/build/lib/`directory. To run a jar, type:
```
java -jar $project-name/build/lib/$project-name.jar
```

To create the Eclipse/Idea specific descriptor files, add corresponding plugin and follow it's commands. To clean the project, type:
```
gradle clean
```


## Contribution

All forms of contribution (bug reports, bug fixes, pull requests and simple suggestions) are welcomed. Thanks!
