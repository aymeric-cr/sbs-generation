# SBS FDIA Generator
> Test Data Generation for False Data Injection Attack Testing in Air Traffic Surveillance

This library lets you perform alterations on legitimate ADS-B recordings in the SBS format. 

## Installation

- You need Java 1.8+ and maven 3 in order to build and run this code. To build the library enter the following maven command:

```shell
$ mvn clean package
```

You will find the generated jar with dependencies in the target folder.

## Usage

This code requires an XML file as input, which contains a relative path to the SBS recording to alter, as well as information regarding the alteration directive to perform, namely:
- A time window
- A list of targeted aircraft
- Alteration-specific parameters. For trajectory modification, a list of way points. For property modification, a list of property values changes.

To use the library, use the following command:
```shell
$ java -jar sbs-generation-0.1-SNAPSHOT-jar-with-dependencies.jar alt-directive.xml
```
The exact content of the XML file is described in the file *src/main/resources/xsd/scenario.xsd*. 
The various alterations and their corresponding XML file are described below.

In order to generate XML files, it is possible to use an high level dedicated Domain Specific Langage, avaible here: https://github.com/aymeric-cr/dsl-scenario

## Features

This library allows for the simulation of several attack scenarios based on the taxonomy found in [1]:
- **Ghost Aircraft Injection**. The goal is to create a non-existing aircraft by broadcasting fake ADS-B messages on the communication channel. 
- **Ghost Aircraft Flooding**. This attack is similar to the one above but consists of injecting multiple aircraft simultaneously with the goal of saturating the recognized air picture and thus generates a denial of service of the controller's surveillance system.
- **Virtual Trajectory Modification**. Using either message modification or a combination of message injection and deletion, the goal of this attack is to modify the trajectory of an aircraft.
- **False Alarm Attack**. Based on the same techniques as the previous attack, the goal is to modify the messages of an aircraft in order to indicate a fake alarm. A typical example would be modifying the squawk code to 7500, indicating the aircraft has been hijacked.
- **Aircraft Disappearance**. Deleting all messages emitted by an aircraft can lead to the failure of collision avoidance systems and ground sensors confusion. It could also force the aircraft under attack to land for safety check.
- **Aircraft Spoofing**. This scenario consists of spoofing the ICAO number of an aircraft through message deletion and injection. This could allow an enemy aircraft to pass for a friendly one and reduce causes for alarm when picked up by PSR. 
- **Replay**. Although this type of attack is not part of the taxonomy, we take it from recent discussions with experts that replay attacks represent a very serious threat as it abstracts itself from realism issues. A typical example of such an attack would be terrorists who collected ADS-B messages of a regular flight on a certain day, then a few days later, hop in on the plane, hijack it and physically temper with the ADS-B transponders to make them send out the messages they previously recorded. This could allow terrorists to change course of flight without being noticed immediately.

### Property Modification 

This alteration allows users to perform *False Alarm* and *Aircraft spoofing* attacks, as well as any kind of property modification. 
The XML file given as input should contain, in addition to what has been mentioned above, a list of property value changes. 
A property value change is  a triplet *p = (i,v,o)* where *i* is the property identifier (e.g., altitude or ground speed), *v* v is a value, and *o* specifies how *v* shall be employed to modify the property's initial value. 
It can be of four modes: 
- **simple**: *v* replaces the property's initial value
- **offset**: *v* is added to the property's initial value
- **noise**: a random value ranging 0 -- *v* is added the property's initial value
- **drift**: *v* plus the sum of the previous drift is added to *v*.

For instance:
```XML
<scenario>
  <record>../relativepath/to/recording.sbs</record>
  <firstDate>1481274814831</firstDate>
  <action alterationType="ALTERATION">
    <scope type="timeWindow">
      <lowerBound>282285</lowerBound>
      <upperBound>732405</upperBound>
    </scope>
    <parameters>
      <target identifier="hexIdent">37AC45</target>
      <parameter mode="simple">
        <key>hexIdent</key>
        <value>RANDOM</value>
      </parameter>
      <parameter mode="simple">
        <key>squawk</key>
        <value>7700</value>
      </parameter>
    </parameters>
  </action>
</scenario>
```

Recording to alter: 
The previous file formalize an property modification on the recording *recording.sbs* starting at the timestamp 1481274814831ms (12/9/2016 - 9:13:34.831). 

Time window: 
The alteration start 282285ms after the beginning of the recording, at the timestamp 1481274814831 + 282285 = 1481275097116 (12/9/2016 - 9:18:17.116) until the timestamp 1481274814831 + 732405 = 1481275547236ms (12/9/2016 à 9:25:47).

Target: 
The only targeted aircraft is the aircraft with an ICAO equals to *37AC45*.

Parameters: 
There are two parameters, the first one specify that the ICAO of the targeted aicraft must be changed to a random one.
The second parameter specify that the squawk of the targeted aircraft must be changed to 7700 (emergency status).

### Aircraft Disappearance

The objective of this alteration is to hide aircraft i.e. delete messages of certain aircraft at a certain time. 
Additional information required in the XML file is *n*, is the number of deleted consecutive messages e.g. if *n = 0* then all messages are deleted, while if *n = 3* for instance, then only three out of four messages are deleted.

For instance:
```XML
<scenario>
  <record>../relativepath/to/recording.sbs</record>
  <firstDate>1481274814831</firstDate>
  <action alterationType="DELETION">
    <scope type="timeWindow">
      <lowerBound>282285</lowerBound>
      <upperBound>732405</upperBound>
    </scope>
    <parameters>
      <target identifier="hexIdent">37AC45</target>
      <frequency>4</frequency>
    </parameters>
  </action>
</scenario>
```

This file will hide 4 out of 5 messages (frequency tag in the file = *n*) emitted by aircraft 37AC45 between seconds 282285 and 732405, according to the recording's duration.

### Virtual Trajectory Modification 

Additional information required in the XML file is &Omega;, a nonempty set of way-points. 
A way-point is defined as &omega; = (lat,lon,alt,ts), i.e. 3D coordinates and a time of passage. 

For instance:

```XML
<scenario>
  <record>../relativepath/to/recording.sbs</record>
  <firstDate>1481274814831</firstDate>
  <action alterationType="TRAJECTORY">
    <scope type="timeWindow">
      <lowerBound>282285</lowerBound>
      <upperBound>732405</upperBound>
    </scope>
    <parameters>
      <target identifier="hexIdent">37AC45</target>
        <trajectory>
          <waypoint>
            <vertex>
              <lat>6.123</lat>
              <lon>45.38</lon>
            </vertex>
            <alt>26500</alt>
            <time>282285</time>
          </waypoint>
          <waypoint>
            <vertex>
              <lat>6.5</lat>
              <lon>45.58327</lon>
            </vertex>
            <alt>27750</alt>
            <time>732405</time>
          </waypoint>
        </trajectory>
    </parameters>
  </action>
</scenario>
```

This will edit messages emitted by aircraft 37AC45 (within the time-window) by replacing all the property values (position, velocity) so that the aircraft's trajectory passes through the two way-points. 
This process does not involve message creation, meaning that if the modified trajectory is much longer than the initial one, aircraft's velocity may be set to abnormally high values in order to travel the extra distance within the same amount of time.

### Ghost Aircraft Creation

In this attack, the attacker creates a fake track from scratch, implying that in this case, fake messages must be created and inserted into the target recording.
The XML file supplied as input must contain a set of way points as well as a list of property values (static properties such as ICAO address, callsign, etc.). 

For instance:

```XML
<scenario>
  <record>../relativepath/to/recording.sbs</record>
  <firstDate>1481274814831</firstDate>
  <action alterationType="TRAJECTORY">
    <scope type="timeWindow">
      <lowerBound>282285</lowerBound>
      <upperBound>732405</upperBound>
    </scope>
    <parameters>
        <trajectory>
          <waypoint>
            <vertex>
              <lat>6.123</lat>
              <lon>45.38</lon>
            </vertex>
            <alt>26500</alt>
            <time>282285</time>
          </waypoint>
          <waypoint>
            <vertex>
              <lat>6.5</lat>
              <lon>45.58327</lon>
            </vertex>
            <alt>27750</alt>
            <time>732405</time>
          </waypoint>
        </trajectory>
        <parameter mode="simple">
            <key>hexIdent</key>
            <value>RANDOM</value>
        </parameter>
        <parameter mode="simple">
            <key>callsign</key>
            <value>AL89LRE</value>
        </parameter>
    </parameters>
  </action>
</scenario>
```

This will create aircraft with random ICAO and callsign set to AL89LRE. A starting point and an ending point have been defined (both are way-points). 
Contrary to trajectory modification, this alteration creates messages.

### Ghost Aircraft Flooding

The initial definition of this attack consists of suddenly creating a lot of ghost aircraft, thus supposedly saturating the Recognized Aircraft Picture (RAP -- i.e. what the controller sees). This attack can easily be achieved by creating a script that would call the Ghost Aircraft Creation algorithm a certain number of time with varying parameters. Instead, we propose a slightly  
However, this has proven to be quite straightforward for detection systems to recover from this type denial of service. 
The definition of the attack is instead slightly modified to become **virtual trajectory modification flooding**. 
The goal is to suddenly generate many different trajectories for a targeted aircraft, as if the aircraft was being split in multiple pieces, thus saturating the detection systems with many conflicting messages.

The XML file should contain the number of fake trajectories to create. 

For instance:

```XML
<scenario>
  <record>../relativepath/to/recording.sbs</record>
  <firstDate>1481274814831</firstDate>
  <action alterationType="SATURATION">
    <scope type="timeWindow">
      <lowerBound>282285</lowerBound>
      <upperBound>732405</upperBound>
    </scope>
    <parameters>
      <target identifier="hexIdent">37AC45</target>
      <parameter mode="simple">
        <key>hexIdent</key>
        <value>RANDOM</value>
        <number>20</number>
      </parameter>
    </parameters>
  </action>
</scenario>
```

This will create 20 fake trajectories for aircraft 37AC45.

### Replay

It takes two recordings, one from which extract a flight track, and another into which insert the flight track. 

For instance:

```XML
<scenario>
  <record>../relativepath/to/recording.sbs</record>
  <firstDate>1481274814831</firstDate>
  <action alterationType="REPLAY">
    <scope type="timeWindow">
      <lowerBound>282285</lowerBound>
      <upperBound>732405</upperBound>
    </scope>
    <parameters>
      <target identifier="hexIdent">37AC45</target>
      <recordPath>../relativepath/to/sourceRecording.sbs</recordPath>
      <parameter mode="simple">
        <key>hexIdent</key>
        <value>RANDOM</value>
      </parameter>
    </parameters>
  </action>
</scenario>
```

This will extract messages of aircraft 37AC45 from recording *sourceRecording.sbs*, and inject them into recording *recording.sbs* from second 282285 to 732405 (any subsequent message that would be outside of the specified time window is not injected).

## References
<a id="1">[1]</a> 
Matthias Schafer, Vincent Lenders, and Ivan Martinovic.
*Experimental analysis of attacks on next generation air traffic communication*. 
In International Conference on Applied Cryptography and Network Security,pages 253–271. Springer, 2013.
