package com.markjin.artmall.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.markjin.artmall.BaseActivity;
import com.markjin.artmall.R;
import com.markjin.artmall.adapter.ManagerGoodsAdapter;
import com.markjin.artmall.db.bean.GoodsDetail;
import com.markjin.artmall.db.bean.GoodsImage;
import com.markjin.artmall.db.bean.NetGoodsDetail;
import com.markjin.artmall.db.bean.NetGoodsList;
import com.markjin.artmall.db.dao.BaseDaoFactory;
import com.markjin.artmall.db.dao.GoodsDetailDao;
import com.markjin.artmall.db.dao.ImageDao;
import com.markjin.artmall.utils.ToastUtil;
import com.markjin.artmall.view.Loadlayout;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理商品
 */

public class ManagerGoodsActivity extends BaseActivity implements View.OnClickListener {
    public static final int MSG_ADD_GOOD = 1000;
    public static final int MSG_REDUCE_GOOD = 1001;
    public static final int MSG_ADD_EXHIBITION = 1002;
    public static final int MSG_REDUCE_EXHIBITION = 1003;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recycler_manager;
    private ManagerGoodsAdapter adapter;
    private List<NetGoodsList.Goods> data = new ArrayList<>();
    private Loadlayout loadlayout;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case MSG_ADD_GOOD:
                    if (message.obj != null) {
                        initGoodsDetail((NetGoodsList.Goods) message.obj, MSG_ADD_GOOD);
                    }
                    break;
                case MSG_REDUCE_GOOD:
                    if (message.obj != null) {
                        initGoodsDetail((NetGoodsList.Goods) message.obj, MSG_REDUCE_GOOD);
                    }
                    break;
                case MSG_ADD_EXHIBITION:
                    if (message.obj != null) {
                        initGoodsDetail((NetGoodsList.Goods) message.obj, MSG_ADD_EXHIBITION);
                    }
                    break;
                case MSG_REDUCE_EXHIBITION:
                    if (message.obj != null) {
                        initGoodsDetail((NetGoodsList.Goods) message.obj, MSG_REDUCE_EXHIBITION);
                    }
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_goods);
        initView();
    }

    private void initView() {
        loadlayout = new Loadlayout(this, R.style.loadingDialog);
        findViewById(R.id.iv_left).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText(R.string.user_admin);
        refreshLayout = findViewById(R.id.refresh_layout);
        recycler_manager = findViewById(R.id.recycler_manager);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.clear();
                adapter.notifyDataSetChanged();
                initData(data.size());
            }
        });
        recycler_manager.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));
        adapter = new ManagerGoodsAdapter(data, handler);
        recycler_manager.setAdapter(adapter);
        recycler_manager.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            }
        });
        initData(data.size());
    }

    private void initData(int offset) {
        refreshLayout.setRefreshing(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://api.baozhenart.com/api/goods?&limit=10&offset=" + offset, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("errorCode") && response.getInt("errorCode") == 0) {//有errorCode
                        NetGoodsList netGoodsList = new Gson().fromJson(response.toString(), NetGoodsList.class);
                        data.addAll(netGoodsList.data);
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    public void initGoodsDetail(final NetGoodsList.Goods goods, final int msgWhat) {
        refreshLayout.setRefreshing(false);
        loadlayout.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://api.baozhenart.com/api/goods/" + goods.goods_id, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("errorCode") && response.getInt("errorCode") == 0) {//有errorCode
                        NetGoodsDetail detail = new Gson().fromJson(response.toString(), NetGoodsDetail.class);
                        GoodsDetailDao detailDao = BaseDaoFactory.getInstance().getDataHelper(GoodsDetailDao.class, GoodsDetail.class);
                        ImageDao imageDao = BaseDaoFactory.getInstance().getDataHelper(ImageDao.class, GoodsImage.class);
                        switch (msgWhat) {
                            case MSG_ADD_GOOD:
                                GoodsDetail check = new GoodsDetail();
                                check.goods_id = detail.data.goods_id;
                                List<GoodsDetail> result = detailDao.query(check);
                                if (result != null && result.size() > 0) {
                                    return;
                                }
                                GoodsDetail where = new GoodsDetail();
                                where.goods_id = detail.data.goods_id;
                                where.artist_name = detail.data.artist.artist_name;
                                where.brand_name = detail.data.brand_name;
                                where.brand_id = "1";
                                where.cat_id = "1";
                                where.cat_name = detail.data.category_name;
                                where.goods_price = detail.data.shop_price;
                                where.goods_height = detail.data.goods_height;
                                where.goods_width = detail.data.goods_width;
                                where.goods_name = detail.data.goods_name;
                                where.goods_status = detail.data.goods_status;
                                where.goods_status_tag = detail.data.goods_status_tag;
                                where.img_url = detail.data.goods_imgs.get(0).img_url;
                                where.img_width = detail.data.goods_imgs.get(0).img_width;
                                where.img_height = detail.data.goods_imgs.get(0).img_height;
                                where.goods_height_mount = detail.data.goods_mount_height;
                                where.goods_width_mount = detail.data.goods_mount_width;
                                where.add_time = String.valueOf(System.currentTimeMillis());
                                where.original_goods_width = detail.data.goods_width;
                                where.original_goods_height = detail.data.goods_height;
                                where.goods_base_price = String.valueOf(Integer.parseInt(where.goods_price) - 400);
                                where.update_time = where.add_time;
                                where.goods_number = "1";
                                where.is_sale = "1";
                                where.is_delete = "0";
                                where.market_price = String.valueOf(Integer.parseInt(where.goods_price) + 200);
                                detailDao.insert(where);
                                for (int i = 0; i < detail.data.goods_imgs.size(); i++) {
                                    GoodsImage whereImage = new GoodsImage();
                                    whereImage.goods_id = detail.data.goods_id;
                                    whereImage.img_url = detail.data.goods_imgs.get(i).img_url;
                                    whereImage.img_width = detail.data.goods_imgs.get(i).img_width;
                                    whereImage.img_height = detail.data.goods_imgs.get(i).img_height;
                                    whereImage.img_original_url = detail.data.goods_imgs.get(i).img_original;
                                    imageDao.insert(whereImage);
                                }
                                ToastUtil.showMessage(ManagerGoodsActivity.this, R.string.success_add);
                                loadlayout.dismiss();
                                break;
                            case MSG_REDUCE_GOOD:
                                where = new GoodsDetail();
                                where.goods_id = detail.data.goods_id;
                                detailDao.delete(where);
                                ToastUtil.showMessage(ManagerGoodsActivity.this, R.string.success_delete);
                                break;
                            case MSG_ADD_EXHIBITION:

                                break;
                            case MSG_REDUCE_EXHIBITION:

                                break;
                        }
                    }
                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    private void insertGoods(NetGoodsList.Goods goods) {

    }

    private void deleteGoods(NetGoodsList.Goods goods) {

    }

    private void insertExhibition(NetGoodsList.Goods goods) {

    }

    private void deleteExhibition(NetGoodsList.Goods goods) {

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
