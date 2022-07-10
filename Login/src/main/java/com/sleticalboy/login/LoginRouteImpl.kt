package com.sleticalboy.login

import android.content.Context
import android.content.Intent
import com.sleticalboy.login.ui.login.LoginActivity
import com.sleticalboy.router.ILoginRoute
import com.sleticalboy.router.IRoute

class LoginRouteImpl : ILoginRoute {

    override val tag: String = IRoute.LOGIN

    override fun startMain(context: Context) {
        context.startActivity(Intent(context, LoginActivity::class.java))
    }
}