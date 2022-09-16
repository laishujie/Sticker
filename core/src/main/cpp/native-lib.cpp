#include <jni.h>
#include <string>
#include "core.h"

#ifdef __cplusplus
extern "C" {
#endif

#define RENDER_PATH "com/lai/core/StickerCoreJni"

JavaVM *javaVm = nullptr;

static jlong Android_JNI_createEngine(JNIEnv *env, jobject object) {
    auto textEngineCore = new Core();
    return reinterpret_cast<jlong>(textEngineCore);
}


static JNINativeMethod textMethods[] = {
        {"create", "()J", (void **) Android_JNI_createEngine}
};

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, __unused void *reserved) {
    JNIEnv *env = nullptr;
    if ((vm)->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
    javaVm = vm;

    //注册函数方法
    jclass record = env->FindClass(RENDER_PATH);
    env->RegisterNatives(record, textMethods, (int) (sizeof(textMethods) / sizeof((textMethods)[0])));
    env->DeleteLocalRef(record);
    return JNI_VERSION_1_6;
}


#ifdef __cplusplus
}
#endif