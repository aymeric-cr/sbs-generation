package fr.femtost.sbs.alteration.core.basestation.engine;

import fr.femtost.sbs.alteration.core.engine.AlterationUtils;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Ignore;
import org.junit.Test;

import static fr.femtost.sbs.alteration.core.basestation.engine.AircraftTrajectoryHelper.*;
import static org.junit.Assert.*;

public class AircraftTrajectoryTest {

    @Test(expected = NumberIsTooSmallException.class)
    public void interpolate_no_data() {
        final AircraftTrajectory trajectory = aircraftTrajectory(withWayPoints());
        trajectory.interpolate();
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void interpolate_no_enough_points() {
        final AircraftTrajectory trajectory = aircraftTrajectory(withWayPoints(
                wayPoint(1.0, 9.5, 15000, 1000),
                wayPoint(1.1, 9.6, 15025, 2000),
                wayPoint(1.2, 9.7, 15050, 3000),
                wayPoint(1.3, 9.8, 15075, 4000)));
        trajectory.interpolate();
    }

    @Test
    public void interpolate_ok() {
        final AircraftTrajectory trajectory = aircraftTrajectory(withWayPoints(
                wayPoint(1.0, 9.5, 15000, 1000),
                wayPoint(1.1, 9.6, 15025, 2000),
                wayPoint(1.2, 9.7, 15050, 3000),
                wayPoint(1.3, 9.8, 15075, 4000),
                wayPoint(1.4, 9.9, 15100, 5000)));
        trajectory.interpolate();
        assertEquals(4, trajectory.getLatitudeFunction().getN());
        assertEquals(4, trajectory.getLongitudeFunction().getN());
        assertEquals(4, trajectory.getAltitudeFunction().getN());

        assertFalse(trajectory.getAltitude(999).isPresent());
        assertFalse(trajectory.getTrack(999).isPresent());
        assertFalse(trajectory.getGroundSpeed(999).isPresent());
        assertFalse(trajectory.getLatitude(999).isPresent());
        assertFalse(trajectory.getLongitude(999).isPresent());

        assertEquals(1.0, trajectory.getLatitude(1000).get(), 0.1);
        assertEquals(1.1, trajectory.getLatitude(2000).get(), 0.1);
        assertEquals(1.2, trajectory.getLatitude(3000).get(), 0.1);
        assertEquals(1.3, trajectory.getLatitude(4000).get(), 0.1);
        assertEquals(1.4, trajectory.getLatitude(5000).get(), 0.1);

        assertEquals(9.5, trajectory.getLongitude(1000).get(), 0.1);
        assertEquals(9.6, trajectory.getLongitude(2000).get(), 0.1);
        assertEquals(9.7, trajectory.getLongitude(3000).get(), 0.1);
        assertEquals(9.8, trajectory.getLongitude(4000).get(), 0.1);
        assertEquals(9.9, trajectory.getLongitude(5000).get(), 0.1);

        assertEquals(15000, trajectory.getAltitude(1000).get(), 0.1);
        assertEquals(15025, trajectory.getAltitude(2000).get(), 0.1);
        assertEquals(15050, trajectory.getAltitude(3000).get(), 0.1);
        assertEquals(15075, trajectory.getAltitude(4000).get(), 0.1);
        assertEquals(15100, trajectory.getAltitude(5000).get(), 0.1);
    }

    @Test
    public void getTrack() {
        final AircraftTrajectory trajectory = aircraftTrajectory(withWayPoints(
                wayPoint(0.0, 0.0, 10000, 1000),
                wayPoint(1.0, 0.0, 10000, 2000),
                wayPoint(2.0, 0.0, 10000, 3000),
                wayPoint(2.0, 1.0, 10000, 4000),
                wayPoint(2.0, 2.0, 10000, 5000),
                wayPoint(1.0, 2.0, 10000, 6000),
                wayPoint(0.0, 2.0, 10000, 7000),
                wayPoint(0.0, 1.0, 10000, 8000),
                wayPoint(0.0, 0.0, 10000, 9000)));
        trajectory.interpolate();
        assertFalse(trajectory.getTrack(0).isPresent());
        assertFalse(trajectory.getTrack(1000).isPresent());
        assertFalse(trajectory.getTrack(1499).isPresent());
        assertFalse(trajectory.getTrack(9501).isPresent());
        assertEquals(0.0, trajectory.getTrack(2500).get(), 0.1);
        assertEquals(90.0, trajectory.getTrack(4500).get(), 0.1);
        assertEquals(180.0, trajectory.getTrack(6500).get(), 0.1);
        assertEquals(270.0, trajectory.getTrack(8500).get(), 0.1);
    }

    @Ignore
    @Test
    public void getVerticalRate() {
        final AircraftTrajectory trajectory = aircraftTrajectory(withWayPoints(
                wayPoint(0.0, 0.0, 10000, 0),
                wayPoint(1.0, 0.0, 11000, 10000),
                wayPoint(1.0, 0.0, 10900, 11000),
                wayPoint(2.0, 0.0, 10000, 20000),
                wayPoint(2.0, 1.0, 10500, 30000),
                wayPoint(2.0, 2.0, 10000, 40000)));
        trajectory.interpolate();
        assertFalse(trajectory.getVerticalRate(0).isPresent());
        assertFalse(trajectory.getVerticalRate(499).isPresent());
        assertFalse(trajectory.getVerticalRate(99501).isPresent());
        assertEquals(6000, (int) trajectory.getVerticalRate(5000).get());
        assertEquals(-6000, (int) trajectory.getVerticalRate(15000).get());
        assertEquals(3000, (int) trajectory.getVerticalRate(25000).get());
        assertEquals(-3000, (int) trajectory.getVerticalRate(35000).get());
    }

    @Ignore
    @Test
    public void getGroundSpeed() {
        /* MSG,3,28,835,4008B4,3321,2018/02/09,14:13:27.690,2018/02/09,14:13:28.578,,26825,,,46.89606,4.62689,,,0,0,0,0
           MSG,4,28,835,4008B4,3321,2018/02/09,14:13:28.090,2018/02/09,14:13:28.578,,,410.2,120.6,,,-960,,,,,
           MSG,3,28,835,4008B4,3321,2018/02/09,14:13:28.296,2018/02/09,14:13:28.578,,26825,,,46.89565,4.62785,,,0,0,0,0*/
        // timestamp : 1518185607690 - pos: (46.89606,4.62689)
        // ground speed : 410.2 kts
        // timestamp : 1518185608578 - pos: (46.89565,4.62785)
        // time diff: 1518185608578 - 1518185607690 = 888 ms
        // computed speed : 188.5 kts
        AlterationUtils.computeSpeed(46.89606, 4.62689, 46.89565, 4.62785, 888);
        final AircraftTrajectory trajectory = aircraftTrajectory(withWayPoints(
                wayPoint(0.0, 0.0, 10000, 1000),
                wayPoint(0.0, 1.0, 10100, 2000),
                wayPoint(0.0, 2.0, 10200, 3000),
                wayPoint(0.0, 3.0, 10300, 4000),
                wayPoint(0.0, 4.0, 10400, 5000),
                wayPoint(0.0, 5.0, 10500, 6000),
                wayPoint(0.0, 6.0, 10600, 7000),
                wayPoint(0.0, 7.0, 10700, 8000),
                wayPoint(0.0, 8.0, 10800, 9000)));
        trajectory.interpolate();
        fail();
    }
}