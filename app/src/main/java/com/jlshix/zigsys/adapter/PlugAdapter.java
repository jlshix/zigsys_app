package com.jlshix.zigsys.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.jlshix.zigsys.R;
import com.jlshix.zigsys.data.PlugData;
import com.jlshix.zigsys.frag.Light;
import com.jlshix.zigsys.frag.Plug;
import com.jlshix.zigsys.utils.L;


import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by Leo on 2016/6/16.
 * 插座Fragment的Adapter
 * 注意extend Adapter<>
 */
public class PlugAdapter extends RecyclerView.Adapter<PlugAdapter.PlugViewHolder> {

    private static final String TAG = "PLUG_ADAPTER";
    // 上下文 与 数据
    private Context context;
    private Handler handler;
    private List<PlugData> datas;

    // 构造函数
    public PlugAdapter(Context context, Handler handler, List<PlugData> datas) {
        this.context = context;
        this.handler = handler;
        this.datas = datas;
    }


    /**
     * 内部类 ViewHolder
     * //TODO xUtils 关于ViewHolder的Inject
     */
    static class PlugViewHolder extends RecyclerView.ViewHolder {

        Switch[] mSwitch;
        TextView place;

        public PlugViewHolder(View itemView) {
            super(itemView);

            place = (TextView) itemView.findViewById(R.id.place);
            mSwitch = new Switch[4];
            mSwitch[0] = (Switch) itemView.findViewById(R.id.s0);
            mSwitch[1] = (Switch) itemView.findViewById(R.id.s1);
            mSwitch[2] = (Switch) itemView.findViewById(R.id.s2);
            mSwitch[3] = (Switch) itemView.findViewById(R.id.s3);
        }
    }



    /**
     * 设定布局文件，还要ViewHolder一起
     * @param parent viewGroup
     * @param viewType type
     * @return viewHolder
     */
    @Override
    public PlugViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.plug_list_item, parent, false);
        return new PlugViewHolder(v);
    }

    /**
     * 绑定， 包括布局和自己实现的监听器
     * @param holder viewHolder
     * @param position 第几个
     */
    @Override
    public void onBindViewHolder(final PlugAdapter.PlugViewHolder holder, int position) {
        for (int i = 0; i < holder.mSwitch.length; i++) {
            holder.mSwitch[i].setChecked(datas.get(position).getState(i));
            holder.place.setText(datas.get(position).getName());
            final int finalI = i;
            final int finalPosition = position;
            holder.mSwitch[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    L.toast(context, "mSwitch " + finalI + " " + finalPosition + isChecked);
                    Log.e(TAG, "onCheckedChanged: "+"mSwitch " + finalI + " " + finalPosition +" " + isChecked);
                    String state = "0000";
                    boolean[] tmp = new boolean[4];
                    for (int i = 0; i < holder.mSwitch.length; i++) {
                        tmp[i] = holder.mSwitch[i].isChecked();
                    }
                    state = bool2String(tmp);
                    // TODO 十个以内
                    String no = "0"+(finalPosition+1);
                    upload2Server(no, state);
                }
            });
        }

    }

    private void upload2Server(String no, String state) {
        String URL = L.URL_SET + "?gate=4718&type=0A&no=" + no + "&state=" + state;
        Log.e(TAG, "uploadToServer: " + URL);
        RequestParams params = new RequestParams(URL);
        x.http().get(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.optString("code").equals("1")) {
                    handler.sendEmptyMessage(Plug.REFRESH);
                } else {
                    handler.sendEmptyMessage(Plug.CODE_ERR);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                handler.sendEmptyMessage(Light.HTTP_ERR);
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
     * 将按钮的布尔值转化为字符串0101
     * @param tmp bool[]
     * @return String
     */
    private static String bool2String(boolean[] tmp) {
        StringBuilder sb = new StringBuilder();
        for (boolean aTmp : tmp) {
            if (aTmp) {
                sb.append("1");
            } else {
                sb.append("0");
            }
        }
        return sb.toString();
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }





}
