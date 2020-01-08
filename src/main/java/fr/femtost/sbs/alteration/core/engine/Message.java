package fr.femtost.sbs.alteration.core.engine;

public interface Message {

    String getIcao();

    void setIcao(final String icao);

    Message copy();
}