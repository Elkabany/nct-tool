package com.cae.nct.data.entity;

import javax.persistence.Entity;

@Entity
public class NEEDSUBCAT_PROD extends AbstractEntity {

    private String needSubcategory;
    private Integer needSubExtId;
    private String product;
    private Integer productExtId;
    private boolean active;

    public String getNeedSubcategory() {
        return needSubcategory;
    }
    public void setNeedSubcategory(String needSubcategory) {
        this.needSubcategory = needSubcategory;
    }
    public Integer getNeedSubExtId() {
        return needSubExtId;
    }
    public void setNeedSubExtId(Integer needSubExtId) {
        this.needSubExtId = needSubExtId;
    }
    public String getProduct() {
        return product;
    }
    public void setProduct(String product) {
        this.product = product;
    }
    public Integer getProductExtId() {
        return productExtId;
    }
    public void setProductExtId(Integer productExtId) {
        this.productExtId = productExtId;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

}
