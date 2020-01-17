package fr.femtost.sbs.alteration.core.basestation.engine;

import fr.femtost.sbs.alteration.core.basestation.message.BaseStationMessage;
import fr.femtost.sbs.alteration.core.engine.Message;
import fr.femtost.sbs.alteration.core.scenario.Action;
import fr.femtost.sbs.alteration.core.scenario.Parameter;
import fr.femtost.sbs.alteration.core.scenario.Recording;
import fr.femtost.sbs.alteration.core.scenario.UnknownScopeException;

public class BaseStationDeletionEngine extends BaseStationActionEngine {

    private int counter;
    private int frequency = 0;

    BaseStationDeletionEngine(final Recording recording, final Action action) {
        super(recording, action);
        for (final Parameter parameter : action.getParameters().getParameterList()) {
            if (parameter.getFrequency() > 0) {
                frequency = parameter.getFrequency();
            }
        }
        counter = frequency;
    }

    @Override
    protected String applyAction(final Message message) throws UnknownScopeException {
        final BaseStationMessage baseStationMessage = (BaseStationMessage) message;
        final String targets = action.getParameters().getTarget().getContent();
        if (isMessageInScope(baseStationMessage, action.getScope(), recording.getFirstDate()) &&
                isMessageTargeted(message, targets)) {
            if (counter++ != frequency || frequency == 0) {
                return "";
            } else {
                counter = 0;
            }
        }
        return message.toString();
    }
}