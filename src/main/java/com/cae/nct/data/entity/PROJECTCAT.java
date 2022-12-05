package com.cae.nct.data.entity;

import javax.persistence.Entity;

@Entity
public class PROJECTCAT extends AbstractEntity {

    private String nameAr;
    private String nameEn;
    private boolean active;
    private Integer extId;

    public String getNameAr() {
        return nameAr;
    }
    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }
    public String getNameEn() {
        return nameEn;
    }
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public Integer getExtId() {
        return extId;
    }
    public void setExtId(Integer extId) {
        this.extId = extId;
    }

}
