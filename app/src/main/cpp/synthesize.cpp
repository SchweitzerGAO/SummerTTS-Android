//
// Created by Administrator on 2023/8/6.
//

#include <cstdio>
#include "SynthesizerTrn.h"
#include <utils.h>
#include <cstring>
#include <jni.h>
#include <string>
#include <iostream>
#include <fstream>





std::string jstring2string(JNIEnv* env, jstring jstr) {
    char *rtn = nullptr;
    jclass clsstring = env->FindClass("java/lang/String");
    jstring strencode = env->NewStringUTF("UTF-8");
    jmethodID mid = env->GetMethodID(clsstring, "getBytes", "(Ljava/lang/String;)[B");
    auto barr = (jbyteArray) env->CallObjectMethod(jstr, mid, strencode);
    jsize alen = env->GetArrayLength(barr);
    jbyte *ba = env->GetByteArrayElements(barr, JNI_FALSE);
    if (alen > 0) {
        rtn = (char *) malloc(alen + 1);
        memcpy(rtn, ba, alen);
        rtn[alen] = 0;
    }
    env->ReleaseByteArrayElements(barr, ba, 0);
    std::string stemp(rtn);
    free(rtn);
    return stemp;
}



extern "C"
JNIEXPORT jshortArray JNICALL
Java_edu_tongji_chinese_1tts_utils_ChineseTTSUtil_synthesize(JNIEnv *env, jclass clazz, jstring text, jstring model_path) {

    std::string line = jstring2string(env, text);
    std::string str_model_path = jstring2string(env, model_path);

    float* dataW = nullptr;
    char* c_str_model_path = const_cast<char *>(str_model_path.c_str());
    int32_t modelSize = ttsLoadModel(c_str_model_path,&dataW);

    auto* synthesizer = new SynthesizerTrn(dataW, modelSize);

    int32_t retLen = 0;
    int16_t* wavData = synthesizer -> infer(line,0, 1.0,retLen);

    jsize size = retLen;
    jshortArray ret_arr = env -> NewShortArray(size);
    auto* ptr_arr = new jshort[size];
    for (int i = 0; i < size; i++) {
        if(wavData[i] != 0) {
            ptr_arr[i] = wavData[i];
        }
        else {
            ptr_arr[i] = 0;
        }
    }
    env -> SetShortArrayRegion(ret_arr,0,size,ptr_arr);

    delete synthesizer;
    tts_free_data(wavData);
    tts_free_data(dataW);

    return ret_arr;
}


