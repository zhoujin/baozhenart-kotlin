package com.markjin.artmall.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.markjin.artmall.BaseActivity;
import com.markjin.artmall.R;
import com.markjin.artmall.adapter.OrderListAdapter;
import com.markjin.artmall.db.bean.OrderList;
import com.markjin.artmall.db.dao.BaseDaoFactory;
import com.markjin.artmall.db.dao.OrderListDao;
import com.markjin.artmall.utils.PreferenceUtil;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * order list
 */

public class OrderListActivity extends BaseActivity implements View.OnClickListener {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recycler_order;
    private List<OrderList> data = new ArrayList<>();
    private OrderListAdapter listAdapter;
    private View empty_layout;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            data.clear();
            listAdapter.notifyDataSetChanged();
            initData(data.size());
            return false;
        }
    });

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        initView();
    }

    private void initView() {
        findViewById(R.id.iv_left).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText(R.string.order_list);
        refreshLayout = findViewById(R.id.refresh_layout);
        recycler_order = findViewById(R.id.recycler_order);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.clear();
                listAdapter.notifyDataSetChanged();
                initData(data.size());
            }
        });
        listAdapter = new OrderListAdapter(data,handler);
        recycler_order.setAdapter(listAdapter);
        recycler_order.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        empty_layout = findViewById(R.id.empty_layout);
        initData(data.size());
    }

    private void initData(int offset) {
        if (!PreferenceUtil.isLogin()) {
            return;
        }
        OrderListDao listDao = BaseDaoFactory.getInstance().getDataHelper(OrderListDao.class, OrderList.class);
        OrderList where = new OrderList();
        where.user_id = PreferenceUtil.getUserInfo().user_id;
        List<OrderList> result = listDao.query(where, " add_time desc ", String.valueOf(offset), "10");
        refreshLayout.setRefreshing(false);
        if (result != null && result.size() > 0) {//有订单
            data.addAll(result);
            listAdapter.notifyDataSetChanged();
        }
        if (data.size() == 0) {
            empty_layout.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
        } else {
            empty_layout.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
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
