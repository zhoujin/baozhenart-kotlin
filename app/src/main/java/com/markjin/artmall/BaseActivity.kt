package com.markjin.artmall

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity


abstract class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super<AppCompatActivity>.onResume()
    }


    override fun onPause() {
        super<AppCompatActivity>.onPause()
    }


    override fun onDestroy() {
        super<AppCompatActivity>.onDestroy()
    }


    fun intentActivity(context: Activity, obtain: Class<*>) {
        val intent = Intent(context, obtain)
        startActivity(intent)
    }

    fun intentActivity(context: Activity, obtains: Class<*>, bundle: Bundle) {
        var intent = Intent(context, obtains)
        intent.putExtras(bundle)
        context.startActivity(intent)
    }

    fun finishActivity(context: Activity) {
        context.finish()
    }
}