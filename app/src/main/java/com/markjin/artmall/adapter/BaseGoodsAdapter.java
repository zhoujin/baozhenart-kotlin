package com.markjin.artmall.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.markjin.artmall.GlideApp;
import com.markjin.artmall.R;
import com.markjin.artmall.db.bean.Goods;
import com.markjin.artmall.db.bean.GoodsCollect;
import com.markjin.artmall.db.bean.GoodsHistory;
import com.markjin.artmall.db.bean.OrderGoods;
import com.markjin.artmall.ui.GoodsDetailActivity;
import com.markjin.artmall.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class BaseGoodsAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<T> data;
    private Context context;
    private boolean is_have_delete = false;
    private ArrayList<String> check_ids = new ArrayList<>();

    public BaseGoodsAdapter(List<T> data) {
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods_list, parent, false));
    }

    public void setHaveDelete(boolean is_have_delete) {
        this.is_have_delete = is_have_delete;
    }

    public void clearSelect() {
        check_ids.clear();
    }

    public ArrayList<String> getCheckIds() {
        return check_ids;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof Holder) {
            if (data.get(position) instanceof Goods) {
                Goods goods = (Goods) data.get(position);
                GlideApp.with(context).load(goods.img_url).override(ScreenUtils.getScreenWidth(context), ScreenUtils.getScreenWidth(context))
                        .error(R.mipmap.ic_image_default).centerCrop().into(((Holder) holder).iv_goods);
                ((Holder) holder).tv_goods_name.setText(goods.goods_name);
                if (goods.goods_status.equals("1")) {
                    ((Holder) holder).tv_goods_price.setText(context.getString(R.string.rmb, goods.goods_price));
                } else {
                    ((Holder) holder).tv_goods_price.setText(goods.goods_status_tag);
                }
                if (TextUtils.isEmpty(goods.artist_name)) {
                    ((Holder) holder).tv_artist_size.setText(context.getString(R.string.goods_size, goods.goods_width, goods.goods_height));
                } else {
                    ((Holder) holder).tv_artist_size.setText(context.getString(R.string.goods_artist_size, goods.artist_name, goods.goods_width, goods.goods_height));
                }
                holder.itemView.setTag(goods.goods_id);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, GoodsDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("goods_id", String.valueOf(view.getTag()));
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
            } else if (data.get(position) instanceof GoodsCollect) {
                GoodsCollect goods = (GoodsCollect) data.get(position);
                GlideApp.with(context).load(goods.img_url).override(ScreenUtils.getScreenWidth(context), ScreenUtils.getScreenWidth(context))
                        .error(R.mipmap.ic_image_default).centerCrop().into(((Holder) holder).iv_goods);
                ((Holder) holder).tv_goods_name.setText(goods.goods_name);
                if (goods.goods_status.equals("1")) {
                    ((Holder) holder).tv_goods_price.setText(context.getString(R.string.rmb, goods.goods_price));
                } else {
                    ((Holder) holder).tv_goods_price.setText(goods.goods_status_tag);
                }
                if (TextUtils.isEmpty(goods.artist_name)) {
                    ((Holder) holder).tv_artist_size.setText(context.getString(R.string.goods_size, goods.goods_width, goods.goods_height));
                } else {
                    ((Holder) holder).tv_artist_size.setText(context.getString(R.string.goods_artist_size, goods.artist_name, goods.goods_width, goods.goods_height));
                }
                if (is_have_delete) {
                    ((Holder) holder).cb_check.setVisibility(View.VISIBLE);
                } else {
                    ((Holder) holder).cb_check.setVisibility(View.GONE);
                }
                ((Holder) holder).cb_check.setTag(goods.goods_id);
                ((Holder) holder).cb_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            check_ids.add(compoundButton.getTag().toString());
                        } else {
                            check_ids.remove(compoundButton.getTag().toString());
                        }
                    }
                });
                holder.itemView.setTag(goods.goods_id);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, GoodsDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("goods_id", String.valueOf(view.getTag()));
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
            } else if (data.get(position) instanceof GoodsHistory) {
                GoodsHistory goods = (GoodsHistory) data.get(position);
                GlideApp.with(context).load(goods.img_url).override(ScreenUtils.getScreenWidth(context), ScreenUtils.getScreenWidth(context))
                        .error(R.mipmap.ic_image_default).centerCrop().into(((Holder) holder).iv_goods);
                ((Holder) holder).tv_goods_name.setText(goods.goods_name);
                if (goods.goods_status.equals("1")) {
                    ((Holder) holder).tv_goods_price.setText(context.getString(R.string.rmb, goods.goods_price));
                } else {
                    ((Holder) holder).tv_goods_price.setText(goods.goods_status_tag);
                }
                if (TextUtils.isEmpty(goods.artist_name)) {
                    ((Holder) holder).tv_artist_size.setText(context.getString(R.string.goods_size, goods.goods_width, goods.goods_height));
                } else {
                    ((Holder) holder).tv_artist_size.setText(context.getString(R.string.goods_artist_size, goods.artist_name, goods.goods_width, goods.goods_height));
                }
                if (is_have_delete) {
                    ((Holder) holder).cb_check.setVisibility(View.VISIBLE);
                } else {
                    ((Holder) holder).cb_check.setVisibility(View.GONE);
                }
                ((Holder) holder).cb_check.setTag(goods.goods_id);
                ((Holder) holder).cb_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            check_ids.add(compoundButton.getTag().toString());
                        } else {
                            check_ids.remove(compoundButton.getTag().toString());
                        }
                    }
                });
                holder.itemView.setTag(goods.goods_id);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, GoodsDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("goods_id", String.valueOf(view.getTag()));
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
            }else if(data.get(position) instanceof OrderGoods){
                OrderGoods goods = (OrderGoods) data.get(position);
                GlideApp.with(context).load(goods.img_url).override(ScreenUtils.getScreenWidth(context), ScreenUtils.getScreenWidth(context))
                        .error(R.mipmap.ic_image_default).fitCenter().into(((Holder) holder).iv_goods);
                ((Holder) holder).tv_goods_name.setText(goods.goods_name);
                ((Holder) holder).tv_goods_price.setText(context.getString(R.string.rmb, goods.goods_price));
//                if (goods.goods_status.equals("1")) {
//                    ((Holder) holder).tv_goods_price.setText(context.getString(R.string.rmb, goods.goods_price));
//                } else {
//                    ((Holder) holder).tv_goods_price.setText(goods.goods_status_tag);
//                }
                if (TextUtils.isEmpty(goods.artist_name)) {
                    ((Holder) holder).tv_artist_size.setText(context.getString(R.string.goods_size, goods.goods_width, goods.goods_height));
                } else {
                    ((Holder) holder).tv_artist_size.setText(context.getString(R.string.goods_artist_size, goods.artist_name, goods.goods_width, goods.goods_height));
                }
                holder.itemView.setTag(goods.goods_id);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, GoodsDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("goods_id", String.valueOf(view.getTag()));
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
            }
        }

    }

    public class Holder<T> extends RecyclerView.ViewHolder {
        private ImageView iv_goods;
        private TextView tv_goods_name, tv_artist_size, tv_goods_price;
        private CheckBox cb_check;

        public Holder(View itemView) {
            super(itemView);
            iv_goods = itemView.findViewById(R.id.iv_goods);
            tv_goods_name = itemView.findViewById(R.id.tv_goods_name);
            tv_artist_size = itemView.findViewById(R.id.tv_artist_size);
            tv_goods_price = itemView.findViewById(R.id.tv_goods_price);
            cb_check = itemView.findViewById(R.id.cb_check);
        }
    }
}
