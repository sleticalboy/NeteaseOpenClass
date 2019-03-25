package com.sleticalboy.ldbus

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LiveDataBus.get().observe("circle-return", this@MainActivity, Observer<Int> {
            textContainer.text = "MainActivity----> $it"
            Toast.makeText(application, "MainActivity----> $it", Toast.LENGTH_SHORT).show()
        })
    }

    // 跳转页面并发送事件
    fun postEvent(view: View) {
        Handler(mainLooper).postDelayed({ LiveDataBus.get().postValue("circle-forward", 9) }, 5000L)
        startActivity(Intent(view.context, SecondActivity::class.java))
    }
}
