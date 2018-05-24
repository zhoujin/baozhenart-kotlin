package com.markjin.artmall

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.markjin.artmall.ui.GuideActivity
import com.markjin.artmall.ui.MainActivity

/**
 * welcome page
 */
class WelcomeActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isTaskRoot) {//解决下载安装后出现的home 再次进入界面异常的问题。
            val mainIntent = intent
            val action = mainIntent.action
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action == Intent.ACTION_MAIN) {
                finish()
                return
            }
        }
        setContentView(R.layout.activity_welcome)
        initCheck()
    }

    fun initCheck() {
        var intent = Intent(this, DBRegionService::class.java)
        startService(intent)
        Handler().postDelayed({
            val preferences = getSharedPreferences("count", 0)
            var count = preferences.getInt("count", 0)
            // 第一次运行程序
            if (count == 0) {
                val editor = preferences.edit()
//                editor.putInt("count", ++count)
                editor.putInt("versionCode", AppContext.instance.getVersionCode())
                editor.commit()
                redirectToGuideView()
            } else {
                redirectToMain()
            }
        }, 2000)

    }

    fun redirectToMain() {
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
        finishActivity(this)
    }

    fun redirectToGuideView() {
        val intent = Intent(this, GuideActivity::class.java)
        this.startActivity(intent)
        finishActivity(this)
    }
}