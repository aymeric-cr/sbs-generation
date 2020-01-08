package fr.femtost.sbs.alteration.core.incident;

import java.util.Collection;

public class Polygon {

    private String id;

    private String name;

    private int lowerAlt;

    private int upperAlt;

    private Vertices vertices;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getLowerAlt() {
        return lowerAlt;
    }

    public void setLowerAlt(final int lowerAlt) {
        this.lowerAlt = lowerAlt;
    }

    public int getUpperAlt() {
        return upperAlt;
    }

    public void setUpperAlt(final int upperAlt) {
        this.upperAlt = upperAlt;
    }

    public Collection<Vertex> getVertices() {
        return vertices.getVertexList();
    }

    public void setVertices(final Vertices vertices) {
        this.vertices = vertices;
    }
}