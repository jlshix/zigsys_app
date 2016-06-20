package com.jlshix.zigsys.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.jlshix.zigsys.R;
import com.jlshix.zigsys.data.OthersData;
import com.jlshix.zigsys.frag.Others;
import com.jlshix.zigsys.utils.L;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by Leo on 2016/6/16.
 * 环境Fragment的Adapter
 * 注意extend Adapter<>
 */
public class OthersAdapter extends RecyclerView.Adapter<OthersAdapter.OthersViewHolder> {

    private static final String TAG = "OTHERS_ADAPTER";
    private Context context;
    private List<OthersData> datas;
    private Handler handler;

    public OthersAdapter(Context context, Handler handler,List<OthersData> list) {
        this.context = context;
        this.handler = handler;
        this.datas = list;
    }

    @Override
    public OthersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.others_list_item, parent, false);
        return new OthersViewHolder(v);


    }

    @Override
    public void onBindViewHolder(final OthersViewHolder holder, final int position) {
        final OthersData data = datas.get(position);
        holder.place.setText(data.getPlace());
        holder.imei.setText(data.getType() + "-" + data.getNo());
        if (data.getState().equals("1")) {
            holder.power.setChecked(true);
        }
        switch (data.getType()) {
            case "05":
                // 红外
                holder.img.setImageResource(R.drawable.ic_lightbulb_outline_blue_500_48dp);
                break;
            case "04":
                // 烟雾
                holder.img.setImageResource(R.drawable.ic_portable_wifi_off_blue_500_48dp);
                break;
            case "06":
                // 电机
                holder.img.setImageResource(R.drawable.ic_settings_input_hdmi_blue_500_48dp);
                break;
        }
        holder.power.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 推送
                String last = isChecked ? "01" : "00";
                String order = data.getType() + data.getNo() + last;
                L.send2Gate(L.getGateTag(), order);
                // 数据库
                uploadToServer(data.getType(), data.getNo(), last);
            }
        });

    }

    private void uploadToServer(String type, String no, String last) {
        RequestParams params = new RequestParams(L.URL_SET + "?gate=" + L.getGateImei() + "&type=" + type
                        + "&no=" + no + "&state=" + last.charAt(1));
        x.http().get(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.optString("code").equals("1")) {
                    handler.sendEmptyMessage(Others.REFRESH);
                } else {
                    handler.sendEmptyMessage(Others.CODE_ERR);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                handler.sendEmptyMessage(Others.HTTP_ERR);
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
    public int getItemCount() {
        return datas.size();
    }


    static class OthersViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView place;
        TextView imei;
        Switch power;

        public OthersViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.type_img);
            place = (TextView) itemView.findViewById(R.id.place);
            imei = (TextView) itemView.findViewById(R.id.imei);
            power = (Switch) itemView.findViewById(R.id.power);
        }
    }

}
