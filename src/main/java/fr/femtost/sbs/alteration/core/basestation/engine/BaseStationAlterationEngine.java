package fr.femtost.sbs.alteration.core.basestation.engine;

import fr.femtost.sbs.alteration.core.basestation.message.BaseStationMessage;
import fr.femtost.sbs.alteration.core.basestation.message.parameter.*;
import fr.femtost.sbs.alteration.core.engine.Message;
import fr.femtost.sbs.alteration.core.incident.Action;
import fr.femtost.sbs.alteration.core.incident.Parameter;
import fr.femtost.sbs.alteration.core.incident.Recording;
import fr.femtost.sbs.alteration.core.incident.UnknownScopeException;

import java.util.HashMap;

import static com.google.common.collect.Maps.newHashMap;
import static fr.femtost.sbs.alteration.core.engine.AlterationUtils.computeNewValue;
import static fr.femtost.sbs.alteration.core.engine.AlterationUtils.getIcaoRandomOffset;

public class BaseStationAlterationEngine extends BaseStationActionEngine {

    private HashMap<String, Integer> steps = newHashMap();

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

    static void alterAltitude(final AltitudeParameter message,
                              final int value,
                              final String mode,
                              final int step) {
        message.getAltitude().ifPresent(altitude -> message.setAltitude((int) computeNewValue(altitude, value, mode, step)));
    }

    static void alterGroundSpeed(final GroundSpeedParameter message,
                                 final double value,
                                 final String mode,
                                 final int step) {
        message.getGroundSpeed().ifPresent(groundSpeed ->
                message.setGroundSpeed(computeNewValue(groundSpeed, value, mode, step)));
    }

    static void alterTrack(final TrackParameter message,
                           final double value,
                           final String mode,
                           final int step) {
        message.getTrack().ifPresent(track -> message.setTrack(computeNewValue(track, value, mode, step)));
    }

    static void alterLatitude(final LatitudeParameter message,
                              final double value,
                              final String mode,
                              final int step) {
        message.getLatitude().ifPresent(latitude -> message.setLatitude(computeNewValue(latitude, value, mode, step)));
    }

    static void alterLongitude(final LongitudeParameter message,
                               final double value,
                               final String mode,
                               final int step) {
        message.getLongitude().ifPresent(longitude -> message.setLongitude(computeNewValue(longitude, value, mode, step)));
    }

    static void alterVerticalRate(final VerticalRateParameter message,
                                  final int value,
                                  final String mode,
                                  final int step) {
        message.getVerticalRate().ifPresent(verticalRate ->
                message.setVerticalRate((int) computeNewValue(verticalRate, value, mode, step)));
    }

    static void alterSquawk(final SquawkParameter message, final int value) {
        message.getSquawk().ifPresent(integer -> message.setSquawk(value));
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
    protected String applyAction(final Message message) throws UnknownScopeException {
        final BaseStationMessage baseStationMessage = (BaseStationMessage) message;
        final String targets = action.getParameters().getTarget().getContent();
        if (isMessageInScope(baseStationMessage, action.getScope(), recording.getFirstDate()) &&
                isMessageTargeted(message, targets)) {
            processAlteration(baseStationMessage, action, getAndIncrementStep(message.getIcao()));
        }
        return message.toString();
    }

    private int getAndIncrementStep(final String icao) {
        steps.putIfAbsent(icao, 1);
        int step = steps.get(icao);
        steps.put(icao, step + 1);
        return step;
    }

    private void processAlteration(final BaseStationMessage message, final Action action, final int step) {
        for (final Parameter parameter : action.getParameters().getParameterList()) {
            new AlterationBstMessageVisitor(parameter, step).accept(message);
        }
    }
}