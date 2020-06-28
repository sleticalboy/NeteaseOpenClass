package com.sleticalboy.ic

import android.Manifest
import android.net.Uri
import android.os.Build
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), 702
                )
            } else {
                compressImage()
            }
        }
        button1.setOnClickListener {
            val sum = NativeHelper.sum(intArrayOf(22, 33, 45))
            button1.text = "native sum is $sum"
        }
        button2.setOnClickListener {
            NativeHelper.sayHello(this)
        }
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 702) {
            compressImage()
        }
    }

    private fun compressImage() {
        val dir = File("/sdcard/")
        val src = File(dir, "original.jpg")
        val dest = File(dir, "compressed.jpg")
        val start = System.currentTimeMillis()
        NativeHelper.compressImage(src, 60, dest.absolutePath)
        val cost = System.currentTimeMillis() - start
        Log.d("imageActivity", "compress cost $cost ms")
        image_view.setImageURI(Uri.fromFile(dest))
    }

}
