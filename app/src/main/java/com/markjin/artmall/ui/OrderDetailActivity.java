package com.markjin.artmall.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.markjin.artmall.BaseActivity;
import com.markjin.artmall.R;
import com.markjin.artmall.adapter.BaseGoodsAdapter;
import com.markjin.artmall.db.bean.OrderDetail;
import com.markjin.artmall.db.bean.OrderGoods;
import com.markjin.artmall.db.dao.BaseDaoFactory;
import com.markjin.artmall.db.dao.OrderDetailDao;
import com.markjin.artmall.db.dao.OrderGoodsDao;
import com.markjin.artmall.utils.StringUtil;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * bundle(order_id)
 */

public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_order_status, tv_order_sn, tv_order_create_time, tv_order_pay_time, tv_order_finish_time;
    private LinearLayout ll_order_send_type, ll_logistics;
    private TextView tv_order_address_people, tv_order_address_phone, tv_order_address_detail;
    private ImageView iv_goods_image;
    private TextView tv_order_send_type, tv_pay_type, tv_invoice_title_content;
    private TextView tv_order_goods_price, tv_order_logistics_price;
    private RelativeLayout rl_bottom;
    private TextView tv_order_need_pay;
    private OrderDetail detail;
    private RecyclerView rv_goods;
    private List<OrderGoods> data = new ArrayList<>();
    private BaseGoodsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initView();
    }

    private void initView() {
        findViewById(R.id.iv_left).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText(R.string.order_detail);
        tv_order_status = findViewById(R.id.tv_order_status);
        tv_order_sn = findViewById(R.id.tv_order_sn);
        tv_order_create_time = findViewById(R.id.tv_order_create_time);
        tv_order_pay_time = findViewById(R.id.tv_order_pay_time);
        tv_order_finish_time = findViewById(R.id.tv_order_finish_time);
        ll_order_send_type = findViewById(R.id.ll_order_send_type);
        ll_logistics = findViewById(R.id.ll_logistics);
        tv_order_address_people = findViewById(R.id.tv_order_address_people);
        tv_order_address_phone = findViewById(R.id.tv_order_address_phone);
        tv_order_address_detail = findViewById(R.id.tv_order_address_detail);

        rv_goods = findViewById(R.id.rv_goods);
        rv_goods.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new BaseGoodsAdapter(data);
        rv_goods.setAdapter(adapter);

        iv_goods_image = findViewById(R.id.iv_goods_image);

        tv_order_send_type = findViewById(R.id.tv_order_send_type);
        tv_pay_type = findViewById(R.id.tv_pay_type);
        tv_invoice_title_content = findViewById(R.id.tv_invoice_title_content);
        tv_order_goods_price = findViewById(R.id.tv_order_goods_price);
        tv_order_logistics_price = findViewById(R.id.tv_order_logistics_price);
        rl_bottom = findViewById(R.id.rl_bottom);
        tv_order_need_pay = findViewById(R.id.tv_order_need_pay);
        findViewById(R.id.tv_order_pay_now).setOnClickListener(this);
        initData();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("order_id")) {
            OrderDetail where = new OrderDetail();
            where.order_id = bundle.getString("order_id");
            List<OrderDetail> result = (List<OrderDetail>) BaseDaoFactory.getInstance().getDataHelper(OrderDetailDao.class, OrderDetail.class).query(where);
            if (result != null && result.size() != 0) {
                detail = result.get(0);
                tv_order_status.setText(detail.order_status_tag);
                tv_order_sn.setText(getString(R.string.order_sn, detail.order_sn));
                tv_order_create_time.setText(getString(R.string.order_create_time, StringUtil.timeStamp2Date(detail.add_time)));
                if (StringUtil.isEmpty(detail.pay_time)) {
                    tv_order_pay_time.setVisibility(View.GONE);
                } else {
                    tv_order_pay_time.setVisibility(View.VISIBLE);
                    tv_order_pay_time.setText(getString(R.string.order_pay_time, StringUtil.timeStamp2Date(detail.pay_time)));
                }
                if (StringUtil.isEmpty(detail.receive_time)) {
                    tv_order_finish_time.setVisibility(View.GONE);
                } else {
                    tv_order_finish_time.setVisibility(View.VISIBLE);
                    tv_order_finish_time.setText(getString(R.string.order_finish_time, StringUtil.timeStamp2Date(detail.receive_time)));
                }
                if (!StringUtil.isEmpty(detail.logistics_id) && !detail.logistics_id.equals("-1")) {
                    ll_logistics.setVisibility(View.VISIBLE);
                } else {
                    ll_logistics.setVisibility(View.GONE);
                }

                tv_order_address_people.setText(detail.address_name);
                tv_order_address_phone.setText(detail.address_phone);
                tv_order_address_detail.setText(detail.address_detail_all);

                if (!StringUtil.isEmpty(detail.delivery_type) && !detail.delivery_type.equals("-1")) {
                    ll_order_send_type.setVisibility(View.VISIBLE);
                    tv_order_send_type.setVisibility(View.VISIBLE);
                    if (detail.delivery_type.equals("1")) {
                        tv_order_send_type.setText(R.string.send_type1);
                    } else if (detail.delivery_type.equals("2")) {
                        tv_order_send_type.setText(R.string.send_type2);
                    }
                } else {
                    ll_order_send_type.setVisibility(View.GONE);
                }

                tv_pay_type.setText(detail.pay_name);
                if (!StringUtil.isEmpty(detail.invoice_title) && !StringUtil.isEmpty(detail.invoice_content)) {
                    tv_invoice_title_content.setText(detail.invoice_title + " " + detail.invoice_content);
                } else {
                    tv_invoice_title_content.setText(R.string.invoice_default);
                }
                tv_order_goods_price.setText(getString(R.string.rmb_reduce, detail.total_goods_price));
                tv_order_logistics_price.setText(getString(R.string.rmb_reduce, "0.00"));
                if (detail.order_status.equals("1")) {
                    rl_bottom.setVisibility(View.VISIBLE);
                    tv_order_need_pay.setText(getString(R.string.order_need_price, detail.order_price));
                } else {
                    rl_bottom.setVisibility(View.GONE);
                }
            }
            OrderGoodsDao orderGoodsDao = BaseDaoFactory.getInstance().getDataHelper(OrderGoodsDao.class, OrderGoods.class);
            OrderGoods orderGoods = new OrderGoods();
            orderGoods.order_id = bundle.getString("order_id");
            List<OrderGoods> goodsList = orderGoodsDao.query(orderGoods);
            if (goodsList != null && goodsList.size() > 0) {
                data.addAll(goodsList);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finishActivity(this);
                break;
        }
    }
}
