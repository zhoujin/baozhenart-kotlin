package com.markjin.artmall.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.markjin.artmall.GlideApp;
import com.markjin.artmall.R;
import com.markjin.artmall.db.bean.GoodsCart;
import com.markjin.artmall.ui.GoodsDetailActivity;
import com.markjin.artmall.utils.ScreenUtils;
import com.markjin.artmall.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.markjin.artmall.ui.CartActivity.MSG_ALL;
import static com.markjin.artmall.ui.CartActivity.MSG_CLEAR;
import static com.markjin.artmall.ui.CartActivity.MSG_HAVE;

/**
 *
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Holder> {
    private List<GoodsCart> data;
    private Context context;
    private Map<String, Integer> checkMap = new HashMap<>();

    private boolean is_delete = false;
    private Handler handler;

    public CartAdapter(List<GoodsCart> data, Handler handler) {
        this.data = data;
        this.handler = handler;
    }

    public void setDelete(boolean is_delete) {
        this.is_delete = is_delete;
    }

    public void setCheckAll(boolean delete) {
        for (int i = 0; i < data.size(); i++) {
            if (delete) {
                checkMap.put(data.get(i).goods_id, 1);
            } else {
                if (data.get(i).goods_status.equals("1")) {
                    checkMap.put(data.get(i).goods_id, 1);
                }
            }
        }
        notifyDataSetChanged();
    }

    public int getAllSize(boolean delete) {
        int i = 0;
        for (int j = 0; j < data.size(); j++) {
            if (delete) {
                i++;
            } else {
                if (data.get(j).goods_status.equals("1")) {
                    i++;
                }
            }
        }
        return i;
    }

    public int getCheckSize() {
        return checkMap.size();
    }

    public int getCheckPrice() {
        int total_price = 0;
        for (Map.Entry<String, Integer> entry : checkMap.entrySet()) {
            for (int i = 0; i < data.size(); i++) {
                if (entry.getKey().equals(data.get(i).goods_id)) {
                    total_price += Float.valueOf(data.get(i).goods_price) * entry.getValue();
                }
            }
        }
        return total_price;
    }

    public void clear() {
        checkMap.clear();
        notifyDataSetChanged();
    }

    public ArrayList<String> getSelectIds() {
        ArrayList<String> select = new ArrayList<>();
        if (checkMap.size() > 0) {
            for (Map.Entry<String, Integer> entry : checkMap.entrySet()) {
                select.add(entry.getKey());
            }
        }
        return select;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_goods_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        GoodsCart cart = data.get(position);
        Log.e("=====","onBindViewHolder=="+cart.img_url);
        GlideApp.with(context).load(cart.img_url).override(ScreenUtils.dip2px(80), ScreenUtils.dip2px(80))
                .skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.mipmap.ic_image_default)
                .fitCenter().into(holder.iv_goods_image);
        holder.tv_goods_name.setText(cart.goods_name);
        holder.tv_goods_price.setText(context.getString(R.string.rmb, cart.goods_price));
        if (!StringUtil.isEmpty(cart.artist_name)) {
            holder.tv_artist_name.setVisibility(View.VISIBLE);
            holder.tv_artist_name.setText(cart.artist_name);
        } else {
            holder.tv_artist_name.setVisibility(View.GONE);
        }
        if (!StringUtil.isEmpty(cart.cat_name) && !StringUtil.isEmpty(cart.brand_name)) {
            holder.tv_cat_brand.setVisibility(View.VISIBLE);
            holder.tv_cat_brand.setText(cart.cat_name + "/" + cart.brand_name);
        } else if (!StringUtil.isEmpty(cart.cat_name)) {
            holder.tv_cat_brand.setVisibility(View.VISIBLE);
            holder.tv_cat_brand.setText(cart.cat_name);
        } else if (!StringUtil.isEmpty(cart.brand_name)) {
            holder.tv_cat_brand.setVisibility(View.VISIBLE);
            holder.tv_cat_brand.setText(cart.brand_name);
        } else {
            holder.tv_cat_brand.setVisibility(View.GONE);
        }
        if (!StringUtil.isEmpty(cart.goods_width) && !StringUtil.isEmpty(cart.goods_height)) {
            holder.tv_goods_size.setVisibility(View.VISIBLE);
            holder.tv_goods_size.setText(context.getString(R.string.goods_size, cart.goods_width, cart.goods_height));
        } else {
            holder.tv_goods_size.setVisibility(View.GONE);
        }

        if (cart.cart_type.equals("1")) {//购买
            if (checkMap.containsKey(cart.goods_id)) {
                holder.iv_check.setImageResource(R.mipmap.ic_cb_select);
            } else {
                holder.iv_check.setImageResource(R.mipmap.ic_cb_default);
            }
            if (cart.goods_status.equals("1")) {//可以购买
                holder.iv_check.setVisibility(View.VISIBLE);
                holder.tv_goods_name.setTextColor(ContextCompat.getColor(context, R.color.color_22));
                holder.tv_goods_price.setTextColor(ContextCompat.getColor(context, R.color.color_66));
                holder.tv_artist_name.setTextColor(ContextCompat.getColor(context, R.color.color_66));
                holder.tv_cat_brand.setTextColor(ContextCompat.getColor(context, R.color.color_66));
                holder.tv_goods_size.setTextColor(ContextCompat.getColor(context, R.color.color_66));
                holder.tv_goods_price_bottom.setTextColor(ContextCompat.getColor(context, R.color.color_66));
            } else {
                if (is_delete) {
                    holder.iv_check.setVisibility(View.VISIBLE);
                } else {
                    holder.iv_check.setVisibility(View.INVISIBLE);
                }
                holder.tv_goods_name.setTextColor(ContextCompat.getColor(context, R.color.color_99));
                holder.tv_goods_price.setTextColor(ContextCompat.getColor(context, R.color.color_99));
                holder.tv_artist_name.setTextColor(ContextCompat.getColor(context, R.color.color_99));
                holder.tv_cat_brand.setTextColor(ContextCompat.getColor(context, R.color.color_99));
                holder.tv_goods_size.setTextColor(ContextCompat.getColor(context, R.color.color_99));
                holder.tv_goods_price_bottom.setTextColor(ContextCompat.getColor(context, R.color.color_99));
            }

            holder.iv_check.setTag(cart.goods_id);
            holder.iv_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkMap.containsKey(view.getTag().toString())) {
                        checkMap.remove(view.getTag().toString());
                        ((ImageView) view).setImageResource(R.mipmap.ic_cb_default);
                        if (checkMap.size() == 0) {
                            Message msg = handler.obtainMessage();
                            msg.what = MSG_CLEAR;
                            handler.sendMessage(msg);
                        } else {
                            Message msg = handler.obtainMessage();
                            msg.what = MSG_HAVE;
                            handler.sendMessage(msg);
                        }
                    } else {
                        checkMap.put(view.getTag().toString(), 1);
                        ((ImageView) view).setImageResource(R.mipmap.ic_cb_select);
                        if (checkMap.size() == getAllSize(is_delete)) {
                            Message msg = handler.obtainMessage();
                            msg.what = MSG_ALL;
                            handler.sendMessage(msg);
                        } else {
                            Message msg = handler.obtainMessage();
                            msg.what = MSG_HAVE;
                            handler.sendMessage(msg);
                        }
                    }
                }
            });
            holder.iv_goods_image.setTag(R.id.iv_goods_image, cart.goods_id);
            holder.iv_goods_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, GoodsDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("goods_id", view.getTag(R.id.iv_goods_image).toString());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        } else if (cart.cart_type.equals("2")) {//租赁

        }
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView iv_check, iv_goods_image;
        private TextView tv_goods_name, tv_goods_price, tv_artist_name, tv_cat_brand, tv_goods_size, tv_goods_price_bottom;

        public Holder(View itemView) {
            super(itemView);
            iv_check = itemView.findViewById(R.id.iv_check);
            iv_goods_image = itemView.findViewById(R.id.iv_goods_image);
            tv_goods_name = itemView.findViewById(R.id.tv_goods_name);
            tv_goods_price = itemView.findViewById(R.id.tv_goods_price);
            tv_artist_name = itemView.findViewById(R.id.tv_artist_name);
            tv_cat_brand = itemView.findViewById(R.id.tv_cat_brand);
            tv_goods_size = itemView.findViewById(R.id.tv_goods_size);
            tv_goods_price_bottom = itemView.findViewById(R.id.tv_goods_price_bottom);
        }
    }
}
