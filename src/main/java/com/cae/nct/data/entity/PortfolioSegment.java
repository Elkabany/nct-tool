package com.cae.nct.data.entity;

import javax.persistence.Entity;

@Entity
public class PortfolioSegment extends AbstractEntity {

    private Integer permittedVolume;
    private boolean active;
    private String type;
    private String segment;
    private Integer typeExtId;
    private Integer segmentExtId;

    public Integer getPermittedVolume() {
        return permittedVolume;
    }
    public void setPermittedVolume(Integer permittedVolume) {
        this.permittedVolume = permittedVolume;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getSegment() {
        return segment;
    }
    public void setSegment(String segment) {
        this.segment = segment;
    }
    public Integer getTypeExtId() {
        return typeExtId;
    }
    public void setTypeExtId(Integer typeExtId) {
        this.typeExtId = typeExtId;
    }
    public Integer getSegmentExtId() {
        return segmentExtId;
    }
    public void setSegmentExtId(Integer segmentExtId) {
        this.segmentExtId = segmentExtId;
    }

}
