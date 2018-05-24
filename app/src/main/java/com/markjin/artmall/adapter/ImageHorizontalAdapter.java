package com.markjin.artmall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.markjin.artmall.GlideApp;
import com.markjin.artmall.R;
import com.markjin.artmall.db.bean.GoodsCart;
import com.markjin.artmall.db.bean.GoodsDetail;
import com.markjin.artmall.utils.ScreenUtils;

import java.util.List;

/**
 * image adapter
 */

public class ImageHorizontalAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<T> data;
    private Context context;

    public ImageHorizontalAdapter(List<T> data) {
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ScreenUtils.dip2px(80), ScreenUtils.dip2px(80)));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new Holder(imageView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (data.get(position) instanceof GoodsCart) {
            GoodsCart goods = (GoodsCart) data.get(position);
            GlideApp.with(context).load(goods.img_url)
                    .override(ScreenUtils.dip2px(80), ScreenUtils.dip2px(80))
                    .error(R.mipmap.ic_image_default)
                    .into((ImageView) holder.itemView);
        } else if (data.get(position) instanceof GoodsDetail) {
            GoodsDetail goods = (GoodsDetail) data.get(position);
            GlideApp.with(context).load(goods.img_url)
                    .override(ScreenUtils.dip2px(80), ScreenUtils.dip2px(80))
                    .error(R.mipmap.ic_image_default)
                    .into((ImageView) holder.itemView);
        }
    }

    private class Holder extends RecyclerView.ViewHolder {
        public Holder(View itemView) {
            super(itemView);
        }
    }
}
