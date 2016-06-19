package com.jlshix.zigsys.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jlshix.zigsys.R;
import com.jlshix.zigsys.data.EnvirData;

import java.util.List;

/**
 * Created by Leo on 2016/6/16.
 * 环境Fragment的Adapter
 * 注意extend Adapter<>
 */
public class OthersAdapter extends RecyclerView.Adapter<OthersAdapter.EnvirViewHolder> {

    private static final String TAG = "ENVIR_ADAPTER";
    // 上下文 与 数据
    private Context context;
    private List<EnvirData> datas;

    // 构造函数
    public OthersAdapter(Context context, List<EnvirData> datas) {
        this.context = context;
        this.datas = datas;
    }


    /**
     * 内部类 ViewHolder
     * //TODO xUtils 关于ViewHolder的Inject
     */
    static class EnvirViewHolder extends RecyclerView.ViewHolder {

        TextView place;
        TextView temp;
        TextView humi;
        TextView light;

        public EnvirViewHolder(View itemView) {
            super(itemView);
            place = (TextView) itemView.findViewById(R.id.place);
            temp = (TextView) itemView.findViewById(R.id.temp);
            humi = (TextView) itemView.findViewById(R.id.humi);
            light = (TextView) itemView.findViewById(R.id.light);
        }
    }



    /**
     * 设定布局文件，还要ViewHolder一起
     * @param parent viewGroup
     * @param viewType type
     * @return viewHolder
     */
    @Override
    public EnvirViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.envir_list_item, parent, false);
        return new EnvirViewHolder(v);
    }

    /**
     * 绑定， 包括布局和自己实现的监听器
     * @param holder viewHolder
     * @param position 第几个
     */
    @Override
    public void onBindViewHolder(OthersAdapter.EnvirViewHolder holder, int position) {
        holder.place.setText(datas.get(position).getPlace());
        holder.temp.setText(datas.get(position).getTemp());
        holder.humi.setText(datas.get(position).getHumi());
        holder.light.setText(datas.get(position).getLight());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }



}
