package com.cae.nct.data.entity;

import javax.persistence.Entity;

@Entity
public class Segment extends AbstractEntity {

    private String name;
    private boolean prospect;
    private Integer extId;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isProspect() {
        return prospect;
    }
    public void setProspect(boolean prospect) {
        this.prospect = prospect;
    }
    public Integer getExtId() {
        return extId;
    }
    public void setExtId(Integer extId) {
        this.extId = extId;
    }

}
