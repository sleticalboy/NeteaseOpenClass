package com.sleticalboy.user

import android.content.Context
import android.content.Intent
import com.sleticalboy.router.AbsUserRoute

/**
 * Created on 19-3-16.
 * @author leebin
 */
class UserRoute : AbsUserRoute() {

    override fun startMain(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
    }
}