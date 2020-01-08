package fr.femtost.sbs.alteration.core.engine;

import fr.femtost.sbs.alteration.core.basestation.engine.BaseStationActionEngine;
import fr.femtost.sbs.alteration.core.incident.*;

import java.io.*;
import java.util.Optional;
import java.util.UUID;

import static com.google.common.io.Files.getFileExtension;
import static fr.femtost.sbs.alteration.core.incident.Target.TARGET_ALL;
import static java.io.File.createTempFile;
import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;

public abstract class ActionEngine {

    private static final String SBS_EXTENSION = "sbs";
    private static final String BST_EXTENSION = "bst";

    protected final Recording recording;
    protected final Action action;

    protected ActionEngine(final Recording recording, final Action action) {
        this.recording = recording;
        this.action = action;
    }

    public static File run(final Recording recording, final Action action) throws
            UnrecognizedRecordingException,
            UnknownActionException,
            UnknownScopeException,
            UnknownCharacteristicException,
            IOException {
        final String recordingExtension = getFileExtension(recording.getFile().getName()).toLowerCase().trim();
        if (recordingExtension.compareTo(BST_EXTENSION) == 0 ||
                recordingExtension.compareTo(SBS_EXTENSION) == 0) {
            return BaseStationActionEngine.run(recording, action);
        }
        throw new UnrecognizedRecordingException(recording.getFile().getName());
    }

    protected static boolean isMessageTargeted(final Message message, final String targetsStr) {
        return targetsStr.compareToIgnoreCase(TARGET_ALL) == 0 ||
                asList(targetsStr.toLowerCase().split(",")).contains(message.getIcao().toLowerCase());
    }

    protected File processAction() throws IOException, UnknownScopeException, UnknownCharacteristicException {
        final File recordingFile = recording.getFile();
        final File outputFile = createTempFile(UUID.randomUUID().toString(), getExtension());
        try (final FileReader fileReader = new FileReader(recordingFile);
             final BufferedReader bufferedReader = new BufferedReader(fileReader);
             final FileWriter fileWriter = new FileWriter(outputFile);
             final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            String currentMessage = bufferedReader.readLine();
            while (currentMessage != null) {
                final Optional<String> alteredMessage = handleMessage(currentMessage);
                if (alteredMessage.isPresent()) {
                    bufferedWriter.write(alteredMessage.get() + "\n");
                } else {
                    bufferedWriter.write(currentMessage + "\n");
                }
                currentMessage = bufferedReader.readLine();
            }
        }
        return outputFile;
    }

    protected abstract String getExtension();

    protected Optional<String> handleMessage(final String message) throws UnknownScopeException, UnknownCharacteristicException {
        final Optional<? extends Message> processedMessage = messagePreprocessing(message);
        if (processedMessage.isPresent()) {
            final String newMessage = applyAction(processedMessage.get());
            if (!newMessage.isEmpty()) {
                return of(newMessage);
            }
        }
        return empty();
    }

    protected abstract Optional<Message> messagePreprocessing(final String message);

    protected abstract String applyAction(final Message message) throws UnknownScopeException, UnknownCharacteristicException;
}