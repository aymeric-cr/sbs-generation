package fr.femtost.sbs.alteration.core.scenario;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import fr.femtost.sbs.alteration.core.utils.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ScenarioDeserializer {

    private final File scenarioFile;

    public ScenarioDeserializer(final File scenarioFile) {
        this.scenarioFile = scenarioFile;
    }

    public Scenario deserialize() throws IOException {
        final XmlMapper mapper = new XmlMapper();
        final String content = StreamUtils.inputStreamToString(new FileInputStream(scenarioFile));
        return mapper.readValue(content, Scenario.class);
    }
}