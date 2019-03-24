package com.sleticalboy.ioc.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sleticalboy.ioc.annotation.AnnotationParser

/**
 * Created on 19-3-20.
 *
 * @author leebin
 */
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AnnotationParser.parseAnnotations(this)
    }
}
