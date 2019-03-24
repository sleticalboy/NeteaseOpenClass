package com.sleticalboy.ioc.annotation

/**
 * Created on 19-3-20.
 *
 * @author leebin
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class GetView(
    /**
     * 控件 id
     */
    val value: Int = -1
)
