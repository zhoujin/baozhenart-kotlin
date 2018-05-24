package com.markjin.artmall.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.markjin.artmall.GlideApp;
import com.markjin.artmall.R;
import com.markjin.artmall.db.bean.HomeData;
import com.markjin.artmall.utils.ScreenUtils;

import java.util.List;


/**
 *
 */

public class HomeHotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<HomeData.Data.HotLabel> data;

    public HomeHotAdapter(Context context, List<HomeData.Data.HotLabel> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_hot, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Holder) {
            final HomeData.Data.HotLabel label = data.get(position);
            GlideApp.with(context).load(label.label_image).skipMemoryCache(false).error(R.mipmap.ic_image_default).into(((Holder) holder).iv_hot);
            ((Holder) holder).tv_title.setText(label.label_name);
        }
    }

    private class Holder extends RecyclerView.ViewHolder {

        private ImageView iv_hot;
        private TextView tv_title;

        public Holder(View itemView) {
            super(itemView);
            iv_hot =  itemView.findViewById(R.id.iv_hot);
            tv_title = itemView.findViewById(R.id.tv_title);
            int size = (ScreenUtils.getScreenWidth(context) - 160 - ScreenUtils.dip2px(20)) / 3;
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size, size);
            iv_hot.setLayoutParams(params);
            itemView.findViewById(R.id.view_shadow).setLayoutParams(params);
        }
    }
}
