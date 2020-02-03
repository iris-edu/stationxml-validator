## Description

The IRIS stationxml-validator is a Java library and related command-line tool for validating [FDSN StationXML](http://www.fdsn.org/xml/station/) documents.  The purpose is ensure that FDSN StationXML metadata is complete and formatted sufficiently for acceptance at the [IRIS DMC](http://ds.iris.edu/).

## Documentation
* [Validation tests](tests.md)
* [Unit name overview for IRIS stationxml-validator](units.md)
* [Rule restrictions](orientation.md)
* [Channel:Code orthogonal orientation guidelines](orientation.md)

### Downloading releases

Releases of the StationXML Validator can be downloaded from the project release page:

https://github.com/iris-edu/StationXML-Validator/releases

The compiled .jar may be used immediately.

If you wish to compile you can download the source code and follow the [instructions below](#building-the-command-line-validator-from-source).

## Using the Validator

The command line version of the Validator requires [Java](https://www.java.com/) 8 or 11.  A usage message will be printed if no arguments are supplied:

    java -jar station-xml-validator-1.5.9.6.jar

To validate a StationXML file provide the name of the StationXML formatted document with the file extensaion .xml on the command line:

    java -jar station-xml-validator-1.5.9.6.jar IU.ANMO.00.BHZ.xml

The Validator accepts dataless seed files as input arguments. If dataless files are provided, the Validator automatically converters these files to station-xml and performs a validation.

    java -jar station-xml-validator-1.5.9.6.jar IU.ANMO.00.BHZ.dataless

Users interested in generating StationXML formatted metadata from dataless seed files should refer to the [stationxml-seed-converter](https://github.com/iris-edu/stationxml-seed-converter).

The Validator has options for formatting results. By default, the validator returns html. For viewing results quickly the argument `--format report` is helpful and returns text output to the terminal.

    java -jar station-xml-validator-1.5.9.3.jar IU.ANMO.00.BHZ.xml --format report

Refer to the usage message for more arguments. 

## Validation tests

The validator performs a number of tests starting with validation of the StationXML schema followed by a number of other tests to ensure completeness sufficient for long-term archiving.  All tests with descriptions are listed on the wiki pages:

[[Validation tests]]


## Convention for Units

The Validator includes a rule to check the unit names specified in StationXML metadata.  The guidelines for unit names and the list of accepted names are available on the page below:

[[Unit name overview for IRIS StationXML validator]]

## Building the command line validator from source

To build the stationXML-validator from source code you will need a working instance of the Java Development Kit (JDK 1.8) and [Apache Maven](https://maven.apache.org/).  The validator can be built using these steps:

1. Download source code and untar/unzip, either a [release](https://github.com/iris-edu/StationXML-Validator/releases) or clone the repository
1. Change to the created StationXML-Validator-<version> directory
1. `mvn clean install`

The resulting jar is under the _target_ directory, e.g. stationxml-validtor-<version>.jar
