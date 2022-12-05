package com.cae.nct.data.entity;

import javax.persistence.Entity;

@Entity
public class TargetMeeting extends AbstractEntity {

    private String positionId;
    private String name;
    private String number;

    public String getPositionId() {
        return positionId;
    }
    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }

}
