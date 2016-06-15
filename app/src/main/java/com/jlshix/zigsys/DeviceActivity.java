package com.jlshix.zigsys;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jlshix.zigsys.frag.Envir;
import com.jlshix.zigsys.frag.Light;
import com.jlshix.zigsys.frag.Msg;
import com.jlshix.zigsys.frag.Plug;
import com.jlshix.zigsys.utils.L;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_device)
public class DeviceActivity extends BaseActivity {

    //fab 用于添加设备
    @ViewInject(R.id.fab)
    private FloatingActionButton fab;

    //mViewPager 作为容器
    @ViewInject(R.id.vp)
    private ViewPager vp;

    //tabs 选项卡
    @ViewInject(R.id.tabs)
    private TabLayout tabs;

    //toolbar
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;

    //fragments
    private Envir envir;
    private Plug plug;
    private Light light;
    private Msg msg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        initView();

    }

    /**
     * 初始化界面
     */
    private void initView() {
        envir = new Envir();
        plug = new Plug();
        light = new Light();
        msg = new Msg();
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return envir;
                    case 1:
                        return plug;
                    case 2:
                        return light;
                    case 3:
                        return msg;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                String[] title = getResources().getStringArray(R.array.frag_title);
                return title[position];
            }
        });
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabs.setupWithViewPager(vp);
    }

    @Event(R.id.fab)
    private void addDevice(View v) {
        L.toast(getApplication(), "add device");
    }

}
