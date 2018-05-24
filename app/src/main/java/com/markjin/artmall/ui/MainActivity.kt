package com.markjin.artmall.ui

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.markjin.artmall.BaseActivity
import com.markjin.artmall.R
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem
import com.luseen.luseenbottomnavigation.BottomNavigation.OnBottomNavigationItemClickListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    private var homeFragment: HomeFragment? = null
    private var goodsFragment: GoodsFragment? = null
    private var exhibitionFragment: CartActivity.ExhibitionFragment? = null
    private var userFragment: UserFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFragment()
        initNav()
    }

    override fun onResume() {
        super.onResume()

    }


    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putInt("index", 0)
        super.onSaveInstanceState(outState)
    }

    private fun initFragment() {
        if (homeFragment == null) {
            homeFragment = HomeFragment()
            supportFragmentManager.beginTransaction().add(R.id.fragment_content, homeFragment).commit()
        }
        if (goodsFragment == null) {
            goodsFragment = GoodsFragment()
        }
        if (exhibitionFragment == null) {
            exhibitionFragment = CartActivity.ExhibitionFragment()
        }
        if (userFragment == null) {
            userFragment = UserFragment()
        }
    }

    private fun initNav() {//have a bug when init the active is not show
        navigation_bottom.setItemActiveColorWithoutColoredBackground(ContextCompat.getColor(this, R.color.color_22))
        var home = BottomNavigationItem(getString(R.string.title_home), ContextCompat.getColor(this, R.color.color_22), R.mipmap.ic_nav_home_normal)
        navigation_bottom.addTab(home)
        var art = BottomNavigationItem(getString(R.string.title_art), ContextCompat.getColor(this, R.color.color_22), R.mipmap.ic_nav_art_normal)
        navigation_bottom.addTab(art)
        var exhibition = BottomNavigationItem(getString(R.string.title_exhibition), ContextCompat.getColor(this, R.color.color_22), R.mipmap.ic_nav_exhibition_normal)
        navigation_bottom.addTab(exhibition)
//        var finance = BottomNavigationItem(getString(R.string.title_exhibition), ContextCompat.getColor(this, R.color.color_22), R.mipmap.ic_nav_finance_normal)
//        navigation_bottom.addTab(finance)
        var user = BottomNavigationItem(getString(R.string.title_user), ContextCompat.getColor(this, R.color.color_22), R.mipmap.ic_nav_user_normal)
        navigation_bottom.addTab(user)
        navigation_bottom.setOnBottomNavigationItemClickListener(OnBottomNavigationItemClickListener { index ->
            when (index) {
                0 -> {
                    if (homeFragment == null) {
                        homeFragment = HomeFragment()
                        supportFragmentManager.beginTransaction().add(R.id.fragment_content, homeFragment).hide(userFragment).hide(goodsFragment).hide(exhibitionFragment).commit()
                    } else {
                        supportFragmentManager.beginTransaction().replace(R.id.fragment_content, homeFragment).show(homeFragment).hide(userFragment).hide(goodsFragment).hide(exhibitionFragment).commit()
                    }
                }
                1 -> {
                    if (goodsFragment == null) {
                        goodsFragment = GoodsFragment()
                        supportFragmentManager.beginTransaction().add(R.id.fragment_content, goodsFragment).hide(userFragment).hide(homeFragment).hide(exhibitionFragment).commit()
                    } else {
                        supportFragmentManager.beginTransaction().replace(R.id.fragment_content, goodsFragment).show(goodsFragment).hide(userFragment).hide(homeFragment).hide(exhibitionFragment).commit()
                    }
                }
                2 -> {
                    if (exhibitionFragment == null) {
                        exhibitionFragment = CartActivity.ExhibitionFragment()
                        supportFragmentManager.beginTransaction().add(R.id.fragment_content, exhibitionFragment).hide(homeFragment).hide(goodsFragment).hide(userFragment).commit()
                    } else {
                        supportFragmentManager.beginTransaction().replace(R.id.fragment_content, exhibitionFragment).show(exhibitionFragment).hide(homeFragment).hide(goodsFragment).hide(userFragment).commit()
                    }
                }
                3 -> {
                    if (userFragment == null) {
                        userFragment = UserFragment()
                        supportFragmentManager.beginTransaction().add(R.id.fragment_content, userFragment).hide(homeFragment).hide(goodsFragment).hide(exhibitionFragment).commit()
                    } else {
                        supportFragmentManager.beginTransaction().replace(R.id.fragment_content, userFragment).show(userFragment).hide(homeFragment).hide(goodsFragment).hide(exhibitionFragment).commit()
                    }
                }
            }
        })
    }
}
