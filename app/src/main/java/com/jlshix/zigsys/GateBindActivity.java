package com.jlshix.zigsys;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.jlshix.zigsys.utils.L;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_gate_add)
public class GateBindActivity extends BaseActivity {

    @ViewInject(R.id.gate_imei)
    private EditText gateImei;

    @ViewInject(R.id.gate_name)
    private EditText gateName;

    @ViewInject(R.id.scan)
    private Button scan;

    @ViewInject(R.id.name)
    private RadioGroup name;

    @ViewInject(R.id.btn_add_gate)
    private Button gateAdd;

    private String[] names = {
            "我的设备", "我家主控", "客厅网关"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.name0:
                        gateName.setText(names[0]);
                        break;
                    case R.id.name1:
                        gateName.setText(names[1]);
                        break;
                    case R.id.name2:
                        gateName.setText(names[2]);
                        break;
                }
            }
        });

    }

    // 扫描二维码
    @Event(R.id.scan)
    private void scanCode(View view) {
        L.toast(GateBindActivity.this, "SCAN_CODE");
    }

    // 添加网关
    @Event(R.id.btn_add_gate)
    private void bindGate(View view) {
        L.toast(GateBindActivity.this, "BIND_GATE");
        final String gateImei1 = gateImei.getText().toString();
        String name1 = gateName.getText().toString();
        if (gateImei1.length() == 0 || name1.length() == 0) {
            L.toast(GateBindActivity.this, "信息不完整");
            return;
        }
        RequestParams params = new RequestParams(L.URL_GATE_BIND + "?mail=" + L.MAIL + "&gate=" + gateImei1 + "&name=" + name1);
        x.http().get(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if (!result.getString("code").equals("1")) {
                        L.toast(GateBindActivity.this, "CODE_ERR");
                    } else {
                        // 绑定成功后设定配置文件内容
                        L.setBIND(true);
                        L.setGateImei(gateImei1);
                        // 结束
                        GateBindActivity.this.setResult(L.ADD_RETURN);
                        GateBindActivity.this.finish();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }






}
