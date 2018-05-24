package com.markjin.artmall.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import cn.jpush.android.api.JPushInterface
import com.markjin.artmall.AppContext
import com.markjin.artmall.BaseActivity
import com.markjin.artmall.R
import com.markjin.artmall.utils.FileUtils
import com.markjin.artmall.utils.PreferenceUtil
import com.markjin.artmall.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.fragment_user.*

/**
 *
 */
class SettingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initView()
    }

    override fun onResume() {
        super.onResume()
        if (PreferenceUtil.isLogin()) {
            bt_quit.visibility = View.VISIBLE
        } else {
            bt_quit.visibility = View.GONE
        }
    }

    fun initView() {
        tv_title.setText(R.string.setting)
        iv_left.setOnClickListener(View.OnClickListener {
            finishActivity(this)
        })
        if (PreferenceUtil.getBoolean("is_push", true)) {
            sw_push.setChecked(true)
        } else {
            sw_push.setChecked(false)
        }
        sw_push.setOnCheckedChangeListener { _, b ->
            if (b) {
                PreferenceUtil.putBoolean("is_push", true)
                if (JPushInterface.isPushStopped(applicationContext)) {
                    JPushInterface.resumePush(applicationContext)
                }
            } else {//推送关闭
                PreferenceUtil.putBoolean("is_push", false)
                if (!JPushInterface.isPushStopped(applicationContext)) {
                    JPushInterface.stopPush(applicationContext)
                }
            }
        }
        rl_clear.setOnClickListener(View.OnClickListener {
            clear()
        })
        bt_quit.setOnClickListener(View.OnClickListener {
            showQuitDialog(getString(R.string.hint), getString(R.string.quit_login_hint))
        })
    }

    private fun showQuitDialog(title: String, content: String) {
        val dialog = Dialog(this, R.style.customDialogStyle)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_middle_normal, null)
        view.findViewById<TextView>(R.id.tv_dialog_title).visibility = View.GONE
        view.findViewById<TextView>(R.id.tv_dialog_title).text = title
        view.findViewById<TextView>(R.id.tv_dialog_content).text = content
        dialog.setContentView(view)
        view.findViewById<Button>(R.id.bt_dialog_left).setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        view.findViewById<Button>(R.id.bt_dialog_right).setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            if (PreferenceUtil.isLogin()) {
                PreferenceUtil.clearUser()
                bt_quit.visibility = View.GONE
            }
        })
        dialog.window!!.setLayout(AppContext.instance.getDisplayMetrics().widthPixels - 160, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialog.show()
    }

    private fun clear() {
        Thread(Runnable {
            try {
                FileUtils.deleteDir(cacheDir)
                runOnUiThread {
                    ToastUtil.showMessage(this@SettingActivity, getString(R.string.success_clear))
                }
            } catch (e: Exception) {
                runOnUiThread {
                    ToastUtil.showMessage(this@SettingActivity, getString(R.string.success_clear))
                }
                e.printStackTrace()
            } finally {
            }
        }).start()
    }
}