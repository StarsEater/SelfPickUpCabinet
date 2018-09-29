package com.qfzj.selfpickupcabinet.bean;

import java.io.Serializable;

public class BoxStatusBean implements Serializable {

    public String boxNo;
    public int hasItem;
    public int doorStatus;

    public BoxStatusBean(String boxNo, int hasItem, int doorStatus) {
        this.boxNo = boxNo;
        this.hasItem = hasItem;
        this.doorStatus = doorStatus;
    }

    public BoxStatusBean(CabinetBean cabinetBean) {
        this.boxNo = cabinetBean.boxNo;
        this.hasItem = cabinetBean.hasItem;
        this.doorStatus = 0;
    }

    public BoxStatusBean(DoorBean doorBean) {
        this.boxNo = doorBean.boxNo;
        this.hasItem = doorBean.doorStatus;
        this.hasItem = 0;
    }

    public void update(CabinetBean cabinetBean) {
        if(cabinetBean.boxNo.equals(this.boxNo)) {
            this.hasItem = cabinetBean.hasItem;
        }
    }

    public void update(DoorBean doorBean) {
        if(doorBean.boxNo.equals(this.boxNo)) {
            this.doorStatus = doorBean.doorStatus;
        }
    }

    public int doorStatusChange(){
        if(doorStatus==1){
            doorStatus = 0;
        }
        else{
            doorStatus=1;
        }
        return doorStatus;
    }
}
