package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;
import java.util.Date;

@Table
public class Medicament extends SugarRecord implements Serializable, Comparable<Medicament>{

private Long id;
private String name;
private String detail;
private Date creationDate;

    public Medicament() {
    }

    public Medicament(String name, String detail) {
        this.name = name;
        this.detail = detail;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Medicament{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", detail='" + detail + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }

    @Override
    public int compareTo(Medicament o) {
        return this.name.compareTo(o.name);
    }
}
