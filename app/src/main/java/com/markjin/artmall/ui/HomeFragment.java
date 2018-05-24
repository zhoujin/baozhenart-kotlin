package com.markjin.artmall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.markjin.artmall.Contants;
import com.markjin.artmall.GlideApp;
import com.markjin.artmall.R;
import com.markjin.artmall.adapter.HomeArtistAdapter;
import com.markjin.artmall.adapter.HomeHotAdapter;
import com.markjin.artmall.adapter.HomeInfoAdapter;
import com.markjin.artmall.adapter.HomeNewAdapter;
import com.markjin.artmall.adapter.HomePagerAdapter;
import com.markjin.artmall.adapter.HomeSoftAdapter;
import com.markjin.artmall.db.bean.HomeBanner;
import com.markjin.artmall.db.bean.HomeData;
import com.markjin.artmall.utils.PreferenceUtil;
import com.markjin.artmall.utils.ScreenUtils;
import com.markjin.artmall.utils.SpacesItemDecoration;
import com.markjin.artmall.utils.StringUtil;
import com.markjin.artmall.view.Loadlayout;
import com.markjin.artmall.view.ViewPagerAutoFlipper;
import com.markjin.artmall.zxing.CaptureActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 */

public class HomeFragment extends Fragment implements View.OnClickListener {
    private SwipeRefreshLayout refresh_layout;
    private FrameLayout fl_banner;
    private ViewPager pager_banner;
    private LinearLayout ll_points;
    private ViewPagerAutoFlipper homeFlipper;
    private ArrayList<View> views = new ArrayList<>();
    private ArrayList<View> iv_points = new ArrayList<>();
    private int previousSelectPosition = 0;
    private HomeBanner banner;
    private HomePagerAdapter pagerAdapter;
    private Loadlayout loadlayout;
    private HomeData homeData;
    //热门推荐
    private RecyclerView rv_home_hot;
    private List<HomeData.Data.HotLabel> data_hot = new ArrayList<>();
    private HomeHotAdapter hotAdapter;
    //艺术家
    private RecyclerView rv_home_artist;
    private List<HomeData.Data.Artist> data_artist = new ArrayList<>();
    private HomeArtistAdapter artistAdapter;
    //作品上新
    private LinearLayout ll_new_goods;
    private List<HomeData.Data.NewGoods> data_new = new ArrayList<>();
    private RecyclerView rv_home_new;
    private HomeNewAdapter newAdapter;
    //软装
    private RecyclerView rv_home_soft;
    private List<HomeData.Data.Soft> data_soft = new ArrayList<>();
    private HomeSoftAdapter softAdapter;
    //艺起看
    private RecyclerView rv_home_info;
    private List<HomeData.Data.Subject> data_topic = new ArrayList<>();
    private HomeInfoAdapter infoAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refresh_layout = view.findViewById(R.id.refresh_layout);
        loadlayout = new Loadlayout(view.getContext(), R.style.loadingDialog);
        view.findViewById(R.id.iv_left).setVisibility(View.INVISIBLE);
        ((TextView) view.findViewById(R.id.tv_title)).setText(R.string.app_name);
        ImageView cart = view.findViewById(R.id.iv_right);
        cart.setImageResource(R.mipmap.ic_cart);
        cart.setOnClickListener(this);
        view.findViewById(R.id.iv_scan).setOnClickListener(this);
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initBannerData();
            }
        });
        fl_banner = view.findViewById(R.id.fl_banner);
        LinearLayout.LayoutParams fl_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getScreenWidth(getContext()) * 267 / 414);
        fl_banner.setLayoutParams(fl_params);
        pager_banner = view.findViewById(R.id.pager_banner);
        ll_points = view.findViewById(R.id.ll_point);
        homeFlipper = new ViewPagerAutoFlipper(pager_banner);

        homeFlipper.setInterval(5000);
        pagerAdapter = new HomePagerAdapter(views);
        pager_banner.setAdapter(pagerAdapter);
        pager_banner.addOnPageChangeListener(new BannerPageChangeListener());

        view.findViewById(R.id.ll_banner_bottom).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (ScreenUtils.getScreenWidth(getContext()) - ScreenUtils.dip2px(40)) / 3 + ScreenUtils.dip2px(20)));
        view.findViewById(R.id.iv_home_private_custom).setOnClickListener(this);
        view.findViewById(R.id.iv_home_baozhen_sale).setOnClickListener(this);
        view.findViewById(R.id.iv_home_resale).setOnClickListener(this);
        //热门标签
        view.findViewById(R.id.ll_hot).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (ScreenUtils.getScreenWidth(getContext()) - 160 - ScreenUtils.dip2px(20)) / 3 + ScreenUtils.dip2px(66)));
        rv_home_hot = view.findViewById(R.id.rv_home_hot);
        rv_home_hot.setHasFixedSize(true);
        view.findViewById(R.id.tv_hot_all).setOnClickListener(this);
        hotAdapter = new HomeHotAdapter(getActivity(), data_hot);
        rv_home_hot.setAdapter(hotAdapter);
        rv_home_hot.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_home_hot.addItemDecoration(new SpacesItemDecoration(ScreenUtils.dip2px(10), 7));
        //艺术家
        view.findViewById(R.id.tv_artist_all).setOnClickListener(this);
        rv_home_artist = view.findViewById(R.id.rv_home_artist);
        rv_home_artist.setHasFixedSize(true);
        artistAdapter = new HomeArtistAdapter(getContext(), data_artist);
        rv_home_artist.setAdapter(artistAdapter);
        rv_home_artist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_home_artist.addItemDecoration(new SpacesItemDecoration(ScreenUtils.dip2px(10), 7));
        //作品上新
        ll_new_goods = view.findViewById(R.id.ll_new_goods);
        ll_new_goods.setVisibility(View.VISIBLE);
        view.findViewById(R.id.tv_new_all).setOnClickListener(this);
        ll_new_goods.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (ScreenUtils.getScreenWidth(getContext()) - 160 - ScreenUtils.dip2px(20)) / 3 + ScreenUtils.dip2px(66)));
        rv_home_new = view.findViewById(R.id.rv_home_new);
        rv_home_new.setHasFixedSize(true);
        rv_home_new.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        newAdapter = new HomeNewAdapter(this.getContext(), data_new);
        rv_home_new.addItemDecoration(new SpacesItemDecoration(ScreenUtils.dip2px(10), 7));
        rv_home_new.setAdapter(newAdapter);
        //艺术软装
        view.findViewById(R.id.ll_soft).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (ScreenUtils.getScreenWidth(getContext()) - 160) / 2 + ((ScreenUtils.getScreenWidth(getContext()) - 160 - ScreenUtils.dip2px(20)) / 3) + ScreenUtils.dip2px(76)));
        rv_home_soft = view.findViewById(R.id.rv_home_soft);
        rv_home_soft.setHasFixedSize(true);
        view.findViewById(R.id.tv_soft_all).setOnClickListener(this);
        softAdapter = new HomeSoftAdapter(getContext(), data_soft);
        rv_home_soft.setAdapter(softAdapter);
        rv_home_soft.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_home_soft.addItemDecoration(new SpacesItemDecoration(ScreenUtils.dip2px(10), 7));
        //艺笔艺画
        view.findViewById(R.id.ll_info).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (ScreenUtils.getScreenWidth(getContext()) - 160) / 2 + ScreenUtils.dip2px(66)));
        view.findViewById(R.id.tv_info_all).setOnClickListener(this);
        rv_home_info = view.findViewById(R.id.rv_home_info);
        rv_home_info.setHasFixedSize(true);
        infoAdapter = new HomeInfoAdapter(getContext(), data_topic);
        rv_home_info.setAdapter(infoAdapter);
        rv_home_info.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_home_info.addItemDecoration(new SpacesItemDecoration(ScreenUtils.dip2px(10), 7));
        initBannerData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_right:
                if (PreferenceUtil.isLogin()) {
                    Intent intent = new Intent(getActivity(), CartActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), UserLoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.iv_scan:
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_home_private_custom:

                break;
            case R.id.iv_home_baozhen_sale:

                break;
            case R.id.iv_home_resale:

                break;
        }
    }

    private void initBannerData() {
        loadlayout.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = Contants.Companion.getHOME_BANNER() + "?&client=android&v=2.8&app_v=3.5.0&channel=artmall&cust=00000000-7552-cdf7-ffff-fffffacbb56e";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadlayout.dismiss();
                refresh_layout.setRefreshing(false);
                try {
                    if (response.has("errorCode") && response.getInt("errorCode") == 0) {//有errorCode
                        banner = new Gson().fromJson(response.toString(), HomeBanner.class);
                        loadBanner(banner);
                    }
                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadlayout.dismiss();
                refresh_layout.setRefreshing(false);
            }
        });
        requestQueue.add(jsonObjectRequest);
        final String home_url = Contants.Companion.getHOME_DATA() + "?&client=android&v=2.8&app_v=3.5.0&channel=artmall&cust=00000000-7552-cdf7-ffff-fffffacbb56e";
        JsonObjectRequest homeRequest = new JsonObjectRequest(Request.Method.GET, home_url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadlayout.dismiss();
                refresh_layout.setRefreshing(false);
                try {
                    if (response.has("errorCode") && response.getInt("errorCode") == 0) {//有errorCode
                        homeData = new Gson().fromJson(response.toString(), HomeData.class);
                        loadData(homeData);
                    }
                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadlayout.dismiss();
                refresh_layout.setRefreshing(false);
            }
        });
        requestQueue.add(homeRequest);
    }

    private void loadBanner(HomeBanner banner) {
        if (banner == null) {
            return;
        }
        initPoints();
        initImage();
        if (banner.data.size() > 1) {
            homeFlipper.start();
        } else {
            homeFlipper.stop();
        }
        pagerAdapter.notifyDataSetChanged();
    }

    private void loadData(HomeData homeData) {
        data_hot.clear();
        data_hot.addAll(homeData.data.recommend_label);
        hotAdapter.notifyDataSetChanged();
        data_artist.clear();
        data_artist.addAll(homeData.data.recommend_artist);
        artistAdapter.notifyDataSetChanged();
        data_new.clear();
        data_new.addAll(homeData.data.new_goods);
        newAdapter.notifyDataSetChanged();
        data_soft.clear();
        data_soft.addAll(homeData.data.recommend_decoration);
        softAdapter.notifyDataSetChanged();
        data_topic.clear();
        data_topic.addAll(homeData.data.recommend_subject);
        infoAdapter.notifyDataSetChanged();

    }

    private void initPoints() {
        if (banner == null) {
            return;
        }
        if (banner.data.size() <= 1) {
            return;
        }
        for (int i = 0; i < banner.data.size(); i++) {
            if (i >= iv_points.size()) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.dip2px(5), ScreenUtils.dip2px(5));
                params.setMargins(10, 10, 10, 10);
                ImageView point = new ImageView(getContext());
                point.setLayoutParams(params);
                iv_points.add(point);
                point.setBackgroundResource(R.drawable.ic_dot_guide_default);
                ll_points.addView(point, i);
            }
            if (pager_banner.getCurrentItem() == i) {
                if (i < iv_points.size()) {
                    iv_points.get(i).setBackgroundResource(R.drawable.ic_dot_guide_select);
                }
            }
        }
    }

    private void initImage() {
        views.clear();
        for (int i = 0; i < banner.data.size(); i++) {
            final HomeBanner.Banner bean = banner.data.get(i);
            ImageView image;
            if (i >= views.size()) {
                image = new ImageView(getContext());
                image.setScaleType(ImageView.ScaleType.FIT_XY);
                image.setImageResource(R.mipmap.ic_image_default);
                GlideApp.with(getActivity()).load(bean.image_url).centerInside().skipMemoryCache(false).error(R.mipmap.ic_image_default).into(image);
                views.add(image);
            } else {
                image = (ImageView) views.get(i);
                image.setScaleType(ImageView.ScaleType.FIT_XY);
                GlideApp.with(getActivity()).load(bean.image_url).centerInside().skipMemoryCache(false).error(R.mipmap.ic_image_default).into(image);
            }
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (bean.type.trim().equals("2")) {//网站
                        if (StringUtil.isEmpty(bean.link)) {
                            return;
                        }
                        Intent intent = new Intent(getContext(), WebviewActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("web_url", bean.link);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if (bean.type.trim().equals("4")) {//艺术品详情
                        Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("goods_id", bean.data_id);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    class BannerPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int status) {
            switch (status) {
                case ViewPager.SCROLL_STATE_DRAGGING:
                    break;
                case ViewPager.SCROLL_STATE_IDLE:
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            previousSelectPosition = position % views.size();
            for (int i = 0; i < views.size(); i++) {
                if (previousSelectPosition != i) {
                    iv_points.get(i).setBackgroundResource(R.drawable.ic_dot_guide_default);
                } else {
                    iv_points.get(i).setBackgroundResource(R.drawable.ic_dot_guide_select);
                }
            }
        }
    }
}
