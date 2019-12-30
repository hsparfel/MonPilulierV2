package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.Date;

public class Duree extends SugarRecord implements Serializable, Comparable<Duree> {

private Long id;
private String name;
private Date creationDate;

    public Duree() {
    }

    public Duree(String name) {
        this.name = name;
    }
    @Override
    public Long getId() {
        return id;
    }
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Duree{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }

    @Override
    public int compareTo(Duree o) {
        return this.id.compareTo(o.id);
    }
}
