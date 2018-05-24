package com.markjin.artmall.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 瀑布流形式之间的间隔设置
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private int type = 0;//0：为默认 纵向两列瀑布流 1：横向瀑布流 2：纵向list有左右边距 3:纵向list无左右边距有上下边距4: 纵向两列瀑布流 带有header

    //5：纵向两列，头部无边距，其他有边距 6:纵向 list有头部 无左右边距有下边距7:横向list 无左边距有右边距上下无边距8:纵向list 上左右无边距下有边距
    //9: 纵向两列瀑布流 带有header 上左右无边距下有边距 10 横向list 有左边距无右边距上下无边距
    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    public SpacesItemDecoration(int space, int type) {
        this.space = space;
        this.type = type;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (type == 0) {//两列瀑布流
            if (parent.getChildAdapterPosition(view) == 0 || parent.getChildAdapterPosition(view) == 1) {
                outRect.top = 0;
            }
            outRect.left = space / 2;
            outRect.right = space / 2;
            outRect.bottom = space;
        } else if (type == 1) {//横向list
            outRect.left = space / 2;
            outRect.right = space / 2;
            outRect.bottom = space;
            outRect.top = space;
        } else if (type == 2) {//纵向list有左右边距
            outRect.left = space / 2;
            outRect.right = space / 2;
            outRect.bottom = space;
            outRect.top = space;
        } else if (type == 3) {//纵向list无左右边距有上下边距
            outRect.left = 0;
            outRect.right = 0;
            outRect.bottom = space;
            outRect.top = space;
        } else if (type == 4) {
            if (parent.getChildAdapterPosition(view) == 1 || parent.getChildAdapterPosition(view) == 2) {
                outRect.top = space;
            }
            outRect.left = space / 2;
            outRect.right = space / 2;
            outRect.bottom = space;
        } else if (type == 5) {
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = 0;
                outRect.left = 0;
                outRect.right = 0;
                outRect.bottom = 0;
            } else {
                outRect.top = space;
                outRect.left = space;
                outRect.right = space;
                outRect.bottom = space;
            }
        } else if (type == 6) {
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = 0;
                outRect.left = 0;
                outRect.right = 0;
                outRect.bottom = 0;
            } else {
                outRect.left = 0;
                outRect.right = 0;
                outRect.bottom = space;
                outRect.top = 0;
            }
        } else if (type == 7) {
            outRect.left = 0;
            outRect.right = space;
            outRect.bottom = 0;
            outRect.top = 0;
        } else if (type == 8) {
            outRect.left = 0;
            outRect.right = 0;
            outRect.bottom = space;
            outRect.top = 0;
        } else if (type == 9) {
            if (parent.getChildAdapterPosition(view) == 1 || parent.getChildAdapterPosition(view) == 2) {
                outRect.top = 0;
            }
            outRect.left = space / 2;
            outRect.right = space / 2;
            outRect.bottom = space;
        } else if (type == 10) {
            outRect.left = space;
            outRect.right = 0;
            outRect.bottom = 0;
            outRect.top = 0;
        }
    }
}
