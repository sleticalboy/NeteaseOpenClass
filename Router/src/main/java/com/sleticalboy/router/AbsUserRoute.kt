package com.sleticalboy.router

/**
 * Created on 19-3-16.
 * @author leebin
 */
abstract class AbsUserRoute : IRoute {

    final override fun getTag(): String {
        return IRoute.USER
    }
}