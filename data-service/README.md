## GBIF Data Services

This project showcases a simple [Spring Boot](https://spring.io/projects/spring-boot/) application that triggers a data pipeline job. 
It exposes a service which, using the [beam-data-pipelines](../beam-data-pipeline) module, executes data transformation from CSV to Apache Avro format.

The REST service follow the specification:
 - Endpoint: ```/data/transform```
 - Method: ```POST```
 - Parameter: ````file```` (input file)
 - Response: a ````zip```` file containing all the Avro files after the transformation.
 - Performs error handling for  invalid input format and errors in processing the input file. 
    


