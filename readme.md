The project uses Spring Boot therefore you may run the project 
with command ```mvn spring-boot:run```. The project requires Java 11 or greater.
to run write mvn spring-boot:run.

The server is run on localhost port 3456. 
To run the service in microservices environment one can packge it intoe a runnable jar
```mvn clean packge``` and run ```java -jar sms-0.0.1-SNAPSHOT.jar```.

The service has only one POST endpoint ```/match``` that accepts body in JSON format like:

```
[
    {
        "name":"Egor",
        "interests":["cars","music"]
    },
    {
        "name":"Ivan",
        "interests":["hiking","cars"]
    }
]
```

and returns response with JSON body as the following:

```
[
    {
        "left":"Egor","right":"Ivan","interests":["dancing"]
    }
]
```

Internally the service uses graph as its internal structure 
with vertexes as users and edges as their common interests 
with edges wights as an amount of common interests.

Following are some technical decision made during project implementation:
- to foster the development it was decided ti use lombok for boilerplate code for models
- TDD was used throughout project development
- as graph implementation library JGraphT is used due to its popularity 
and an extension that help visualize build graph for testing purposes