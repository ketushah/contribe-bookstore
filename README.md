# Contribe Book Store
IDE Used for Development: IntelliJ Idea

Java Version: 1.8.0_131

Application Run Instruction:

Main Class: App.java

Once cloning the repo into the IDE, set the run configuration to select App.java as main class.

The Application is very basic and can be improved in many ways. The structure of the application has been kept modular to accommodate future feature addition. 
The interaction with Application can be done by command prompt. The front-end can be developed on the top of this backend. I would also use Database instead of Local File for this application as it would increase the performance and make it easy for us to maintain changes as well as modifications to inventory easy.

The Application loads data at the start of the Application from the Remote URL provided in the requirement. After initializing local file from the remote file, we will only work and maintain local file. We can and we should update the changes to remote as well but for now, since it is not required, I have not implemented it.

DTO contains the containers for holding data.
Service package holds the class which deals with the user interaction.
DAO contains class that will handle transaction with the Storage (File on Local Storage for this project). 

For the sake of simplicity and time constraint, I have kept it a really simple Application. 
