package fr.femtost.sbs.alteration.core.scenario;

import java.io.File;
import java.util.function.Predicate;

import static fr.femtost.sbs.alteration.core.scenario.Action.*;
import static fr.femtost.sbs.alteration.core.scenario.Parameter.CHARAC_ICAO;
import static java.util.Arrays.asList;
import static testools.predicate.CollectionPredicate.containsOnly;
import static testools.predicate.PredicateUtils.and;

public class ScenarioHelper {

    public static Parameter parameter(final String characteristic,
                                      final String value) {
        return parameter(characteristic, value, "simple", 0, 1);
    }

    public static Parameter parameter(final String characteristic,
                                      final String value,
                                      final String mode) {
        final Parameter parameter = new Parameter();
        parameter.setCharacteristic(characteristic);
        parameter.setMode(mode);
        parameter.setNumber(0);
        parameter.setValue(value);
        return parameter;
    }

    public static Parameter saturationParameter(final String icao,
                                                final int number) {
        return parameter(CHARAC_ICAO, icao, "simple", number, 1);
    }

    public static Parameter deletionParameter(final int frequency) {
        return parameter("", "", "simple", 0, frequency);
    }

    public static Parameter parameter(final String characteristic,
                                      final String value,
                                      final String mode,
                                      final int number,
                                      final int frequency) {
        final Parameter parameter = new Parameter();
        parameter.setCharacteristic(characteristic);
        parameter.setMode(mode);
        parameter.setNumber(number);
        parameter.setFrequency(frequency);
        parameter.setValue(value);
        return parameter;
    }

    public static Target bstTarget(final String targets) {
        return target("hexIdent", targets);
    }

    private static Target target(final String identifier,
                                 final String targets) {
        final Target target = new Target();
        target.setIdentifier(identifier);
        target.setContent(targets);
        return target;
    }

    public static Parameters parameters(final Trajectory trajectory,
                                        final Parameter... parameters) {
        final Parameters params = new Parameters();
        params.setTrajectory(trajectory);
        params.setParameterList(asList(parameters));
        return params;
    }


    public static Parameters parameters(final Target target, final Parameter... parameters) {
        final Parameters params = new Parameters();
        params.setTarget(target);
        params.setParameterList(asList(parameters));
        return params;
    }

    public static Parameters parameters(final Target target, final Trajectory trajectory) {
        final Parameters params = new Parameters();
        params.setTarget(target);
        params.setTrajectory(trajectory);
        return params;
    }

    public static Trajectory trajectory(final WayPoint... wayPoints) {
        final Trajectory trajectory = new Trajectory();
        trajectory.setWayPoints(asList(wayPoints));
        return trajectory;
    }

    public static WayPoint wayPoint(final long time,
                                    final double latitude,
                                    final double longitude,
                                    final int altitude) {
        final Vertex vertex = new Vertex();
        vertex.setLat(latitude);
        vertex.setLon(longitude);
        final WayPoint wayPoint = new WayPoint();
        wayPoint.setVertex(vertex);
        wayPoint.setAltitude(altitude);
        wayPoint.setTime(time);
        return wayPoint;
    }

    public static Parameters parameters(final Target target, final String recordPath, final Parameter... parameters) {
        final Parameters params = new Parameters();
        params.setTarget(target);
        params.setRecordPath(recordPath);
        params.setParameterList(asList(parameters));
        return params;
    }

    public static Action action(final String type, final Scope scope, final Parameters parameters) {
        final Action action = new Action();
        action.setActionType(type);
        action.setScope(scope);
        action.setParameters(parameters);
        return action;
    }

    public static Action alteration(final Scope scope, final Parameters parameters) {
        return action(ACTION_TYPE_ALTERATION, scope, parameters);
    }

    public static Action creation(final Scope scope, final Parameters parameters) {
        return action(ACTION_TYPE_CREATION, scope, parameters);
    }

    public static Action saturation(final Scope scope, final Parameters parameters) {
        return action(ACTION_TYPE_SATURATION, scope, parameters);
    }


    public static Action replay(final Scope scope, final Parameters parameters) {
        return action(ACTION_TYPE_REPLAY, scope, parameters);
    }

    public static Action trajectoryModification(final Scope scope, final Parameters parameters) {
        return action(ACTION_TYPE_TRAJECTORY_MODIFICATION, scope, parameters);
    }

    public static Action delay(final Scope scope, final Parameters parameters) {
        return action(ACTION_TYPE_DELAY, scope, parameters);
    }

    public static Action deletion(final Scope scope, final Parameters parameters) {
        return action(ACTION_TYPE_DELETION, scope, parameters);
    }

    public static Recording recording(final File file) {
        return new Recording(file);
    }

    public static Recording recording(final File file, final long epochMilli) {
        return new Recording(file, epochMilli);
    }

    public static Scope timeWindow(final long lowerBound,
                                   final long upperBound) {
        return scope("timeWindow", 0, 0, lowerBound, upperBound, "", "", "", "", null);
    }

    private static Scope scope(final String type,
                               final int lowerAlt,
                               final int upperAlt,
                               final long lowerBound,
                               final long upperBound,
                               final String threshold,
                               final String thresholdType,
                               final String boundType,
                               final String time,
                               final Polygon polygon) {
        final Scope scope = new Scope();
        scope.setType(type);
        scope.setLowerAlt(lowerAlt);
        scope.setUpperAlt(upperAlt);
        scope.setLowerBound(lowerBound);
        scope.setUpperBound(upperBound);
        scope.setThreshold(threshold);
        scope.setThresholdType(thresholdType);
        scope.setBoundType(boundType);
        scope.setTime(time);
        scope.setPolygon(polygon);
        return scope;
    }

    public static Predicate<Scenario> aScenario(final String record,
                                                final String filter,
                                                final Predicate<Scenario> predicate) {
        return scenario -> {
            if (scenario.getRecord().compareTo(record) != 0) {
                System.err.println("Record path - Expected: " + record + ". Got: " + scenario.getRecord());
                return false;
            }
            if (scenario.getFilter().compareTo(filter) != 0) {
                System.err.println("Filter - Expected: " + filter + ". Got: " + scenario.getFilter());
                return false;
            }
            return predicate.test(scenario);
        };
    }

    @SafeVarargs
    public static Predicate<Scenario> withActions(final Predicate<Action>... actions) {
        return scenario -> {
            if (actions.length != scenario.getActions().size()) {
                System.err.println("Number of actions - Expected: " + actions.length +
                        ". Got: " + scenario.getActions().size());
                return false;
            }
            return containsOnly(actions).test(scenario.getActions());
        };
    }

    static Predicate<Action> anAction(final String type,
                                      final Predicate<Scope> scope,
                                      final Predicate<Parameters> parameters) {
        return action -> {
            if (type.compareTo(action.getActionType()) != 0) {
                System.err.println("Alteration type - Expected: " + type + ". Got: " + action.getActionType());
                return false;
            }
            return scope.test(action.getScope()) && parameters.test(action.getParameters());
        };
    }

    private static Predicate<Action> anAlteration(final Predicate<Scope> scope,
                                                  final Predicate<Parameters> parameters) {
        return anAction("ALTERATION", scope, parameters);
    }

    private static Predicate<Action> aDeletion(final Predicate<Scope> scope,
                                               final Predicate<Parameters> parameters) {
        return anAction("DELETION", scope, parameters);
    }

    private static Predicate<Action> aReplay(final Predicate<Scope> scope,
                                             final Predicate<Parameters> parameters) {
        return anAction("REPLAY", scope, parameters);
    }

    private static Predicate<Scope> withScope(final String type,
                                              final int lowerAlt,
                                              final int upperAlt,
                                              final long lowerBound,
                                              final long upperBound,
                                              final String threshold,
                                              final String thresholdType,
                                              final String boundType,
                                              final String time) {
        return scope -> {
            if (scope.getType().compareTo(type) != 0) {
                System.err.println("Scope type - Expected: " + type + ". Got: " + scope.getType());
                return false;
            }
            if (scope.getLowerAlt() != lowerAlt) {
                System.err.println("Lower alt. - Expected: " + lowerAlt + ". Got: " + scope.getLowerAlt());
                return false;
            }
            if (scope.getUpperAlt() != upperAlt) {
                System.err.println("Upper alt. - Expected: " + upperAlt + ". Got: " + scope.getUpperAlt());
                return false;
            }
            if (scope.getLowerBound() != lowerBound) {
                System.err.println("Lower bound - Expected: " + lowerBound + ". Got: " + scope.getLowerBound());
                return false;
            }
            if (scope.getUpperBound() != upperBound) {
                System.err.println("Upper bound - Expected: " + upperBound + ". Got: " + scope.getUpperBound());
                return false;
            }
            if (scope.getThreshold().compareTo(threshold) != 0) {
                System.err.println("Threshold - Expected: " + threshold + ". Got: " + scope.getThreshold());
                return false;
            }
            if (scope.getThresholdType().compareTo(thresholdType) != 0) {
                System.err.println("Threshold type - Expected: " + thresholdType + ". Got: " + scope.getThresholdType());
                return false;
            }
            if (scope.getBoundType().compareTo(boundType) != 0) {
                System.err.println("Bound type - Expected: " + boundType + ". Got: " + scope.getBoundType());
                return false;
            }
            if (scope.getTime().compareTo(time) != 0) {
                System.err.println("Scope time - Expected: " + time + ". Got: " + scope.getTime());
                return false;
            }
            return true;
        };
    }

    public static Predicate<Scope> withScope(final String type,
                                             final int lowerAlt,
                                             final int upperAlt,
                                             final long lowerBound,
                                             final long upperBound,
                                             final String threshold,
                                             final String thresholdType,
                                             final String boundType,
                                             final String time,
                                             final Predicate<Polygon> polygon) {
        return and(
                withScope(type, lowerAlt, upperAlt, lowerBound, upperBound, threshold, thresholdType, boundType, time),
                scope -> polygon.test(scope.getPolygon()));
    }

    public static Predicate<Scope> withScopeTimeWindow(final long lowerBound,
                                                       final long upperBound) {
        return withScope("timeWindow", 0, 0, lowerBound, upperBound, "", "", "", "");
    }

    public static Predicate<Scope> withScopeGeoTimeWindow(final long lowerBound,
                                                          final long upperBound,
                                                          final Predicate<Polygon> polygonPredicate) {
        return withScope("geoTimeWindow", 0, 0, lowerBound, upperBound, "", "", "", "", polygonPredicate);
    }

    public static Predicate<Polygon> withPolygon(final String id,
                                                 final String name,
                                                 final int lowerAlt,
                                                 final int upperAlt,
                                                 final Predicate<Polygon> predicate) {
        return polygon -> {
            if (polygon.getId().compareTo(id) != 0) {
                System.err.println("Polygon ID - Expected: " + id + ". Got: " + polygon.getId());
                return false;
            }
            if (polygon.getName().compareTo(name) != 0) {
                System.err.println("Polygon name - Expected: " + name + ". Got: " + polygon.getName());
                return false;
            }
            if (polygon.getLowerAlt() != lowerAlt) {
                System.err.println("Lower alt. - Expected: " + lowerAlt + ". Got: " + polygon.getLowerAlt());
                return false;
            }
            if (polygon.getUpperAlt() != upperAlt) {
                System.err.println("Upper alt. - Expected: " + upperAlt + ". Got: " + polygon.getUpperAlt());
                return false;
            }
            return predicate.test(polygon);
        };
    }

    @SafeVarargs
    public static Predicate<Polygon> withVertices(final Predicate<Vertex>... vertex) {
        return polygon -> {
            if (vertex.length != polygon.getVertices().size()) {
                System.err.println("Number of vertex - Expected: " + vertex.length +
                        ". Got: " + polygon.getVertices().size());
                return false;
            }
            return containsOnly(vertex).test(polygon.getVertices());
        };
    }

    public static Predicate<Vertex> aVertex(final double lat,
                                            final double lon) {
        return vertex -> {
            if (vertex.getLat() != lat) {
                System.err.println("Latitude - Expected: " + lat + ". Got: " + vertex.getLat());
                return false;
            }
            if (vertex.getLon() != lon) {
                System.err.println("Longitude - Expected: " + lon + ". Got: " + vertex.getLon());
                return false;
            }
            return true;
        };
    }

    @SafeVarargs
    public static Predicate<Parameters> withParameters(final Predicate<Target> target,
                                                       final Predicate<Parameter>... parameters) {
        return parameters1 ->
                target.test(parameters1.getTarget()) && containsOnly(parameters).test(parameters1.getParameterList());
    }

    public static Predicate<Parameters> withTrajectoryParameters(final Predicate<Target> target,
                                                                 final Predicate<Trajectory> trajectory) {
        return parameters1 ->
                target.test(parameters1.getTarget()) && trajectory.test(parameters1.getTrajectory());
    }

    @SafeVarargs
    public static Predicate<Parameters> withReplayParameters(final Predicate<Target> target,
                                                             final String recordPath,
                                                             final Predicate<Parameter>... parameters) {
        return and(
                withParameters(target, parameters),
                parameters1 -> {
                    if (parameters1.getRecordPath().compareTo(recordPath) != 0) {
                        System.err.println("Record path - Expected: " + recordPath + ". Got: " + parameters1.getRecordPath());
                        return false;
                    }
                    return true;
                });
    }

    public static Predicate<Parameter> aParameter(final String mode,
                                                  final String key,
                                                  final String value) {
        return parameter -> {
            if (parameter.getMode().compareToIgnoreCase(mode) != 0) {
                System.err.println("Parameter mode - Expected: " + mode + ". Got: " + parameter.getMode());
                return false;
            }
            if (parameter.getCharacteristic().compareTo(key) != 0) {
                System.err.println("Parameter key - Expected: " + key + ". Got: " + parameter.getCharacteristic());
                return false;
            }
            if (parameter.getValue().compareTo(value) != 0) {
                System.err.println("Parameter value - Expected: " + value + ". Got: " + parameter.getValue());
                return false;
            }
            return true;
        };
    }

    public static Predicate<Parameter> aParameter(final String mode,
                                                  final String key,
                                                  final String value,
                                                  final int number) {
        return and(
                aParameter(mode, key, value),
                parameter -> {
                    if (parameter.getNumber() != number) {
                        System.err.println("Parameter 'number' - Expected: " + number + ". Got: " + parameter.getNumber());
                        return false;
                    }
                    return true;
                });
    }

    @SafeVarargs
    public static Predicate<Trajectory> aTrajectory(final Predicate<WayPoint>... wayPoints) {
        return trajectory -> containsOnly(wayPoints).test(trajectory.getWayPoints());
    }

    public static Predicate<WayPoint> withWayPoint(final double latitude,
                                                   final double longitude,
                                                   final int altitude,
                                                   final long time) {
        return wayPoint -> {
            if (wayPoint.getVertex().getLat() != latitude) {
                System.err.println("Way point latitude - Expected: " + latitude + ". Got: " + wayPoint.getVertex().getLat());
                return false;
            }
            if (wayPoint.getVertex().getLon() != longitude) {
                System.err.println("Way point longitude - Expected: " + longitude + ". Got: " + wayPoint.getVertex().getLon());
                return false;
            }
            if (wayPoint.getAltitude() != altitude) {
                System.err.println("Way point altitude - Expected: " + altitude + ". Got: " + wayPoint.getAltitude());
                return false;
            }
            if (wayPoint.getTime() != time) {
                System.err.println("Way point time - Expected: " + time + ". Got: " + wayPoint.getTime());
                return false;
            }
            return true;
        };
    }

    private static Predicate<Target> onTarget(final String identifier,
                                              final String targets) {
        return target -> {
            if (target.getIdentifier().compareTo(identifier) != 0) {
                System.err.println("Target identifier - Expected: " + identifier + ". Got: " + target.getIdentifier());
                return false;
            }
            if (target.getContent().compareTo(targets) != 0) {
                System.err.println("Targets - Expected: " + targets + ". Got: " + target.getContent());
                return false;
            }
            return true;
        };
    }

    public static Predicate<Target> onTargetHexIdent(final String targets) {
        return onTarget("hexIdent", targets);
    }
}