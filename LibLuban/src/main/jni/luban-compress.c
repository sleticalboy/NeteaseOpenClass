//
// Created by leebin on 19-3-28.
//

#include <jni.h>
#include <stdint.h>
#include <string.h>
#include <android/log.h>
#include <android/bitmap.h>
#include <malloc.h>
#include "include/jpeglib.h"

#include <stdio.h>
#include <stdlib.h>

#define LOG_TAG "Luban"
#define LOG_I(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOG_E(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

/**
 * 鲁班压缩核心代码
 * @param data 图片数组
 * @param width 图片宽
 * @param height  图片高
 * @param quality 压缩质量
 * @param outfile 输出文件
 * @return 0 表示出错，其他正常
 */
int writeIntoImage(uint8_t *data, int width, int height, jint quality, const char *outfile) {
    struct jpeg_compress_struct compress_struct; // 声明结构体
    struct jpeg_error_mgr error_mgr;
    compress_struct.err = jpeg_std_error(&error_mgr); // 设置错误函数
    jpeg_create_compress(&compress_struct); // 创建结构体
    FILE *image = fopen(outfile, "wb"); // 打开输出文件
    if (image == NULL) {
        LOG_E("open file error %s", outfile);
        return JNI_FALSE;
    }
    jpeg_stdio_dest(&compress_struct, image); // 设置输出文件句柄

    // 配置信息
    compress_struct.image_width = (JDIMENSION) width; // 宽
    compress_struct.image_height = (JDIMENSION) height; // 高
    // TRUE=arithmetic coding, FALSE=Huffman false 表示启用 Huffman 算法
    compress_struct.arith_code = FALSE; // 启用哈夫曼编码
    compress_struct.input_components = 3; // 像素值只取 rgb
    compress_struct.in_color_space = JCS_RGB;
    compress_struct.optimize_coding = TRUE;
    jpeg_set_defaults(&compress_struct); // 设置默认参数
    jpeg_set_quality(&compress_struct, quality, TRUE); // 设置压缩质量

    // 开始压缩
    jpeg_start_compress(&compress_struct, TRUE);

    unsigned int row_stride = compress_struct.image_width * compress_struct.input_components;
    JSAMPROW row_ptr[1];
    // 循环写入文件
    while (compress_struct.next_scanline < compress_struct.image_height) {
        row_ptr[0] = &data[compress_struct.next_scanline * row_stride];
        jpeg_write_scanlines(&compress_struct, row_ptr, 1);
    }

    // 释放资源
    jpeg_finish_compress(&compress_struct);
    jpeg_destroy_compress(&compress_struct);
    fclose(image);
    return JNI_TRUE;
}

jboolean JNICALL nCompress(JNIEnv *env, jclass clazz, jobject bitmap, jint quality,
                           jstring outPath_) {
    LOG_I("native start compress quality is %d", quality);
    const char *outfile = (*env)->GetStringUTFChars(env, outPath_, 0);
    LOG_I("out file path is %s", outfile);

    // 1、获取 bitmap info
    AndroidBitmapInfo bitmapInfo;
    int result = AndroidBitmap_getInfo(env, bitmap, &bitmapInfo);
    if (result < 0) {
        LOG_E("get bitmap info error");
        // 释放资源
        (*env)->ReleaseStringUTFChars(env, outPath_, outfile);
        return FALSE;
    }
    // 2、从 bitmap 中将像素点提取到数组中
    uint8_t *pixelColor;
    result = AndroidBitmap_lockPixels(env, bitmap, (void **) &pixelColor);
    if (result < 0) {
        LOG_E("lock bitmap pixel error");
        (*env)->ReleaseStringUTFChars(env, outPath_, outfile);
        return FALSE;
    }

    // 分配数组：宽*高*3 (rgb)
    uint8_t *data = malloc((size_t) (bitmapInfo.width * bitmapInfo.height * 3));
    uint8_t *tempData = data;
    // 修改色值
    int color;
    uint8_t r, g, b;
    for (int x = 0; x < bitmapInfo.height; ++x) {
        for (int y = 0; y < bitmapInfo.width; ++y) {
            // 1 1 0 0 0 0 0 0 -> a
            // 0 0 1 1 0 0 0 0 -> r
            // 0 0 0 0 1 1 0 0 -> g
            // 0 0 0 0 0 0 1 1 -> b
            color = *((int *) pixelColor); // pixelColor[i][j]
            r = (uint8_t) ((color & 0x00ff0000) >> 16); // r
            g = (uint8_t) ((color & 0x0000ff00) >> 8); // g
            b = (uint8_t) ((color & 0x000000ff)); // b
            // 存放时要按照 b g r 顺序存储
            *(data + 0) = b;
            *(data + 1) = g;
            *(data + 2) = r;
            data += 3;
            pixelColor += 4; // 4 是因为有 alpha 通道
        }
    }

    // 释放像素点
    AndroidBitmap_unlockPixels(env, bitmap);

    // 压缩图片：调用 jpeg 引擎，哈夫曼压缩
    result = writeIntoImage(tempData, bitmapInfo.width, bitmapInfo.height, quality, outfile);
    LOG_I("write image to file result is %d", result);

    // 释放资源
    free(tempData);
    (*env)->ReleaseStringUTFChars(env, outPath_, outfile);

    // 返回结果
    return result == 0 ? FALSE : TRUE;
}

JNIEXPORT jint JNICALL Java_com_minxing_kit_helper_NativeHelper_nArraySum(
        JNIEnv *env, jclass clazz, jintArray array_) {
    jint *array = (*env)->GetIntArrayElements(env, array_, NULL);
    int size = (*env)->GetArrayLength(env, array_);
    int result = 0;
    for (int i = 0; i < size; ++i) {
        // 第一种方式: 索引方式
        // result += array[i];
        // LOG_I("int array index[%d] value[%d]", i, array[i]);
        // 第二种方式: 指针方式
        result += (*(array + i));
        LOG_I("int array index[%d] value[%d]", i, *(array + i));
    }
    (*env)->ReleaseIntArrayElements(env, array_, array, 0);
    LOG_I("native array sum is %d", result);
    return result;
}

////////////////////////// 动态注册方式 //////////////////////

static void nToast(JNIEnv *env, jclass/*class*/ clazz,
                   jobject/*android.content.Context*/ context,
                   jobject/*java.lang.CharSequence*/ arg) {
    /*  android 4.2 之后不能以此种方式创建 Toast
        final Toast toast = new Toast(context);
        toast.setText(getClass().getName());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
        or
        Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
     */
    const char *clsName = "android/widget/Toast";
    jclass toastCls = (*env)->FindClass(env, clsName);
    if (toastCls == NULL) {
        LOG_E("find %s error.", clsName);
        return;
    }
    const char *desc = "(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;";
    jmethodID makeText = (*env)->GetStaticMethodID(env, toastCls, "makeText", desc);
    jobject toastObj = (*env)->CallStaticObjectMethod(env, toastCls, makeText, context, arg, 0);
    (*env)->CallVoidMethod(env, toastObj, (*env)->GetMethodID(env, toastCls, "show", "()V"));
    LOG_I("dynamic register method success");
    (*env)->DeleteLocalRef(env, toastObj);
}

/*
 * java 数据类型、方法 -> 签名
 *
 * boolean      -> Z
 * byte         -> B
 * char         -> C
 * short        -> S
 * int          -> I
 * long         -> J
 * float        -> F
 * double       -> D
 * int[]        -> [I                   // 基本数据类型数组
 * String[]     -> [Ljava/lang/Object   // 非基本数据类型数组
 * <init>       -> ()V                  // 构造函数
 * void(long)   -> (J)V                 // 方法
 */
// 声明需要注册的方法表
static JNINativeMethod GetMethodTables[] = {
        // see jni.h struct JNINativeMethod
        // {方法名， 方法签名[(参数)返回值]， c 函数指针}
        {"nToast",    "(Landroid/content/Context;Ljava/lang/CharSequence;)V", (void *) nToast},
        {"nCompress", "(Landroid/graphics/Bitmap;ILjava/lang/String;)Z",      (void *) nCompress},
};

static void RegisterNativeMethods(
        JNIEnv *env, const char *cls_name, const JNINativeMethod *methods, int count) {
    LOG_I("start register methods %d cls: %s", *methods, cls_name);
    jclass clazz = (*env)->FindClass(env, cls_name);
    if (clazz == NULL) {
        LOG_E("couldn't find class %s", cls_name);
        return;
    }
    if ((*env)->RegisterNatives(env, clazz, methods, count) < 0) {
        LOG_E("register method failed for class %s", cls_name);
    }
    (*env)->DeleteLocalRef(env, clazz);
}

// 重载 jni.h JNI_OnLoad() 方法
jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    LOG_I("override JNI_OnLoad() method");
    JNIEnv *env = NULL;
    if ((*vm)->GetEnv(vm, (void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        return -1;
    }
    RegisterNativeMethods(env, "com/sleticalboy/ic/NativeHelper", GetMethodTables,
                          sizeof(GetMethodTables) / sizeof(JNINativeMethod));
    return JNI_VERSION_1_4;
}