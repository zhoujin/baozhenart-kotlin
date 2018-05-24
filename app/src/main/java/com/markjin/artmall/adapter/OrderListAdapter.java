package com.markjin.artmall.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.markjin.artmall.Contants;
import com.markjin.artmall.GlideApp;
import com.markjin.artmall.R;
import com.markjin.artmall.db.bean.GoodsDetail;
import com.markjin.artmall.db.bean.OrderDetail;
import com.markjin.artmall.db.bean.OrderGoods;
import com.markjin.artmall.db.bean.OrderList;
import com.markjin.artmall.db.dao.BaseDaoFactory;
import com.markjin.artmall.db.dao.GoodsDetailDao;
import com.markjin.artmall.db.dao.OrderDetailDao;
import com.markjin.artmall.db.dao.OrderGoodsDao;
import com.markjin.artmall.ui.OrderDetailActivity;
import com.markjin.artmall.utils.ScreenUtils;
import com.markjin.artmall.utils.StringUtil;
import com.markjin.artmall.utils.ToastUtil;

import java.util.List;
import java.util.Random;

//import com.sina.weibo.sdk.utils.LogUtil;

/**
 */

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private List<OrderList> data;
    private Context context;
    private Handler handler;

    public OrderListAdapter(List<OrderList> data, Handler handler) {
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
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final OrderList list = data.get(position);
        holder.tv_order_sn.setText(context.getString(R.string.order_sn, list.order_sn));
        holder.tv_order_status.setText(list.order_status_tag);
        holder.tv_goods_name.setText(list.goods_name);
        holder.tv_brand_name.setText(list.brand_name);
        if (!StringUtil.isEmpty(list.goods_width) && !StringUtil.isEmpty(list.goods_height)) {
            holder.tv_goods_size.setVisibility(View.VISIBLE);
            holder.tv_goods_size.setText(context.getString(R.string.goods_size, list.goods_width, list.goods_height));
        } else {
            holder.tv_goods_size.setVisibility(View.GONE);
        }
        holder.itemView.setTag(R.id.tv_order_sn, list.order_id);
        holder.tv_order_price.setText(context.getString(R.string.rmb, list.order_price));
        GlideApp.with(context).load(list.img_url)
                .skipMemoryCache(false)
                .override(ScreenUtils.dip2px(60), ScreenUtils.dip2px(60))
                .fitCenter()
                .into(holder.iv_goods_order);
        if (list.order_status.equals("5") || list.order_status.equals("4")) {//已取消 | 已完成
            holder.rl_bottom.setVisibility(View.GONE);
        } else {
            holder.rl_bottom.setVisibility(View.VISIBLE);
            if (list.order_status.equals("1")) {//未付款
                holder.bt_right.setVisibility(View.VISIBLE);
                holder.bt_right.setText(R.string.order_cancel);
                holder.bt_middle.setVisibility(View.VISIBLE);
                holder.bt_middle.setText(R.string.order_pay_now);
                holder.bt_left.setVisibility(View.GONE);
            } else if (list.order_status.equals("2")) {//已付款，待发货
                holder.rl_bottom.setVisibility(View.GONE);
            } else if (list.order_status.equals("3")) {//已发货
                holder.bt_right.setVisibility(View.VISIBLE);
                holder.bt_right.setText(R.string.order_receive);
                if (!StringUtil.isEmpty(list.logistics_id)) {
                    holder.bt_middle.setVisibility(View.VISIBLE);
                    holder.bt_middle.setText(R.string.order_logistics);
                } else {
                    holder.bt_middle.setVisibility(View.GONE);
                }
                holder.bt_left.setVisibility(View.GONE);
            }
        }
        holder.bt_middle.setTag(list);
        holder.bt_middle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((Button) view).getText().toString().trim().equals(context.getString(R.string.order_pay_now))) {
                    Random a = new Random();
                    int pay_status = a.nextInt(10);
                    OrderDetail where = new OrderDetail();
                    where.order_id = ((OrderList) view.getTag()).order_id;
                    OrderDetail detail = new OrderDetail();
                    detail.order_id = ((OrderList) view.getTag()).order_id;
                    if (pay_status < Contants.Companion.getPAY_SUCCESS_LIST()) {
                        detail.order_status = "2";
                        detail.order_status_tag = context.getString(R.string.order_status_need_send);
                    } else {
                        ToastUtil.showMessage(context, R.string.pay_error);
                    }

                    OrderGoodsDao orderGoodsDao = BaseDaoFactory.getInstance().getDataHelper(OrderGoodsDao.class, OrderGoods.class);
                    OrderGoods whereOrderGoods = new OrderGoods();
                    whereOrderGoods.order_id = where.order_id;
                    List<OrderGoods> resultOrderGoods = orderGoodsDao.query(whereOrderGoods);
                    if (resultOrderGoods != null && resultOrderGoods.size() > 0) {
                        for (int i = 0; i < resultOrderGoods.size(); i++) {
                            GoodsDetail whereGoods = new GoodsDetail();
                            whereGoods.goods_id = resultOrderGoods.get(i).goods_id;
                            GoodsDetail goodsDetail = new GoodsDetail();
                            goodsDetail.goods_id = resultOrderGoods.get(i).goods_id;
                            goodsDetail.goods_status = "2";
                            goodsDetail.goods_status_tag = "已售";
                            GoodsDetailDao goodsDetailDao = BaseDaoFactory.getInstance().getDataHelper(GoodsDetailDao.class, GoodsDetail.class);
                            goodsDetailDao.update(goodsDetail, whereGoods);
                        }
                    }
                    OrderDetailDao detailDao = BaseDaoFactory.getInstance().getDataHelper(OrderDetailDao.class, OrderDetail.class);
                    int result = detailDao.update(detail, where);
                    if (result != -1) {
                        handler.sendEmptyMessage(-1);
                    }
                }
            }
        });
        holder.bt_right.setTag(list);
        holder.bt_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((Button) view).getText().toString().trim().equals(context.getString(R.string.order_cancel))) {
                    final OrderList list = ((OrderList) view.getTag());
                    new AlertDialog.Builder(context)
                            .setTitle(context.getString(R.string.dialog_hint))
                            .setMessage(R.string.sure_delete)
                            .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    OrderDetail where = new OrderDetail();
                                    where.order_id = list.order_id;
                                    OrderDetail detail = new OrderDetail();
                                    detail.order_id = list.order_id;
                                    detail.order_status = "5";
                                    detail.order_status_tag = context.getString(R.string.order_cancel);
                                    OrderDetailDao detailDao = BaseDaoFactory.getInstance().getDataHelper(OrderDetailDao.class, OrderDetail.class);
                                    detailDao.update(detail, where);
                                    OrderGoodsDao orderGoodsDao = BaseDaoFactory.getInstance().getDataHelper(OrderGoodsDao.class, OrderGoods.class);
                                    OrderGoods whereOrderGoods = new OrderGoods();
                                    whereOrderGoods.order_id = where.order_id;
                                    List<OrderGoods> resultOrderGoods = orderGoodsDao.query(whereOrderGoods);
                                    if (resultOrderGoods != null && resultOrderGoods.size() > 0) {
                                        for (int j = 0; j < resultOrderGoods.size(); j++) {
                                            GoodsDetail whereGoods = new GoodsDetail();
                                            whereGoods.goods_id = resultOrderGoods.get(i).goods_id;
                                            GoodsDetail goodsDetail = new GoodsDetail();
                                            goodsDetail.goods_id = resultOrderGoods.get(i).goods_id;
                                            goodsDetail.goods_status = "1";
                                            goodsDetail.goods_status_tag = "在售";
                                            GoodsDetailDao goodsDetailDao = BaseDaoFactory.getInstance().getDataHelper(GoodsDetailDao.class, GoodsDetail.class);
                                            goodsDetailDao.update(goodsDetail, whereGoods);
                                        }
                                    }
                                    dialogInterface.dismiss();
                                    handler.sendEmptyMessage(-1);
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_order_sn, tv_order_status, tv_goods_name, tv_brand_name, tv_goods_size, tv_order_price;
        private ImageView iv_goods_order;
        private RelativeLayout rl_bottom;
        private Button bt_left, bt_middle, bt_right;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_order_sn = itemView.findViewById(R.id.tv_order_sn);
            tv_order_status = itemView.findViewById(R.id.tv_order_status);
            tv_goods_name = itemView.findViewById(R.id.tv_goods_name);
            tv_brand_name = itemView.findViewById(R.id.tv_brand_name);
            tv_goods_size = itemView.findViewById(R.id.tv_goods_size);
            tv_order_price = itemView.findViewById(R.id.tv_order_price);
            iv_goods_order = itemView.findViewById(R.id.iv_goods_order);
            rl_bottom = itemView.findViewById(R.id.rl_bottom);
            bt_left = itemView.findViewById(R.id.bt_left);
            bt_middle = itemView.findViewById(R.id.bt_middle);
            bt_right = itemView.findViewById(R.id.bt_right);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String order_id = (String) view.getTag(R.id.tv_order_sn);
                    Intent intent = new Intent(view.getContext(), OrderDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("order_id", order_id);
                    intent.putExtras(bundle);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
