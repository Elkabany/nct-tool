package com.cae.nct.data.entity;

import javax.persistence.Entity;

@Entity
public class NEEDCAT extends AbstractEntity {

    private String nameEn;
    private String nameAr;
    private String icon;
    private String picture;
    private String descEn;
    private String descAr;
    private String tipsLink;
    private String tipsDescEn;
    private String tipsDescAr;
    private String simulatorLink;
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
    public String getTipsLink() {
        return tipsLink;
    }
    public void setTipsLink(String tipsLink) {
        this.tipsLink = tipsLink;
    }
    public String getTipsDescEn() {
        return tipsDescEn;
    }
    public void setTipsDescEn(String tipsDescEn) {
        this.tipsDescEn = tipsDescEn;
    }
    public String getTipsDescAr() {
        return tipsDescAr;
    }
    public void setTipsDescAr(String tipsDescAr) {
        this.tipsDescAr = tipsDescAr;
    }
    public String getSimulatorLink() {
        return simulatorLink;
    }
    public void setSimulatorLink(String simulatorLink) {
        this.simulatorLink = simulatorLink;
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
