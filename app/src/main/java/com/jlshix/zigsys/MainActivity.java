package com.jlshix.zigsys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jlshix.zigsys.data.GateData;
import com.jlshix.zigsys.utils.L;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    // fab button
    @ViewInject(R.id.fab)
    private FloatingActionButton fab;

    //swipe
    @ViewInject(R.id.swipe)
    private SwipeRefreshLayout swipe;

    // weather
    @ViewInject(R.id.weather)
    private CardView weather;

    // temperature
    @ViewInject(R.id.temp)
    private TextView temperature;

    // weatherState
    @ViewInject(R.id.state)
    private TextView weatherState;

    // place but now pm2.5
    @ViewInject(R.id.place)
    private TextView place;

    // ImageView
    @ViewInject(R.id.weather_pic)
    private ImageView weatherPic;



    // device
    @ViewInject(R.id.device)
    private CardView device;

    @ViewInject(R.id.title)
    private TextView name;

    @ViewInject(R.id.summary)
    private TextView gateState;

    // spinner for mode
    @ViewInject(R.id.mode_spinner)
    private Spinner spinner;


    // handler
    public static final int REFRESH = 0x01;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH:
                    updateWeather();
                    updateGate();
                    swipe.setRefreshing(false);
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        swipe.setOnRefreshListener(this);
        spinner.setAdapter(new ArrayAdapter<>(this, R.layout.spinner,
                getResources().getStringArray(R.array.mode)));
        handler.sendEmptyMessage(REFRESH);
    }

    // device OnClickListener
    @Event(value = R.id.device,
            type = View.OnClickListener.class)
    private void toDeviceActivity(View v) {
        Intent intent = new Intent(MainActivity.this, DeviceActivity.class);
        startActivity(intent);
    }

    // fab OnClickListener
    @Event(R.id.fab)
    private void addGate(View v) {
        startActivity(new Intent(getApplicationContext(), GateBindActivity.class));
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        swipe.setRefreshing(true);
        handler.sendEmptyMessage(REFRESH);
    }


    /**
     * 更新天气数据
     */
    private void updateWeather() {
        RequestParams params = new RequestParams(L.URL_WEATHER + L.getGPS(MainActivity.this) + "/realtime.json");
        x.http().get(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (!result.optString("status").equals("ok")) {
                    L.toast(MainActivity.this, "CODE_ERR");
                    return;
                }
                setWeatherCard(result.optJSONObject("result"));


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

    /**
     * 更新网关数据
     */
    private void updateGate() {
        RequestParams params = new RequestParams(L.URL_GATE + "?mail=" + L.MAIL);
        x.http().get(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (!result.optString("code").equals("1")) {
                    L.toast(MainActivity.this, "CODE_ERR");
                    return;
                }
                // 设定天气卡片 json内容太复杂，不再使用新类
                setGateCard(new GateData(result.optJSONObject("info")));
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

    private void setGateCard(GateData info) {
        name.setText(info.getName());
        if (info.getOnline().equals("1")){
            gateState.setText(R.string.online);
        } else {
            gateState.setText(R.string.offline);
        }
        int index = Integer.valueOf(info.getMode());
        spinner.setSelection(index - 1);
    }

    private void setWeatherCard(JSONObject object) {
        String temp = String.valueOf((int) object.optDouble("temperature")) + "°";
        String skycon = object.optString("skycon");
        String humidity = String.valueOf(object.optDouble("humidity") * 100) + "%";

        temperature.setText(temp);
        place.setText(humidity);
        /*
        Skycon 的取值包括
            CLEAR_DAY：晴天
            CLEAR_NIGHT：晴夜
            PARTLY_CLOUDY_DAY：多云
            PARTLY_CLOUDY_NIGHT：多云
            CLOUDY：阴
            RAIN： 雨
            SLEET：冻雨
            SNOW：雪
            WIND：风
            FOG：雾
            HAZE：霾
         */
        switch (skycon) {
            case "CLEAR_DAY":
                weatherState.setText("晴天");
                weatherPic.setImageResource(R.drawable.clear_day);
                break;
            case "CLEAR_NIGHT":
                weatherState.setText("晴夜");
                weatherPic.setImageResource(R.drawable.clear_night);
                break;
            case "PARTLY_CLOUDY_DAY":
                weatherState.setText("多云");
                weatherPic.setImageResource(R.drawable.partly_cloudy_day);
                break;
            case "PARTLY_CLOUDY_NIGHT":
                weatherState.setText("多云");
                weatherPic.setImageResource(R.drawable.partly_cloudy_night);
                break;
            case "CLOUDY":
                weatherState.setText("阴");
                weatherPic.setImageResource(R.drawable.cloudy);
                break;
            case "RAIN":
                weatherState.setText("雨");
                weatherPic.setImageResource(R.drawable.rain);
                break;
            case "SLEET":
                weatherState.setText("冻雨");
                weatherPic.setImageResource(R.drawable.sleet);
                break;
            case "SNOW":
                weatherState.setText("雪");
                weatherPic.setImageResource(R.drawable.snow);
                break;
            case "WIND":
                weatherState.setText("风");
                weatherPic.setImageResource(R.drawable.wind);
                break;
            case "FOG":
                weatherState.setText("雾");
                weatherPic.setImageResource(R.drawable.fog);
                break;
            case "HAZE":
                weatherState.setText("霾");
                weatherPic.setImageResource(R.drawable.fog);
                break;

        }
    }

}
