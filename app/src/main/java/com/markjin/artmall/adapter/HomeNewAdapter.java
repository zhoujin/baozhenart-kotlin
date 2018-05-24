package com.markjin.artmall.adapter;

import android.content.Context;
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
import com.markjin.artmall.utils.StringUtil;

import java.util.List;

/**
 *
 */

public class HomeNewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<HomeData.Data.NewGoods> data;

    public HomeNewAdapter(Context context, List<HomeData.Data.NewGoods> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_new, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Holder) {
            final HomeData.Data.NewGoods newGoods = data.get(position);
            GlideApp.with(context).load(newGoods.goods_image.img_url)
                    .skipMemoryCache(false)
                    .error(R.mipmap.ic_image_default)
                    .into(((Holder) holder).iv_new);
            if (!StringUtil.isEmpty(newGoods.goods_status)) {
                if (newGoods.goods_status.equals("1")) {
                    ((Holder) holder).tv_price.setText(context.getString(R.string.rmb, newGoods.shop_price));
                } else {
                    ((Holder) holder).tv_price.setText(newGoods.goods_status_tag);
                }
            } else {
                ((Holder) holder).tv_price.setText(context.getString(R.string.rmb, newGoods.shop_price));
            }
        }
    }

    private class Holder extends RecyclerView.ViewHolder {

        private ImageView iv_new;
        private TextView tv_price;

        public Holder(View itemView) {
            super(itemView);
            iv_new = itemView.findViewById(R.id.iv_new);
            tv_price = itemView.findViewById(R.id.tv_price);
            int size = (ScreenUtils.getScreenWidth(context) - 160 - ScreenUtils.dip2px(20)) / 3;
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size, size);
            iv_new.setLayoutParams(params);
            itemView.findViewById(R.id.view_shadow).setLayoutParams(params);
        }
    }
}
