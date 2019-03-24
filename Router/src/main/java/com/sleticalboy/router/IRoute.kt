package com.sleticalboy.router

import android.content.Context
import androidx.annotation.NonNull

/**
 * Created on 19-3-16.
 * @author leebin
 */
interface IRoute {

    companion object {
        const val LOGIN = "module_login"
        const val USER = "module_user"
    }

    /**
     * tag for this router
     */
    @NonNull
    fun getTag(): String

    /**
     * start index page for this module
     */
    fun startMain(context: Context)
}