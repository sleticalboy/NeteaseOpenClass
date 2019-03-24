package com.sleticalboy.login

import android.content.Context
import android.content.Intent
import com.sleticalboy.login.ui.login.LoginActivity
import com.sleticalboy.router.AbsLoginRoute

class LoginRoute : AbsLoginRoute() {

    override fun startMain(context: Context) {
        context.startActivity(Intent(context, LoginActivity::class.java))
    }
}