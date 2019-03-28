package com.sleticalboy.ic

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_image.*
import java.io.File
import java.util.*

class ImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        helloWorld.text = ImageActivity::class.java.name
        Log.d(javaClass.simpleName, "uuid -> " + UUID.randomUUID().toString())

        helloWorld.setOnClickListener {
            compressImage()
            val sum = JNIHelper.get().sum(intArrayOf(22, 33))
            helloWorld.append(" -> $sum")
        }
    }

    private fun compressImage() {
        val dir = File("/sdcard/")
        val src = File(dir, "original.jpg")
        val dest = File(dir, "compressed.jpg")
        JNIHelper.get().compress(src, 50, dest.absolutePath)
        image_view.setImageURI(Uri.fromFile(dest))
    }

}
