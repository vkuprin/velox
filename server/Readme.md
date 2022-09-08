To build and run:
 - install JDK
 - install maven
 - Postgre 
 
Build
 - create empty DB name - velox
 - in /src/main/resources/application.properties set password for DB
 - run 'mvn package'
 
Run 
 - navigate to /target
 - run 'java - jar *.jar'
 
Check http://localhost:8080/swagger-ui.html