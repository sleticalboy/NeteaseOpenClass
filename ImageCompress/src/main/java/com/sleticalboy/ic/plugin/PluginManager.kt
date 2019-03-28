package com.sleticalboy.ic.plugin

import android.content.res.AssetManager
import android.content.res.Resources

/**
 * Created on 19-3-19.
 *
 * @author leebin
 */
class PluginManager private constructor() {

    var pluginApk: PluginApk? = null

    private object Singleton {
        internal val MANAGER = PluginManager()
    }

    fun loadApk(apkPath: String) {}

    companion object {

        fun get(): PluginManager {
            return Singleton.MANAGER
        }
    }

    fun getResource(): Resources? = pluginApk?.resources

    fun getClassLoader(): ClassLoader? = pluginApk?.classLoader

    fun getAssets(): AssetManager? = pluginApk?.assertManager
}
