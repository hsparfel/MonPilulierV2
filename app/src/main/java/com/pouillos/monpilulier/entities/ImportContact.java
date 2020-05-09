package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.NotNull;

import java.io.Serializable;

public class ImportContact extends SugarRecord implements Serializable, Comparable<Analyse> {

    @NotNull
    private String path;
    @NotNull
    private boolean importCompleted;

    public ImportContact(String path, boolean importCompleted) {
        this.path = path;
        this.importCompleted = importCompleted;
    }

    public ImportContact() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isImportCompleted() {
        return importCompleted;
    }

    public void setImportCompleted(boolean importCompleted) {
        this.importCompleted = importCompleted;
    }

    @Override
    public int compareTo(Analyse o) {
        return this.getId().compareTo(o.getId());
    }
}
