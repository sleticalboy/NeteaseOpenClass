package com.sleticalboy.tinker;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;
import com.sleticalboy.util.HotfixHelper;

/**
 * Created on 19-4-2.
 *
 * @author leebin
 */
public class BaseApp extends Application {

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(base);
  }

  @Override
  public void onCreate() {
    super.onCreate();
    HotfixHelper.loadHotfixFiles(this);
  }
}
