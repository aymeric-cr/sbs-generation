package fr.femtost.sbs.alteration.core.incident;

public class WayPoint {

    private Vertex vertex;

    private int altitude;

    private long time;

    public Vertex getVertex() {
        return vertex;
    }

    public void setVertex(final Vertex vertex) {
        this.vertex = vertex;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(final int altitude) {
        this.altitude = altitude;
    }

    public long getTime() {
        return time;
    }

    public void setTime(final long time) {
        this.time = time;
    }
}