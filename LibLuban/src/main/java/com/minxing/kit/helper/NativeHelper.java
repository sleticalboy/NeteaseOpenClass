package com.minxing.kit.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created on 19-7-4.
 *
 * @author leebin
 */
public final class NativeHelper {

    private static final String TAG = "Luban";

    static {
        Log.d(TAG, "load library jpeg-compress");
        System.loadLibrary("luban-compress");
    }

    public static void compressImage(@NonNull String src, int quality, String outPath) {
        compressImage(new File(src), quality, outPath);
    }

    public static void compressImage(@NonNull File src, int quality, String outPath) {
        try {
            compressImage(new FileInputStream(src), quality, outPath);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "compress: " + e.getMessage(), e);
        }
    }

    public static void compressImage(@NonNull InputStream src, int quality, String outPath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        final Bitmap bitmap = BitmapFactory.decodeStream(src, null, options);
        compressImage(bitmap, quality, outPath);
    }

    public static void compressImage(Bitmap bitmap, int quality, String outPath) {
        // quality = 60 就可以了
        if (nCompress(bitmap, quality, outPath)) {
            Log.d(TAG, "compress success");
        } else {
            Log.d(TAG, "compress failure");
        }
    }

    public static int sum(int[] array) {
        return nArraySum(array);
    }

    public static void sayHello(Context context) {
        nToast(context, "native toast: " + NativeHelper.class.getSimpleName());
    }

    /////////////////静态注册///////////////////////
    // 先由Java得到本地方法的声明，然后再通过JNI实现该声明方法

    /**
     * 数组求和
     *
     * @param array
     * @return
     */
    private static native int nArraySum(int[] array);

    /////////////////动态注册///////////////////////
    // 先通过JNI重载JNI_OnLoad()实现本地方法，然后直接在Java中调用本地方法。

    // native 方法不混淆
    private static native void nToast(Context context, CharSequence arg);

    /**
     * 图片压缩
     *
     * @param bitmap  原始 Bitmap
     * @param quality 质量
     * @param outPath 输出图片路径
     * @return true 成功， false 失败
     */
    private static native boolean nCompress(Bitmap bitmap, int quality, String outPath);
}
