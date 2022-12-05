package com.cae.nct.data.entity;

import javax.persistence.Entity;

@Entity
public class PortfolioType extends AbstractEntity {

    private String name;
    private String code;
    private Integer extId;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public Integer getExtId() {
        return extId;
    }
    public void setExtId(Integer extId) {
        this.extId = extId;
    }

}
