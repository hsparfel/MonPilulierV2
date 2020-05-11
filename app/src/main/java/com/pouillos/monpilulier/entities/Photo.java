package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.List;


public class Photo extends SugarRecord implements Serializable, Comparable<Photo>{

    private String type;
    private byte[] data;
    private Long itemId;

    public Photo() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    @Override
    public int compareTo(Photo o) {
        return this.getId().compareTo(o.getId());
    }


}
