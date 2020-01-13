package fr.femtost.sbs.alteration.core.engine;

import fr.femtost.sbs.alteration.core.basestation.engine.AlterationBstMessageVisitor;
import fr.femtost.sbs.alteration.core.basestation.message.BaseStationMessage;
import fr.femtost.sbs.alteration.core.incident.Action;
import fr.femtost.sbs.alteration.core.incident.Parameter;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;

import java.io.*;
import java.util.Deque;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static fr.femtost.sbs.alteration.core.basestation.BaseStationParser.createBstMessage;
import static java.io.File.createTempFile;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.toHexString;
import static java.lang.Math.*;
import static java.util.Objects.requireNonNull;

public class AlterationUtils {

    public static final Random RANDOM = new Random();
    private static final double EARTH_RADIUS_METER = 6378100.0;
    private static final double NAUTICAL_MILE_METER = 1852.0;
    private static final double MPS_TO_KNOTS = (3600.0 / NAUTICAL_MILE_METER);

    private AlterationUtils() {
    }

    public static File insertMessages(final File recording,
                                      final String extension,
                                      final Deque<BaseStationMessage> messages,
                                      final Action action) throws IOException {
        final File outputFile = createTempFile(UUID.randomUUID().toString(), extension);
        try (final FileReader fileReader = new FileReader(recording);
             final BufferedReader bufferedReader = new BufferedReader(fileReader);
             final FileWriter fileWriter = new FileWriter(outputFile);
             final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            String currentMessage = bufferedReader.readLine();
            while (currentMessage != null) {
                final Optional<Message> optionalMessage = createBstMessage(currentMessage);
                if (optionalMessage.isPresent() && optionalMessage.get() instanceof BaseStationMessage) {
                    while (!messages.isEmpty() &&
                            ((BaseStationMessage) optionalMessage.get()).getTimestampGenerated() >
                                    requireNonNull(messages.peekFirst()).getTimestampGenerated()) {
                        final Message message = messages.pollFirst();
                        alterMessage(action, message);
                        bufferedWriter.write(message + "\n");
                    }
                    bufferedWriter.write(optionalMessage.get() + "\n");
                }
                currentMessage = bufferedReader.readLine();
            }
            while (!messages.isEmpty()) {
                bufferedWriter.write(messages.pollFirst() + "\n");
            }
            return outputFile;
        }
    }

    public static void alterMessage(final Action action, final Message message) {
        for (final Parameter parameter : action.getParameters().getParameterList()) {
            new AlterationBstMessageVisitor(parameter).accept(message);
        }
    }

    public static String getIcaoRandomOffset(final String initialIcao, final int range) {
        final int offset = RANDOM.nextInt(range * 2) - range;
        return toHexString(parseInt(initialIcao, 16) + offset).toUpperCase();
    }

    public static double generateRandomAngle() {
        return 360 * RANDOM.nextDouble();
    }

    public static double generateRandomDistanceCoeff() {
        return 2.0 * RANDOM.nextDouble();
    }

    public static double computeRotation(double latitudeBefore,
                                         double longitudeBefore,
                                         double latitudeAfter,
                                         double longitudeAfter) {
        final LatLon before = LatLon.fromDegrees(latitudeBefore, longitudeBefore);
        final LatLon after = LatLon.fromDegrees(latitudeAfter, longitudeAfter);
        final Angle angle = LatLon.greatCircleAzimuth(before, after);
        if (angle.degrees < 0) {
            return angle.degrees + 360;
        }
        return angle.degrees;
    }

    public static double computeSpeed(final double latitudeBefore,
                                      final double longitudeBefore,
                                      final double latitudeAfter,
                                      final double longitudeAfter,
                                      final long time) {
        final double distance = distanceMeters(latitudeBefore, longitudeBefore, latitudeAfter, longitudeAfter);
        double speedMps = distance / (time / 1000.0);
        return speedMps * MPS_TO_KNOTS;
    }

    public static int computeVerticalRate(final double altitudeBefore,
                                          final double altitudeAfter,
                                          final double timeMillis) {
        final double timeSeconds = timeMillis / 1000;
        final double altitudeDiff = altitudeAfter - altitudeBefore;
        return (int) ((altitudeDiff / timeSeconds) * 60);
    }

    public static double distanceMeters(double latitudeFrom,
                                        double longitudeFrom,
                                        double latitudeTo,
                                        double longitudeTo) {
        latitudeFrom = toRadians(latitudeFrom);
        longitudeFrom = toRadians(longitudeFrom);
        latitudeTo = toRadians(latitudeTo);
        longitudeTo = toRadians(longitudeTo);

        // P
        double rho1 = EARTH_RADIUS_METER * cos(latitudeFrom);
        double z1 = EARTH_RADIUS_METER * sin(latitudeFrom);
        double x1 = rho1 * cos(longitudeFrom);
        double y1 = rho1 * sin(longitudeFrom);

        // Q
        double rho2 = EARTH_RADIUS_METER * cos(latitudeTo);
        double z2 = EARTH_RADIUS_METER * sin(latitudeTo);
        double x2 = rho2 * cos(longitudeTo);
        double y2 = rho2 * sin(longitudeTo);

        // Dot product
        double dot = (x1 * x2 + y1 * y2 + z1 * z2);
        double cosTheta = dot / (EARTH_RADIUS_METER * EARTH_RADIUS_METER);

        double theta = acos(cosTheta);

        // Distance in Metres
        return EARTH_RADIUS_METER * theta;
    }

    public static double computeNewValue(final double actualValue, double newValue, String mode) {
        return computeNewValue(actualValue, newValue, mode, 1);
    }

    public static double computeNewValue(final double actualValue, double newValue, String mode, int step) {
        return new Parameter.ModeSwitch<Double>() {

            @Override
            public Double visitNoise() {
                double randomValue = newValue * RANDOM.nextDouble();
                return actualValue * randomValue;
            }

            @Override
            public Double visitDrift() {
                return actualValue + newValue * step;
            }

            @Override
            public Double visitOffset() {
                return newValue + actualValue;
            }

            @Override
            public Double visitSimple() {
                return newValue;
            }
        }.doSwitch(mode);
    }
}