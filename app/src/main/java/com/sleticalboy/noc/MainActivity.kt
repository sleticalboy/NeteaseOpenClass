package com.sleticalboy.noc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sleticalboy.router.Router
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startUser.setOnClickListener {
            Router.get().userRouter()?.startMain(it.context)
        }
        startLogin.setOnClickListener {
            Router.get().loginRouter()?.startMain(it.context)
        }
    }
}
