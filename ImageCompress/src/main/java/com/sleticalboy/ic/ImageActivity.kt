package com.sleticalboy.ic

import android.net.Uri
import android.os.Bundle
import android.os.MessageQueue
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_image.*
import java.io.File
import java.util.*

class ImageActivity : AppCompatActivity() {

    var count = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        Log.d(javaClass.simpleName, "uuid -> " + UUID.randomUUID().toString())

        helloWorld.setOnClickListener {
            compressImage()
            val sum = JNIHelper.get().sum(intArrayOf(22, 33))
            Log.d("ImageActivity", "sum is $sum")
        }
        JNIHelper.get().sayHello()
        mainLooper.queue.addIdleHandler(object : MessageQueue.IdleHandler {
            override fun queueIdle(): Boolean {
//                Toast.makeText(application, "idle handler trigger", Toast.LENGTH_SHORT).show()
                Log.d("ImageActivity", "idle handler triggered $count times")
                count++
                if (count == 11) {
                    return false
                }
                return true
            }
        })
    }

    private fun compressImage() {
        val dir = File("/sdcard/")
        val src = File(dir, "original.jpg")
        val dest = File(dir, "compressed.jpg")
        val start = System.currentTimeMillis()
        JNIHelper.get().compress(src, 50, dest.absolutePath)
        val cost = System.currentTimeMillis() - start
        Log.d("imageActivity", "compress cost $cost ms")
        image_view.setImageURI(Uri.fromFile(dest))
    }

}
