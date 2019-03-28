package com.sleticalboy.ic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Created on 19-3-17.
 *
 * @author leebin
 */
public final class JNIHelper {

    private static final Charset UFT_8 = Charset.forName("UTF-8");
    private static final JNIHelper HELPER = new JNIHelper();

    static {
        Log.d("JNIHelper", "load library native demo");
        System.loadLibrary("native-demo");
    }

    private JNIHelper() {
    }

    public static JNIHelper get() {
        return HELPER;
    }

    public void compress(@NonNull String src, int quality, String outPath) {
        compress(new File(src), quality, outPath);
    }

    public void compress(@NonNull File src, int quality, String outPath) {
        try {
            compress(new FileInputStream(src), quality, outPath);
        } catch (FileNotFoundException e) {
            Log.e("JNIHelper", "compress: " + e.getMessage(), e);
        }
    }

    public void compress(@NonNull InputStream src, int quality, String outPath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        final Bitmap bitmap = BitmapFactory.decodeStream(src, null, options);
        compress(bitmap, quality, outPath);
    }

    public void compress(Bitmap src, int quality, String outPath) {
        if (nCompress(src, quality, outPath)) {
            Log.d("JNIHelper", "compress success");
        } else {
            Log.d("JNIHelper", "compress failure");
        }
    }

    public int sum(int[] array) {
        return nArraySum(array);
    }

    public void sayHello() {
        dynamicRegister(Long.MAX_VALUE);
    }

    /////////////////静态注册///////////////////////
    // 先由Java得到本地方法的声明，然后再通过JNI实现该声明方法

    /**
     * 图片压缩
     *
     * @param bitmap
     * @param quality
     * @param outPath
     * @return
     */
    private native boolean nCompress(Bitmap bitmap, int quality, String outPath);

    /**
     * 数组求和
     *
     * @param array
     * @return
     */
    private native int nArraySum(int[] array);

    /////////////////动态注册///////////////////////
    // 先通过JNI重载JNI_OnLoad()实现本地方法，然后直接在Java中调用本地方法。

    private native void dynamicRegister(long arg);
}
