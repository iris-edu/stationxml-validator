## Description

The IRIS stationxml-validator is a Java library and related command-line utility for validating [FDSN StationXML](http://www.fdsn.org/xml/station/) 1.1 documents. The program's purpose is to ensure that FDSN StationXML metadata are complete and formatted to standards set by [IRIS DMC](http://ds.iris.edu/).

## Documentation
* [Validation tests](tests.md)
* [Unit name overview for IRIS stationxml-validator](units.md)
* [Rule restrictions](restrictions.md)
* [Band Code Dictionary](bandcode.md)
* [Channel:Code orthogonal orientation guidelines](orientation.md)
* [Contributing to the StationXML Validator](contribution.md)

### Downloading releases

Releases of the StationXML Validator can be downloaded from the project's release page:

https://github.com/iris-edu/StationXML-Validator/releases

The compiled .jar may be used immediately.

If you wish to compile the program, the source code is available. Follow the [instructions below](#building-the-command-line-validator-from-source) to download the source code.

## Using the Validator


The Validator's jar runs in both [Java](https://www.java.com/) 8 or 11 enviroments.  A usage message will be printed if the stationxml-validator is run with no arguments are supplied:

    java -jar stationxml-validator-1.7.0.jar

The Validator contains arguments that output messages to help users understand its function. The Validator's --help argument prints out a list of all of the available validator arguments. 
    
    java -jar stationxml-validator-1.7.0.jar --help

The rules argument prints the list of rules that the validator uses to check metadata integrety. 
   
    java -jar stationxml-validator-1.7.0.jar --rules

The units argument prints a table of SI units names that are acceptable in stationXML metadata.

     java -jar stationxml-validator-1.7.0.jar --units

To validate stationXML metadata, provide the path and name of a stationXML formatted document as an argument.

    java -jar stationxml-validator-1.7.0.jar IU.ANMO.00.BHZ.xml

The input argument may be prepended before the path.

     java -jar stationxml-validator-1.7.0.jar --input IU.ANMO.00.BHZ.xml

The Validator accepts dataless SEED metadata and directories in addation to stationXML. If dataless files are provided, the Validator automatically converters these files to stationXML and performs validation. If a directory is provided as an argument, the Validator will loop through it's contents and validate both stationXML and dataless files. 

    java -jar station-xml-validator-1.7.0.jar IU.ANMO.00.BHZ.dataless
    java -jar stationxml-validator-1.7.0.jar /path/to/metadata

Output are printed to System.out by default. Use an stdout and stderr redirect to ouput Validator messages to a file. 

    java -jar stationxml-validator-1.7.0.jar /path/to/metadata &> /path/for/output.txt

Additional arguments may be provided to the Validator including: `--verbose` which provides additional output messages to the user, and `continue-on-error` which forces the validator to continue if an exception is encountered. The `--verbose` flag is only effective if stderr is redirected into stdinfo. `continue-on-error` is only useful when validating directories. 
    
    java -jar stationxml-validator-1.7.0.jar /path/to/metadata --verbose --continue-on-error &> /path/for/output.txt

Users interested in generating stationXML formatted metadata from dataless SEED files should refer to the [stationxml-seed-converter](https://github.com/iris-edu/stationxml-seed-converter).

## Validation tests

The validator performs a number of tests. First, it validates the metadata against the StationXML 1.1 schema. After, a suite of test are run on the metadata to ensure completeness sufficient for long-term archiving. The test and their descriptions can be accessed using the `--rules` argument or can be found on the wiki pages:

[[Validation tests]]

## Convention for Units

The Validator includes rules that check if unit names specified in StationXML metadata are SI compliant. The guidelines for unit names and the list of accepted unit names can be accessed using the `--units` argument or can be found on the wiki pages:

[[Unit name overview for IRIS StationXML validator]]

## Building the command line validator from source

Building the stationxml-validator from source code is dependent on the Java Development Kit (JDK 1.8) and [Apache Maven](https://maven.apache.org/).  The validator can be built using these steps:

1. Download source code and untar/unzip. The code can be downloaded either from the project's [release](https://github.com/iris-edu/StationXML-Validator/releases) page or it can be cloned from the Validator's [git repository](https://github.com/iris-edu/stationxml-validator/)
1. cd to the newly created stationxml-=validator directory
1. run the command: `mvn clean install`

The resulting jar is under the target directory. e.g. stationxml-validtor-<version>.jar

Pleae report any any issues to the project's [issues page](https://github.com/iris-edu/stationxml-validator/issues). 

[Updated 04-2020]
