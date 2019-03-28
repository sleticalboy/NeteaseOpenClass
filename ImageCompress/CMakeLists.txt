cmake_minimum_required(VERSION 3.4.1)

add_library(
        native-demo
        SHARED
        src/main/jni/native-demo.c)

include_directories(src/main/jni/include)
add_library(jpeg SHARED IMPORTED)
set_target_properties(jpeg
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/jniLibs/${ANDROID_ABI}/libjpeg-turbo.a
        )

find_library(log-lib log)

target_link_libraries(
        native-demo
        jpeg
        jnigraphics
        ${log-lib})
