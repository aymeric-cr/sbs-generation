package fr.femtost.sbs.alteration.core.basestation.engine;

import fr.femtost.sbs.alteration.core.basestation.message.BaseStationMessage;
import fr.femtost.sbs.alteration.core.basestation.message.parameter.*;
import fr.femtost.sbs.alteration.core.engine.Message;
import fr.femtost.sbs.alteration.core.incident.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.lang.Math.round;

public class BaseStationTrajectoryModificationEngine extends BaseStationActionEngine {

    private final Map<String, AircraftTrajectory> trajectories = newHashMap();
    private final Map<String, Long> firstAlteredMessage = newHashMap();
    private final Collection<WayPoint> newTrajectory = newArrayList();

    BaseStationTrajectoryModificationEngine(final Recording recording, final Action action) {
        super(recording, action);
        newTrajectory.addAll(action.getParameters().getTrajectory().getWayPoints());
    }

    @Override
    protected File processAction() throws IOException, UnknownScopeException, UnknownCharacteristicException {
        buildNewTrajectories();
        return super.processAction();
    }

    @Override
    protected String applyAction(final Message message) throws UnknownScopeException {
        final BaseStationMessage baseStationMessage = (BaseStationMessage) message;
        final String targets = action.getParameters().getTarget().getContent();
        if (isMessageInScope(baseStationMessage, action.getScope(), recording.getFirstDate()) &&
                isMessageTargeted(message, targets)) {
            processAlteration(baseStationMessage);
        }
        return message.toString();
    }

    private void processAlteration(final BaseStationMessage message) {
        final AircraftTrajectory trajectory = trajectories.get(message.getIcao());
        final long timestamp = message.getTimestampGenerated();
        if (message instanceof LatitudeParameter) {
            trajectory.getLatitude(timestamp).ifPresent(((LatitudeParameter) message)::setLatitude);
        }
        if (message instanceof LongitudeParameter) {
            trajectory.getLongitude(timestamp).ifPresent(((LongitudeParameter) message)::setLongitude);
        }
        if (message instanceof AltitudeParameter) {
            trajectory.getAltitude(timestamp).ifPresent(aDouble ->
                    ((AltitudeParameter) message).setAltitude(round(aDouble)));
        }
        if (message instanceof TrackParameter) {
            trajectory.getTrack(timestamp).ifPresent(((TrackParameter) message)::setTrack);
        }
        if (message instanceof GroundSpeedParameter) {
            trajectory.getGroundSpeed(timestamp).ifPresent(((GroundSpeedParameter) message)::setGroundSpeed);
        }
        if (message instanceof VerticalRateParameter) {
            trajectory.getVerticalRate(timestamp).ifPresent(((VerticalRateParameter) message)::setVerticalRate);
        }
    }

    private void buildNewTrajectories() throws IOException, UnknownScopeException {
        try (final FileReader fileReader = new FileReader(recording.getFile());
             final BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String currentMessage = bufferedReader.readLine();
            while (currentMessage != null) {
                final Optional<? extends Message> message = messagePreprocessing(currentMessage);
                if (message.isPresent() && message.get() instanceof BaseStationMessage) {
                    updateTrajectory(trajectories, (BaseStationMessage) message.get());
                }
                currentMessage = bufferedReader.readLine();
            }
        }
        trajectories.values().forEach(AircraftTrajectory::interpolate);
    }

    private void updateTrajectory(final Map<String, AircraftTrajectory> trajectories,
                                  final BaseStationMessage message) throws UnknownScopeException {
        if (isMessageTargeted(message, action.getParameters().getTarget().getContent())) {
            trajectories.putIfAbsent(message.getIcao(), new AircraftTrajectory());
            final AircraftTrajectory trajectory = trajectories.get(message.getIcao());
            if (!isMessageInScope(message, action.getScope(), recording.getFirstDate())) {
                addMessageToTrajectory(message, trajectory);
            } else {
                if (!firstAlteredMessage.containsKey(message.getIcao())) {
                    addMessageToTrajectory(message, trajectory);
                    final long timestamp = message.getTimestampGenerated();
                    firstAlteredMessage.put(message.getIcao(), timestamp);
                    for (final WayPoint wayPoint : newTrajectory) {
                        trajectory.addAltitude(wayPoint.getAltitude(), wayPoint.getTime() + (double) timestamp);
                        trajectory.addLatitude(wayPoint.getVertex().getLat(), wayPoint.getTime() + (double) timestamp);
                        trajectory.addLongitude(wayPoint.getVertex().getLon(), wayPoint.getTime() + (double) timestamp);
                    }
                }
            }
        }
    }

    private void addMessageToTrajectory(final BaseStationMessage message,
                                        final AircraftTrajectory trajectory) {
        if (message instanceof LatitudeParameter && ((LatitudeParameter) message).getLatitude().isPresent()) {
            trajectory.addLatitude(
                    ((LatitudeParameter) message).getLatitude().get(),
                    message.getTimestampGenerated());
        }
        if (message instanceof LongitudeParameter && ((LongitudeParameter) message).getLongitude().isPresent()) {
            trajectory.addLongitude(
                    ((LongitudeParameter) message).getLongitude().get(),
                    message.getTimestampGenerated());
        }
        if (message instanceof AltitudeParameter && ((AltitudeParameter) message).getAltitude().isPresent()) {
            trajectory.addAltitude(
                    ((AltitudeParameter) message).getAltitude().get(),
                    message.getTimestampGenerated());
        }
    }

    public Map<String, AircraftTrajectory> getTrajectories() {
        return trajectories;
    }
}