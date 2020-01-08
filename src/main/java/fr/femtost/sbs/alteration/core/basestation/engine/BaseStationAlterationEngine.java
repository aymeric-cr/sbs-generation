package fr.femtost.sbs.alteration.core.basestation.engine;

import fr.femtost.sbs.alteration.core.basestation.message.BaseStationMessage;
import fr.femtost.sbs.alteration.core.basestation.message.parameter.*;
import fr.femtost.sbs.alteration.core.engine.Message;
import fr.femtost.sbs.alteration.core.incident.Action;
import fr.femtost.sbs.alteration.core.incident.Parameter;
import fr.femtost.sbs.alteration.core.incident.Recording;
import fr.femtost.sbs.alteration.core.incident.UnknownScopeException;

import static fr.femtost.sbs.alteration.core.engine.AlterationUtils.getIcaoRandomOffset;

public class BaseStationAlterationEngine extends BaseStationActionEngine {

    BaseStationAlterationEngine(final Recording recording, final Action action) {
        super(recording, action);
    }

    static void alterIcao(final Message message, String value) {
        if (value.compareToIgnoreCase("random") == 0) {
            value = getIcaoRandomOffset(message.getIcao(), 1000);
        }
        message.setIcao(value);
    }

    static void alterCallsign(final CallsignParameter message, final String value) {
        message.setCallsign(value);
    }

    static void alterAltitude(final AltitudeParameter message, final int value, final boolean offset) {
        message.getAltitude().ifPresent(integer -> message.setAltitude(offset ? integer + value : value));
    }

    static void alterGroundSpeed(final GroundSpeedParameter message, final double value, final boolean offset) {
        message.getGroundSpeed().ifPresent(aDouble -> message.setGroundSpeed(offset ? aDouble + value : value));
    }

    static void alterTrack(final TrackParameter message, final double value, final boolean offset) {
        message.getTrack().ifPresent(aDouble -> message.setTrack(offset ? aDouble + value : value));
    }

    static void alterLatitude(final LatitudeParameter message, final double value, final boolean offset) {
        message.getLatitude().ifPresent(aDouble -> message.setLatitude(offset ? aDouble + value : value));
    }

    static void alterLongitude(final LongitudeParameter message, final double value, final boolean offset) {
        message.getLongitude().ifPresent(aDouble -> message.setLongitude(offset ? aDouble + value : value));
    }

    static void alterVerticalRate(final VerticalRateParameter message, final int value, final boolean offset) {
        message.getVerticalRate().ifPresent(integer -> message.setVerticalRate(offset ? integer + value : value));
    }

    static void alterSquawk(final SquawkParameter message, final int value, final boolean offset) {
        message.getSquawk().ifPresent(integer -> message.setSquawk(offset ? integer + value : value));
    }

    static void alterAlert(final AlertParameter message, final boolean value) {
        message.isAlert().ifPresent(integer -> message.setAlert(value));
    }

    static void alterEmergency(final EmergencyParameter message, final boolean value) {
        message.isEmergency().ifPresent(integer -> message.setEmergency(value));
    }

    static void alterSpi(final SpiParameter message, final boolean value) {
        message.isSpi().ifPresent(integer -> message.setSpi(value));
    }

    static void alterOnGround(final OnGroundParameter message, final boolean value) {
        message.isOnGround().ifPresent(integer -> message.setOnGround(value));
    }

    @Override
    protected String applyAction(Message message) throws UnknownScopeException {
        final BaseStationMessage baseStationMessage = (BaseStationMessage) message;
        final String targets = action.getParameters().getTarget().getContent();
        if (isMessageInScope(baseStationMessage, action.getScope(), recording.getFirstDate()) &&
                isMessageTargeted(message, targets)) {
            processAlteration(baseStationMessage, action);
        }
        return message.toString();
    }

    private void processAlteration(final BaseStationMessage message, final Action action) {
        for (final Parameter parameter : action.getParameters().getParameterList()) {
            new AlterationBstMessageVisitor(parameter).accept(message);
        }
    }
}