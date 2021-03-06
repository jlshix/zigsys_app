package com.jlshix.zigsys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jlshix.zigsys.utils.CaptureActivityAnyOrientation;
import com.jlshix.zigsys.utils.L;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

// 从 GateBindActivity 复刻 部分变量名不再修改
@ContentView(R.layout.activity_add_device)
public class AddDeviceActivity extends BaseActivity {

    private static final String TAG = "ADD_DEVICE_ACTIVITY";
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

    @ViewInject(R.id.type)
    private TextView devType;

    private String[] names = {
            "客厅-", "卧室-", "书房-"
    };

    private String gate = L.getGateImei();
    private String type;
    private String no;
    private String state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // radioGroup 设置名字
        name.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.name0:
                        gateName.setText(names[0] + devType.getText().toString());
                        break;
                    case R.id.name1:
                        gateName.setText(names[1] + devType.getText().toString());
                        break;
                    case R.id.name2:
                        gateName.setText(names[2] + devType.getText().toString());
                        break;
                }
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == L.DEV_IMEI_LENGTH) {
                    gateAdd.setEnabled(true);
                } else {
                    gateAdd.setEnabled(false);
                    devType.setText("");
                }

                if (s.length() >= 8) {
                    type = gateImei.getText().toString().substring(4, 6);
                    no = gateImei.getText().toString().substring(6, 8);
                    switch (type) {
                        case "02":
                            devType.setText("温湿度光照检测器");
                            state = "--------";
                            break;
                        case "04":
                            devType.setText("烟雾监测器");
                            state = "-";
                            break;
                        case "05":
                            devType.setText("红外防盗器");
                            state = "-";
                            break;
                        case "06":
                            devType.setText("窗帘控制器");
                            state = "-";
                            break;
                        case "09":
                            devType.setText("多级调光灯");
                            state = "0";
                            break;
                        case "0A":
                            devType.setText("智能插座");
                            state = "0000";
                            break;
                        default:
                            devType.setText("暂未支持，敬请期待");
                            gateAdd.setEnabled(false);
                    }
                }


            }
        };
        // 设定监听器 显示类型 控制按钮是否可用
        gateImei.addTextChangedListener(textWatcher);

    }

    // 扫描二维码
    @Event(R.id.scan)
    private void scanCode(View view) {
        IntentIntegrator integrator = new IntentIntegrator(AddDeviceActivity.this);
        integrator.setPrompt("请扫描设备生成的二维码").setCaptureActivity(CaptureActivityAnyOrientation.class)
                .setOrientationLocked(true).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && result != null) {
            String code = result.getContents();
            Log.i(TAG, "onActivityResult: " + code);
            gateImei.setText(code);
        }

    }

    // 添加网关
    @Event(R.id.btn_add_gate)
    private void addDevice(View view) {
//        L.toast(AddDeviceActivity.this, "ADD_DEVICE");

        /**
         * imei 示例
         * 201302010809
         * 012345678901
         * 0123 45 67 8901
         * 年份4类型2编号2编号4 总计12
         */
        final String gateImei1 = gateImei.getText().toString();

        final String name1 = gateName.getText().toString();
        if (gateImei1.length() != L.DEV_IMEI_LENGTH || name1.length() == 0) {
            L.toast(AddDeviceActivity.this, "信息不完整");
            return;
        }

        type = gateImei1.substring(4, 6);
        no = gateImei1.substring(6, 8);

        RequestParams params = new RequestParams(L.URL_ADD_DEV + "?gate=" + gate + "&dev=" + gateImei1 + "&no=" + no
                        + "&type=" + type + "&name=" + name1 + "&state=" + state);
        x.http().get(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (L.DEBUG) {
                    L.toast(AddDeviceActivity.this, result.toString());
                }
                try {
                    if (!result.getString("code").equals("1")) {
                        L.toast(AddDeviceActivity.this, "CODE_ERR");
                    } else {

                        // 结束
                        Intent intent = new Intent();
                        intent.putExtra("type", type);
                        intent.putExtra("name", name1);
                        intent.putExtra("state", state);
                        AddDeviceActivity.this.setResult(L.ADD_RETURN, intent);
                        AddDeviceActivity.this.finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                L.toast(AddDeviceActivity.this, "ADD_ERR " + ex.getMessage());
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
