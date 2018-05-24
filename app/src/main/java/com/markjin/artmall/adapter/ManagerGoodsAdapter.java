package com.markjin.artmall.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.markjin.artmall.GlideApp;
import com.markjin.artmall.R;
import com.markjin.artmall.db.bean.NetGoodsList;
import com.markjin.artmall.utils.ScreenUtils;

import java.util.List;

import static com.markjin.artmall.ui.ManagerGoodsActivity.MSG_ADD_EXHIBITION;
import static com.markjin.artmall.ui.ManagerGoodsActivity.MSG_ADD_GOOD;
import static com.markjin.artmall.ui.ManagerGoodsActivity.MSG_REDUCE_EXHIBITION;
import static com.markjin.artmall.ui.ManagerGoodsActivity.MSG_REDUCE_GOOD;

/**
 *
 */

public class ManagerGoodsAdapter extends RecyclerView.Adapter<ManagerGoodsAdapter.ViewHolder> {

    private List<NetGoodsList.Goods> data;
    private Context context;
    private Handler handler;

    public ManagerGoodsAdapter(List<NetGoodsList.Goods> data, Handler handler) {
        this.data = data;
        this.handler = handler;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_goods_net, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NetGoodsList.Goods goods = data.get(position);
        GlideApp.with(context)
                .load(goods.goods_image.img_url)
                .centerCrop()
                .override(ScreenUtils.getScreenWidth(context) / 2, ScreenUtils.getScreenWidth(context) / 2)
                .skipMemoryCache(false)
                .into(holder.iv_goods_net);
        holder.iv_add_goods.setTag(goods);
        holder.iv_add_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetGoodsList.Goods goods = (NetGoodsList.Goods) view.getTag();
                Message msg = handler.obtainMessage();
                msg.what = MSG_ADD_GOOD;
                msg.obj = goods;
                handler.sendMessage(msg);
            }
        });
        holder.iv_reduce_goods.setTag(goods);
        holder.iv_reduce_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetGoodsList.Goods goods = (NetGoodsList.Goods) view.getTag();
                Message msg = handler.obtainMessage();
                msg.what = MSG_REDUCE_GOOD;
                msg.obj = goods;
                handler.sendMessage(msg);
            }
        });
        holder.iv_add_exhibition.setTag(goods);
        holder.iv_add_exhibition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetGoodsList.Goods goods = (NetGoodsList.Goods) view.getTag();
                Message msg = handler.obtainMessage();
                msg.what = MSG_ADD_EXHIBITION;
                msg.obj = goods;
                handler.sendMessage(msg);
            }
        });
        holder.iv_reduce_exhibition.setTag(goods);
        holder.iv_reduce_exhibition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetGoodsList.Goods goods = (NetGoodsList.Goods) view.getTag();
                Message msg = handler.obtainMessage();
                msg.what = MSG_REDUCE_EXHIBITION;
                msg.obj = goods;
                handler.sendMessage(msg);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_goods_net;
        private ImageView iv_add_goods, iv_reduce_goods, iv_add_exhibition, iv_reduce_exhibition;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_goods_net = itemView.findViewById(R.id.iv_goods_net);

            iv_add_goods = itemView.findViewById(R.id.iv_add_goods);
            iv_reduce_goods = itemView.findViewById(R.id.iv_reduce_goods);
            iv_add_exhibition = itemView.findViewById(R.id.iv_add_exhibition);
            iv_reduce_exhibition = itemView.findViewById(R.id.iv_reduce_exhibition);
        }
    }
}
