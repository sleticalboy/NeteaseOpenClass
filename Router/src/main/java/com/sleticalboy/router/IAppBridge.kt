package com.sleticalboy.router

import android.app.Application

/**
 * Created on 19-3-16.
 * @author leebin
 */
interface IAppBridge {

    fun initRoute(app: Application)

    companion object {
        fun bridges(): Array<String> = arrayOf(
            "com.sleticalboy.login.LoginApp",
            "com.sleticalboy.user.UserApp"
        )
    }
}