package com.cae.nct.data.entity;

import javax.persistence.Entity;

@Entity
public class NEEDCAT_SEG extends AbstractEntity {

    private String category;
    private String segment;
    private Integer categortExtId;
    private Integer segmentExtId;
    private boolean active;

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getSegment() {
        return segment;
    }
    public void setSegment(String segment) {
        this.segment = segment;
    }
    public Integer getCategortExtId() {
        return categortExtId;
    }
    public void setCategortExtId(Integer categortExtId) {
        this.categortExtId = categortExtId;
    }
    public Integer getSegmentExtId() {
        return segmentExtId;
    }
    public void setSegmentExtId(Integer segmentExtId) {
        this.segmentExtId = segmentExtId;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

}
