package fr.femtost.sbs.alteration.core.basestation.message.parameter;

import java.util.Optional;

public interface EmergencyParameter {

    Optional<Boolean> isEmergency();

    void setEmergency(final boolean emergency);
}