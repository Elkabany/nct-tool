package com.cae.nct.data.entity;

import javax.persistence.Entity;

@Entity
public class PRODSUBGR extends AbstractEntity {

    private String group;
    private String code;
    private String nameEn;
    private String nameAr;
    private Integer order;
    private boolean active;
    private Integer extId;
    private String role;
    private Integer groupExtId;

    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }
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
    public Integer getOrder() {
        return order;
    }
    public void setOrder(Integer order) {
        this.order = order;
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
    public Integer getGroupExtId() {
        return groupExtId;
    }
    public void setGroupExtId(Integer groupExtId) {
        this.groupExtId = groupExtId;
    }

}
