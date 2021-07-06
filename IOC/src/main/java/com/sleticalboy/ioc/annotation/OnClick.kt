package com.sleticalboy.ioc.annotation

/**
 * Created on 19-3-20.
 *
 * @author leebin
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@BaseEvent
annotation class OnClick(
    /**
     * 控件 ids
     */
    vararg val value: Int = []
)
