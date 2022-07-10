package com.sleticalboy.user

import android.app.Application
import com.sleticalboy.router.IAppBridge
import com.sleticalboy.router.Router

/**
 * Created on 19-3-16.
 *
 * @author leebin
 */
class UserApp : Application(), IAppBridge {

    override fun onCreate() {
        super.onCreate()
        initRoute(this)
    }

    override fun initRoute(app: Application) {
        Router.get().register(UserRoute())
    }
}
