package com.sleticalboy.router

import android.content.Context

/**
 * Created on 19-3-16.
 * @author leebin
 */
interface IRoute {

    /**
     * tag for this router
     */
    val tag: String

    companion object {
        const val LOGIN = "module_login"
        const val USER = "module_user"
    }

    /**
     * start index page for this module
     */
    fun startMain(context: Context)
}