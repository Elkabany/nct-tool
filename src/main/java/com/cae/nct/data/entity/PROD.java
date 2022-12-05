package com.cae.nct.data.entity;

import javax.persistence.Entity;

@Entity
public class PROD extends AbstractEntity {

    private String code;
    private String nameEn;
    private String nameAr;
    private String picture;
    private String shortDescEn;
    private String shortDescAr;
    private String longLink;
    private String longDescEn;
    private String longDescAr;
    private String tipsLink;
    private String tipsDescEn;
    private String tipsDescAr;
    private String simulatorLink;
    private String color;
    private boolean hasAmount;
    private boolean hasPieces;
    private boolean active;
    private boolean restricted;
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
    public String getShortDescEn() {
        return shortDescEn;
    }
    public void setShortDescEn(String shortDescEn) {
        this.shortDescEn = shortDescEn;
    }
    public String getShortDescAr() {
        return shortDescAr;
    }
    public void setShortDescAr(String shortDescAr) {
        this.shortDescAr = shortDescAr;
    }
    public String getLongLink() {
        return longLink;
    }
    public void setLongLink(String longLink) {
        this.longLink = longLink;
    }
    public String getLongDescEn() {
        return longDescEn;
    }
    public void setLongDescEn(String longDescEn) {
        this.longDescEn = longDescEn;
    }
    public String getLongDescAr() {
        return longDescAr;
    }
    public void setLongDescAr(String longDescAr) {
        this.longDescAr = longDescAr;
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
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public boolean isHasAmount() {
        return hasAmount;
    }
    public void setHasAmount(boolean hasAmount) {
        this.hasAmount = hasAmount;
    }
    public boolean isHasPieces() {
        return hasPieces;
    }
    public void setHasPieces(boolean hasPieces) {
        this.hasPieces = hasPieces;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public boolean isRestricted() {
        return restricted;
    }
    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
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
