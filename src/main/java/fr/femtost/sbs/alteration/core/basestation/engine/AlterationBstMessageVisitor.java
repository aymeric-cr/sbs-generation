package fr.femtost.sbs.alteration.core.basestation.engine;

import fr.femtost.sbs.alteration.core.basestation.message.parameter.*;
import fr.femtost.sbs.alteration.core.engine.Message;
import fr.femtost.sbs.alteration.core.scenario.Parameter;

import static fr.femtost.sbs.alteration.core.basestation.engine.BaseStationAlterationEngine.*;
import static fr.femtost.sbs.alteration.core.scenario.Parameter.*;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class AlterationBstMessageVisitor implements ParameterVisitor {

    private final Parameter parameter;
    private final int step;

    public AlterationBstMessageVisitor(final Parameter parameter, final int step) {
        this.parameter = parameter;
        this.step = step;
    }

    public AlterationBstMessageVisitor(final Parameter parameter) {
        this.parameter = parameter;
        this.step = 1;
    }

    private static boolean isParameterCharacteristic(final Parameter parameter, final String characteristic) {
        return parameter.getCharacteristic().compareTo(characteristic) == 0;
    }


    @Override
    public void visitIcaoParameter(final Message message) {
        if (isParameterCharacteristic(parameter, CHARAC_HEX_IDENT)) {
            alterIcao(message, parameter.getValue());
        }
    }

    @Override
    public void visitCallsignParameter(final CallsignParameter message) {
        if (isParameterCharacteristic(parameter, CHARAC_CALLSIGN)) {
            alterCallsign(message, parameter.getValue());
        }
    }

    @Override
    public void visitAltitudeParameter(final AltitudeParameter message) {
        if (isParameterCharacteristic(parameter, CHARAC_ALTITUDE)) {
            alterAltitude(message, parseInt(parameter.getValue()), parameter.getMode(), step);
        }
    }

    @Override
    public void visitGroundSpeedParameter(final GroundSpeedParameter message) {
        if (isParameterCharacteristic(parameter, CHARAC_GROUNDSPEED)) {
            alterGroundSpeed(message, parseDouble(parameter.getValue()), parameter.getMode(), step);
        }
    }

    @Override
    public void visitTrackParameter(final TrackParameter message) {
        if (isParameterCharacteristic(parameter, CHARAC_TRACK)) {
            alterTrack(message, parseDouble(parameter.getValue()), parameter.getMode(), step);
        }
    }

    @Override
    public void visitLatitudeParameter(final LatitudeParameter message) {
        if (isParameterCharacteristic(parameter, CHARAC_LATITUDE)) {
            alterLatitude(message, parseDouble(parameter.getValue()), parameter.getMode(), step);
        }
    }

    @Override
    public void visitLongitudeParameter(final LongitudeParameter message) {
        if (isParameterCharacteristic(parameter, CHARAC_LONGITUDE)) {
            alterLongitude(message, parseDouble(parameter.getValue()), parameter.getMode(), step);
        }
    }

    @Override
    public void visitVerticalRateParameter(final VerticalRateParameter message) {
        if (isParameterCharacteristic(parameter, CHARAC_VERTICALRATE)) {
            alterVerticalRate(message, parseInt(parameter.getValue()), parameter.getMode(), step);
        }
    }

    @Override
    public void visitSquawkParameter(final SquawkParameter message) {
        if (isParameterCharacteristic(parameter, CHARAC_SQUAWK)) {
            alterSquawk(message, parseInt(parameter.getValue()));
        }
    }

    @Override
    public void visitAlertParameter(final AlertParameter message) {
        if (isParameterCharacteristic(parameter, CHARAC_ALERT)) {
            alterAlert(message, parseBoolean(parameter.getValue()));
        }
    }

    @Override
    public void visitEmergencyParameter(final EmergencyParameter message) {
        if (isParameterCharacteristic(parameter, CHARAC_EMERGENCY)) {
            alterEmergency(message, parseBoolean(parameter.getValue()));
        }
    }

    @Override
    public void visitSpiParameter(final SpiParameter message) {
        if (isParameterCharacteristic(parameter, CHARAC_SPI)) {
            alterSpi(message, parseBoolean(parameter.getValue()));
        }
    }

    @Override
    public void visitOnGroundParameter(final OnGroundParameter message) {
        if (isParameterCharacteristic(parameter, CHARAC_ISONGROUND)) {
            alterOnGround(message, parseBoolean(parameter.getValue()));
        }
    }
}