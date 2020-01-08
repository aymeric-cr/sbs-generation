package fr.femtost.sbs.alteration.core.incident;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static com.google.common.io.Files.write;
import static fr.femtost.sbs.alteration.core.incident.IncidentHelper.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static testools.PredicateAssert.assertThat;

public class IncidentDeserializerTest {

    @Test
    public void deserialize_test_1() throws IOException {
        final File incidentFile = File.createTempFile("incident", ".xml");
        write("<parameters>\n" +
                "    <sensors>\n" +
                "        <sensor sensorType=\"SBS\">\n" +
                "            <sID>ALL</sID>\n" +
                "            <record>../recordings/.VwHQorPfkuiBCN6l_ywV_g__/sbs_1305686021.json</record>\n" +
                "            <filter />\n" +
                "            <action alterationType=\"ALTERATION\">\n" +
                "                <scope type=\"timeWindow\">\n" +
                "                    <lowerBound>450000</lowerBound>\n" +
                "                    <upperBound>636649</upperBound>\n" +
                "                </scope>\n" +
                "                <parameters>\n" +
                "                    <target identifier=\"hexIdent\">ALL</target>\n" +
                "                    <parameter offset=\"false\">\n" +
                "                        <key>altitude</key>\n" +
                "                        <value>1200</value>\n" +
                "                    </parameter>\n" +
                "                </parameters>\n" +
                "            </action>\n" +
                "            <action alterationType=\"ALTERATION\">\n" +
                "                <scope type=\"timeWindow\">\n" +
                "                    <lowerBound>450000</lowerBound>\n" +
                "                    <upperBound>636649</upperBound>\n" +
                "                </scope>\n" +
                "                <parameters>\n" +
                "                    <target identifier=\"hexIdent\">ALL</target>\n" +
                "                    <parameter offset=\"false\">\n" +
                "                        <key>squawk</key>\n" +
                "                        <value>7700</value>\n" +
                "                    </parameter>\n" +
                "                </parameters>\n" +
                "            </action>\n" +
                "        </sensor>\n" +
                "    </sensors>\n" +
                "</parameters>", incidentFile, UTF_8);

        final IncidentDeserializer deserializer = new IncidentDeserializer(incidentFile);
        assertThat(deserializer.deserialize(),
                anIncident(withSensors(
                        aSensor("SBS", "ALL", "../recordings/.VwHQorPfkuiBCN6l_ywV_g__/sbs_1305686021.json", "",
                                withActions(
                                        anAction("ALTERATION",
                                                withScopeTimeWindow(450000, 636649),
                                                withParameters(
                                                        onTargetHexIdent("ALL"),
                                                        aParameter(false, "altitude", "1200"))),
                                        anAction("ALTERATION",
                                                withScopeTimeWindow(450000, 636649),
                                                withParameters(
                                                        onTargetHexIdent("ALL"),
                                                        aParameter(false, "squawk", "7700"))))))));

    }

    @Test
    public void deserialize_test_4() throws IOException {
        final File incidentFile = File.createTempFile("incident", ".xml");
        write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<parameters>\n" +
                "  <sensors>\n" +
                "    <sensor sensorType=\"SBS\">\n" +
                "      <sID>ALL</sID>\n" +
                "      <record>../recordings/.aPSf6iTuPZXsc1UrjhRkgQ__/sbs_2080211301.json</record>\n" +
                "      <filter />\n" +
                "      <action alterationType=\"ALTERATION\">\n" +
                "        <scope type=\"timeWindow\">\n" +
                "          <lowerBound>0</lowerBound>\n" +
                "          <upperBound>3599995</upperBound>\n" +
                "        </scope>\n" +
                "        <parameters>\n" +
                "          <target identifier=\"hexIdent\">4223681,3950945,5022552</target>\n" +
                "          <parameter offset=\"false\">\n" +
                "            <key>latitude</key>\n" +
                "            <value>12</value>\n" +
                "          </parameter>\n" +
                "          <parameter offset=\"true\">\n" +
                "            <key>longitude</key>\n" +
                "            <value>-1</value>\n" +
                "          </parameter>\n" +
                "        </parameters>\n" +
                "      </action>\n" +
                "    </sensor>\n" +
                "    <sensor sensorType=\"SBS\">\n" +
                "      <sID>ALL</sID>\n" +
                "      <record>../recordings/.aPSf6iTuPZXsc1UrjhRkgQ__/sbs_2009861432.json</record>\n" +
                "      <filter />\n" +
                "      <action alterationType=\"SATURATION\">\n" +
                "        <scope type=\"geoTimeWindow\">\n" +
                "          <polygon>\n" +
                "            <id>5dbc948e-1ef4-4044-bebb-b9322f2bd200</id>\n" +
                "            <name>ghost-zone</name>\n" +
                "            <vertices>\n" +
                "              <vertex>\n" +
                "                <lat>49.08</lat>\n" +
                "                <lon>2.39</lon>\n" +
                "              </vertex>\n" +
                "              <vertex>\n" +
                "                <lat>48.95</lat>\n" +
                "                <lon>1.75</lon>\n" +
                "              </vertex>\n" +
                "              <vertex>\n" +
                "                <lat>48.63</lat>\n" +
                "                <lon>1.52</lon>\n" +
                "              </vertex>\n" +
                "              <vertex>\n" +
                "                <lat>48.39</lat>\n" +
                "                <lon>1.5</lon>\n" +
                "              </vertex>\n" +
                "              <vertex>\n" +
                "                <lat>48.13</lat>\n" +
                "                <lon>2.17</lon>\n" +
                "              </vertex>\n" +
                "              <vertex>\n" +
                "                <lat>48.13</lat>\n" +
                "                <lon>2.92</lon>\n" +
                "              </vertex>\n" +
                "              <vertex>\n" +
                "                <lat>48.38</lat>\n" +
                "                <lon>3.89</lon>\n" +
                "              </vertex>\n" +
                "              <vertex>\n" +
                "                <lat>48.85</lat>\n" +
                "                <lon>4.06</lon>\n" +
                "              </vertex>\n" +
                "              <vertex>\n" +
                "                <lat>49.23</lat>\n" +
                "                <lon>3.31</lon>\n" +
                "              </vertex>\n" +
                "              <vertex>\n" +
                "                <lat>49.2</lat>\n" +
                "                <lon>2.54</lon>\n" +
                "              </vertex>\n" +
                "            </vertices>\n" +
                "            <lowerAlt>0</lowerAlt>\n" +
                "            <upperAlt>50000</upperAlt>\n" +
                "          </polygon>\n" +
                "          <lowerBound>10000</lowerBound>\n" +
                "          <upperBound>1094465</upperBound>\n" +
                "        </scope>\n" +
                "        <parameters>\n" +
                "          <target identifier=\"hexIdent\">ALL</target>\n" +
                "          <parameter offset=\"false\">\n" +
                "            <key>ICAO</key>\n" +
                "            <value>RANDOM</value>\n" +
                "            <number>20</number>\n" +
                "          </parameter>\n" +
                "        </parameters>\n" +
                "      </action>\n" +
                "    </sensor>\n" +
                "    <sensor sensorType=\"SBS\">\n" +
                "      <sID>ALL</sID>\n" +
                "      <record>../recordings/.aPSf6iTuPZXsc1UrjhRkgQ__/sbs_429601252.json</record>\n" +
                "      <filter />\n" +
                "      <action alterationType=\"DELETION\">\n" +
                "        <scope type=\"timeWindow\">\n" +
                "          <lowerBound>0</lowerBound>\n" +
                "          <upperBound>3599995</upperBound>\n" +
                "        </scope>\n" +
                "        <parameters>\n" +
                "          <target identifier=\"hexIdent\">4223681,3950945,4198141,1260776,14509124</target>\n" +
                "        </parameters>\n" +
                "      </action>\n" +
                "      <action alterationType=\"ALTERATION\">\n" +
                "        <scope type=\"timeWindow\">\n" +
                "          <lowerBound>560000</lowerBound>\n" +
                "          <upperBound>584000</upperBound>\n" +
                "        </scope>\n" +
                "        <parameters>\n" +
                "          <target identifier=\"hexIdent\">480C1A</target>\n" +
                "          <parameter offset=\"true\">\n" +
                "            <key>altitude</key>\n" +
                "            <value>2342</value>\n" +
                "          </parameter>\n" +
                "        </parameters>\n" +
                "      </action>\n" +
                "    </sensor>\n" +
                "  </sensors>\n" +
                "</parameters>\n", incidentFile, UTF_8);

        final IncidentDeserializer deserializer = new IncidentDeserializer(incidentFile);
        assertThat(deserializer.deserialize(),
                anIncident(withSensors(
                        aSensor("SBS", "ALL", "../recordings/.aPSf6iTuPZXsc1UrjhRkgQ__/sbs_2080211301.json", "",
                                withActions(
                                        anAction("ALTERATION",
                                                withScopeTimeWindow(0, 3599995),
                                                withParameters(
                                                        onTargetHexIdent("4223681,3950945,5022552"),
                                                        aParameter(false, "latitude", "12"),
                                                        aParameter(true, "longitude", "-1"))))),
                        aSensor("SBS", "ALL", "../recordings/.aPSf6iTuPZXsc1UrjhRkgQ__/sbs_2009861432.json", "",
                                withActions(
                                        anAction("SATURATION",
                                                withScopeGeoTimeWindow(10000, 1094465,
                                                        withPolygon(
                                                                "5dbc948e-1ef4-4044-bebb-b9322f2bd200",
                                                                "ghost-zone",
                                                                0, 50000,
                                                                withVertices(aVertex(49.08, 2.39),
                                                                        aVertex(48.95, 1.75),
                                                                        aVertex(48.63, 1.52),
                                                                        aVertex(48.39, 1.5),
                                                                        aVertex(48.13, 2.17),
                                                                        aVertex(48.13, 2.92),
                                                                        aVertex(48.38, 3.89),
                                                                        aVertex(48.85, 4.06),
                                                                        aVertex(49.23, 3.31),
                                                                        aVertex(49.2, 2.54)))),
                                                withParameters(
                                                        onTargetHexIdent("ALL"),
                                                        aParameter(false, "ICAO", "RANDOM", 20))))),
                        aSensor("SBS", "ALL", "../recordings/.aPSf6iTuPZXsc1UrjhRkgQ__/sbs_429601252.json", "",
                                withActions(
                                        anAction("DELETION",
                                                withScopeTimeWindow(0, 3599995),
                                                withParameters(
                                                        onTargetHexIdent("4223681,3950945,4198141,1260776,14509124"))),
                                        anAction("ALTERATION",
                                                withScopeTimeWindow(560000, 584000),
                                                withParameters(
                                                        onTargetHexIdent("480C1A"),
                                                        aParameter(true, "altitude", "2342"))))))));

    }

    @Test
    public void deserialize_trajectory_modification() throws IOException {
        final File incidentFile = File.createTempFile("incident", ".xml");
        write("<parameters>\n" +
                "  <sensors>\n" +
                "    <sensor sensorType=\"PSR\">\n" +
                "      <sID>ALL</sID>\n" +
                "      <record>.dJXRXAw4EmGUCCdLZd_Xrw__/sbs_2.json</record>\n" +
                "      <filter />\n" +
                "    </sensor>\n" +
                "    <sensor sensorType=\"SBS\">\n" +
                "      <sID>ALL</sID>\n" +
                "      <record>.dJXRXAw4EmGUCCdLZd_Xrw__/sbs_1.json</record>\n" +
                "      <filter />\n" +
                "      <action alterationType=\"TRAJECTORY\">\n" +
                "        <scope type=\"timeWindow\">\n" +
                "          <lowerBound>100</lowerBound>\n" +
                "          <upperBound>200</upperBound>\n" +
                "        </scope>\n" +
                "        <parameters>\n" +
                "          <target identifier=\"hexIdent\">2B67</target>\n" +
                "          <trajectory>\n" +
                "            <waypoint>\n" +
                "              <vertex>\n" +
                "                <lat>1.4</lat>\n" +
                "                <lon>-1.9</lon>\n" +
                "              </vertex>\n" +
                "              <altitude>10000</altitude>\n" +
                "              <time>0</time>\n" +
                "            </waypoint>\n" +
                "            <waypoint>\n" +
                "              <vertex>\n" +
                "                <lat>1.5</lat>\n" +
                "                <lon>-2.0</lon>\n" +
                "              </vertex>\n" +
                "              <altitude>10025</altitude>\n" +
                "              <time>1000</time>\n" +
                "            </waypoint>\n" +
                "            <waypoint>\n" +
                "              <vertex>\n" +
                "                <lat>1.6</lat>\n" +
                "                <lon>-2.1</lon>\n" +
                "              </vertex>\n" +
                "              <altitude>10050</altitude>\n" +
                "              <time>2000</time>\n" +
                "            </waypoint>\n" +
                "            <waypoint>\n" +
                "              <vertex>\n" +
                "                <lat>1.7</lat>\n" +
                "                <lon>-2.2</lon>\n" +
                "              </vertex>\n" +
                "              <altitude>10075</altitude>\n" +
                "              <time>3000</time>\n" +
                "            </waypoint>\n" +
                "            <waypoint>\n" +
                "              <vertex>\n" +
                "                <lat>1.8</lat>\n" +
                "                <lon>-2.3</lon>\n" +
                "              </vertex>\n" +
                "              <altitude>10100</altitude>\n" +
                "              <time>4000</time>\n" +
                "            </waypoint>\n" +
                "            <waypoint>\n" +
                "              <vertex>\n" +
                "                <lat>1.9</lat>\n" +
                "                <lon>-2.4</lon>\n" +
                "              </vertex>\n" +
                "              <altitude>10125</altitude>\n" +
                "              <time>5000</time>\n" +
                "            </waypoint>\n" +
                "          </trajectory>\n" +
                "        </parameters>\n" +
                "      </action>\n" +
                "    </sensor>\n" +
                "  </sensors>\n" +
                "</parameters>\n", incidentFile, UTF_8);

        final IncidentDeserializer deserializer = new IncidentDeserializer(incidentFile);
        assertThat(deserializer.deserialize(),
                anIncident(withSensors(
                        aSensor("PSR", "ALL", ".dJXRXAw4EmGUCCdLZd_Xrw__/sbs_2.json", "", withActions()),
                        aSensor("SBS", "ALL", ".dJXRXAw4EmGUCCdLZd_Xrw__/sbs_1.json", "",
                                withActions(
                                        anAction("TRAJECTORY",
                                                withScopeTimeWindow(100, 200),
                                                withTrajectoryParameters(
                                                        onTargetHexIdent("2B67"),
                                                        aTrajectory(
                                                                withWayPoint(1.4, -1.9, 10000, 0),
                                                                withWayPoint(1.5, -2.0, 10025, 1000),
                                                                withWayPoint(1.6, -2.1, 10050, 2000),
                                                                withWayPoint(1.7, -2.2, 10075, 3000),
                                                                withWayPoint(1.8, -2.3, 10100, 4000),
                                                                withWayPoint(1.9, -2.4, 10125, 5000)))))))));
    }
}