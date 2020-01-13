package fr.femtost.sbs.alteration.core.scenario;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Action {

    public static final String ACTION_TYPE_ALTERATION = "ALTERATION";
    public static final String ACTION_TYPE_CREATION = "CREATION";
    public static final String ACTION_TYPE_DELAY = "DELAY";
    public static final String ACTION_TYPE_DELETION = "DELETION";
    public static final String ACTION_TYPE_SATURATION = "SATURATION";
    public static final String ACTION_TYPE_REPLAY = "REPLAY";
    public static final String ACTION_TYPE_TRAJECTORY_MODIFICATION = "TRAJECTORY";

    @JacksonXmlProperty(isAttribute = true, localName = "alterationType")
    private String actionType;

    private Scope scope;

    private Parameters parameters;

    public String getActionType() {
        return actionType;
    }

    public void setActionType(final String actionType) {
        this.actionType = actionType;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(final Scope scope) {
        this.scope = scope;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(final Parameters parameters) {
        this.parameters = parameters;
    }

    public interface ActionTypeSwitch<T> {

        T visitAlteration();

        T visitDeletion();

        T visitSaturation();

        T visitDelay();

        T visitReplay();

        T visitTrajectoryModification();

        default T doSwitch(final String type) throws UnknownActionException {
            if (type.compareTo(ACTION_TYPE_ALTERATION) == 0) {
                return visitAlteration();
            }
            if (type.compareTo(ACTION_TYPE_DELAY) == 0) {
                return visitDelay();
            }
            if (type.compareTo(ACTION_TYPE_DELETION) == 0) {
                return visitDeletion();
            }
            if (type.compareTo(ACTION_TYPE_SATURATION) == 0) {
                return visitSaturation();
            }
            if (type.compareTo(ACTION_TYPE_REPLAY) == 0) {
                return visitReplay();
            }
            if (type.compareTo(ACTION_TYPE_TRAJECTORY_MODIFICATION) == 0) {
                return visitTrajectoryModification();
            }
            throw new UnknownActionException(type);
        }
    }
}