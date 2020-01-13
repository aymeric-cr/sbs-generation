package fr.femtost.sbs.alteration.core.basestation.engine;

import fr.femtost.sbs.alteration.core.basestation.message.BaseStationMessageFull;
import org.junit.Test;

import static fr.femtost.sbs.alteration.core.basestation.BstHelper.*;
import static fr.femtost.sbs.alteration.core.scenario.ScenarioHelper.parameter;
import static fr.femtost.sbs.alteration.core.scenario.Parameter.*;
import static testools.PredicateAssert.assertThat;

public class AlterationBaseStationMessageVisitorTest {

    @Test
    public void processAlteration_Message1_latitude_noOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349));
        new AlterationBstMessageVisitor(parameter(CHARAC_LATITUDE, "43.22")).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(43.22),
                withLongitude(0.25),
                withAltitude(12349)));
    }

    @Test
    public void processAlteration_Message1_latitude_posOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349));
        new AlterationBstMessageVisitor(parameter(CHARAC_LATITUDE, "1.22", MODE_OFFSET)).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(51.42),
                withLongitude(0.25),
                withAltitude(12349)));
    }

    @Test
    public void processAlteration_Message1_latitude_negOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349));
        new AlterationBstMessageVisitor(parameter(CHARAC_LATITUDE, "-5.7", MODE_OFFSET)).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(44.5),
                withLongitude(0.25),
                withAltitude(12349)));
    }

    @Test
    public void processAlteration_Message1_longitude_noOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349));
        new AlterationBstMessageVisitor(parameter(CHARAC_LONGITUDE, "24.2")).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(24.2),
                withAltitude(12349)));
    }

    @Test
    public void processAlteration_Message1_longitude_posOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349));
        new AlterationBstMessageVisitor(parameter(CHARAC_LONGITUDE, "30.22", MODE_OFFSET)).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(30.47),
                withAltitude(12349)));
    }

    @Test
    public void processAlteration_Message1_longitude_negOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(20.25), altitude(12349));
        new AlterationBstMessageVisitor(parameter(CHARAC_LONGITUDE, "-12.24", MODE_OFFSET)).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(8.01),
                withAltitude(12349)));
    }

    @Test
    public void processAlteration_Message1_altitude_noOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349));
        new AlterationBstMessageVisitor(parameter(CHARAC_ALTITUDE, "27465")).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(27465)));
    }

    @Test
    public void processAlteration_Message1_altitude_posOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349));
        new AlterationBstMessageVisitor(parameter(CHARAC_ALTITUDE, "1023", MODE_OFFSET)).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(13372)));
    }

    @Test
    public void processAlteration_Message1_altitude_negOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349));
        new AlterationBstMessageVisitor(parameter(CHARAC_ALTITUDE, "-24", MODE_OFFSET)).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12325)));
    }

    @Test
    public void processAlteration_Message1_groundspeed_noOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341.0));
        new AlterationBstMessageVisitor(parameter(CHARAC_GROUNDSPEED, "412")).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(412)));
    }

    @Test
    public void processAlteration_Message1_groundspeed_posOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341));
        new AlterationBstMessageVisitor(parameter(CHARAC_GROUNDSPEED, "4.99", MODE_OFFSET)).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(345.99)));
    }

    @Test
    public void processAlteration_Message1_groundspeed_negOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341));
        new AlterationBstMessageVisitor(parameter(CHARAC_GROUNDSPEED, "-340.0", MODE_OFFSET)).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(1)));
    }


    @Test
    public void processAlteration_Message1_vertrate_noOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341), verticalRate(4));
        new AlterationBstMessageVisitor(parameter(CHARAC_VERTICALRATE, "6")).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(341),
                withVerticalRate(6)));
    }

    @Test
    public void processAlteration_Message1_vertrate_posOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341), verticalRate(4));
        new AlterationBstMessageVisitor(parameter(CHARAC_VERTICALRATE, "1023", MODE_OFFSET)).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(341),
                withVerticalRate(1027)));
    }

    @Test
    public void processAlteration_Message1_vertrate_negOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341), verticalRate(4));
        new AlterationBstMessageVisitor(parameter(CHARAC_VERTICALRATE, "-2", MODE_OFFSET)).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(341),
                withVerticalRate(2)));
    }

    @Test
    public void processAlteration_Message1_alert_noOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341), verticalRate(4), alert(true));
        new AlterationBstMessageVisitor(parameter(CHARAC_ALERT, "false")).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(341),
                withVerticalRate(4),
                withAlert(false)));
    }

    @Test
    public void processAlteration_Message1_alert_noOffset_same() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341), verticalRate(4), alert(true));
        new AlterationBstMessageVisitor(parameter(CHARAC_ALERT, "true")).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(341),
                withVerticalRate(4),
                withAlert(true)));
    }

    @Test
    public void processAlteration_Message1_alert_posOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341), verticalRate(4), alert(true));
        new AlterationBstMessageVisitor(parameter(CHARAC_ALERT, "false", MODE_OFFSET)).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(341),
                withVerticalRate(4),
                withAlert(false)));
    }

    @Test
    public void processAlteration_Message1_alert_negOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341), verticalRate(4), alert(true));
        new AlterationBstMessageVisitor(parameter(CHARAC_ALERT, "true", MODE_OFFSET)).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(341),
                withVerticalRate(4),
                withAlert(true)));
    }


    @Test
    public void processAlteration_Message1_emergency_noOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341), verticalRate(4), alert(true), emergency(false));
        new AlterationBstMessageVisitor(parameter(CHARAC_EMERGENCY, "true")).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(341),
                withVerticalRate(4),
                withAlert(true),
                withEmergency(true)));
    }

    @Test
    public void processAlteration_Message1_emergency_noOffset_same() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341), verticalRate(4), alert(true), emergency(false));
        new AlterationBstMessageVisitor(parameter(CHARAC_EMERGENCY, "false")).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(341),
                withVerticalRate(4),
                withAlert(true),
                withEmergency(false)));
    }

    @Test
    public void processAlteration_Message1_emergency_posOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341), verticalRate(4), alert(true), emergency(false));
        new AlterationBstMessageVisitor(parameter(CHARAC_EMERGENCY, "false", MODE_OFFSET)).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(341),
                withVerticalRate(4),
                withAlert(true),
                withEmergency(false)));
    }

    @Test
    public void processAlteration_Message1_emergency_negOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341), verticalRate(4), alert(true), emergency(false));
        new AlterationBstMessageVisitor(parameter(CHARAC_EMERGENCY, "true", MODE_OFFSET)).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(341),
                withVerticalRate(4),
                withAlert(true),
                withEmergency(true)));
    }

    @Test
    public void processAlteration_Message1_spi_noOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341), verticalRate(4), alert(true), emergency(false), spi(true));
        new AlterationBstMessageVisitor(parameter(CHARAC_SPI, "false")).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(341),
                withVerticalRate(4),
                withAlert(true),
                withEmergency(false),
                withSpi(false)));
    }

    @Test
    public void processAlteration_Message1_spi_noOffset_same() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341), verticalRate(4), alert(true), emergency(false), spi(true));
        new AlterationBstMessageVisitor(parameter(CHARAC_SPI, "true")).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(341),
                withVerticalRate(4),
                withAlert(true),
                withEmergency(false),
                withSpi(true)));
    }

    @Test
    public void processAlteration_Message1_spi_posOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341), verticalRate(4), alert(true), emergency(false), spi(true));
        new AlterationBstMessageVisitor(parameter(CHARAC_SPI, "false", MODE_OFFSET)).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(341),
                withVerticalRate(4),
                withAlert(true),
                withEmergency(false),
                withSpi(false)));
    }

    @Test
    public void processAlteration_Message1_spi_negOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341), verticalRate(4), alert(true), emergency(false), spi(true));
        new AlterationBstMessageVisitor(parameter(CHARAC_SPI, "true", MODE_OFFSET)).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(341),
                withVerticalRate(4),
                withAlert(true),
                withEmergency(false),
                withSpi(true)));
    }

    @Test
    public void processAlteration_Message1_onGround_noOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341), verticalRate(4), alert(true), emergency(false), spi(true), onGround(false));
        new AlterationBstMessageVisitor(parameter(CHARAC_ISONGROUND, "true")).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(341),
                withVerticalRate(4),
                withAlert(true),
                withEmergency(false),
                withSpi(true),
                withOnGround(true)));
    }

    @Test
    public void processAlteration_Message1_onGround_noOffset_same() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341), verticalRate(4), alert(true), emergency(false), spi(true), onGround(false));
        new AlterationBstMessageVisitor(parameter(CHARAC_ISONGROUND, "false")).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(341),
                withVerticalRate(4),
                withAlert(true),
                withEmergency(false),
                withSpi(true),
                withOnGround(false)));
    }

    @Test
    public void processAlteration_Message1_onGround_posOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341), verticalRate(4), alert(true), emergency(false), spi(true), onGround(false));
        new AlterationBstMessageVisitor(parameter(CHARAC_ISONGROUND, "true", MODE_OFFSET)).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(341),
                withVerticalRate(4),
                withAlert(true),
                withEmergency(false),
                withSpi(true),
                withOnGround(true)));
    }

    @Test
    public void processAlteration_Message1_onGround_negOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341), verticalRate(4), alert(true), emergency(false), spi(true), onGround(false));
        new AlterationBstMessageVisitor(parameter(CHARAC_ISONGROUND, "false", MODE_OFFSET)).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(341),
                withVerticalRate(4),
                withAlert(true),
                withEmergency(false),
                withSpi(true),
                withOnGround(false)));
    }

    @Test
    public void processAlteration_Message1_track_noOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341), verticalRate(4), alert(true), emergency(false), spi(true), onGround(false), track(12.1));
        new AlterationBstMessageVisitor(parameter(CHARAC_TRACK, "34.988")).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(341),
                withVerticalRate(4),
                withAlert(true),
                withEmergency(false),
                withSpi(true),
                withOnGround(false),
                withTrack(34.988)));
    }

    @Test
    public void processAlteration_Message1_track_posOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341), verticalRate(4), alert(true), emergency(false), spi(true), onGround(false), track(12.1));
        new AlterationBstMessageVisitor(parameter(CHARAC_TRACK, "11.999999", MODE_OFFSET)).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(341),
                withVerticalRate(4),
                withAlert(true),
                withEmergency(false),
                withSpi(true),
                withOnGround(false),
                withTrack(24.099999)));
    }

    @Test
    public void processAlteration_Message1_track_negOffset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341), verticalRate(4), alert(true), emergency(false), spi(true), onGround(false), track(12.1));
        new AlterationBstMessageVisitor(parameter(CHARAC_TRACK, "-1.00001", MODE_OFFSET)).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(341),
                withVerticalRate(4),
                withAlert(true),
                withEmergency(false),
                withSpi(true),
                withOnGround(false),
                withTrack(11.09999)));
    }


    @Test
    public void processAlteration_Message1_icao() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341), verticalRate(4), alert(true), emergency(false), spi(true), onGround(false), track(12.1));
        new AlterationBstMessageVisitor(parameter(CHARAC_HEX_IDENT, "25TR987")).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("25TR987"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(341),
                withVerticalRate(4),
                withAlert(true),
                withEmergency(false),
                withSpi(true),
                withOnGround(false),
                withTrack(12.1)));
    }

    @Test
    public void processAlteration_Message1_icao_offset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341), verticalRate(4), alert(true), emergency(false), spi(true), onGround(false), track(12.1));
        new AlterationBstMessageVisitor(parameter(CHARAC_HEX_IDENT, "25TR987", MODE_OFFSET)).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("25TR987"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(341),
                withVerticalRate(4),
                withAlert(true),
                withEmergency(false),
                withSpi(true),
                withOnGround(false),
                withTrack(12.1)));
    }

    @Test
    public void processAlteration_Message1_callsign() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341), verticalRate(4), alert(true), emergency(false), spi(true), onGround(false), track(12.1), callsign("ER34OT9"));
        new AlterationBstMessageVisitor(parameter(CHARAC_CALLSIGN, "34A789")).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(341),
                withVerticalRate(4),
                withAlert(true),
                withEmergency(false),
                withSpi(true),
                withOnGround(false),
                withTrack(12.1),
                withCallsign("34A789")));
    }

    @Test
    public void processAlteration_Message1_callsign_offset() {
        final BaseStationMessageFull message = bstMessageFull("39AC47", 1555694990L, latitude(50.2), longitude(0.25), altitude(12349),
                groundSpeed(341), verticalRate(4), alert(true), emergency(false), spi(true), onGround(false), track(12.1), callsign("ER34OT9"));
        new AlterationBstMessageVisitor(parameter(CHARAC_CALLSIGN, "34A789", MODE_OFFSET)).accept(message);
        assertThat(message, aBstMessageFull(
                withIcao("39AC47"),
                withDate(1555694990L),
                withLatitude(50.2),
                withLongitude(0.25),
                withAltitude(12349),
                withGroundSpeed(341),
                withVerticalRate(4),
                withAlert(true),
                withEmergency(false),
                withSpi(true),
                withOnGround(false),
                withTrack(12.1),
                withCallsign("34A789")));
    }
}