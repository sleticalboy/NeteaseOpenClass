package com.sleticalboy.router

/**
 * Created on 19-3-16.
 * @author leebin
 */
abstract class AbsLoginRoute : IRoute {

    final override fun getTag(): String {
        return IRoute.LOGIN
    }
}