package com.jlshix.zigsys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


import com.dd.CircularProgressButton;
import com.jlshix.zigsys.utils.L;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;



@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {
    private static final String TAG = "REGISTER_ACTIVITY";

    @ViewInject(R.id.mail)
    private EditText mail;

    @ViewInject(R.id.pw)
    private EditText pw;

    @ViewInject(R.id.pw2)
    private EditText pw2;

    @ViewInject(R.id.name)
    private EditText name;

    @ViewInject(R.id.reg)
    private CircularProgressButton reg;

    public static void startMe(Activity activity) {
        activity.startActivityForResult(new Intent(activity, RegisterActivity.class), 1000);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reg.setIndeterminateProgressMode(true);
    }


    @Event(R.id.reg)
    private void doReg(View v) {
        final String et_mail = mail.getText().toString();
        final String et_pw = pw.getText().toString();
        final String et_pw2 = pw2.getText().toString();
        final String et_name = name.getText().toString();

        if (!et_pw.equals(et_pw2)) {
            reg.setProgress(50);
            reg.setErrorText("密码不一致");
            reg.setProgress(-1);
            return;
        }
        if (et_mail.equals("") || et_name.equals("") || et_pw.equals("") || et_pw2.equals("")) {
            reg.setProgress(50);
            reg.setErrorText("信息不完整");
            reg.setProgress(-1);
            return;
        }
        if (reg.getProgress() == 0) {
            reg.setProgress(50);
            RequestParams params = new RequestParams(L.URL_REG + "?mail=" + et_mail
                    +  "&name=" + et_name + "&pw=" + et_pw);
            x.http().get(params, new Callback.CommonCallback<JSONObject>() {
                @Override
                public void onSuccess(JSONObject result) {
                    String code = result.optString("code", "x");
                    switch (code) {
                        case "1":
                            success(et_mail, et_pw);
                            break;
                        case "0":
                            fail();
                            break;
                        case "-1":
                            noPara();
                            break;
                        default:
                            error();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    L.toast(RegisterActivity.this, ex.getMessage());
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        } else {
            reg.setProgress(0);
        }

    }

    private void error() {
        reg.setErrorText("错误x");
        reg.setProgress(-1);
    }

    private void noPara() {
        reg.setErrorText("错误-1");
        reg.setProgress(-1);
    }

    private void fail() {
        reg.setErrorText("错误0");
        reg.setProgress(-1);
    }

    private void success(String mail, String pw) {
        Log.i(TAG, "in_success: ");
        reg.setProgress(100);
        Intent back = new Intent();
        back.putExtra("status", "1");
        back.putExtra("mail", mail);
        back.putExtra("pw", pw);
        setResult(2000, back);
        finish();
    }
}
