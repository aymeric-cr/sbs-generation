package fr.femtost.sbs.alteration.core.basestation.message.parameter;

import java.util.Optional;

public interface AltitudeParameter {

    Optional<Integer> getAltitude();

    void setAltitude(final int altitude);
}