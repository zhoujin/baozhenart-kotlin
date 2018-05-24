package com.markjin.artmall.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 首页viewpager adapter
 */
public class HomePagerAdapter extends PagerAdapter {
    private ArrayList<View> pageViews;

    public HomePagerAdapter(ArrayList<View> pageViews) {
        this.pageViews = pageViews;
    }

    @Override
    public int getCount() {
        if (pageViews.size() > 2) {
            return Integer.MAX_VALUE;
        } else {
            return pageViews.size();
        }
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public void destroyItem(@NonNull View container, int position, @NonNull Object object) {

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (pageViews.get(position % pageViews.size()).getParent() != null) {
            ((ViewPager) pageViews.get(position % pageViews.size()).getParent()).removeView(pageViews.get(position % pageViews.size()));
        }
        container.addView(pageViews.get(position % pageViews.size()), 0);
        return pageViews.get(position % pageViews.size());
    }
}
