package com.cae.nct.data.entity;

import javax.persistence.Entity;

@Entity
public class OPPSUBCAT extends AbstractEntity {

    private String category;
    private String priority;
    private String nameEn;
    private String nameAr;
    private Integer order;
    private String picture;
    private String descEn;
    private String descAr;
    private String tipsLink;
    private String tipsDescEn;
    private String tipsDescAr;
    private String simulatorLink;
    private String blueprintsLink;
    private boolean active;
    private Integer categoryExtId;

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getPriority() {
        return priority;
    }
    public void setPriority(String priority) {
        this.priority = priority;
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
    public Integer getOrder() {
        return order;
    }
    public void setOrder(Integer order) {
        this.order = order;
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
    public String getBlueprintsLink() {
        return blueprintsLink;
    }
    public void setBlueprintsLink(String blueprintsLink) {
        this.blueprintsLink = blueprintsLink;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public Integer getCategoryExtId() {
        return categoryExtId;
    }
    public void setCategoryExtId(Integer categoryExtId) {
        this.categoryExtId = categoryExtId;
    }

}
