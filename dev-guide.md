### Add new client to server command

1. Declare a new input command type in `command/CommandType.java`

2. Add a new class inside `clientserver/command/clienttoserver` package by extending the `command/ExecutableCommand.java` class
   1. Declare all the fields corresponding to json in the class
   2. Implement the `execute` method.
   
   See `clientserver/command/clienttoserver/NewIdentityC2SCommand.java` for an example.

3. Add a new case in `clientserver/command/clienttoserver/C2SCommandFactory.java`
