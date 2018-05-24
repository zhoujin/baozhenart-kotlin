package com.markjin.artmall.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.markjin.artmall.AppContext;
import com.markjin.artmall.BaseActivity;
import com.markjin.artmall.Contants;
import com.markjin.artmall.R;
import com.markjin.artmall.adapter.ImageHorizontalAdapter;
import com.markjin.artmall.adapter.PayTypeAdapter;
import com.markjin.artmall.db.bean.Address;
import com.markjin.artmall.db.bean.GoodsCart;
import com.markjin.artmall.db.bean.GoodsDetail;
import com.markjin.artmall.db.bean.OrderDetail;
import com.markjin.artmall.db.bean.OrderGoods;
import com.markjin.artmall.db.bean.PayType;
import com.markjin.artmall.db.dao.AddressDao;
import com.markjin.artmall.db.dao.BaseDaoFactory;
import com.markjin.artmall.db.dao.GoodsCartDao;
import com.markjin.artmall.db.dao.GoodsDetailDao;
import com.markjin.artmall.db.dao.OrderDetailDao;
import com.markjin.artmall.db.dao.OrderGoodsDao;
import com.markjin.artmall.db.dao.PayTypeDao;
import com.markjin.artmall.utils.PreferenceUtil;
import com.markjin.artmall.utils.StringUtil;
import com.markjin.artmall.utils.ToastUtil;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * order create
 * buy now or cart
 */

public class OrderCreateActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_ADDRESS = 1000;
    private ImageView iv_address;
    private LinearLayout ll_address_info;
    private TextView tv_address_empty, tv_address_name_phone, tv_address_detail;
    private RecyclerView recycler_goods;
    private ImageHorizontalAdapter adapter;
    private List<GoodsCart> data_cart = new ArrayList<>();
    private List<GoodsDetail> data_now = new ArrayList<>();
    private TextView tv_goods_number, tv_goods_price;

    private TextView tv_delivery_type_fee, tv_invoice;
    private ListView lv_pay_type;
    private PayTypeAdapter payAdapter;
    private TextView tv_order_need_pay;
    private Address address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_create);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText(R.string.order_submit);
        findViewById(R.id.iv_left).setOnClickListener(this);
        findViewById(R.id.rl_address).setOnClickListener(this);
        findViewById(R.id.ll_delivery).setOnClickListener(this);
        findViewById(R.id.ll_invoice).setOnClickListener(this);
        findViewById(R.id.tv_order_submit).setOnClickListener(this);
        recycler_goods = findViewById(R.id.recycler_goods);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_goods.setLayoutManager(layoutManager);

        iv_address = findViewById(R.id.iv_address);
        ll_address_info = findViewById(R.id.ll_address_info);
        tv_address_empty = findViewById(R.id.tv_address_empty);
        tv_address_name_phone = findViewById(R.id.tv_address_name_phone);
        tv_address_detail = findViewById(R.id.tv_address_detail);
        tv_delivery_type_fee = findViewById(R.id.tv_delivery_type_fee);
        tv_invoice = findViewById(R.id.tv_invoice);
        tv_goods_number = findViewById(R.id.tv_goods_number);
        tv_goods_price = findViewById(R.id.tv_goods_price);
        lv_pay_type = findViewById(R.id.lv_pay_type);
        tv_order_need_pay = findViewById(R.id.tv_order_need_pay);
        getAddressInfo();
        initGoodsInfo();
        initPayList();
    }

    private void getAddressInfo() {
        Address where_default = new Address();
        where_default.is_default = "1";
        where_default.user_id = PreferenceUtil.getUserInfo().user_id;
        AddressDao addressDao = BaseDaoFactory.getInstance().getDataHelper(AddressDao.class, Address.class);
        List<Address> result = addressDao.query(where_default);
        if (result != null && result.size() == 1) {
            tv_address_empty.setVisibility(View.GONE);
            iv_address.setVisibility(View.VISIBLE);
            ll_address_info.setVisibility(View.VISIBLE);
            address = result.get(0);
            tv_address_name_phone.setText(getString(R.string.address_name_phone, result.get(0).address_name, result.get(0).address_phone));
            tv_address_detail.setText(result.get(0).province + result.get(0).country + result.get(0).district + result.get(0).detail_address);
        } else {
            where_default.is_default = "0";
            List<Address> all = addressDao.query(where_default);
            if (all != null && all.size() > 0) {
                tv_address_empty.setVisibility(View.GONE);
                iv_address.setVisibility(View.VISIBLE);
                ll_address_info.setVisibility(View.VISIBLE);
                address = all.get(0);
                tv_address_name_phone.setText(getString(R.string.address_name_phone, all.get(0).address_name, all.get(0).address_phone));
                tv_address_detail.setText(all.get(0).province + all.get(0).country + all.get(0).district + all.get(0).detail_address);
            } else {
                tv_address_empty.setVisibility(View.VISIBLE);
                iv_address.setVisibility(View.GONE);
                ll_address_info.setVisibility(View.GONE);
            }
        }
        tv_delivery_type_fee.setText(getString(R.string.delivery_fee, 0));
    }

    private void initGoodsInfo() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("is_cart")) {//购物车
                adapter = new ImageHorizontalAdapter<>(data_cart);
                List<String> goods_id = bundle.getStringArrayList("goods");
                if (goods_id.size() > 0) {
                    GoodsCartDao cartDao = BaseDaoFactory.getInstance().getDataHelper(GoodsCartDao.class, GoodsCart.class);
                    int total_goods_price = 0;
                    GoodsCart where = new GoodsCart();
                    for (int i = 0; i < goods_id.size(); i++) {
                        where.goods_id = goods_id.get(i);
                        List<GoodsCart> result = cartDao.query(where);
                        if (result != null && result.size() > 0) {
                            if (!StringUtil.isEmpty(result.get(i).goods_price)) {
                                total_goods_price += Float.valueOf(result.get(i).goods_price);
                            }
                            data_cart.addAll(result);
                        }
                    }
                    tv_order_need_pay.setText(getString(R.string.order_need_pay, String.valueOf(total_goods_price)));
                    tv_goods_price.setText(getString(R.string.rmb, String.valueOf(total_goods_price)));
                    tv_goods_number.setText(getString(R.string.goods_number, data_cart.size()));
                    recycler_goods.setAdapter(adapter);
                } else {//error

                }
            } else {//直接购买
                adapter = new ImageHorizontalAdapter<>(data_now);
                GoodsDetail goods = (GoodsDetail) bundle.getSerializable("goods");
                data_now.add(goods);
                tv_goods_number.setText(getString(R.string.goods_number, 1));
                tv_goods_price.setText(getString(R.string.rmb, goods.goods_price));
                recycler_goods.setAdapter(adapter);
                tv_order_need_pay.setText(getString(R.string.order_need_pay, String.valueOf(goods.goods_price)));
            }
        }
    }

    private void initPayList() {
        PayTypeDao payDao = BaseDaoFactory.getInstance().getDataHelper(PayTypeDao.class, PayType.class);
        PayType where = new PayType();
        List<PayType> result = payDao.query(where);
        if (result != null) {
            if (result.size() > 0) {
                payAdapter = new PayTypeAdapter(result);
                lv_pay_type.setAdapter(payAdapter);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finishActivity(this);
                break;
            case R.id.rl_address:
                Intent intent = new Intent(this, AddressListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("is_order", true);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE_ADDRESS);
                break;
            case R.id.ll_delivery:

                break;
            case R.id.ll_invoice:

                break;
            case R.id.tv_order_submit:
                submitOrder();
                break;
        }
    }

    private void submitOrder() {
        if (address == null) {
            ToastUtil.showMessage(this, R.string.address_empty);
            return;
        }
        OrderDetailDao detailDao = BaseDaoFactory.getInstance().getDataHelper(OrderDetailDao.class, OrderDetail.class);
        OrderDetail where = new OrderDetail();
        where.user_id = PreferenceUtil.getUserInfo().user_id;
        where.address_id = address.address_id;
        where.address_phone = address.address_phone;
        where.address_name = address.address_name;
        where.address_detail_all = address.province + address.country + address.district + address.detail_address;

        Random a = new Random();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        where.order_sn = format.format(new Date(System.currentTimeMillis())) + a.nextInt(10) + a.nextInt(10);
        int pay_status = a.nextInt(10);
        if (pay_status < Contants.Companion.getPAY_SUCCESS_VALUE()) {
            where.order_status = "2";//已支付
        } else {
            where.order_status = "1";//未支付
        }
        if (where.order_status.equals("1")) {
            where.order_status_tag = getString(R.string.order_status_un_pay);
        } else if (where.order_status.equals("2")) {
            where.pay_time = String.valueOf(System.currentTimeMillis() + 10000);
            where.order_status_tag = getString(R.string.order_status_need_send);
        }
        where.total_goods_price = tv_goods_price.getText().toString().substring(1, tv_goods_price.length());
        where.order_price = tv_goods_price.getText().toString().substring(1, tv_goods_price.length());
        if (!StringUtil.isEmpty(tv_invoice.getText().toString()) && !tv_invoice.getText().equals(getString(R.string.invoice_default))) {
            where.invoice_title = "";
            where.invoice_content = "";
        }
        where.logistics_id = "-1";
        where.pay_code = payAdapter.getSelect().pay_code;
        where.pay_name = payAdapter.getSelect().pay_name;
        where.delivery_price = "0";
        where.add_time = String.valueOf(System.currentTimeMillis());
        where.app_version = AppContext.Companion.getInstance().getVersionName();

        long result = detailDao.insert(where);
        long goodsResult = -1;

        GoodsDetailDao goodsDetailDao = BaseDaoFactory.getInstance().getDataHelper(GoodsDetailDao.class, GoodsDetail.class);
        GoodsDetail goodsDetail = new GoodsDetail();
        OrderGoodsDao orderGoodsDao = BaseDaoFactory.getInstance().getDataHelper(OrderGoodsDao.class, OrderGoods.class);
        GoodsCartDao cartDao = BaseDaoFactory.getInstance().getDataHelper(GoodsCartDao.class, GoodsCart.class);
        if (data_cart.size() != 0) {
            for (int i = 0; i < data_cart.size(); i++) {
                OrderGoods goods = new OrderGoods();
                goods.goods_id = data_cart.get(i).goods_id;
                goods.goods_price = data_cart.get(i).goods_price;
                goods.goods_name = data_cart.get(i).goods_name;
                goods.artist_name = data_cart.get(i).artist_name;
                goods.brand_name = data_cart.get(i).brand_name;
                goods.cat_name = data_cart.get(i).cat_name;
                goods.goods_height = data_cart.get(i).goods_height;
                goods.goods_width = data_cart.get(i).goods_width;
                goods.img_url = data_cart.get(i).img_url;
                goods.order_id = String.valueOf(result);
                goods.goods_status = data_cart.get(i).goods_status;
                goods.goods_status_tag = data_cart.get(i).goods_status_tag;
                goodsResult = orderGoodsDao.insert(goods);

                GoodsDetail detail = new GoodsDetail();
                detail.goods_id = goods.goods_id;
                goodsDetail.goods_id=goods.goods_id;
                goodsDetail.goods_status = String.valueOf(2);
                goodsDetail.goods_status_tag = "已售";
                goodsDetailDao.update(goodsDetail, detail);
                Log.e("更新商品", "商品状态改为已售");
                GoodsCart cart = new GoodsCart();
                cart.user_id = PreferenceUtil.getUserInfo().user_id;
                cart.goods_id = goods.goods_id;
                cartDao.delete(cart);
                Log.e("更新商品", "购物车删除商品id");
            }
        } else if (data_now.size() != 0) {
            OrderGoods goods = new OrderGoods();
            goods.goods_id = data_now.get(0).goods_id;
            goods.goods_price = data_now.get(0).goods_price;
            goods.goods_name = data_now.get(0).goods_name;
            goods.artist_name = data_now.get(0).artist_name;
            goods.brand_name = data_now.get(0).brand_name;
            goods.cat_name = data_now.get(0).cat_name;
            goods.goods_height = data_now.get(0).goods_height;
            goods.goods_width = data_now.get(0).goods_width;
            goods.img_url = data_now.get(0).img_url;
            goods.goods_status = data_now.get(0).goods_status;
            goods.goods_status_tag = data_now.get(0).goods_status_tag;
            goods.order_id = String.valueOf(result);
            goodsResult = orderGoodsDao.insert(goods);
            GoodsDetail detail = new GoodsDetail();
            detail.goods_id = goods.goods_id;
            goodsDetail.goods_id=goods.goods_id;
            goodsDetail.goods_status = String.valueOf(2);
            goodsDetail.goods_status_tag = "已售";
            goodsDetailDao.update(goodsDetail, detail);
        }
        if (result != -1 && goodsResult != -1) {
            finishActivity(this);
            intentActivity(this, OrderListActivity.class);
        } else {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADDRESS && resultCode == Activity.RESULT_OK) {
            tv_address_empty.setVisibility(View.GONE);
            iv_address.setVisibility(View.VISIBLE);
            ll_address_info.setVisibility(View.VISIBLE);
            address = (Address) data.getExtras().getSerializable("address");
            tv_address_name_phone.setText(getString(R.string.address_name_phone, address.address_name, address.address_phone));
            tv_address_detail.setText(address.province + address.country + address.district + address.detail_address);
        }
    }
}
