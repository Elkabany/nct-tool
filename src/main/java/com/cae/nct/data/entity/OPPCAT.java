package com.cae.nct.data.entity;

import javax.persistence.Entity;

@Entity
public class OPPCAT extends AbstractEntity {

    private String nameEn;
    private String nameAr;
    private String icon;
    private boolean active;
    private Integer extId;

    public String getNameEn() {
        return nameEn;
    }
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
    public String getNameAr() {
        return nameAr;
    }
    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
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
