package com.jlshix.zigsys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_gate_add)
public class GateBindActivity extends BaseActivity {

    @ViewInject(R.id.gate_imei)
    private EditText gateImei;

    @ViewInject(R.id.gate_name)
    private EditText gateName;

    @ViewInject(R.id.name)
    private RadioGroup name;

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
}
