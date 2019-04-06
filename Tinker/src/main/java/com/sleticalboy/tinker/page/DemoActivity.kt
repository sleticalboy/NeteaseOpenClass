package com.sleticalboy.tinker.page

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.sleticalboy.tinker.R
import com.sleticalboy.tinker.base.BaseActivity
import com.sleticalboy.tinker.fake.Bug
import com.sleticalboy.util.FileUtils
import com.sleticalboy.util.HotfixHelper
import kotlinx.android.synthetic.main.activity_demo.*
import java.io.File

/**
 * Created on 19-4-5.
 * @author leebin
 */
class DemoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        doCrash.setOnClickListener { doCrash() }
        doFix.setOnClickListener { doFix() }
    }

    private fun doCrash() {
        result.append(Bug().bug())
    }

    private fun doFix() {
        // 模拟下载下来的修复文件
        // /storage/emulated/0/classes2.dex
        val source = File("/sdcard/classes2.dex")
        // 将 source 复制到 base-apk 所在的目录
        if (FileUtils.copy(source, File(getDir(HotfixHelper.DEX_DIR, Context.MODE_PRIVATE), "classes2.dex"))) {
            HotfixHelper.loadHotfixFiles(application)
            if (source.delete()) {
                Log.d("DemoActivity", "delete src: $source")
            }
        }
    }
}