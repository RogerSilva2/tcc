#include <jni.h>
#include <stdio.h>
#include <string.h>

jstring Java_com_rogersilva_tcc_activities_MainActivity_invokeNativeFunction(JNIEnv* env,
                                                                             jobject javaThis) {
    jclass javaClass = (*env)->GetObjectClass(env, javaThis);

    jmethodID javaMethod = (*env)->GetStaticMethodID(env, javaClass,
            "generateKey", "()Ljava/lang/String;");

    jstring javaString = (*env)->CallStaticObjectMethod(env, javaClass, javaMethod);
    const char *key = (*env)->GetStringUTFChars(env, javaString, NULL);

    return (*env)->NewStringUTF(env, key);
}