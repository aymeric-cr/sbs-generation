package fr.femtost.sbs.alteration.core.basestation.engine;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.io.File;

import static com.google.common.io.Files.write;
import static fr.femtost.sbs.alteration.core.scenario.ScenarioHelper.*;
import static java.io.File.createTempFile;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BaseStationTrajectoryModificationEngineTest {

    @Test
    public void buildNewTrajectories_noModification_noTimeWindow() throws Exception {
        final File recordingFile = createTempFile("initial", ".bst");
        write("MSG,0,30,1105,300065,3839,2018/02/28,16:04:30.987,2018/02/28,16:04:31.888,,,414.1,333.0,,,64,,,,,\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:31.987,2018/02/28,16:04:32.888,,36025,,,47.33528,4.16787,,,0,0,0,0\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:32.391,2018/02/28,16:04:33.888,,36025,,,47.33606,4.16732,,,0,0,0,0\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:32.590,2018/02/28,16:04:34.888,,,415.0,333.1,,,0,,,,,\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:33.801,2018/02/28,16:04:35.888,,36025,,,47.33678,4.16678,,,0,0,0,0\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:33.998,2018/02/28,16:04:36.894,,,415.0,333.1,,,0,,,,,\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:34.404,2018/02/28,16:04:37.894,,36025,,,47.33752,4.16622,,,0,0,0,0\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:34.607,2018/02/28,16:04:38.894,,,415.0,333.1,,,0,,,,,\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:35.608,2018/02/28,16:04:39.894,,36025,,,47.33821,4.16567,,,0,0,0,0\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:36.017,2018/02/28,16:04:40.889,,,415.0,333.1,,,0,,,,,\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:36.217,2018/02/28,16:04:41.889,,36025,,,47.33926,4.16492,,,0,0,0,0",
                recordingFile,
                UTF_8);
        final BaseStationTrajectoryModificationEngine engine = new BaseStationTrajectoryModificationEngine(
                recording(recordingFile, 1519833870987L),
                trajectoryModification(timeWindow(0, 0), parameters(bstTarget("300065"), trajectory())));
        Whitebox.invokeMethod(engine, "buildNewTrajectories");
        assertEquals(1, engine.getTrajectories().size());
        assertTrue(engine.getTrajectories().containsKey("300065"));

        final AircraftTrajectory trajectory = engine.getTrajectories().get("300065");
        assertEquals(6, trajectory.getAltitudes().size());
        assertEquals(6, trajectory.getLongitudes().size());
        assertEquals(6, trajectory.getLatitudes().size());

        assertEquals(47.33528, trajectory.getLatitudes().get(1519833871987.0), 0.0001);
        assertEquals(47.33606, trajectory.getLatitudes().get(1519833872391.0), 0.0001);
        assertEquals(47.33678, trajectory.getLatitudes().get(1519833873801.0), 0.0001);
        assertEquals(47.33752, trajectory.getLatitudes().get(1519833874404.0), 0.0001);
        assertEquals(47.33821, trajectory.getLatitudes().get(1519833875608.0), 0.0001);
        assertEquals(47.33926, trajectory.getLatitudes().get(1519833876217.0), 0.0001);

        assertEquals(4.16787, trajectory.getLongitudes().get(1519833871987.0), 0.0001);
        assertEquals(4.16732, trajectory.getLongitudes().get(1519833872391.0), 0.0001);
        assertEquals(4.16678, trajectory.getLongitudes().get(1519833873801.0), 0.0001);
        assertEquals(4.16622, trajectory.getLongitudes().get(1519833874404.0), 0.0001);
        assertEquals(4.16567, trajectory.getLongitudes().get(1519833875608.0), 0.0001);
        assertEquals(4.16492, trajectory.getLongitudes().get(1519833876217.0), 0.0001);

        assertEquals(36025, trajectory.getAltitudes().get(1519833871987.0), 0.0001);
        assertEquals(36025, trajectory.getAltitudes().get(1519833872391.0), 0.0001);
        assertEquals(36025, trajectory.getAltitudes().get(1519833873801.0), 0.0001);
        assertEquals(36025, trajectory.getAltitudes().get(1519833874404.0), 0.0001);
        assertEquals(36025, trajectory.getAltitudes().get(1519833875608.0), 0.0001);
        assertEquals(36025, trajectory.getAltitudes().get(1519833876217.0), 0.0001);
    }

    @Test
    public void buildNewTrajectories_noModification_withTimeWindow() throws Exception {
        final File recordingFile = createTempFile("initial", ".bst");
        write("MSG,0,30,1105,300065,3839,2018/02/28,16:04:30.987,2018/02/28,16:04:31.888,,,414.1,333.0,,,64,,,,,\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:31.987,2018/02/28,16:04:32.888,,36025,,,47.33528,4.16787,,,0,0,0,0\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:32.391,2018/02/28,16:04:33.888,,36025,,,47.33606,4.16732,,,0,0,0,0\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:32.590,2018/02/28,16:04:34.888,,,415.0,333.1,,,0,,,,,\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:33.801,2018/02/28,16:04:35.888,,36025,,,47.33678,4.16678,,,0,0,0,0\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:33.998,2018/02/28,16:04:36.894,,,415.0,333.1,,,0,,,,,\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:34.404,2018/02/28,16:04:37.894,,36025,,,47.33752,4.16622,,,0,0,0,0\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:34.607,2018/02/28,16:04:38.894,,,415.0,333.1,,,0,,,,,\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:35.608,2018/02/28,16:04:39.894,,36025,,,47.33821,4.16567,,,0,0,0,0\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:36.017,2018/02/28,16:04:40.889,,,415.0,333.1,,,0,,,,,\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:36.217,2018/02/28,16:04:41.889,,36025,,,47.33926,4.16492,,,0,0,0,0",
                recordingFile,
                UTF_8);
        final BaseStationTrajectoryModificationEngine engine = new BaseStationTrajectoryModificationEngine(
                recording(recordingFile, 1519833870987L),
                trajectoryModification(timeWindow(2500, 3000), parameters(bstTarget("300065"), trajectory())));
        Whitebox.invokeMethod(engine, "buildNewTrajectories");
        assertEquals(1, engine.getTrajectories().size());
        assertTrue(engine.getTrajectories().containsKey("300065"));

        final AircraftTrajectory trajectory = engine.getTrajectories().get("300065");
        assertEquals(6, trajectory.getAltitudes().size());
        assertEquals(6, trajectory.getLongitudes().size());
        assertEquals(6, trajectory.getLatitudes().size());

        assertEquals(47.33528, trajectory.getLatitudes().get(1519833871987.0), 0.0001);
        assertEquals(47.33606, trajectory.getLatitudes().get(1519833872391.0), 0.0001);
        assertEquals(47.33752, trajectory.getLatitudes().get(1519833874404.0), 0.0001);
        assertEquals(47.33821, trajectory.getLatitudes().get(1519833875608.0), 0.0001);
        assertEquals(47.33926, trajectory.getLatitudes().get(1519833876217.0), 0.0001);

        assertEquals(4.16787, trajectory.getLongitudes().get(1519833871987.0), 0.0001);
        assertEquals(4.16732, trajectory.getLongitudes().get(1519833872391.0), 0.0001);
        assertEquals(4.16622, trajectory.getLongitudes().get(1519833874404.0), 0.0001);
        assertEquals(4.16567, trajectory.getLongitudes().get(1519833875608.0), 0.0001);
        assertEquals(4.16492, trajectory.getLongitudes().get(1519833876217.0), 0.0001);

        assertEquals(36025, trajectory.getAltitudes().get(1519833871987.0), 0.0001);
        assertEquals(36025, trajectory.getAltitudes().get(1519833872391.0), 0.0001);
        assertEquals(36025, trajectory.getAltitudes().get(1519833874404.0), 0.0001);
        assertEquals(36025, trajectory.getAltitudes().get(1519833875608.0), 0.0001);
        assertEquals(36025, trajectory.getAltitudes().get(1519833876217.0), 0.0001);
    }

    @Test
    public void buildNewTrajectories_withModification_withTimeWindow() throws Exception {
        final File recordingFile = createTempFile("initial", ".bst");
        write("MSG,0,30,1105,300065,3839,2018/02/28,16:04:30.987,2018/02/28,16:04:31.888,,,414.1,333.0,,,64,,,,,\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:31.987,2018/02/28,16:04:32.888,,36025,,,47.33528,4.16787,,,0,0,0,0\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:32.391,2018/02/28,16:04:33.888,,36025,,,47.33606,4.16732,,,0,0,0,0\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:32.590,2018/02/28,16:04:34.888,,,415.0,333.1,,,0,,,,,\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:33.801,2018/02/28,16:04:35.888,,36025,,,47.33678,4.16678,,,0,0,0,0\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:33.998,2018/02/28,16:04:36.894,,,415.0,333.1,,,0,,,,,\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:34.404,2018/02/28,16:04:37.894,,36025,,,47.33752,4.16622,,,0,0,0,0\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:34.607,2018/02/28,16:04:38.894,,,415.0,333.1,,,0,,,,,\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:35.608,2018/02/28,16:04:39.894,,36025,,,47.33821,4.16567,,,0,0,0,0\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:36.017,2018/02/28,16:04:40.889,,,415.0,333.1,,,0,,,,,\n" +
                        "MSG,0,30,1105,300065,3839,2018/02/28,16:04:36.217,2018/02/28,16:04:41.889,,36025,,,47.33926,4.16492,,,0,0,0,0",
                recordingFile,
                UTF_8);
        final BaseStationTrajectoryModificationEngine engine = new BaseStationTrajectoryModificationEngine(
                recording(recordingFile, 1519833870987L),
                trajectoryModification(
                        timeWindow(2500, 5000),
                        parameters(
                                bstTarget("300065"),
                                trajectory(
                                        wayPoint(750, 47.33688, 4.16668, 36000),
                                        wayPoint(1500, 47.33762, 4.16612, 35975),
                                        wayPoint(2100, 47.33831, 4.16557, 36000)))));
        Whitebox.invokeMethod(engine, "buildNewTrajectories");
        assertEquals(1, engine.getTrajectories().size());
        assertTrue(engine.getTrajectories().containsKey("300065"));

        final AircraftTrajectory trajectory = engine.getTrajectories().get("300065");
        assertEquals(7, trajectory.getAltitudes().size());
        assertEquals(7, trajectory.getLongitudes().size());
        assertEquals(7, trajectory.getLatitudes().size());
        assertEquals(47.33528, trajectory.getLatitudes().get(1519833871987.0), 0.0001);
        assertEquals(47.33606, trajectory.getLatitudes().get(1519833872391.0), 0.0001);
        assertEquals(47.33688, trajectory.getLatitudes().get(1519833874551.0), 0.0001); // 1519833873801L (first altered message date) + 750
        assertEquals(47.33762, trajectory.getLatitudes().get(1519833875301.0), 0.0001); // 1519833873801L + 1500
        assertEquals(47.33831, trajectory.getLatitudes().get(1519833875901.0), 0.0001); // 1519833873801L + 2100
        assertEquals(47.33926, trajectory.getLatitudes().get(1519833876217.0), 0.0001);

        assertEquals(4.16787, trajectory.getLongitudes().get(1519833871987.0), 0.0001);
        assertEquals(4.16732, trajectory.getLongitudes().get(1519833872391.0), 0.0001);
        assertEquals(4.16668, trajectory.getLongitudes().get(1519833874551.0), 0.0001); // 1519833873801L (first altered message date) + 750
        assertEquals(4.16612, trajectory.getLongitudes().get(1519833875301.0), 0.0001); // 1519833873801L + 1500
        assertEquals(4.16557, trajectory.getLongitudes().get(1519833875901.0), 0.0001); // 1519833873801L + 2100
        assertEquals(4.16492, trajectory.getLongitudes().get(1519833876217.0), 0.0001);

        assertEquals(36025, trajectory.getAltitudes().get(1519833871987.0), 0.0001);
        assertEquals(36025, trajectory.getAltitudes().get(1519833872391.0), 0.0001);
        assertEquals(36000, trajectory.getAltitudes().get(1519833874551.0), 0.0001); // 1519833873801L (first altered message date) + 750
        assertEquals(35975, trajectory.getAltitudes().get(1519833875301.0), 0.0001); // 1519833873801L + 1500
        assertEquals(36000, trajectory.getAltitudes().get(1519833875901.0), 0.0001); // 1519833873801L + 2100
        assertEquals(36025, trajectory.getAltitudes().get(1519833876217.0), 0.0001);
    }
}