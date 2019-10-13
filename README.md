= Arp-workshop

== Building

To launch your tests:
```
./mvnw clean test
```

To package your application:
```
./mvnw clean package
```

To run your application:
```
./mvnw clean compile exec:java
```

To build your container
```
docker build -t arp-workshop .
```

To run your container
```
docker run -t -i -p 8080:8080 arp-workshop
```

To run database
```
docker run --rm --name pg-docker -e POSTGRES_PASSWORD=docker -d -p 5432:5432 postgres
```
