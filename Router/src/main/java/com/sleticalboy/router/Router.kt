package com.sleticalboy.router

/**
 * Created on 19-3-16.
 * @author leebin
 */
class Router private constructor() {

    private val routeMap: MutableMap<String, IRoute> = mutableMapOf()

    /**
     * register route
     */
    fun registerRouter(route: IRoute) {
        routeMap[route.tag] = route
    }

    /**
     * login router
     */
    fun loginRouter(): AbsLoginRoute? = routeMap[IRoute.LOGIN] as AbsLoginRoute?

    /**
     * user info router
     */
    fun userRouter(): AbsUserRoute? = routeMap[IRoute.USER] as AbsUserRoute?

    private object Singleton {
        val router = Router()
    }

    companion object {
        fun get(): Router = Singleton.router
    }
}