cmake_minimum_required(VERSION 3.4.1)

#add_library(
#        luban-compress
#        SHARED
#        src/main/jni/luban-compress.c)
#
#include_directories(src/main/jni/include)
#add_library(jpeg SHARED IMPORTED)
#set_target_properties(jpeg
#        PROPERTIES IMPORTED_LOCATION
#        ${CMAKE_SOURCE_DIR}/jniLibs/${ANDROID_ABI}/libjpeg-turbo.a
#        )
#
#find_library(log-lib log)
#
#target_link_libraries(
#        luban-compress
#        jpeg
#        jnigraphics
#        ${log-lib})
