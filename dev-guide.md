### Add new input command

1. Declare a new input command type in `command/CommandType.java`
2. Add a new class by extending the `command/inputcommand/InputCommand.java` class
   1. Declare all the fields corresponding to json in the class
   2. Implement the `execute` method
3. Add a new case in `command/inputcommand/InputCommandFactory.java`