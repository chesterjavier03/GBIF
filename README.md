## GBIF Pipelines Test

This test project contains two modules:
 - [beam-data-pipelines](beam-data-pipeline/README.md): contains an Apache Beam data pipeline to transform CSV files to Avro.
 - [data-service](data-service/README.md): Spring Boot application that executes the data pipeline from a Rest HTTP service.

To build the project:
```
mvn clean package
```

The tasks to complete for this project are:
 1. Complete the data pipeline as is specified in the [README.md](beam-data-pipeline/README.md) file.
 2. Develop a Spring Boot application following the specification in the [README.md](data-service/README.md) file.
    This task requires modifications to the data pipeline to accept an input file and export the files to a specific location.


