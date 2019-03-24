package com.sleticalboy.router

import androidx.annotation.NonNull
import androidx.annotation.Nullable

/**
 * Created on 19-3-16.
 * @author leebin
 */
class Router private constructor() {

    private val routeMap: MutableMap<String, IRoute> = mutableMapOf()

    /**
     * register route
     */
    fun registerRouter(@NonNull route: IRoute) {
        routeMap[route.getTag()] = route
    }

    /**
     * login router
     */
    @Nullable
    fun loginRouter(): AbsLoginRoute? = routeMap[IRoute.LOGIN] as AbsLoginRoute?

    /**
     * user info router
     */
    @Nullable
    fun userRouter(): AbsUserRoute? = routeMap[IRoute.USER] as AbsUserRoute?

    private object Singleton {
        val router = Router()
    }

    companion object {
        fun get(): Router = Singleton.router
    }
}