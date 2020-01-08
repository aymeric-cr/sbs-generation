package fr.femtost.sbs.alteration.api;

import fr.femtost.sbs.alteration.core.engine.ActionEngine;
import fr.femtost.sbs.alteration.core.incident.Action;
import fr.femtost.sbs.alteration.core.incident.IncidentDeserializer;
import fr.femtost.sbs.alteration.core.incident.Recording;
import fr.femtost.sbs.alteration.core.incident.Sensor;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;

import static com.google.common.io.Files.copy;
import static java.util.Date.from;
import static org.apache.commons.io.FilenameUtils.separatorsToSystem;

public class AlterationAPI {

    private AlterationAPI() {

    }

    public static void startAlteration(final File incidentFile) throws Exception {
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        for (final Sensor sensor : new IncidentDeserializer(incidentFile).deserialize().getSensors()) {
            final File recordingFile = new File(incidentFile.getParent() +
                    separatorsToSystem("/") +
                    sensor.getRecord());
            if (!recordingFile.exists() || !recordingFile.isFile()) {
                throw new FileNotFoundException(recordingFile.getAbsolutePath());
            }
            final Recording recording = new Recording(recordingFile, sensor.getFirstDate());
            final String initialName = recordingFile.getName();
            for (final Action action : sensor.getActions()) {
                final File alteredFile = ActionEngine.run(recording, action);
                recording.setFile(alteredFile);
            }
            copy(recording.getFile(), new File(
                    recordingFile.getParentFile(),
                    "modified_" + dateFormat.format(from(Instant.now())) + "_" + initialName));
        }
    }
}