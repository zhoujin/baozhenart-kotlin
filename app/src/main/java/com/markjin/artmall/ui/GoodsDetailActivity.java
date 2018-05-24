package com.markjin.artmall.ui;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.markjin.artmall.BaseActivity;
import com.markjin.artmall.GlideApp;
import com.markjin.artmall.R;
import com.markjin.artmall.db.bean.GoodsCart;
import com.markjin.artmall.db.bean.GoodsCollect;
import com.markjin.artmall.db.bean.GoodsDetail;
import com.markjin.artmall.db.bean.GoodsHistory;
import com.markjin.artmall.db.bean.GoodsImage;
import com.markjin.artmall.db.dao.BaseDaoFactory;
import com.markjin.artmall.db.dao.GoodsCartDao;
import com.markjin.artmall.db.dao.GoodsCollectDao;
import com.markjin.artmall.db.dao.GoodsDetailDao;
import com.markjin.artmall.db.dao.GoodsHistoryDao;
import com.markjin.artmall.db.dao.ImageDao;
import com.markjin.artmall.utils.PreferenceUtil;
import com.markjin.artmall.utils.ScreenUtils;
import com.markjin.artmall.utils.StringUtil;
import com.markjin.artmall.utils.ToastUtil;
import com.markjin.artmall.view.CustomViewPager;
import com.markjin.artmall.view.TextViewPlus;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * goods detail (bundle:goods_id)
 */

public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title;
    private ImageView iv_right;
    private CustomViewPager vp_goods_images;
    private ImageAdapter imageAdapter;
    private List<GoodsImage> images = new ArrayList<>();
    private TextView tv_number_images;
    private TextView tv_price_status;
    private TextViewPlus tv_collect;
    private TextView tv_artist_name, tv_goods_size, tv_mount_size;
    private LinearLayout ll_detail_bottom;

    private String goods_id;
    private GoodsDetail detail;
    private int max_height = 0;
    private boolean is_collect = false;
    private int icon_size;
    private String img_url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        initView();
    }

    private void initView() {
        icon_size = ScreenUtils.dip2px(36);
//        max_height = ScreenUtils.getScreenHeight(GoodsDetailActivity.this) - ScreenUtils.dip2px(96) - ScreenUtils.dip2px(48) - getStatusBarHeight();
        max_height = ScreenUtils.getScreenHeight(GoodsDetailActivity.this) - ScreenUtils.dip2px(48) - getStatusBarHeight();
        findViewById(R.id.iv_left).setOnClickListener(this);
        tv_title = findViewById(R.id.tv_title);
        iv_right = findViewById(R.id.iv_right);
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setImageResource(R.mipmap.ic_cart);
        iv_right.setOnClickListener(this);
        tv_number_images = findViewById(R.id.tv_number_images);
        vp_goods_images = findViewById(R.id.vp_goods_images);
        imageAdapter = new ImageAdapter();
        vp_goods_images.setAdapter(imageAdapter);
        vp_goods_images.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                vp_goods_images.resetHeight(position);
                tv_number_images.setText((position + 1) + "/" + images.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        findViewById(R.id.tv_preview).setOnClickListener(this);
        tv_price_status = findViewById(R.id.tv_price_status);
        tv_artist_name = findViewById(R.id.tv_artist_name);
        tv_goods_size = findViewById(R.id.tv_goods_size);
        tv_mount_size = findViewById(R.id.tv_mount_size);
        tv_collect = findViewById(R.id.tv_collect);
        tv_collect.setOnClickListener(this);
        ll_detail_bottom = findViewById(R.id.ll_detail_bottom);
        findViewById(R.id.tv_add_cart).setOnClickListener(this);
        findViewById(R.id.tv_buy_now).setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("goods_id")) {
            goods_id = bundle.getString("goods_id");
        }
        initData();
    }

    private void initData() {
        if (TextUtils.isEmpty(goods_id)) {
            return;
        }
        GoodsDetail where = new GoodsDetail();
        where.goods_id = goods_id;
        List<GoodsDetail> result = BaseDaoFactory.getInstance().getDataHelper(GoodsDetailDao.class, GoodsDetail.class).query(where);
        if (result != null && result.size() == 1) {
            detail = result.get(0);
        }
        tv_title.setText(detail.goods_name);
        if (detail.goods_status.equals("1")) {
            ll_detail_bottom.setVisibility(View.VISIBLE);
            tv_price_status.setText(getString(R.string.rmb, detail.goods_price));
        } else {
            tv_price_status.setText(detail.goods_status_tag);
            ll_detail_bottom.setVisibility(View.GONE);
        }
        if (StringUtil.isEmpty(detail.artist_name)) {
            tv_artist_name.setVisibility(View.GONE);
        } else {
            tv_artist_name.setVisibility(View.VISIBLE);
        }
        if (!StringUtil.isEmpty(detail.cat_name) && !StringUtil.isEmpty(detail.brand_name)) {
            tv_goods_size.setText(getString(R.string.goods_two_size, detail.cat_name, detail.brand_name, detail.goods_width, detail.goods_height));
        } else if (!TextUtils.isEmpty(detail.cat_name)) {
            tv_goods_size.setText(getString(R.string.goods_one_size, detail.cat_name, detail.goods_width, detail.goods_height));
        } else if (!TextUtils.isEmpty(detail.brand_name)) {
            tv_goods_size.setText(getString(R.string.goods_one_size, detail.brand_name, detail.goods_width, detail.goods_height));
        } else {
            tv_goods_size.setVisibility(View.GONE);
        }
        if (!StringUtil.isEmpty(detail.goods_width_mount) && !StringUtil.isEmpty(detail.goods_height_mount)) {
            tv_mount_size.setText(getString(R.string.goods_mount_size, detail.goods_width_mount, detail.goods_height_mount));
        } else {
            tv_mount_size.setText(R.string.goods_mount_default);
        }
        if (detail != null) {
            GoodsImage imageWhere = new GoodsImage();
            imageWhere.goods_id = detail.goods_id;
            images.clear();
            List<GoodsImage> imagesResult = BaseDaoFactory.getInstance().getDataHelper(ImageDao.class, GoodsImage.class).query(imageWhere);
            if (imagesResult != null && imagesResult.size() > 0) {
                img_url = imagesResult.get(0).img_url;
                if (imagesResult.size() > 1) {
                    tv_number_images.setVisibility(View.VISIBLE);
                    tv_number_images.setText("1/" + imagesResult.size());
                } else {
                    tv_number_images.setVisibility(View.GONE);
                }
                images.addAll(imagesResult);
                vp_goods_images.resetHeight(0);
                imageAdapter.notifyDataSetChanged();
            }
            if (PreferenceUtil.isLogin()) {
                GoodsCollect collectWhere = new GoodsCollect();
                collectWhere.user_id = PreferenceUtil.getUserInfo().user_id;
                collectWhere.goods_id = detail.goods_id;
                List<GoodsCollect> collectResult = BaseDaoFactory.getInstance().getDataHelper(GoodsCollectDao.class, GoodsCollect.class).query(collectWhere);
                if (collectResult != null && collectResult.size() == 1) {
                    Drawable drawable = getResources().getDrawable(R.mipmap.ic_goods_details_collect_select);
                    drawable.setBounds(0, 0, icon_size, icon_size);
                    tv_collect.setCompoundDrawables(null, drawable, null, null);
                    is_collect = true;
                } else {
                    Drawable drawable = getResources().getDrawable(R.mipmap.ic_goods_details_collect);
                    drawable.setBounds(0, 0, icon_size, icon_size);
                    tv_collect.setCompoundDrawables(null, drawable, null, null);
                    is_collect = false;
                }
            } else {
                is_collect = false;
            }
        }
        insertHistory();
    }

    private void insertHistory() {
        if (detail == null) {
            return;
        }
        if (PreferenceUtil.isLogin()) {
            GoodsHistoryDao historyDao = BaseDaoFactory.getInstance().getDataHelper(GoodsHistoryDao.class, GoodsHistory.class);
            GoodsHistory where = new GoodsHistory();
            where.goods_id = goods_id;
            where.user_id = PreferenceUtil.getUserInfo().user_id;
            List<GoodsHistory> result = historyDao.query(where);
            if (result != null && result.size() == 0) {
                where.add_time = String.valueOf(System.currentTimeMillis());
                where.goods_name = detail.goods_name;
                where.artist_name = detail.artist_name;
                where.goods_height = detail.goods_height;
                where.goods_width = detail.goods_width;
                where.goods_status = detail.goods_status;
                where.goods_status_tag = detail.goods_status_tag;
                where.goods_price = detail.goods_price;
                where.img_url = img_url;
                historyDao.insert(where);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finishActivity(this);
                break;
            case R.id.iv_right:
                intentActivity(this, CartActivity.class);
                break;
            case R.id.tv_preview:

                break;
            case R.id.tv_collect:
                if (!PreferenceUtil.isLogin()) {
                    intentActivity(this, UserLoginActivity.class);
                    return;
                }
                addCancelCollect(is_collect);
                break;
            case R.id.tv_add_cart:
                if (detail == null) {
                    return;
                }
                if (!PreferenceUtil.isLogin()) {
                    intentActivity(GoodsDetailActivity.this, UserLoginActivity.class);
                    return;
                }
                GoodsCart addCart = new GoodsCart();
                addCart.goods_id = detail.goods_id;
                addCart.user_id = PreferenceUtil.getUserInfo().user_id;
                GoodsCartDao cartDao = BaseDaoFactory.getInstance().getDataHelper(GoodsCartDao.class, GoodsCart.class);
                List<GoodsCart> result = cartDao.query(addCart);
                if (result != null) {
                    if (result.size() >= 1) {
                        ToastUtil.showMessage(this, R.string.hint_goods_in_cart);
                        return;
                    } else if (result.size() == 0) {
                        addCart.goods_name = detail.goods_name;
                        addCart.artist_name = detail.artist_name;
                        addCart.brand_name = detail.brand_name;
                        addCart.cat_name = detail.cat_name;
                        addCart.goods_height = detail.goods_height;
                        addCart.goods_width = detail.goods_width;
                        addCart.img_url = detail.img_url;
                        addCart.img_height = detail.goods_height;
                        addCart.img_width = detail.goods_width;
                        addCart.goods_price = detail.goods_price;
                        addCart.goods_status = detail.goods_status;
                        addCart.goods_status_tag = detail.goods_status_tag;
                        addCart.cart_type = "1";
                        addCart.add_time = String.valueOf(System.currentTimeMillis());
                        cartDao.insert(addCart);
                        ToastUtil.showMessage(this, R.string.success_add_cart);
                    }
                } else {
                }
                break;
            case R.id.tv_buy_now:
                if(detail==null){
                    return;
                }
                Bundle buy_now=new Bundle();
                buy_now.putSerializable("goods",detail);
                intentActivity(this,OrderCreateActivity.class,buy_now);
                break;
        }
    }

    private void addCancelCollect(boolean isCollect) {
        GoodsCollect collect = new GoodsCollect();
        collect.goods_id = detail.goods_id;
        collect.user_id = PreferenceUtil.getUserInfo().user_id;
        if (isCollect) {//取消
            int row = BaseDaoFactory.getInstance().getDataHelper(GoodsCollectDao.class, GoodsCollect.class).delete(collect);
            if (row == 1) {
                Drawable drawable = getResources().getDrawable(R.mipmap.ic_goods_details_collect);
                drawable.setBounds(0, 0, icon_size, icon_size);
                tv_collect.setCompoundDrawables(null, drawable, null, null);
                ToastUtil.showMessage(this, R.string.cancel_attention);
                is_collect = false;
            }
        } else {//添加
            collect.goods_name = detail.goods_name;
            collect.goods_price = detail.goods_price;
            collect.goods_width = detail.goods_width;
            collect.goods_height = detail.goods_height;
            if (!StringUtil.isEmpty(detail.artist_name)) {
                collect.artist_name = detail.artist_name;
            }
            collect.img_url = img_url;
            collect.goods_status = detail.goods_status;
            collect.goods_status_tag = detail.goods_status_tag;
            List<GoodsCollect> query = BaseDaoFactory.getInstance().getDataHelper(GoodsCollectDao.class, GoodsCollect.class).query(collect);
            if (query != null && query.size() == 0) {
                collect.add_time = String.valueOf(System.currentTimeMillis());
                long row = BaseDaoFactory.getInstance().getDataHelper(GoodsCollectDao.class, GoodsCollect.class).insert(collect);
                if (row != -1) {
                    Drawable drawable = getResources().getDrawable(R.mipmap.ic_goods_details_collect_select);
                    drawable.setBounds(0, 0, icon_size, icon_size);
                    tv_collect.setCompoundDrawables(null, drawable, null, null);
                    ToastUtil.showMessage(this, R.string.success_attention);
                    is_collect = true;
                }
            } else {
                is_collect = true;
            }
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private class ImageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            GoodsImage image = images.get(position);
            View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_good_banner, container, false);
            ImageView imageView = view.findViewById(R.id.iv_goods_image);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            if (!StringUtil.isEmpty(image.img_height) && !StringUtil.isEmpty(image.img_width)) {
                int height = (int) (Float.parseFloat(image.img_height) * ScreenUtils.getScreenWidth(GoodsDetailActivity.this) / Float.parseFloat(image.img_width));
                if (height >= max_height) {
                    int width = Integer.valueOf(image.img_width) * max_height / Integer.valueOf(image.img_height);
                    GlideApp.with(GoodsDetailActivity.this).load(image.img_url).override(width, height)
                            .error(R.mipmap.ic_image_default).into(imageView);
                } else {
                    GlideApp.with(GoodsDetailActivity.this).load(image.img_url).override(ScreenUtils.getScreenWidth(view.getContext()), height)
                            .error(R.mipmap.ic_image_default).fitCenter().into(imageView);
                }
            } else {
                GlideApp.with(GoodsDetailActivity.this).load(image.img_url).override(ScreenUtils.getScreenWidth(view.getContext()), ScreenUtils.getScreenWidth(view.getContext()))
                        .error(R.mipmap.ic_image_default).fitCenter().into(imageView);
            }
            vp_goods_images.setObjectForPosition(view, position);
            container.addView(view);
            return view;
        }
    }
}
