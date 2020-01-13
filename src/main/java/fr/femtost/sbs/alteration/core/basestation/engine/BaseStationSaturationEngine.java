package fr.femtost.sbs.alteration.core.basestation.engine;

import fr.femtost.sbs.alteration.core.basestation.message.BaseStationMessage;
import fr.femtost.sbs.alteration.core.basestation.message.parameter.*;
import fr.femtost.sbs.alteration.core.engine.Message;
import fr.femtost.sbs.alteration.core.scenario.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static fr.femtost.sbs.alteration.core.engine.AlterationUtils.*;
import static java.lang.Math.*;
import static java.util.Collections.shuffle;

public class BaseStationSaturationEngine extends BaseStationActionEngine {

    private static final double TRANSLATION_DURATION = 600000;
    private static final double DISTANCE = 0.2;
    private final Map<String, AircraftTrajectory> trajectories = newHashMap();
    private int aircraftNumber = 0;
    private HashMap<String, List<GhostAircraft>> ghostAircrafts = newHashMap();

    BaseStationSaturationEngine(final Recording recording, final Action action) {
        super(recording, action);
        for (final Parameter parameter : action.getParameters().getParameterList()) {
            if (parameter.getNumber() > 0) {
                aircraftNumber = parameter.getNumber();
            }
        }
    }

    private static String messagesToLines(final Collection<Message> messages) {
        final StringBuilder builder = new StringBuilder();
        for (final Message message : messages) {
            builder.append(message.toString()).append('\n');
        }
        builder.deleteCharAt(builder.lastIndexOf("\n"));
        return builder.toString();
    }

    private void createGhostAircrafts(final BaseStationMessage initialMessage) {
        if (!this.ghostAircrafts.containsKey(initialMessage.getIcao())) {
            final List<GhostAircraft> ghostAircraftList = newArrayList();
            for (int i = 0; i < aircraftNumber; i++) {
                ghostAircraftList.add(new GhostAircraft(
                        getIcaoRandomOffset(initialMessage.getIcao(), 1000),
                        generateRandomAngle(),
                        generateRandomDistanceCoeff(),
                        initialMessage.getTimestampGenerated()));
            }
            this.ghostAircrafts.put(initialMessage.getIcao(), ghostAircraftList);
        }
    }

    @Override
    protected File processAction() throws IOException, UnknownScopeException, UnknownCharacteristicException {
        buildNewTrajectories();
        return super.processAction();
    }

    private void buildNewTrajectories() throws IOException, UnknownScopeException {
        try (final FileReader fileReader = new FileReader(recording.getFile());
             final BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String currentMessage = bufferedReader.readLine();
            while (currentMessage != null) {
                final Optional<? extends Message> message = messagePreprocessing(currentMessage);
                if (message.isPresent() && message.get() instanceof BaseStationMessage &&
                        isMessageTargeted(message.get(), action.getParameters().getTarget().getContent()) &&
                        isMessageInScope((BaseStationMessage) message.get(), action.getScope(), recording.getFirstDate())) {
                    createGhostAircrafts((BaseStationMessage) message.get());
                    updateTrajectory((BaseStationMessage) message.get());
                }
                currentMessage = bufferedReader.readLine();
            }
        }
        trajectories.values().forEach(AircraftTrajectory::interpolate);
    }

    private void updateTrajectory(final BaseStationMessage message) {
        for (final GhostAircraft ghostAircraft : ghostAircrafts.get(message.getIcao())) {
            trajectories.putIfAbsent(ghostAircraft.getIcao(), new AircraftTrajectory());
            final AircraftTrajectory trajectory = trajectories.get(ghostAircraft.getIcao());

            double progress = min((message.getTimestampGenerated() - ghostAircraft.getFirstAppearance()) / TRANSLATION_DURATION, 1);
            final double stepDistance = progress * ghostAircraft.getDistanceCoeff() * DISTANCE;
            if (message instanceof LatitudeParameter) {
                ((LatitudeParameter) message).getLatitude().ifPresent(latitude -> {
                    ghostAircraft.setLatitude(latitude + stepDistance * cos(toRadians(ghostAircraft.getAngle())));
                    trajectory.addLatitude(ghostAircraft.getLatitude(), message.getTimestampGenerated());
                });
            }
            if (message instanceof LongitudeParameter) {
                ((LongitudeParameter) message).getLongitude().ifPresent(longitude -> {
                    ghostAircraft.setLongitude(longitude + stepDistance * sin(toRadians(ghostAircraft.getAngle())));
                    trajectory.addLongitude(ghostAircraft.getLongitude(), message.getTimestampGenerated());
                });
            }
            if (message instanceof AltitudeParameter) {
                ((AltitudeParameter) message).getAltitude().ifPresent(altitude ->
                        trajectory.addAltitude(altitude, message.getTimestampGenerated()));
            }
        }
    }

    @Override
    protected String applyAction(final Message message) throws UnknownScopeException {
        final BaseStationMessage initialMessage = (BaseStationMessage) message;
        final String icao = message.getIcao();
        final List<Message> messages = newArrayList(message);
        if (isMessageTargeted(message, action.getParameters().getTarget().getContent()) &&
                isMessageInScope(initialMessage, action.getScope(), recording.getFirstDate())) {
            for (final GhostAircraft ghostAircraft : ghostAircrafts.get(icao)) {
                final Message fakeMessage = createMessageFromAircraft(initialMessage, ghostAircraft);
                messages.add(fakeMessage);
            }
        }
        shuffle(messages);
        return messagesToLines(messages);
    }

    private Message createMessageFromAircraft(final BaseStationMessage initialMessage,
                                              final GhostAircraft ghostAircraft) {
        final BaseStationMessage fakeMessage = (BaseStationMessage) initialMessage.copy();
        fakeMessage.setIcao(ghostAircraft.getIcao());
        fakeMessage.setAircraftID(ghostAircraft.getAircraftId());

        final AircraftTrajectory trajectory = trajectories.get(fakeMessage.getIcao());
        final long timestamp = fakeMessage.getTimestampGenerated();
        if (fakeMessage instanceof LatitudeParameter) {
            trajectory.getLatitude(timestamp).ifPresent(((LatitudeParameter) fakeMessage)::setLatitude);
        }
        if (fakeMessage instanceof LongitudeParameter) {
            trajectory.getLongitude(timestamp).ifPresent(((LongitudeParameter) fakeMessage)::setLongitude);
        }
        if (fakeMessage instanceof AltitudeParameter) {
            trajectory.getAltitude(timestamp).ifPresent(aDouble ->
                    ((AltitudeParameter) fakeMessage).setAltitude(round(aDouble)));
        }
        if (fakeMessage instanceof TrackParameter) {
            trajectory.getTrack(timestamp).ifPresent(((TrackParameter) fakeMessage)::setTrack);
        }
        if (fakeMessage instanceof GroundSpeedParameter) {
            trajectory.getGroundSpeed(timestamp).ifPresent(((GroundSpeedParameter) fakeMessage)::setGroundSpeed);
        }
        if (fakeMessage instanceof VerticalRateParameter) {
            trajectory.getVerticalRate(timestamp).ifPresent(((VerticalRateParameter) fakeMessage)::setVerticalRate);
        }
        return fakeMessage;
    }

    public Map<String, List<GhostAircraft>> getGhostAircrafts() {
        return ghostAircrafts;
    }
}