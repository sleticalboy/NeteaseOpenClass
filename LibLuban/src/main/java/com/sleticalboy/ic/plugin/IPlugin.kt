package com.sleticalboy.ic.plugin

import android.app.Activity
import android.content.Intent
import android.os.Bundle

/**
 * Created on 19-3-19.
 *
 * @author leebin
 */
interface IPlugin {

    fun onAttach(delegate: Activity)

    fun onCreate(saved: Bundle?)

    fun onStart()

    fun onResume()

    fun onPause()

    fun onStop()

    fun onRestart()

    fun onDestroy()

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    companion object {
        val FROM_EXTERNAL = 0
        val FROM_INTERNAL = 1
    }
}
