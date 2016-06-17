package com.jlshix.zigsys.frag;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.jlshix.zigsys.BaseFragment;
import com.jlshix.zigsys.R;
import com.jlshix.zigsys.adapter.PlugAdapter;
import com.jlshix.zigsys.data.PlugData;
import com.jlshix.zigsys.utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 2016/6/14.
 * 插座 Fragment
 */

//设定布局文件, 然而只有一个RecyclerView
@ContentView(R.layout.fragment_plug)
public class Plug extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "PLUG_FRAGMENT";
    @ViewInject(R.id.swipe)
    SwipeRefreshLayout swipe;

    @ViewInject(R.id.recycler)
    RecyclerView recycler;

    // 准备的数据
    private List<PlugData> list;

    // 准备的Adapter（重点）
    private PlugAdapter adapter;

    public static final int REFRESH = 0x01;
    public static final int CODE_ERR = 0x02;
    public static final int HTTP_ERR = 0x03;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH:
                    getData();
                    swipe.setRefreshing(false);
                    break;
            }
        }
    };


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipe.setOnRefreshListener(this);
        // 先准备数据再初始化界面？
        list = new ArrayList<>();
        list.add(new PlugData());
        initView();

    }

    private void initView() {
        adapter = new PlugAdapter(getContext(), handler, list);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);
        recycler.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyItemRangeChanged(0, list.size());
        handler.sendEmptyMessage(REFRESH);
    }

    /**
     * 获取数据并显示
     */
    private void getData() {
        list.clear();
        RequestParams params = new RequestParams(L.URL_GET + "?gate=4718&type=0A");
        x.http().get(params,
                new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject object = new JSONObject(result);
                            if (!object.optString("code").equals("1")) {
                                L.toast(getContext(), "PLUG_CODE_ERR");
                                return;
                            }
                            json2List(object.optJSONArray("info"));
                            adapter.notifyDataSetChanged();
                            adapter.notifyItemRangeChanged(0, list.size());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        
                    }

                    private void json2List(JSONArray info) {
                        list.clear();
                        Log.i(TAG, "json2List: " + info.length());
                        for (int i = 0; i < info.length(); i++) {
                            JSONObject object = info.optJSONObject(i);
                            String name = object.optString("name");
                            String state = object.optString("state");
                            list.add(new PlugData(name, state));
                            Log.i(TAG, "json2List: " + list.toString());
                        }
                        Log.i(TAG, "json2List: " + list.toString());
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        L.toast(getContext(), ex.getMessage());
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
    }



    @Override
    public void onRefresh() {
        swipe.setRefreshing(true);
        handler.sendEmptyMessage(REFRESH);
    }
}
