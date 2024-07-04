# Prime Numbers Generator and Filter

This project generates a set of random numbers, filters out the prime numbers, and stores the results in CSV files. The services run in Docker containers and utilize Kafka for message handling.

## Build and Run the Project

### 1. Build the Services/Jars
1. Navigate to the project root folder.
2. Use one of the following commands depending on whether you are using Maven wrapper or a local installation:
   - `mvn clean install` (local installation)
   - `./mvnw clean install` (wrapper)

### 2. Build Docker Images and Run Containers
To build the service images and spawn them into Docker containers, run:
```
docker compose up -d
```

## Additional Information

### Generated CSV Files
The generated files will be in the `{project root folder}/data` directory:
- `generated-numbers.csv`: Contains the generated numbers.
- `prime-numbers.csv`: Contains the filtered prime numbers for every 100 generated numbers.

### Kafka UI
Access the Kafka broker, topics, and messages via the Kafka UI available at [http://localhost:9080](http://localhost:9080).

## Cleaning Up

### 1. Shut Down All Containers
Navigate to the project root folder and execute the following command to shut down all services and remove the containers:
```
docker compose down
```

### 2. Remove Docker Images
To remove the generated Docker images, execute the following command:
```
docker rmi prime-numbers-numbers-producer:latest prime-numbers-numbers-consumer:latest
```

### 3. Remove Generated CSV Files
Navigate to the project root folder and execute the following command to delete the generated CSV files:
```
rm -r ./data
```






