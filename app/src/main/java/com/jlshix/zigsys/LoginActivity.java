package com.jlshix.zigsys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.HashSet;



@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {


    @ViewInject(R.id.mail)
    private EditText mail;

    @ViewInject(R.id.pw)
    private EditText pw;

    @ViewInject(R.id.login)
    private CircularProgressButton login;

    @ViewInject(R.id.new_usr)
    private Button newUser;


    private String TAG = "LOGIN_ACTIVITY";
    private String userMail,userName, gateImei;
    /**
     * 其它Activity调用此方法跳转至此Activity
     * @param activity Context
     */
    public static void startMe(Activity activity) {
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        // 去除本地保存的网关信息
        L.setGateImei("x");
    }

    private void initView() {
        TextWatcher textChange = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (login.getProgress() != 0) {
                    login.setProgress(0);
                }
            }
        };

        login.setIndeterminateProgressMode(true);
        mail.addTextChangedListener(textChange);
        pw.addTextChangedListener(textChange);
    }


    @Event(R.id.new_usr)
    private void toReg(View v) {
        RegisterActivity.startMe(LoginActivity.this);
    }

    @Event(R.id.login)
    private void doLogin(View v) {
        final String et_mail = mail.getText().toString();
        final String et_pw = pw.getText().toString();

        if (et_mail.equals("")) {
            login.setProgress(50);
            login.setErrorText("请输入邮箱");
            login.setProgress(-1);
            return;
        }
        if (et_pw.equals("")) {
            login.setProgress(50);
            login.setErrorText("请输入密码");
            login.setProgress(-1);
            return;
        }
        if (login.getProgress() == 0) {
            login.setProgress(50);

            RequestParams params = new RequestParams(L.URL_LOGIN + "?mail=" + et_mail + "&pw=" + et_pw);
            x.http().get(params, new Callback.CommonCallback<JSONObject>() {
                @Override
                public void onSuccess(JSONObject result) {
                    String code = result.optString("code", "x");
                    if (code.equals("1")) {
                        JSONObject content = result.optJSONObject("info");
                        userMail = content.optString("mail", null);
                        userName = content.optString("name", null);
                        gateImei = content.optString("gate_imei", null);
                        Log.e(TAG, "onSuccess: " + userMail + "--" + userName + "--" + gateImei);
                    }
                    switch (code) {
                        case "1":
                            login.setProgress(100);
                            toMainActivity();
                            break;
                        case "-1":
                            login.setErrorText("内部错误");
                            login.setProgress(-1);
                            break;
                        case "3":
                            login.setErrorText("密码错误");
                            login.setProgress(-1);
                            break;
                        case "2":
                            login.setErrorText("未注册");
                            login.setProgress(-1);
                            break;
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    L.toast(LoginActivity.this, ex.getMessage());
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        } else {
            login.setProgress(0);
        }

    }

    private void toMainActivity() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    App.sp.edit().putInt("isLogin", 1).putString("name", userName)
                            .putString("mail", userMail).putString("gateImei", gateImei)
                            .putString("pw", pw.getText().toString().trim()).apply();

                    //TODO 极光推送Tag
//                    HashSet<String> tag = new HashSet<>();
//                    tag.add(userName);
//                    JPushInterface.setTags(LoginActivity.this, tag, null);
//                    Log.e("TAG", "Jpush Tag Success");
                    Thread.sleep(500);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == 2000) {
            login.setProgress(50);
            mail.setText(data.getStringExtra("mail"));
            pw.setText(data.getStringExtra("pw"));
            login.setIdleText("注册成功 点击登录");
            login.setProgress(0);
        }
    }
}
