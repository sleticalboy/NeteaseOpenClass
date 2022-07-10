package com.sleticalboy.noc

import android.app.Application
import com.sleticalboy.router.IAppBridge
import com.sleticalboy.router.IAppBridge.Companion.bridges

/**
 * Created on 19-3-16.
 *
 * @author leebin
 */
class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initRoute(this)
    }

    private fun initRoute(app: Application) {
        for (klass in bridges()) {
            try {
                val obj = Class.forName(klass).newInstance()
                if (obj is IAppBridge) obj.initRoute(app)
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (t: Throwable) {
                throw RuntimeException("initRoute: $klass failed.", t)
            }
        }
    }
}