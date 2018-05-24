package com.markjin.artmall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.markjin.artmall.R;
import com.markjin.artmall.adapter.BaseGoodsAdapter;
import com.markjin.artmall.db.bean.Goods;
import com.markjin.artmall.db.bean.GoodsDetail;
import com.markjin.artmall.db.dao.BaseDaoFactory;
import com.markjin.artmall.db.dao.GoodsDetailDao;
import com.markjin.artmall.db.dao.GoodsListDao;

import java.util.ArrayList;
import java.util.List;

/**
 */

public class GoodsFragment extends Fragment implements View.OnClickListener {

    private SwipeRefreshLayout refresh_layout;
    private List<Goods> data = new ArrayList<>();
    private BaseGoodsAdapter goodsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_goods, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.iv_left).setVisibility(View.GONE);
        ((TextView) view.findViewById(R.id.tv_title)).setText(R.string.title_art);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        initData(data.size());
    }

    private void initData(int offset) {
        GoodsDetailDao detailDao = BaseDaoFactory.getInstance().getDataHelper(GoodsDetailDao.class, GoodsDetail.class);
        GoodsDetail detail = new GoodsDetail();
        List<GoodsDetail> details = detailDao.query(detail);
        if (details != null && details.size() > 0) {
            GoodsListDao listDao = BaseDaoFactory.getInstance().getDataHelper(GoodsListDao.class, Goods.class);
            Goods goods = new Goods();
            List<Goods> result = listDao.query(goods, "add_time desc ", String.valueOf(offset), "10");
            if (result != null) {
                if (result.size() > 0) {
                    data.addAll(result);
                    goodsAdapter.notifyDataSetChanged();
                } else if (data.size() != 0 && result.size() == 0) {//已到最后
                    goodsAdapter.notifyDataSetChanged();
                }
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
