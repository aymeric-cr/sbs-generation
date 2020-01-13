package fr.femtost.sbs.alteration.application;

import fr.femtost.sbs.alteration.api.AlterationAPI;

import java.io.File;

class Main {

    public static void main(final String[] args) throws Exception {
        if (args.length > 0) {
            alterRecording(args[0]);
        }
    }

    private static void alterRecording(final String scenarioFilePath) throws Exception {
        final File scenarioFile = new File(scenarioFilePath);
        if (scenarioFile.exists() && scenarioFile.isFile()) {
            AlterationAPI.startAlteration(new File(scenarioFilePath));
        }
    }
}