package fr.femtost.sbs.alteration.core.basestation.engine;

import fr.femtost.sbs.alteration.core.basestation.message.BaseStationMessage;
import fr.femtost.sbs.alteration.core.engine.Message;
import fr.femtost.sbs.alteration.core.scenario.Action;
import fr.femtost.sbs.alteration.core.scenario.Parameter;
import fr.femtost.sbs.alteration.core.scenario.Recording;
import fr.femtost.sbs.alteration.core.scenario.UnknownScopeException;

import static fr.femtost.sbs.alteration.core.scenario.Parameter.CHARAC_TIMESTAMP;
import static java.lang.Long.parseLong;

public class BaseStationDelayEngine extends BaseStationActionEngine {

    BaseStationDelayEngine(final Recording recording, final Action action) {
        super(recording, action);
    }

    @Override
    protected String applyAction(final Message message) throws UnknownScopeException {
        final BaseStationMessage bstMessage = (BaseStationMessage) message;
        final String targets = action.getParameters().getTarget().getContent();
        for (final Parameter parameter : action.getParameters().getParameterList()) {
            if (parameter.getCharacteristic().compareTo(CHARAC_TIMESTAMP) == 0 &&
                    isMessageInScope(bstMessage, action.getScope(), recording.getFirstDate()) &&
                    isMessageTargeted(message, targets)) {
                bstMessage.setTimestampGenerated(bstMessage.getTimestampGenerated() + parseLong(parameter.getValue()));
                bstMessage.setTimestampLogged(bstMessage.getTimestampLogged() + parseLong(parameter.getValue()));
            }
        }
        return bstMessage.toString();
    }
}