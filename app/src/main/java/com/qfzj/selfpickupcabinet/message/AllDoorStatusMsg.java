package com.qfzj.selfpickupcabinet.message;

import com.qfzj.selfpickupcabinet.bean.DoorBean;

import java.util.ArrayList;

public class AllDoorStatusMsg {
    public int errorCode;
    public String errorMessage;
    public ArrayList<DoorBean> data;
}
