package com.cae.nct.data.entity;

import javax.persistence.Entity;

@Entity
public class NEEDCAT_PROJECTCAT extends AbstractEntity {

    private String needCategory;
    private String projectCategory;
    private Integer needExtId;
    private Integer projectExtId;
    private boolean active;

    public String getNeedCategory() {
        return needCategory;
    }
    public void setNeedCategory(String needCategory) {
        this.needCategory = needCategory;
    }
    public String getProjectCategory() {
        return projectCategory;
    }
    public void setProjectCategory(String projectCategory) {
        this.projectCategory = projectCategory;
    }
    public Integer getNeedExtId() {
        return needExtId;
    }
    public void setNeedExtId(Integer needExtId) {
        this.needExtId = needExtId;
    }
    public Integer getProjectExtId() {
        return projectExtId;
    }
    public void setProjectExtId(Integer projectExtId) {
        this.projectExtId = projectExtId;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

}
