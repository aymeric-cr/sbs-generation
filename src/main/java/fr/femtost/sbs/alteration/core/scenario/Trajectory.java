package fr.femtost.sbs.alteration.core.scenario;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;

public class Trajectory {

    @JacksonXmlProperty(localName = "waypoint")
    @JacksonXmlElementWrapper(useWrapping = false)
    private Collection<WayPoint> wayPoints = newArrayList();

    public Collection<WayPoint> getWayPoints() {
        return wayPoints;
    }

    public void setWayPoints(final Collection<WayPoint> wayPoints) {
        this.wayPoints = wayPoints;
    }
}