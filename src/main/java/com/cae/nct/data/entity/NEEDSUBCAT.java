package com.cae.nct.data.entity;

import javax.persistence.Entity;

@Entity
public class NEEDSUBCAT extends AbstractEntity {

    private String category;
    private String nameEn;
    private String nameAr;
    private boolean active;
    private Integer extId;
    private Integer categoryExtId;

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
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
    public Integer getCategoryExtId() {
        return categoryExtId;
    }
    public void setCategoryExtId(Integer categoryExtId) {
        this.categoryExtId = categoryExtId;
    }

}
