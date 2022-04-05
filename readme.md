## Supported Java version

The project is configured to support Java 17.


- Make sure you are using JRE version 17 by running:

      `java -version`

- Make sure you are using JDK version 17 by running:

      `javac -version`


- Make sure the JAVA_HOME environment variable points to the installation location of JDK version 17.

  - on Linux: 
        
        `echo $JAVA_HOME`
  
  - on Windows: 
      
        `echo %JAVA_HOME%`


## Build

From the root of the project, run:

- on Linux:

      `./mvnw clean pacakge`

- on Windows:

      `mvnw.cmd clean package`


A single executable jar file (which includes all dependencies) named `chat-app.jar` will be created in the `target` directory.


## Run

- Go into the `target` directory.

      `cd target`

- Run the `chat-app.jar` file.

      `java -jar chat-app.jar -i server_id -o servers_conf_file [-t socket_timeout_in_ms]`

    Command Line Arguments:

| Option | Long Option | Required | Description                                                        |
| ------ |-------------| -------- |--------------------------------------------------------------------|
| -i     | --serverid  | Yes      | Server ID                                                          |
| -o     | --servers_conf   | Yes      | Path to a text file containing the configuration of servers        |
| -t     | --socket_timeout  | No       | Socket timeout in milliseconds. Default is 1800000 ms (30 minutes) |
