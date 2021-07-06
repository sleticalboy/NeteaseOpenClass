package com.sleticalboy.noc;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import com.sleticalboy.router.IAppBridge;

/**
 * Created on 19-3-16.
 *
 * @author leebin
 */
public class MainApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initRoute(this);
    }

    public void initRoute(@NonNull Application app) {
        for (String klass : IAppBridge.Companion.bridges()) {
            try {
                Object obj = Class.forName(klass).newInstance();
                if (obj instanceof IAppBridge) {
                    ((IAppBridge) obj).initRoute(app);
                }
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Throwable t) {
                throw new RuntimeException("initRoute: " + klass + " failed.", t);
            }
        }
    }
}
