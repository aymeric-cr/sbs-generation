package fr.femtost.sbs.alteration.core.basestation.engine;

import fr.femtost.sbs.alteration.core.basestation.message.BaseStationMessage;
import fr.femtost.sbs.alteration.core.basestation.message.BaseStationMessageFull;
import fr.femtost.sbs.alteration.core.engine.Message;
import fr.femtost.sbs.alteration.core.incident.Action;
import fr.femtost.sbs.alteration.core.incident.Recording;
import fr.femtost.sbs.alteration.core.incident.WayPoint;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Queues.newArrayDeque;
import static fr.femtost.sbs.alteration.core.engine.AlterationUtils.*;

public class BaseStationCreationEngine extends BaseStationActionEngine {

    private final AircraftTrajectory trajectory;
    private final ArrayDeque<BaseStationMessage> messages = newArrayDeque();

    BaseStationCreationEngine(final Recording recording, final Action action) {
        super(recording, action);
        Collection<WayPoint> wayPoints = newArrayList();
        wayPoints.addAll(action.getParameters().getTrajectory().getWayPoints());
        trajectory = createTrajectory(action, wayPoints);
    }

    private static BaseStationMessage createMessageFromTrajectory(final long timestamp,
                                                                  final int sessionID,
                                                                  final int aircraftID,
                                                                  final int flightID,
                                                                  final AircraftTrajectory trajectory) {
        return new BaseStationMessageFull(
                3,
                sessionID,
                aircraftID,
                "",
                flightID,
                timestamp,
                timestamp,
                null,
                trajectory.getAltitude(timestamp).orElse(null),
                trajectory.getGroundSpeed(timestamp).orElse(null),
                trajectory.getTrack(timestamp).orElse(null),
                trajectory.getLatitude(timestamp).orElse(null),
                trajectory.getLongitude(timestamp).orElse(null),
                trajectory.getVerticalRate(timestamp).orElse(null),
                null,
                null,
                null,
                null,
                null);
    }

    private static AircraftTrajectory createTrajectory(final Action action,
                                                       final Collection<WayPoint> wayPoints) {
        final AircraftTrajectory trajectory = new AircraftTrajectory();
        final long lowerBound = action.getScope().getLowerBound();
        for (final WayPoint wayPoint : wayPoints) {
            trajectory.addAltitude(wayPoint.getAltitude(), (double) wayPoint.getTime() + lowerBound);
            trajectory.addLatitude(wayPoint.getVertex().getLat(), (double) wayPoint.getTime() + lowerBound);
            trajectory.addLongitude(wayPoint.getVertex().getLon(), (double) wayPoint.getTime() + lowerBound);
        }
        trajectory.interpolate();
        return trajectory;
    }

    @Override
    protected File processAction() throws IOException {
        generateMessages();
        return insertMessages(recording.getFile(), getExtension(), messages, action);
    }

    @Override
    protected String applyAction(final Message message) {
        return "";
    }

    protected int getTimeOffset() {
        return RANDOM.nextInt(200) + 400;
    }

    private void generateMessages() {
        long timestamp = action.getScope().getLowerBound();
        final int sessionID = RANDOM.nextInt(1000);
        final int aircraftID = RANDOM.nextInt(1000);
        final int flightID = RANDOM.nextInt(1000);
        while (timestamp < action.getScope().getUpperBound()) {
            final BaseStationMessage newMessage = createMessageFromTrajectory(
                    timestamp,
                    sessionID,
                    aircraftID,
                    flightID,
                    trajectory);
            alterMessage(action, newMessage);
            messages.add(newMessage);
            timestamp += getTimeOffset();
        }
    }

    public Deque<BaseStationMessage> getMessages() {
        return messages;
    }
}