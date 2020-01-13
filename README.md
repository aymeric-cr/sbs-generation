# SBS FDIA Generator
> Test Data Generation for False Data Injection Attack Testing in Air Traffic Surveillance

## Installation

- You need Java 1.8+ and maven 3 in order to build and run this code. To build the library enter the following maven command:

```shell
$ mvn clean test
```

## Features

This library allows for the simulation of several attack scenarios based on the taxonomy found in [1]:
- **Ghost Aircraft Injection**. The goal is to create a non-existing aircraft by broadcasting fake ADS-B messages on the communication channel. 
- **Ghost Aircraft Flooding**. This attack is similar to the one above but consists of injecting multiple aircraft simultaneously with the goal of saturating the recognized air picture and thus generates a denial of service of the controller's surveillance system.
- **Virtual Trajectory Modification**. Using either message modification or a combination of message injection and deletion, the goal of this attack is to modify the trajectory of an aircraft.
- **False Alarm Attack**. Based on the same techniques as the previous attack, the goal is to modify the messages of an aircraft in order to indicate a fake alarm. A typical example would be modifying the squawk code to 7500, indicating the aircraft has been hijacked.
- **Aircraft Disappearance**. Deleting all messages emitted by an aircraft can lead to the failure of collision avoidance systems and ground sensors confusion. It could also force the aircraft under attack to land for safety check.
- **Aircraft Spoofing**. This scenario consists of spoofing the ICAO number of an aircraft through message deletion and injection. This could allow an enemy aircraft to pass for a friendly one and reduce causes for alarm when picked up by PSR. 
- **Replay**. Although this type of attack is not part of the taxonomy, we take it from recent discussions with experts that replay attacks represent a very serious threat as it abstracts itself from realism issues. A typical example of such an attack would be terrorists who collected ADS-B messages of a regular flight on a certain day, then a few days later, hop in on the plane, hijack it and physically temper with the ADS-B transponders to make them send out the messages they previously recorded. This could allow terrorists to change course of flight without being noticed immediately.

This code requires an XML file as input, which contains a relative path to the SBS recording to alter, as well as information regarding the alteration directive to perform, namely:
- A time window
- A list of targeted aircraft
- Alteration-specific parameters. For trajectory modification, a list of way points. For property modification, a list of property values changes.

### Property Modification 

This alteration allows users to perform *False Alarm* and *Aircraft spoofing* attacks, as well as any kind of property modification. 

```XML
<scenario>
  <record>..\relativepath\to\recording.sbs</record>
  <firstDate>1481274814831</firstDate>
  <action alterationType="ALTERATION">
    <scope type="timeWindow">
      <lowerBound>282285</lowerBound>
      <upperBound>732405</upperBound>
    </scope>
    <parameters>
      <target identifier="hexIdent">37AC45</target>
      <parameter type="SIMPLE">
        <key>hexIdent</key>
        <value>RANDOM</value>
      </parameter>
    <parameter type="SIMPLE">
        <key>squawk</key>
        <value>7700</value>
      </parameter>
    </parameters>
  </action>
</scenario>
```
There is a file nammed (*src/main/resources/xsd/scenario.xsd*) which describes formally a well formed XML scenario file.
 
 
## Usage

```Java

``` 
 
## References
<a id="1">[1]</a> 
Matthias Schafer, Vincent Lenders, and Ivan Martinovic.
*Experimental analysis of attacks on next generation air traffic communication*. 
In International Conference on Applied Cryptography and Network Security,pages 253–271. Springer, 2013.
