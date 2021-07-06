package com.sleticalboy.ioc

import android.widget.TextView
import android.widget.Toast
import com.sleticalboy.ioc.annotation.GetView
import com.sleticalboy.ioc.annotation.OnClick
import com.sleticalboy.ioc.annotation.OnLongClick
import com.sleticalboy.ioc.annotation.SetContentView
import com.sleticalboy.ioc.base.BaseActivity

/**
 * Created on 19-3-20.
 *
 * @author leebin
 */
@SetContentView(R.layout.activity_annotation)
class AnnotationActivity : BaseActivity() {

    @GetView(R.id.helloWorld)
    private val helloWorld: TextView? = null

    override fun onResume() {
        super.onResume()
        helloWorld!!.text = "通过注解赋值"
    }

    @OnClick(R.id.image_view)
    fun toast() {
        Toast.makeText(this, "onClick annotation", Toast.LENGTH_SHORT).show()
    }

    @OnLongClick(R.id.image_view)
    fun anotherToast(): Boolean {
        Toast.makeText(this, "onLongClick annotation", Toast.LENGTH_SHORT).show()
        return true
    }
}
