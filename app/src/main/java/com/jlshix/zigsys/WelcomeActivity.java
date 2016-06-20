package com.jlshix.zigsys;

import com.jlshix.zigsys.utils.L;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;


/**
 * 显示欢迎界面，判断是否登录，从sp中获取isLogin 得到 0:未登录，1:已登录, -1 默认值，第一次使用；
 * 0，-1跳至LoginActivity 1跳至MainActivity
 */
public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    initView();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void initView() {
        final int isLogin = App.sp.getInt("isLogin", -1);
        jumpOut(isLogin);
    }

    private void jumpOut(int isLogin) {
        if (isLogin == 1) {
            String u = App.sp.getString("name", "x");
            String p = App.sp.getString("pw","x");
            //TODO 极光推送
//            if (!"x".equals(u) && !"x".equals(p)) {
//                //极光推送Tag
//                HashSet<String> tag = new HashSet<>();
//                tag.add(u);
//                JPushInterface.setTags(WelcomeActivity.this, tag, null);
//                Log.e("TAG", "Jpush Tag Success");
//
//            }else {
//                Toast.makeText(WelcomeActivity.this, "u_p ERR", Toast.LENGTH_SHORT).show();
//            }

            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        } else if (isLogin == -1 || isLogin == 0) {
            LoginActivity.startMe(WelcomeActivity.this);
            finish();
        } else {
            L.toast(WelcomeActivity.this, "App.sp ERR");
        }

    }
}