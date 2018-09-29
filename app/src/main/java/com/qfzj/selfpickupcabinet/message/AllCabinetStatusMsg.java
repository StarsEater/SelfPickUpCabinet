package com.qfzj.selfpickupcabinet.message;

import com.qfzj.selfpickupcabinet.bean.CabinetBean;

import java.util.ArrayList;

public class AllCabinetStatusMsg {

    public int errorCode;
    public String errorMessage;
    public ArrayList<CabinetBean> data;
}
