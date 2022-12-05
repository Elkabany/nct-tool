package com.cae.nct.data.entity;

import javax.persistence.Entity;

@Entity
public class PROD_PRODSUBGR extends AbstractEntity {

    private Integer productExtId;
    private Integer subgroupExtId;
    private String productName;
    private String subgroupName;
    private boolean active;

    public Integer getProductExtId() {
        return productExtId;
    }
    public void setProductExtId(Integer productExtId) {
        this.productExtId = productExtId;
    }
    public Integer getSubgroupExtId() {
        return subgroupExtId;
    }
    public void setSubgroupExtId(Integer subgroupExtId) {
        this.subgroupExtId = subgroupExtId;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getSubgroupName() {
        return subgroupName;
    }
    public void setSubgroupName(String subgroupName) {
        this.subgroupName = subgroupName;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

}
