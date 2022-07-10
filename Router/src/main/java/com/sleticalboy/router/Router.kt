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
    fun register(route: IRoute) {
        routeMap[route.tag] = route
    }

    /**
     * login router
     */
    fun loginRouter(): ILoginRoute? = routeMap[IRoute.LOGIN] as ILoginRoute?

    /**
     * user info router
     */
    fun userRouter(): AbsUserRoute? = routeMap[IRoute.USER] as AbsUserRoute?

    companion object {

        val router = Router()

        fun get(): Router = router
    }
}