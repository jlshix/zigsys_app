package com.jlshix.zigsys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.jlshix.zigsys.frag.Envir;
import com.jlshix.zigsys.frag.Light;
import com.jlshix.zigsys.frag.Msg;
import com.jlshix.zigsys.frag.Others;
import com.jlshix.zigsys.frag.Plug;
import com.jlshix.zigsys.utils.L;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_device)
public class DeviceActivity extends BaseActivity {

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
    private Others others;
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
        others = new Others();
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
                        return others;
                    case 4:
                        return msg;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 5;
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
        vp.setOffscreenPageLimit(4);
        tabs.setupWithViewPager(vp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_device, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent intent = new Intent(getApplicationContext(), AddDeviceActivity.class);
            startActivityForResult(intent, L.ADD_REQUEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == L.ADD_REQUEST && resultCode == L.ADD_RETURN) {
            // 发送广播 TODO 接收广播
            String type = data.getStringExtra("type");
            Intent intent = new Intent("android.intent.action.MY_BROADCAST");
            intent.putExtra("msg", "REFRESH");
            intent.putExtra("type", type);
            sendBroadcast(intent);
        }
    }
}
