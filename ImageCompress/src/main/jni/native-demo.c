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

#define true 1
#define false 0

typedef uint8_t BYTE;

// int writeIntoImage(BYTE *data, int width, int height, jint quality, const char *outfile);

/**
 * 鲁班压缩核心代码
 * @param data 图片数组
 * @param width 图片宽
 * @param height  图片高
 * @param quality 压缩质量
 * @param outfile 输出文件
 * @return 0 表示出错，其他正常
 */
int writeIntoImage(BYTE *data, int width, int height, jint quality, const char *outfile) {
    struct jpeg_compress_struct compress_struct;
    struct jpeg_error_mgr error_mgr;
    compress_struct.err = jpeg_std_error(&error_mgr);
    jpeg_create_compress(&compress_struct);
    FILE *image = fopen(outfile, "wb");
    if (image == NULL) {
        LOG_E("open file error %s", outfile);
        return JNI_FALSE;
    }
    jpeg_stdio_dest(&compress_struct, image);

    // 一系列配置信息
    compress_struct.image_width = (JDIMENSION) width;
    compress_struct.image_height = (JDIMENSION) height;
    // TRUE=arithmetic coding, FALSE=Huffman false 表示启用 Huffman 算法
    compress_struct.arith_code = FALSE;
    compress_struct.input_components = 3;
    compress_struct.in_color_space = JCS_RGB;
    compress_struct.optimize_coding = TRUE;
    // 其他配置信息
    jpeg_set_defaults(&compress_struct);
    jpeg_set_quality(&compress_struct, quality, TRUE);

    // 开始压缩
    jpeg_start_compress(&compress_struct, TRUE);

    // 循环写入文件
    JSAMPROW row_ptr[1];
    int row_stride = compress_struct.image_width * compress_struct.input_components;
    int next_line;
    while ((next_line = compress_struct.next_scanline) < compress_struct.image_height) {
        row_ptr[0] = &data[next_line * row_stride];
        jpeg_write_scanlines(&compress_struct, row_ptr, 1);
    }

    // 释放资源
    jpeg_finish_compress(&compress_struct);
    jpeg_destroy_compress(&compress_struct);
    fclose(image);
    return JNI_TRUE;
}

jboolean JNICALL Java_com_sleticalboy_ic_JNIHelper_nCompress(
        JNIEnv *env, jobject obj, jobject bitmap, jint quality, jstring outPath_) {
    LOG_I("native start compress quality is %d", quality);
    const char *outfile = (*env)->GetStringUTFChars(env, outPath_, 0);
    LOG_I("out file path is %s", outfile);

    // 获取 bitmap info
    AndroidBitmapInfo bitmapInfo;
    int result = AndroidBitmap_getInfo(env, bitmap, &bitmapInfo);
    if (result < 0) {
        LOG_E("get bitmap info error");
        // 释放资源
        (*env)->ReleaseStringUTFChars(env, outPath_, outfile);
        return false;
    }

    // 提取色值，注入到数组中
    BYTE *pixelColor;
    result = AndroidBitmap_lockPixels(env, bitmap, (void **) &pixelColor);
    if (result < 0) {
        LOG_E("lock bitmap pixel error");
        (*env)->ReleaseStringUTFChars(env, outPath_, outfile);
        return false;
    }

    BYTE *data;
    BYTE *tempData;
    // 分配数组
    data = malloc((size_t) (bitmapInfo.width * bitmapInfo.height * 3));
    tempData = data;
    // 修改色值
    int color;
    BYTE r, g, b;
    for (int x = 0; x < bitmapInfo.height; ++x) {
        for (int y = 0; y < bitmapInfo.width; ++y) {
            color = *((int *) pixelColor);
            r = (BYTE) ((color & 0x00ff0000) >> 16);
            g = (BYTE) ((color & 0x0000ff00) >> 8);
            b = (BYTE) ((color & 0x000000ff));
            *(data + 0) = b;
            *(data + 1) = g;
            *(data + 2) = r;
            data += 3;
            pixelColor += 4;
        }
    }

    // unlock pixels
    AndroidBitmap_unlockPixels(env, bitmap);

    // 压缩图片
    result = writeIntoImage(tempData, bitmapInfo.width, bitmapInfo.height, quality, outfile);
    LOG_I("write image to file result is %d", result);

    // 释放资源
    free(tempData);
    (*env)->ReleaseStringUTFChars(env, outPath_, outfile);

    // 返回结果
    return result == 0 ? false : true;
}

JNIEXPORT jint JNICALL Java_com_sleticalboy_ic_JNIHelper_nArraySum(
        JNIEnv *env, jobject obj, jintArray array_) {
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

static void dynamicRegister(JNIEnv *env, jobject obj, jlong arg) {
    LOG_I("dynamic register method success");
}

// 声明 native 方法所在的类名
static const char *cls_name = "com/sleticalboy/ic/JNIHelper";
// 声明需要注册的方法表
static JNINativeMethod GetMethodTables[] = {
        // {方法名， 方法签名[(参数)返回值]， c 函数指针}
        {"dynamicRegister", "(J)V", (void*)dynamicRegister},
};
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

// void RegisterNativeMethods(JNIEnv *pInterface, const char *name, JNINativeMethod tables[1], size_t i);
static void RegisterNativeMethods(JNIEnv *env, const char *cls_name, const JNINativeMethod *methods, int count) {
    LOG_I("start register methods");
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
    RegisterNativeMethods(env, cls_name, GetMethodTables, sizeof(GetMethodTables) / sizeof(JNINativeMethod));
    return JNI_VERSION_1_4;
}