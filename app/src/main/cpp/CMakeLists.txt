# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.22.1)

# Declares and names the project.

project("chinese_tts")

set(distribution_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../../../libs)

add_library(lib_chinese_tts SHARED IMPORTED)
set_target_properties(lib_chinese_tts PROPERTIES IMPORTED_LOCATION ${distribution_DIR}/${ANDROID_ABI}/libchinese_tts.so)

#add_library(phonemizer SHARED IMPORTED)
#set_target_properties(phonemizer PROPERTIES IMPORTED_LOCATION
#        ${distribution_DIR}/espeak-ng/lib/${ANDROID_ABI}/libphonemizer.so)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.


set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")



add_library(zh_tts SHARED
        synthesize.cpp )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_include_directories(zh_tts PRIVATE
       ${distribution_DIR}/include
        )

target_link_libraries(
        zh_tts
        lib_chinese_tts
        android
        log)
