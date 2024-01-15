## CSV to Avro file

This project contains a simple [Apache Beam](https://beam.apache.org/) pipleline that reads a CSV file and writes to 
an [Avro file](https://avro.apache.org).  The project is built using [Apache Maven](http://maven.apache.org/).

To build the project:
```
mvn package
```

Running the project should be as simple as running the `CSV2AvroPipeline` class.  It will read 
[the input data](src/test/resources/input.csv) and produce a set of avro files as output.

The programming task is to extend this project to add a new field of `countryCode` to the data stored in the avro file.

To populate this field a call to the GBIF reverse geocode web service should be performed.  The service takes a 
latitude and longitude and returns JSON containing the code in the field `isoCountryCode2Digit` from the object where type = "Political".  
A sample call is <http://api.gbif.org/v1/geocode/reverse?lat=60.4&lng=-131.3>.


