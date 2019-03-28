package com.sleticalboy.noc;

import android.app.Application;
import android.content.Context;
import com.sleticalboy.router.IAppBridge;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 19-3-16.
 *
 * @author leebin
 */
public class MainApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initRoute(this);
    }

    public void initRoute(@NotNull Application app) {
        for (String bridgeCls : IAppBridge.Companion.bridges()) {
            try {
                final Class<?> clazz = Class.forName(bridgeCls);
                final Object bridge = clazz.newInstance();
                if (bridge instanceof IAppBridge) {
                    ((IAppBridge) bridge).initRoute(app);
                }
            } catch (Exception ignored) {
            }
        }
    }
}