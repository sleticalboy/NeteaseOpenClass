package com.sleticalboy.ldbus

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlin.concurrent.thread

/**
 * Created on 19-3-24.
 * @author leebin
 */
class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        LiveDataBus.get().observe("circle-forward", this@SecondActivity, Observer<Int> {
            Toast.makeText(application, "SecondActivity----> $it", Toast.LENGTH_SHORT).show()
        })
    }

    // 跳转页面并发送事件
    fun post(view: View) {
        Thread { LiveDataBus.get().postValue("circle-return", 999999) }.start()
        Handler(mainLooper).postDelayed({ finish() }, 5000L)
    }
}