package com.sleticalboy.ioc.annotation

import android.view.View

/**
 * Created on 19-3-20.
 *
 * @author leebin
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@BaseEvent(
    SETTER_ON_LONG_CLICK,
    View.OnLongClickListener::class,
    CALLBACK_VIEW_ON_LONG_CLICK
)
annotation class OnLongClick(
    /**
     * 控件 ids
     */
    vararg val value: Int = []
)
