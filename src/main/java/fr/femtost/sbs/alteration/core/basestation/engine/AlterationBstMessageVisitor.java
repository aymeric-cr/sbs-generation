package fr.femtost.sbs.alteration.core.basestation.engine;

import fr.femtost.sbs.alteration.core.basestation.message.parameter.*;
import fr.femtost.sbs.alteration.core.engine.Message;
import fr.femtost.sbs.alteration.core.incident.Parameter;

import static fr.femtost.sbs.alteration.core.basestation.engine.BaseStationAlterationEngine.*;
import static fr.femtost.sbs.alteration.core.incident.Parameter.*;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class AlterationBstMessageVisitor implements ParameterVisitor {

    private final Parameter parameter;

    public AlterationBstMessageVisitor(final Parameter parameter) {
        this.parameter = parameter;
    }

    private static boolean isParameterCharacteristic(final Parameter parameter, final String characteristic) {
        return parameter.getCharacteristic().compareTo(characteristic) == 0;
    }


    @Override
    public void visitIcaoParameter(Message message) {
        if (isParameterCharacteristic(parameter, CHARAC_ICAO)) {
            alterIcao(message, parameter.getValue());
        }
    }

    @Override
    public void visitCallsignParameter(CallsignParameter message) {
        if (isParameterCharacteristic(parameter, CHARAC_CALLSIGN)) {
            alterCallsign(message, parameter.getValue());
        }
    }

    @Override
    public void visitAltitudeParameter(AltitudeParameter message) {
        if (isParameterCharacteristic(parameter, CHARAC_ALTITUDE)) {
            alterAltitude(message, parseInt(parameter.getValue()), parameter.isOffset());
        }
    }

    @Override
    public void visitGroundSpeedParameter(GroundSpeedParameter message) {
        if (isParameterCharacteristic(parameter, CHARAC_GROUNDSPEED)) {
            alterGroundSpeed(message, parseDouble(parameter.getValue()), parameter.isOffset());
        }
    }

    @Override
    public void visitTrackParameter(TrackParameter message) {
        if (isParameterCharacteristic(parameter, CHARAC_TRACK)) {
            alterTrack(message, parseDouble(parameter.getValue()), parameter.isOffset());
        }
    }

    @Override
    public void visitLatitudeParameter(LatitudeParameter message) {
        if (isParameterCharacteristic(parameter, CHARAC_LATITUDE)) {
            alterLatitude(message, parseDouble(parameter.getValue()), parameter.isOffset());
        }
    }

    @Override
    public void visitLongitudeParameter(LongitudeParameter message) {
        if (isParameterCharacteristic(parameter, CHARAC_LONGITUDE)) {
            alterLongitude(message, parseDouble(parameter.getValue()), parameter.isOffset());
        }
    }

    @Override
    public void visitVerticalRateParameter(VerticalRateParameter message) {
        if (isParameterCharacteristic(parameter, CHARAC_VERTICALRATE)) {
            alterVerticalRate(message, parseInt(parameter.getValue()), parameter.isOffset());
        }
    }

    @Override
    public void visitSquawkParameter(SquawkParameter message) {
        if (isParameterCharacteristic(parameter, CHARAC_SQUAWK)) {
            alterSquawk(message, parseInt(parameter.getValue()), parameter.isOffset());
        }
    }

    @Override
    public void visitAlertParameter(AlertParameter message) {
        if (isParameterCharacteristic(parameter, CHARAC_ALERT)) {
            alterAlert(message, parseBoolean(parameter.getValue()));
        }
    }

    @Override
    public void visitEmergencyParameter(EmergencyParameter message) {
        if (isParameterCharacteristic(parameter, CHARAC_EMERGENCY)) {
            alterEmergency(message, parseBoolean(parameter.getValue()));
        }
    }

    @Override
    public void visitSpiParameter(SpiParameter message) {
        if (isParameterCharacteristic(parameter, CHARAC_SPI)) {
            alterSpi(message, parseBoolean(parameter.getValue()));
        }
    }

    @Override
    public void visitOnGroundParameter(OnGroundParameter message) {
        if (isParameterCharacteristic(parameter, CHARAC_ISONGROUND)) {
            alterOnGround(message, parseBoolean(parameter.getValue()));
        }
    }
}