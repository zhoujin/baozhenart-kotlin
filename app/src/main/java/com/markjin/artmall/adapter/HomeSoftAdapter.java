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

import java.util.List;

/**
 *
 */

public class HomeSoftAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<HomeData.Data.Soft> data;

    public HomeSoftAdapter(Context context, List<HomeData.Data.Soft> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_type_four, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Holder) {
            final HomeData.Data.Soft soft = data.get(position);
            GlideApp.with(context).load(soft.style_image).skipMemoryCache(false)
                    .error(R.mipmap.ic_image_default)
                    .into(((Holder) holder).iv_home_top);
            ((Holder) holder).tv_title.setText(soft.style_name);
            if (soft.goods_list.size() > 0) {//没有list 所以不能用循环
                GlideApp.with(context).load(soft.goods_list.get(0).goods_image.img_url).skipMemoryCache(false)
                        .error(R.mipmap.ic_image_default)
                        .into(((Holder) holder).iv_home_left);
                if (soft.goods_list.get(0).goods_status.equals("1")) {
                    ((Holder) holder).tv_home_left.setText(context.getString(R.string.rmb, soft.goods_list.get(0).shop_price));
                } else {
                    ((Holder) holder).tv_home_left.setText(soft.goods_list.get(0).goods_status_tag);
                }
                ((Holder) holder).view_left_shadow.setVisibility(View.VISIBLE);
                ((Holder) holder).view_middle_shadow.setVisibility(View.INVISIBLE);
                ((Holder) holder).view_right_shadow.setVisibility(View.INVISIBLE);
                if (soft.goods_list.size() > 1) {
                    GlideApp.with(context).load(soft.goods_list.get(1).goods_image.img_url).skipMemoryCache(false)
                            .error(R.mipmap.ic_image_default)
                            .into(((Holder) holder).iv_home_middle);
                    if (soft.goods_list.get(1).goods_status.equals("1")) {
                        ((Holder) holder).tv_home_middle.setText(context.getString(R.string.rmb, soft.goods_list.get(1).shop_price));
                    } else {
                        ((Holder) holder).tv_home_middle.setText(soft.goods_list.get(1).goods_status_tag);
                    }
                    ((Holder) holder).view_left_shadow.setVisibility(View.VISIBLE);
                    ((Holder) holder).view_middle_shadow.setVisibility(View.VISIBLE);
                    ((Holder) holder).view_right_shadow.setVisibility(View.INVISIBLE);
                    if (soft.goods_list.size() > 2) {
                        GlideApp.with(context).load(soft.goods_list.get(2).goods_image.img_url).skipMemoryCache(false)
                                .error(R.mipmap.ic_image_default)
                                .into(((Holder) holder).iv_home_right);
                        if (soft.goods_list.get(2).goods_status.equals("1")) {
                            ((Holder) holder).tv_home_right.setText(context.getString(R.string.rmb, soft.goods_list.get(2).shop_price));
                        } else {
                            ((Holder) holder).tv_home_right.setText(soft.goods_list.get(2).goods_status_tag);
                        }
                        ((Holder) holder).view_left_shadow.setVisibility(View.VISIBLE);
                        ((Holder) holder).view_middle_shadow.setVisibility(View.VISIBLE);
                        ((Holder) holder).view_right_shadow.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    private class Holder extends RecyclerView.ViewHolder {
        private ImageView iv_home_top, iv_home_left, iv_home_middle, iv_home_right;
        private View view_left_shadow, view_middle_shadow, view_right_shadow;
        private TextView tv_title, tv_desc;
        private TextView tv_home_left, tv_home_middle, tv_home_right;

        public Holder(View itemView) {
            super(itemView);
            iv_home_top = itemView.findViewById(R.id.iv_home_top);
            iv_home_left = itemView.findViewById(R.id.iv_home_left);
            iv_home_middle = itemView.findViewById(R.id.iv_home_middle);
            iv_home_right = itemView.findViewById(R.id.iv_home_right);
            view_left_shadow = itemView.findViewById(R.id.view_left_shadow);
            view_middle_shadow = itemView.findViewById(R.id.view_middle_shadow);
            view_right_shadow = itemView.findViewById(R.id.view_right_shadow);
            itemView.findViewById(R.id.view_shadow).setLayoutParams(new FrameLayout.LayoutParams(ScreenUtils.getScreenWidth(context) - 160, (ScreenUtils.getScreenWidth(context) - 160) / 2));
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ScreenUtils.getScreenWidth(context) - 160, (ScreenUtils.getScreenWidth(context) - 160) / 2);
            iv_home_top.setLayoutParams(params);
            iv_home_top.setImageResource(R.mipmap.ic_image_default);
            iv_home_left.setImageResource(R.mipmap.ic_image_default);
            iv_home_middle.setImageResource(R.mipmap.ic_image_default);
            iv_home_right.setImageResource(R.mipmap.ic_image_default);
            int size = (ScreenUtils.getScreenWidth(context) - 160 - ScreenUtils.dip2px(20)) / 3;
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(size, size);
            iv_home_left.setLayoutParams(layoutParams);
            iv_home_right.setLayoutParams(layoutParams);
            iv_home_middle.setLayoutParams(layoutParams);
            iv_home_left.setScaleType(ImageView.ScaleType.FIT_XY);
            iv_home_right.setScaleType(ImageView.ScaleType.FIT_XY);
            iv_home_middle.setScaleType(ImageView.ScaleType.FIT_XY);
            view_left_shadow.setLayoutParams(layoutParams);
            view_middle_shadow.setLayoutParams(layoutParams);
            view_right_shadow.setLayoutParams(layoutParams);
            tv_home_left = itemView.findViewById(R.id.tv_home_left);
            tv_home_middle = itemView.findViewById(R.id.tv_home_middle);
            tv_home_right = itemView.findViewById(R.id.tv_home_right);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_desc.setVisibility(View.GONE);
        }
    }
}
