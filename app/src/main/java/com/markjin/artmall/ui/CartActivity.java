package com.markjin.artmall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.markjin.artmall.BaseActivity;
import com.markjin.artmall.R;
import com.markjin.artmall.adapter.BaseGoodsAdapter;
import com.markjin.artmall.adapter.CartAdapter;
import com.markjin.artmall.db.bean.Goods;
import com.markjin.artmall.db.bean.GoodsCart;
import com.markjin.artmall.db.dao.BaseDaoFactory;
import com.markjin.artmall.db.dao.GoodsCartDao;
import com.markjin.artmall.db.dao.GoodsListDao;
import com.markjin.artmall.utils.PreferenceUtil;
import com.markjin.artmall.utils.ToastUtil;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * cart
 */

public class CartActivity extends BaseActivity implements View.OnClickListener {

    public static final int MSG_HAVE = 1000;
    public static final int MSG_ALL = 1001;
    public static final int MSG_CLEAR = 1002;
    public boolean cb_all_true = true;
    private TextView tv_right;
    private View line_buy, line_exhibition;
    private SwipeRefreshLayout refreshLayout;
    private CartAdapter adapter;
    private List<GoodsCart> data = new ArrayList<>();
    private RecyclerView rv_cart;
    private int DEFAULT_DATA = 1;//1 BUY 2 EXHIBITION
    private AppCompatCheckBox cb_all;
    private TextView tv_cart_total, tv_sure;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case MSG_ALL:
                    cb_all_true = true;
                    cb_all.setChecked(true);
                    if ((boolean) tv_right.getTag()) {
                        tv_sure.setText(getString(R.string.delete_number, adapter.getCheckSize()));
                    } else {
                        tv_sure.setText(getString(R.string.check_number, adapter.getCheckSize()));
                        tv_cart_total.setText(getString(R.string.cart_total, adapter.getCheckPrice()));
                    }
                    break;
                case MSG_HAVE:
                    cb_all_true = false;
                    if ((boolean) tv_right.getTag()) {
                        cb_all.setChecked(false);
                        adapter.notifyDataSetChanged();
                        tv_sure.setText(getString(R.string.delete_number, adapter.getCheckSize()));
                    } else {
                        cb_all.setChecked(false);
                        tv_cart_total.setText(getString(R.string.cart_total, adapter.getCheckPrice()));
                        tv_sure.setText(getString(R.string.check_number, adapter.getCheckSize()));
                    }
                    break;
                case MSG_CLEAR:
                    cb_all.setChecked(false);
                    if ((boolean) tv_right.getTag()) {
                        tv_sure.setText(getString(R.string.delete_number, 0));
                    } else {
                        tv_sure.setText(getString(R.string.check_number, 0));
                        tv_cart_total.setText(getString(R.string.cart_total, 0));
                    }
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        data.clear();
        adapter.notifyDataSetChanged();
        initCartData(DEFAULT_DATA, 0);
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText(R.string.cart);
        findViewById(R.id.iv_left).setOnClickListener(this);
        findViewById(R.id.tv_cart_buy).setOnClickListener(this);
        findViewById(R.id.tv_cart_exhibition).setOnClickListener(this);
        tv_right = findViewById(R.id.tv_right);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(R.string.editor);
        tv_right.setTag(false);
        tv_right.setOnClickListener(this);
        line_buy = findViewById(R.id.line_buy);
        line_exhibition = findViewById(R.id.line_exhibition);
        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                data.clear();
                adapter.notifyDataSetChanged();
                initCartData(DEFAULT_DATA, 0);
            }
        });
        rv_cart = findViewById(R.id.rv_cart);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_cart.setLayoutManager(layoutManager);
        adapter = new CartAdapter(data, handler);
        rv_cart.setAdapter(adapter);

        cb_all = findViewById(R.id.cb_all);
        cb_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    adapter.setCheckAll((Boolean) tv_right.getTag());
                    if ((Boolean) tv_right.getTag()) {
                        tv_cart_total.setVisibility(View.INVISIBLE);
                        tv_sure.setText(getString(R.string.delete_number, adapter.getCheckSize()));
                    } else {
                        tv_cart_total.setVisibility(View.VISIBLE);
                        tv_sure.setText(getString(R.string.check_number, adapter.getCheckSize()));
                        tv_cart_total.setText(getString(R.string.cart_total, adapter.getCheckPrice()));
                    }

                } else {
                    if (cb_all_true) {
                        adapter.clear();
                        if ((Boolean) tv_right.getTag()) {
                            tv_cart_total.setVisibility(View.INVISIBLE);
                            tv_sure.setText(getString(R.string.delete_number, 0));
                        } else {
                            tv_cart_total.setVisibility(View.VISIBLE);
                            tv_cart_total.setText(getString(R.string.cart_total, 0));
                            tv_sure.setText(getString(R.string.check_number, 0));
                        }
                    } else {
                        if ((Boolean) tv_right.getTag()) {
                            tv_cart_total.setVisibility(View.INVISIBLE);
                            tv_sure.setText(getString(R.string.delete_number, adapter.getCheckSize()));
                        } else {
                            tv_cart_total.setVisibility(View.VISIBLE);
                            tv_sure.setText(getString(R.string.check_number, adapter.getCheckSize()));
                            tv_cart_total.setText(getString(R.string.cart_total, adapter.getCheckPrice()));
                        }
                    }
                }
            }
        });
        tv_cart_total = findViewById(R.id.tv_cart_total);
        tv_cart_total.setText(getString(R.string.cart_total, 0));
        tv_sure = findViewById(R.id.tv_sure);
        tv_sure.setText(getString(R.string.check_number, 0));
        tv_sure.setOnClickListener(this);
    }

    private void initCartData(int type, int offset) {
        if (PreferenceUtil.isLogin()) {
            GoodsCart where = new GoodsCart();
            where.user_id = PreferenceUtil.getUserInfo().user_id;
            where.cart_type = String.valueOf(type);
            GoodsCartDao cartDao = BaseDaoFactory.getInstance().getDataHelper(GoodsCartDao.class, GoodsCart.class);
            List<GoodsCart> result = cartDao.query(where, " add_time desc", String.valueOf(offset), "10");
            if (result != null && result.size() > 0) {
                if (data.size() == 0 && result.size() == 0) {//购物车为空

                } else if (data.size() == 0 && result.size() > 0) {
                    data.addAll(result);
                    adapter.notifyDataSetChanged();
                } else if (data.size() != 0 && result.size() == 0) {//已加载全部
                    ToastUtil.showMessage(this, R.string.load_all);
                } else {
                    data.addAll(result);
                    adapter.notifyDataSetChanged();
                }
            } else {//empty view
            }
        } else { //show empty view
            intentActivity(CartActivity.this, UserLoginActivity.class);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finishActivity(this);
                break;
            case R.id.tv_cart_buy:
                line_buy.setVisibility(View.VISIBLE);
                line_exhibition.setVisibility(View.INVISIBLE);

                break;
            case R.id.tv_cart_exhibition:
                line_buy.setVisibility(View.INVISIBLE);
                line_exhibition.setVisibility(View.VISIBLE);

                break;
            case R.id.tv_right:
                if ((boolean) view.getTag()) {//购买
                    adapter.clear();
                    view.setTag(false);
                    cb_all.setChecked(false);
                    tv_right.setText(R.string.editor);
                    adapter.setDelete(false);
                    tv_sure.setText(getString(R.string.check_number, 0));
                    tv_cart_total.setVisibility(View.VISIBLE);
                    tv_cart_total.setText(getString(R.string.cart_total, 0));
                    adapter.notifyDataSetChanged();
                } else {//删除
                    adapter.clear();
                    view.setTag(true);
                    cb_all.setChecked(false);
                    tv_right.setText(R.string.delete);
                    adapter.setDelete(true);
                    tv_sure.setText(getString(R.string.delete_number, 0));
                    tv_cart_total.setVisibility(View.INVISIBLE);
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.tv_sure:
                if (!PreferenceUtil.isLogin()) {
                    intentActivity(this, UserLoginActivity.class);
                    return;
                }
                if (tv_right.getText().equals(getString(R.string.editor))) {//
                    if (adapter.getSelectIds().size() > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("is_cart", true);
                        bundle.putStringArrayList("goods", adapter.getSelectIds());
                        intentActivity(this, OrderCreateActivity.class, bundle);
                    } else {
                        ToastUtil.showMessage(this, R.string.cart_submit_empty);
                    }
                } else {
                    deleteCartsGoods();
                }
                break;
        }
    }

    private void deleteCartsGoods() {
        if (adapter.getSelectIds().size() == 0) {
            return;
        }
        GoodsCartDao cartDao = BaseDaoFactory.getInstance().getDataHelper(GoodsCartDao.class, GoodsCart.class);
        ArrayList<String> sqls = new ArrayList<>();
        for (int i = 0; i < adapter.getSelectIds().size(); i++) {
            sqls.add("delete from tb_goods_cart where user_id=" + PreferenceUtil.getUserInfo().user_id + " and goods_id=" + adapter.getSelectIds().get(i));
        }
        if (cartDao.sqlByTransaction(sqls)) {
            for (int i = 0; i < adapter.getSelectIds().size(); i++) {
                for (int j = 0; j < data.size(); j++) {
                    if (data.get(j).goods_id.equals(adapter.getSelectIds().get(i))) {
                        data.remove(j);
                    }
                }
            }
            adapter.notifyDataSetChanged();
            ToastUtil.showMessage(this, R.string.success_delete);
        } else {
            ToastUtil.showMessage(this, R.string.success_error);
        }
    }

    /**
     *
     */

    public static class ExhibitionFragment extends Fragment implements View.OnClickListener {
        private SwipeRefreshLayout refresh_layout;
        private List<Goods> data = new ArrayList<>();
        private BaseGoodsAdapter goodsAdapter;

        @Override
        public void onCreate(@android.support.annotation.Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @android.support.annotation.Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @android.support.annotation.Nullable ViewGroup container, @android.support.annotation.Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_goods, container, false);
        }

        @Override
        public void onViewCreated(View view, @android.support.annotation.Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            view.findViewById(R.id.iv_left).setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.tv_title)).setText(R.string.title_exhibition);
            ((ImageView) view.findViewById(R.id.iv_top_cart)).setImageResource(R.mipmap.ic_cart);
            view.findViewById(R.id.iv_top_cart).setOnClickListener(this);
            refresh_layout = view.findViewById(R.id.refresh_layout);
            refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refresh_layout.setRefreshing(false);
                    data.clear();
                    goodsAdapter.notifyDataSetChanged();
                    initData(data.size());
                }
            });
            RecyclerView rv_goods_list = view.findViewById(R.id.rv_goods_list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            rv_goods_list.setLayoutManager(layoutManager);
            goodsAdapter = new BaseGoodsAdapter<>(data);
            rv_goods_list.setAdapter(goodsAdapter);

            initData(data.size());
        }

        private void initData(int offset) {
            GoodsListDao listDao = BaseDaoFactory.getInstance().getDataHelper(GoodsListDao.class, Goods.class);
            Goods goods = new Goods();
            List<Goods> result = listDao.query(goods, "add_time desc", String.valueOf(offset), "10");
            if (result != null) {
                if (data.size() == 0 && result.size() > 0) {
                    data.addAll(result);
                    goodsAdapter.notifyDataSetChanged();
                } else if (data.size() != 0 && result.size() == 0) {//已到最后
                    goodsAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_top_cart:
                    Intent intent = new Intent(getContext(), CartActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}
