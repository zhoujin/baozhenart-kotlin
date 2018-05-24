package com.markjin.artmall.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.markjin.artmall.BaseActivity
import com.markjin.artmall.R
import kotlinx.android.synthetic.main.activity_guide.*

/**
 * guide page
 */
class GuideActivity : BaseActivity() {

    val image_guide = intArrayOf(R.mipmap.ic_guide_01, R.mipmap.ic_guide_02, R.mipmap.ic_guide_03)
    var currentIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)
        var params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.setMargins(2, 0, 2, 0)
        for (i in 0 until image_guide.size) {
            var iv_dot = ImageView(this)
            iv_dot.setPadding(3, 3, 3, 3)
            iv_dot.layoutParams = params
            if (i == 0) {
                iv_dot.setImageResource(R.drawable.ic_dot_guide_select)
            } else {
                iv_dot.setImageResource(R.drawable.ic_dot_guide_default)
            }
            ll_dot.addView(iv_dot)
        }
        vp_guide.adapter = GuideAdapter()
        vp_guide.addOnPageChangeListener(GuidePagerListener())
        bt_guide_into.setOnClickListener(View.OnClickListener {
            finishActivity(this)
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        })
    }

    private inner class GuidePagerListener : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(arg0: Int) {}

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}

        override fun onPageSelected(position: Int) {
            if (position == image_guide.size - 1) {
                bt_guide_into.visibility = View.VISIBLE
            } else {
                bt_guide_into.visibility = View.GONE
            }
            currentIndex = position
            setCurView(currentIndex)
            for (i in 0 until ll_dot.childCount) {
                if (position == i) {
                    (ll_dot.getChildAt(i) as ImageView).setImageResource(R.drawable.ic_dot_guide_select)
                } else {
                    (ll_dot.getChildAt(i) as ImageView).setImageResource(R.drawable.ic_dot_guide_default)
                }
            }
        }
    }

    private fun setCurView(position: Int) {
        if (position < 0 || position >= image_guide.size) {
            return
        }
        vp_guide.setCurrentItem(position)
    }

    internal inner class GuideAdapter : PagerAdapter() {
        override fun getCount(): Int {
            return image_guide.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            var imageView = ImageView(container.context)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            imageView.setImageResource(image_guide[position])
            if (position == image_guide.size - 1) {
                bt_guide_into.visibility = View.VISIBLE
                imageView.setOnClickListener(View.OnClickListener {
                    finishActivity(this@GuideActivity)
                    var intent = Intent(this@GuideActivity, MainActivity::class.java)
                    startActivity(intent)
                })
            } else {
                bt_guide_into.visibility = View.GONE
            }
            container.addView(imageView, 0)
            return imageView
        }
    }
}