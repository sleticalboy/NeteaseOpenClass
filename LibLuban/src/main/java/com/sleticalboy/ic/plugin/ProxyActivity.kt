package com.sleticalboy.ic.plugin

import android.app.Activity
import android.content.Intent
import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Created on 19-3-19.
 *
 * @author leebin
 */
class ProxyActivity : AppCompatActivity(), IPlugin {

    private var delegateActivity: Activity? = null

    override fun onAttach(delegate: Activity) {
        delegateActivity = delegate
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun getResources(): Resources {
        return super.getResources()
    }

    override fun getClassLoader(): ClassLoader {
        return super.getClassLoader()
    }

    override fun getAssets(): AssetManager {
        return super.getAssets()
    }

}
