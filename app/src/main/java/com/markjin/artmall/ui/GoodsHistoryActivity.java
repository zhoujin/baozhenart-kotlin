package com.markjin.artmall.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.markjin.artmall.BaseActivity;
import com.markjin.artmall.R;
import com.markjin.artmall.adapter.BaseGoodsAdapter;
import com.markjin.artmall.db.bean.GoodsHistory;
import com.markjin.artmall.db.dao.BaseDaoFactory;
import com.markjin.artmall.db.dao.GoodsHistoryDao;
import com.markjin.artmall.utils.PreferenceUtil;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class GoodsHistoryActivity extends BaseActivity implements View.OnClickListener {

    private SwipeRefreshLayout refreshLayout;
    private BaseGoodsAdapter adapter;
    private List<GoodsHistory> data = new ArrayList<>();
    private TextView tv_right;
    private View empty_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_history);
        initView();
    }

    private void initView() {
        findViewById(R.id.iv_left).setOnClickListener(this);
        tv_right = findViewById(R.id.tv_right);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(R.string.editor);
        tv_right.setOnClickListener(this);
        tv_right.setTag(false);
        ((TextView) findViewById(R.id.tv_title)).setText(R.string.user_history);
        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.clear();
                adapter.notifyDataSetChanged();
                initData(data.size());
            }
        });
        RecyclerView recycler_history = findViewById(R.id.recycler_history);
        recycler_history.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new BaseGoodsAdapter(data);
        recycler_history.setAdapter(adapter);
        empty_layout = findViewById(R.id.empty_layout);
        initData(data.size());
    }

    private void initData(int offset) {
        GoodsHistoryDao historyDao = BaseDaoFactory.getInstance().getDataHelper(GoodsHistoryDao.class, GoodsHistory.class);
        GoodsHistory history = new GoodsHistory();
        history.user_id = PreferenceUtil.getUserInfo().user_id;
        List<GoodsHistory> result = historyDao.query(history, "add_time desc ", String.valueOf(offset), "10");
        refreshLayout.setRefreshing(false);
        if (result != null && result.size() > 0) {
            data.addAll(result);
            adapter.notifyDataSetChanged();
        }
        if (data.size() == 0) {
            refreshLayout.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
        } else {
            refreshLayout.setVisibility(View.VISIBLE);
            empty_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finishActivity(this);
                break;
            case R.id.tv_right:
                if ((boolean) view.getTag()) {
                    tv_right.setText(R.string.editor);
                    tv_right.setTag(false);
                    adapter.setHaveDelete(false);
                    for (int j = 0; j < data.size(); j++) {
                        for (int i = 0; i < adapter.getCheckIds().size(); i++) {
                            GoodsHistoryDao historyDao = BaseDaoFactory.getInstance().getDataHelper(GoodsHistoryDao.class, GoodsHistory.class);
                            GoodsHistory history = new GoodsHistory();
                            history.user_id = PreferenceUtil.getUserInfo().user_id;
                            history.goods_id = adapter.getCheckIds().get(i).toString();
                            if (data.get(j).goods_id.equals(adapter.getCheckIds().get(i).toString())) {
                                data.remove(j);
                            }
                            historyDao.delete(history);
                        }
                    }
                    if (data.size() == 0) {
                        refreshLayout.setVisibility(View.GONE);
                        empty_layout.setVisibility(View.VISIBLE);
                    } else {
                        refreshLayout.setVisibility(View.VISIBLE);
                        empty_layout.setVisibility(View.GONE);
                    }
                } else {
                    tv_right.setText(R.string.delete);
                    tv_right.setTag(true);
                    adapter.setHaveDelete(true);
                    adapter.clearSelect();
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }
}
