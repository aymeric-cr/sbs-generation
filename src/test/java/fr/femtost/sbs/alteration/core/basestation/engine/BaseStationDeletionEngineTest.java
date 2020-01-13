package fr.femtost.sbs.alteration.core.basestation.engine;

import fr.femtost.sbs.alteration.core.basestation.message.BaseStationMessageFull;
import org.junit.Test;

import java.io.File;

import static fr.femtost.sbs.alteration.core.basestation.BstHelper.*;
import static fr.femtost.sbs.alteration.core.scenario.ScenarioHelper.*;
import static org.junit.Assert.assertEquals;
import static org.powermock.reflect.Whitebox.invokeMethod;

public class BaseStationDeletionEngineTest {

    @Test
    public void applyAction_BstMessageFull_deletion() throws Exception {
        final long startDate = 1555694985000L;
        final BaseStationDeletionEngine engine = new BaseStationDeletionEngine(
                recording(new File(""), startDate),
                deletion(timeWindow(12000, 19000), parameters(bstTarget("ALL"))));
        final BaseStationMessageFull message = bstMessageFull(
                "4B1613",
                startDate + 15000,
                callsign("BAW256"),
                altitude(20350),
                groundSpeed(442.2),
                track(358.6),
                latitude(49.6684),
                longitude(8.4823),
                verticalRate(0),
                squawk(4022),
                alert(false),
                emergency(false),
                spi(false),
                onGround(false));
        final String result = invokeMethod(engine, "applyAction", message);
        assertEquals(
                "",
                result);
    }

    @Test
    public void applyAction_BstMessageFull_deletion_targeted() throws Exception {
        final long startDate = 1555694985000L;
        final BaseStationDeletionEngine engine = new BaseStationDeletionEngine(
                recording(new File(""), startDate),
                deletion(timeWindow(12000, 19000), parameters(bstTarget("4B1613"))));
        final BaseStationMessageFull message = bstMessageFull(
                "4B1613",
                startDate + 15000,
                callsign("BAW256"),
                altitude(20350),
                groundSpeed(442.2),
                track(358.6),
                latitude(49.6684),
                longitude(8.4823),
                verticalRate(0),
                squawk(4022),
                alert(false),
                emergency(false),
                spi(false),
                onGround(false));
        final String result = invokeMethod(engine, "applyAction", message);
        assertEquals(
                "",
                result);
    }

    @Test
    public void applyAction_BstMessageFull_deletion_lowerbound() throws Exception {
        final long startDate = 1555694985000L;
        final BaseStationDeletionEngine engine = new BaseStationDeletionEngine(
                recording(new File(""), startDate),
                deletion(timeWindow(12000, 19000), parameters(bstTarget("ALL"))));
        final BaseStationMessageFull message = bstMessageFull(
                "4B1613",
                startDate + 12000,
                callsign("BAW256"),
                altitude(20350),
                groundSpeed(442.2),
                track(358.6),
                latitude(49.6684),
                longitude(8.4823),
                verticalRate(0),
                squawk(4022),
                alert(false),
                emergency(false),
                spi(false),
                onGround(false));
        final String result = invokeMethod(engine, "applyAction", message);
        assertEquals(
                "",
                result);
    }

    @Test
    public void applyAction_BstMessageFull_deletion_upperbound() throws Exception {
        final long startDate = 1555694985000L;
        final BaseStationDeletionEngine engine = new BaseStationDeletionEngine(
                recording(new File(""), startDate),
                deletion(timeWindow(12000, 19000), parameters(bstTarget("ALL"))));
        final BaseStationMessageFull message = bstMessageFull(
                "4B1613",
                startDate + 19000,
                callsign("BAW256"),
                altitude(20350),
                groundSpeed(442.2),
                track(358.6),
                latitude(49.6684),
                longitude(8.4823),
                verticalRate(0),
                squawk(4022),
                alert(false),
                emergency(false),
                spi(false),
                onGround(false));
        final String result = invokeMethod(engine, "applyAction", message);
        assertEquals(
                "",
                result);
    }

    @Test
    public void applyAction_BstMessageFull_deletion_in_lowerbound() throws Exception {
        final long startDate = 1555694985000L;
        final BaseStationDeletionEngine engine = new BaseStationDeletionEngine(
                recording(new File(""), startDate),
                deletion(timeWindow(12000, 19000), parameters(bstTarget("ALL"))));
        final BaseStationMessageFull message = bstMessageFull(
                "4B1613",
                startDate + 12001,
                callsign("BAW256"),
                altitude(20350),
                groundSpeed(442.2),
                track(358.6),
                latitude(49.6684),
                longitude(8.4823),
                verticalRate(0),
                squawk(4022),
                alert(false),
                emergency(false),
                spi(false),
                onGround(false));
        final String result = invokeMethod(engine, "applyAction", message);
        assertEquals(
                "",
                result);
    }

    @Test
    public void applyAction_BstMessageFull_deletion_in_upperbound() throws Exception {
        final long startDate = 1555694985000L;
        final BaseStationDeletionEngine engine = new BaseStationDeletionEngine(
                recording(new File(""), startDate),
                deletion(timeWindow(12000, 19000), parameters(bstTarget("ALL"))));
        final BaseStationMessageFull message = bstMessageFull(
                "4B1613",
                startDate + 18999,
                callsign("BAW256"),
                altitude(20350),
                groundSpeed(442.2),
                track(358.6),
                latitude(49.6684),
                longitude(8.4823),
                verticalRate(0),
                squawk(4022),
                alert(false),
                emergency(false),
                spi(false),
                onGround(false));
        final String result = invokeMethod(engine, "applyAction", message);
        assertEquals(
                "",
                result);
    }

    @Test
    public void applyAction_BstMessageFull_deletion_KO_lower() throws Exception {
        final long startDate = 1555694985000L;
        final BaseStationDeletionEngine engine = new BaseStationDeletionEngine(
                recording(new File(""), startDate),
                deletion(timeWindow(12000, 19000), parameters(bstTarget("ALL"))));
        final BaseStationMessageFull message = bstMessageFull(
                "4B1613",
                startDate + 11999,
                callsign("BAW256"),
                altitude(20350),
                groundSpeed(442.2),
                track(358.6),
                latitude(49.6684),
                longitude(8.4823),
                verticalRate(0),
                squawk(4022),
                alert(false),
                emergency(false),
                spi(false),
                onGround(false));
        final String result = invokeMethod(engine, "applyAction", message);
        assertEquals(
                "MSG,0,0,0,4B1613,0,2019/04/19,17:29:56.999,2019/04/19,17:29:56.999,BAW256,20350,442.2,358.6,49.6684,8.4823,0,4022,0,0,0,0",
                result);
    }

    @Test
    public void applyAction_BstMessageFull_deletion_KO_upper() throws Exception {
        final long startDate = 1555694985000L;
        final BaseStationDeletionEngine engine = new BaseStationDeletionEngine(
                recording(new File(""), startDate),
                deletion(timeWindow(12000, 19000), parameters(bstTarget("ALL"))));
        final BaseStationMessageFull message = bstMessageFull(
                "4B1613",
                startDate + 19001,
                callsign("BAW256"),
                altitude(20350),
                groundSpeed(442.2),
                track(358.6),
                latitude(49.6684),
                longitude(8.4823),
                verticalRate(0),
                squawk(4022),
                alert(false),
                emergency(false),
                spi(false),
                onGround(false));
        final String result = invokeMethod(engine, "applyAction", message);
        assertEquals(
                "MSG,0,0,0,4B1613,0,2019/04/19,17:30:04.001,2019/04/19,17:30:04.001,BAW256,20350,442.2,358.6,49.6684,8.4823,0,4022,0,0,0,0",
                result);
    }

    @Test
    public void applyAction_BstMessageFull_deletion_KO_wrongTarget() throws Exception {
        final long startDate = 1555694985000L;
        final BaseStationDeletionEngine engine = new BaseStationDeletionEngine(
                recording(new File(""), startDate),
                deletion(timeWindow(12000, 19000), parameters(bstTarget("4B1612"))));
        final BaseStationMessageFull message = bstMessageFull(
                "4B1613",
                startDate + 19001,
                callsign("BAW256"),
                altitude(20350),
                groundSpeed(442.2),
                track(358.6),
                latitude(49.6684),
                longitude(8.4823),
                verticalRate(0),
                squawk(4022),
                alert(false),
                emergency(false),
                spi(false),
                onGround(false));
        final String result = invokeMethod(engine, "applyAction", message);
        assertEquals(
                "MSG,0,0,0,4B1613,0,2019/04/19,17:30:04.001,2019/04/19,17:30:04.001,BAW256,20350,442.2,358.6,49.6684,8.4823,0,4022,0,0,0,0",
                result);
    }
}
