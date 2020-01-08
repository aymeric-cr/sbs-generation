package fr.femtost.sbs.alteration.core.basestation.engine;

import fr.femtost.sbs.alteration.core.basestation.message.BaseStationMessage;
import fr.femtost.sbs.alteration.core.engine.Message;
import fr.femtost.sbs.alteration.core.incident.Action;
import fr.femtost.sbs.alteration.core.incident.Recording;
import fr.femtost.sbs.alteration.core.incident.Scope;
import fr.femtost.sbs.alteration.core.incident.Target;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Optional;

import static com.google.common.collect.Queues.newArrayDeque;
import static fr.femtost.sbs.alteration.core.engine.AlterationUtils.insertMessages;

public class BaseStationReplayEngine extends BaseStationActionEngine {


    private ArrayDeque<BaseStationMessage> messages = newArrayDeque();

    BaseStationReplayEngine(final Recording recording, final Action action) {
        super(recording, action);
    }

    @Override
    protected File processAction() throws IOException {
        final File sourceRecording = new File(action.getParameters().getRecordPath());
        final Scope scope = action.getScope();
        final Target target = action.getParameters().getTarget();
        extractMessages(sourceRecording, target, (scope.getUpperBound() - scope.getLowerBound()));
        adjustTimestamp(scope.getLowerBound());
        return insertMessages(recording.getFile(), getExtension(), messages, action);
    }

    @Override
    protected String applyAction(final Message message) {
        return "";
    }

    private void adjustTimestamp(final long scopeTimeOffset) {
        long oldTimestamp = 0;
        if (!messages.isEmpty()) {
            oldTimestamp = messages.peekFirst().getTimestampGenerated();
        }
        for (final BaseStationMessage message : messages) {
            message.setTimestampGenerated(recording.getFirstDate() + message.getTimestampGenerated() - oldTimestamp + scopeTimeOffset);
            message.setTimestampLogged(recording.getFirstDate() + message.getTimestampLogged() - oldTimestamp + scopeTimeOffset);
        }
    }

    private void extractMessages(final File recording, final Target target, final long timeInterval) throws IOException {
        try (final FileReader fileReader = new FileReader(recording);
             final BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String currentMessage = bufferedReader.readLine();
            long firstTimestamp = -1;
            while (currentMessage != null) {
                final Optional<Message> messageOptional = messagePreprocessing(currentMessage);
                if (messageOptional.isPresent() && messageOptional.get() instanceof BaseStationMessage) {
                    final BaseStationMessage message = (BaseStationMessage) messageOptional.get();
                    if (isMessageTargeted(message, target.getContent())) {
                        if (firstTimestamp == -1) {
                            firstTimestamp = message.getTimestampGenerated();
                            messages.add(message);
                        } else if (message.getTimestampGenerated() - firstTimestamp <= timeInterval) {
                            messages.add(message);
                        }
                    }
                }
                currentMessage = bufferedReader.readLine();
            }
        }
    }
}
