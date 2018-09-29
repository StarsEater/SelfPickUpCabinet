package com.qfzj.selfpickupcabinet.util;

import android.os.Bundle;

import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import javax.xml.transform.OutputKeys;

/**
 * Created by ZERO on 2017/10/29.
 */

public class HttpUtil {

    private static String CJF = "10.108.120.225:8084";
    private static String ZMQ = "10.108.112.96:8080";
    private static String CJY = "10.108.122.109:8084";
    private static String ZR = "service.gsubway.com";
    private static String ZR2 = "service2.gsubway.com";

    public static String server = CJF;

    public static void sendStoreOkHttpRequest(Bundle mBundle, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("OrderNo", mBundle.getString("OrderNo"))
                .build();
        Request request = new Request.Builder()
                .url("http://" + server + "/storeBox")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendWithdrawOkHttpRequest(Bundle mBundle, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("OrderNo", mBundle.getString("OrderNo"))
                .build();
        Request request = new Request.Builder()
                .url("http://" + server + "/withdrawBox")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
}

