package com.qfzj.selfpickupcabinet.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.qfzj.selfpickupcabinet.Adapter.StatusAdapter;
import com.qfzj.selfpickupcabinet.R;
import com.qfzj.selfpickupcabinet.bean.BoxStatusBean;
import com.qfzj.selfpickupcabinet.bean.CabinetBean;
import com.qfzj.selfpickupcabinet.message.AllCabinetStatusMsg;
import com.qfzj.selfpickupcabinet.message.AllDoorStatusMsg;
import com.qfzj.selfpickupcabinet.message.CabinetStatusMsg;
import com.qfzj.selfpickupcabinet.message.DoorStatusMsg;
import com.qfzj.selfpickupcabinet.util.HttpUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    @InjectView(R.id.addBtn)
    Button addBtn;
    @InjectView(R.id.takeBtn)
    Button takeBtn;
    @InjectView(R.id.setBtn)
    Button setBtn;

    Map<String, BoxStatusBean> boxNoToBox;
    Map<String, String> orderNoToBoxNo;
    private ArrayList<BoxStatusBean> boxStatusBeans = new ArrayList<BoxStatusBean>();
    private GridLayoutManager layoutManager;
    private RecyclerView setting_recyclerView;
    private StatusAdapter statusAdapter;
    private static final String TAG = "MainActivity";
    private Drawable administratorOn ;
    private Drawable administratorOff ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        boxNoToBox = new HashMap<String, BoxStatusBean>();
        orderNoToBoxNo = new HashMap<String, String>();
        administratorOn = this.getResources().getDrawable(R.drawable.administrator_on);
        administratorOff =this.getResources().getDrawable(R.drawable.administrator_off);
        updateBoxStatus();
        setting_recyclerView = findViewById(R.id.setting_recyclerView);
        layoutManager=new GridLayoutManager(this,4);
        setting_recyclerView.setLayoutManager(layoutManager);
        statusAdapter = new StatusAdapter(boxStatusBeans,false);
        UpdateBoxLayoutView(false);
    }
    /**加载柜子排列视图*/
    private void UpdateBoxLayoutView(boolean permission){
        statusAdapter.mStatusList = boxStatusBeans;
        statusAdapter.misClickable = permission ;
        setting_recyclerView.setAdapter(statusAdapter);

    }


    @OnClick({R.id.addBtn, R.id.takeBtn, R.id.setBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addBtn:
                UpdateBoxLayoutView(false);
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("您好，请扫描二维码").setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //这里需要扫描二维码，还没写
                                final String orderNo = "fffff";

                                if (!orderNo.equals("")) {
                                    final BoxStatusBean vacantCabinet = openVacantCabinet("0");

                                    if (vacantCabinet != null) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                                        builder1.setTitle(vacantCabinet.boxNo + "号柜已打开，请放入物品\n关闭柜门后请点击确定！").setIcon(android.R.drawable.ic_dialog_info)
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //检查该柜门是否关闭
                                                        if (checkItem(vacantCabinet.boxNo) == 1 && checkDoor(vacantCabinet.boxNo) == 0) {
                                                            //将订单号与柜子号绑定
                                                            orderNoToBoxNo.put(orderNo, vacantCabinet.boxNo);

                                                            final Bundle mBundle = new Bundle();
                                                            mBundle.putString("orderNo", orderNo);
                                                            HttpUtil.sendStoreOkHttpRequest(mBundle, new okhttp3.Callback() {
                                                                @Override
                                                                public void onResponse(Call call, Response response) throws IOException {
                                                                }

                                                                @Override
                                                                public void onFailure(Call call, IOException e) {
                                                                    Log.d(TAG, "onFailure: ERROR!");
                                                                    Toast.makeText(MainActivity.this, "连接服务器失败，请重新尝试！", Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                            Toast.makeText(MainActivity.this, "存放成功！", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(MainActivity.this, "存放失败！\n请检查箱门是否关闭或联系管理人员！", Toast.LENGTH_SHORT).show();
                                                        }
                                                        dialog.dismiss();
                                                    }
                                                });
                                        builder1.show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "柜子已满，无法放入！", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                        });
                builder.show();
                break;
            case R.id.takeBtn:
                UpdateBoxLayoutView(false);
                AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                builder2.setTitle("您好，请扫描二维码").setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //这里需要扫描二维码，还没写
                                final String reservationNo = "fffff";

                                if (!orderNoToBoxNo.containsKey(reservationNo)) {
                                    Toast.makeText(MainActivity.this, "没有这个柜子，请重新操作", Toast.LENGTH_SHORT).show();
                                } else {
                                    String cabinetNo = orderNoToBoxNo.get(reservationNo);
                                    if (cabinetNo != null) {
                                        BoxStatusBean boxStatusBean = boxNoToBox.get(cabinetNo);

                                        if (boxStatusBean != null) {
                                            if (boxStatusBean != null && openCabinet(cabinetNo)) {

                                                orderNoToBoxNo.remove(reservationNo);

                                                final Bundle mBundle = new Bundle();
                                                mBundle.putString("orderNo", reservationNo);
                                                HttpUtil.sendWithdrawOkHttpRequest(mBundle, new okhttp3.Callback() {
                                                    @Override
                                                    public void onResponse(Call call, Response response) throws IOException {
                                                    }

                                                    @Override
                                                    public void onFailure(Call call, IOException e) {
                                                        Log.d(TAG, "onFailure: ERROR!");
                                                        Toast.makeText(MainActivity.this, "连接服务器失败，请重新尝试！", Toast.LENGTH_LONG).show();
                                                    }
                                                });

                                                AlertDialog.Builder builder3 = new AlertDialog.Builder(MainActivity.this);
                                                builder3.setTitle(cabinetNo + "号柜已打开\n取走您的物品后请关闭柜门！").setIcon(android.R.drawable.ic_dialog_info)
                                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                                builder3.show();
                                            } else {
                                                Toast.makeText(MainActivity.this, "打开柜子失败！", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }
                                dialog.dismiss();
                            }
                        });
                builder2.show();
                break;
            case R.id.setBtn:
                if (setBtn.getBackground() == administratorOn) {
                    AlertDialog.Builder builder4 = new AlertDialog.Builder(MainActivity.this);
                    builder4.setTitle("您好，请扫描二维码").setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //这里需要扫描二维码，还没写
                                    String userValidation = "aaa";

                                    if (userValidation.equals("aaa")) {
                                        setBtn.setBackground(administratorOff);
                                        updateBoxStatus();
                                        UpdateBoxLayoutView(true);

                                    } else {
                                        Toast.makeText(MainActivity.this, "管理员验证失败！", Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.dismiss();
                                }
                            });
                    builder4.show();
                } else {
                    updateBoxStatus();
                    UpdateBoxLayoutView(false);
                    setBtn.setBackground(administratorOn);
                }
                break;
        }
    }

    public BoxStatusBean openVacantCabinet(String groupNo) {
        updateBoxStatus();

        BoxStatusBean res = null;

        for (Map.Entry<String, BoxStatusBean> entry: boxNoToBox.entrySet()) {
            BoxStatusBean temp = entry.getValue();
            if(temp.hasItem == 0) {
                res = temp;
                openCabinet(res.boxNo);
                break;
            }
        }
        return res;
    }

    public boolean openCabinet(String boxNo) {
        String result = "{\"errorCode\": 0, \"errorMessage\": \"success\", \"data\":{\"boxNo\": \"1\", \"doorStatus\": 1}}";
        DoorStatusMsg doorStatusMsg = new Gson().fromJson(result, DoorStatusMsg.class);

        if(doorStatusMsg.errorCode == 0) {
            if(doorStatusMsg.data.doorStatus == 0) {
                //openBox(boxNo);
                updateBoxStatus();
            }

            return true;
        }

        return false;
    }

    public int checkDoor(String boxNo) {
        String result = "{\"errorCode\": 0, \"errorMessage\": \"success\", \"data\":{\"boxNo\": \"1\", \"doorStatus\": 1}}";
        DoorStatusMsg doorStatusMsg = new Gson().fromJson(result, DoorStatusMsg.class);

        if(doorStatusMsg.errorCode == 0) {
            updateBoxStatus();
            return doorStatusMsg.data.doorStatus;
        }

        return -1;
    }

    public int checkItem(String boxNo) {
        String result = "{\"errorCode\": 0, \"errorMessage\": \"success\", \"data\":{\"boxNo\": \"1\", \"hasItem\": 1}}";
        CabinetStatusMsg cabinetStatusMsg = new Gson().fromJson(result, CabinetStatusMsg.class);

        if(cabinetStatusMsg.errorCode == 0) {
            updateBoxStatus();
            return cabinetStatusMsg.data.hasItem;
        }

        return -1;
    }

    public void updateBoxStatus() {
        String result1 = "{\"errorCode\": 0, \"errorMessage\": \"成功\",\"data\":[{\"boxNo\": \"1\", \"hasItem\": 1}, {\"boxNo\": \"2\", \"hasItem\": 0}]}";
        AllCabinetStatusMsg allCabinetStatusMsg = new Gson().fromJson(result1, AllCabinetStatusMsg.class);

        String result2 = "{\"errorCode\": 0, \"errorMessage\": \"success\", \"data\":[{\"boxNo\": \"1\", \"doorStatus\": 1}, {\"boxNo\": \"2\", \"doorStatus\": 0}]}";
        AllDoorStatusMsg allDoorStatusMsg = new Gson().fromJson(result2, AllDoorStatusMsg.class);

        if(allCabinetStatusMsg.errorCode == 0) {
            for(int i = 0; i < allCabinetStatusMsg.data.size(); i++) {
                String key = allCabinetStatusMsg.data.get(i).boxNo;
                BoxStatusBean value = new BoxStatusBean(allCabinetStatusMsg.data.get(i));

                if(boxNoToBox.containsKey(key)) {
                    value = boxNoToBox.get(key);
                    value.update(allCabinetStatusMsg.data.get(i));
                }
                boxNoToBox.put(key, value);
            }
        }

        if(allDoorStatusMsg.errorCode == 0) {
            for(int i = 0; i < allDoorStatusMsg.data.size(); i++) {
                String key = allDoorStatusMsg.data.get(i).boxNo;
                BoxStatusBean value = new BoxStatusBean(allDoorStatusMsg.data.get(i));

                if(boxNoToBox.containsKey(key)) {
                    value = boxNoToBox.get(key);
                    value.update(allDoorStatusMsg.data.get(i));
                }
                boxNoToBox.put(key, value);
            }
        }
        boxStatusBeans.clear();
        for (Map.Entry<String, BoxStatusBean> entry: boxNoToBox.entrySet()) {
            boxStatusBeans.add(entry.getValue());
        }
    }
}
