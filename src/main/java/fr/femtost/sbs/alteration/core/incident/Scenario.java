package fr.femtost.sbs.alteration.core.incident;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;

public class Scenario {

    private String record;

    private String filter;

    private long firstDate;

    @JacksonXmlProperty(localName = "action")
    @JacksonXmlElementWrapper(useWrapping = false)
    private Collection<Action> actions = newArrayList();

    public String getRecord() {
        return record;
    }

    public void setRecord(final String record) {
        this.record = record;
    }

    public String getFilter() {
        if (filter == null) {
            return "";
        }
        return filter;
    }

    public void setFilter(final String filter) {
        this.filter = filter;
    }

    public Collection<Action> getActions() {
        return actions;
    }

    public void setActions(final Collection<Action> actions) {
        this.actions = actions;
    }

    public long getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(long firstDate) {
        this.firstDate = firstDate;
    }
}