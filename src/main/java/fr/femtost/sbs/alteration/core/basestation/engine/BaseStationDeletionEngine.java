package fr.femtost.sbs.alteration.core.basestation.engine;

import fr.femtost.sbs.alteration.core.basestation.message.BaseStationMessage;
import fr.femtost.sbs.alteration.core.engine.Message;
import fr.femtost.sbs.alteration.core.incident.Action;
import fr.femtost.sbs.alteration.core.incident.Recording;
import fr.femtost.sbs.alteration.core.incident.UnknownScopeException;

public class BaseStationDeletionEngine extends BaseStationActionEngine {


    BaseStationDeletionEngine(final Recording recording, final Action action) {
        super(recording, action);
    }

    @Override
    protected String applyAction(final Message message) throws UnknownScopeException {
        final BaseStationMessage baseStationMessage = (BaseStationMessage) message;
        final String targets = action.getParameters().getTarget().getContent();
        if (isMessageInScope(baseStationMessage, action.getScope(), recording.getFirstDate()) &&
                isMessageTargeted(message, targets)) {
            return "";
        }
        return baseStationMessage.toString();
    }
}