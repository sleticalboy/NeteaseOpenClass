package com.sleticalboy.util;

import android.util.Log;
import java.lang.reflect.Field;

/**
 * Created on 19-4-5.
 *
 * @author leebin
 */
public final class ReflectUtils {

  private static final String TAG = "ReflectUtils";

  public static <CL extends ClassLoader> void setSystemDexElements(CL cl, Object value) {
    final Object pathList = getPathList(cl);
    if (pathList != null) {
      try {
        final Field dexElements = pathList.getClass().getDeclaredField("dexElements");
        if (!dexElements.isAccessible()) {
          dexElements.setAccessible(true);
        }
        dexElements.set(pathList, value);
      } catch (NoSuchFieldException | IllegalAccessException e) {
        Log.e(TAG, "set dexElements to pathList cl: " + cl + ", value: " + value, e);
      }
    }
  }

  private static <CL extends ClassLoader> Object getPathList(CL cl) {
    try {
      Class baseDexClassLoader = Class.forName("dalvik.system.BaseDexClassLoader");
      final Field pathList = baseDexClassLoader.getDeclaredField("pathList");
      if (!pathList.isAccessible()) {
        pathList.setAccessible(true);
      }
      return pathList.get(cl);
    } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException e) {
      Log.e(TAG, "get pathList cl: " + cl + ", error" + e.getMessage(), e);
      return null;
    }
  }

  public static <CL extends ClassLoader> Object getDexElements(CL cl) {
    final Object pathList = getPathList(cl);
    if (pathList != null) {
      try {
        final Field dexElements = pathList.getClass().getDeclaredField("dexElements");
        if (!dexElements.isAccessible()) {
          dexElements.setAccessible(true);
        }
        return dexElements.get(pathList);
      } catch (IllegalAccessException | NoSuchFieldException e) {
        return null;
      }
    }
    return null;
  }
}
