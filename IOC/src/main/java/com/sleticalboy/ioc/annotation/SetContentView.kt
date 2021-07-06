package com.sleticalboy.ioc.annotation

/**
 * Created on 19-3-20.
 *
 * @author leebin
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class SetContentView(
    /**
     * 布局文件
     */
    val value: Int = -1
)
