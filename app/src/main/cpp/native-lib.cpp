#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_dust_app_activity_CppActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
