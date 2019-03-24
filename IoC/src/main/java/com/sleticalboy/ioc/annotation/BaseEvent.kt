package com.sleticalboy.ioc.annotation

import android.view.View
import kotlin.reflect.KClass

/**
 * Created on 19-3-20.
 *
 * @author leebin
 */
const val SETTER_ON_CLICK = "setOnClickListener"
const val CALLBACK_VIEW_ON_CLICK = "onClick"
const val SETTER_ON_LONG_CLICK = "setOnLongClickListener"
const val CALLBACK_VIEW_ON_LONG_CLICK = "onLongClick"

@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class BaseEvent(
    val eventSetter: String = SETTER_ON_CLICK,
    val eventCls: KClass<*> = View.OnClickListener::class,
    val eventCallback: String = CALLBACK_VIEW_ON_CLICK
)
