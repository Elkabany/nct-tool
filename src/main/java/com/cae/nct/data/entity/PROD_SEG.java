package com.cae.nct.data.entity;

import javax.persistence.Entity;

@Entity
public class PROD_SEG extends AbstractEntity {

    private Integer productExtId;
    private Integer segmentExtId;
    private String productName;
    private String segment;
    private boolean active;

    public Integer getProductExtId() {
        return productExtId;
    }
    public void setProductExtId(Integer productExtId) {
        this.productExtId = productExtId;
    }
    public Integer getSegmentExtId() {
        return segmentExtId;
    }
    public void setSegmentExtId(Integer segmentExtId) {
        this.segmentExtId = segmentExtId;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getSegment() {
        return segment;
    }
    public void setSegment(String segment) {
        this.segment = segment;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

}
