package com.cae.nct.data.entity;

import javax.persistence.Entity;

@Entity
public class PRODGR extends AbstractEntity {

    private String code;
    private String nameEn;
    private String nameAr;
    private String picture;
    private String descEn;
    private String descAr;
    private boolean active;
    private Integer extId;
    private String role;

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
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
    public String getPicture() {
        return picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }
    public String getDescEn() {
        return descEn;
    }
    public void setDescEn(String descEn) {
        this.descEn = descEn;
    }
    public String getDescAr() {
        return descAr;
    }
    public void setDescAr(String descAr) {
        this.descAr = descAr;
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
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

}
