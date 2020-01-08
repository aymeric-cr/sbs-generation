package fr.femtost.sbs.alteration.core.basestation.message.parameter;

import java.util.Optional;

public interface LatitudeParameter {

    Optional<Double> getLatitude();

    void setLatitude(final double latitude);
}