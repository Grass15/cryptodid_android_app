#include <cstdio>
#include <cstdlib>
#include <unistd.h>
#include <cstring>
#include <arpa/inet.h>
#include <jni.h>
#include <strings.h>
#include "tfhe.h"
#include "tfhe_io.h"
#include "tfhe_core.h"
#include <sys/sendfile.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fstream>
#include <iostream>
#include <cmath>
#include <ctime>
#include <android/log.h>
#include <cstdlib>
#define  LOG_TAG    "Tfhe:"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_proverapp_TFHECryptho_EncryptData(JNIEnv *env, jobject thiz, jint plaintext, jstring keyname) {


    LOGD("[+]reading the key...\n");
    //reads the cloud key from file
    FILE* secret_key = fopen("/sdcard/Documents/data/secret.key","rb");
    TFheGateBootstrappingSecretKeySet* key = new_tfheGateBootstrappingSecretKeySet_fromFile(secret_key);
    fclose(secret_key);
    LOGD("Here----");
    //if necessary, the params are inside the key
    const TFheGateBootstrappingParameterSet* params = key->params;
    printf("[+]reading the input...\n");
    // Encrypt the number

    int16_t plaintext1 = plaintext;
    LOGD("Here----");
    LweSample* ciphertext1 = new_gate_bootstrapping_ciphertext_array(16, params);
    for (int i=0; i<16; i++) {
        bootsSymEncrypt(&ciphertext1[i], (plaintext1>>i)&1, key);
    }
    LOGD("Here---1-");
    int16_t plaintext2 = plaintext;
    LweSample* ciphertext2 = new_gate_bootstrapping_ciphertext_array(16, params);
    for (int i=0; i<16; i++) {
        bootsSymEncrypt(&ciphertext2[i], (plaintext2>>i)&1, key);
    }
    LOGD("Here---2-");
    FILE* cloud_data = fopen("/sdcard/Documents/data/cloud.data","wb");
    for (int i=0; i<16; i++)
        export_gate_bootstrapping_ciphertext_toFile(cloud_data, &ciphertext1[i], params);
    for (int i=0; i<16; i++)
        export_gate_bootstrapping_ciphertext_toFile(cloud_data, &ciphertext2[i], params);
    fclose(cloud_data);

    LOGD("Encryption for 0 and 1");
    int16_t number1 = 0;
    LweSample* encrypt0 = new_gate_bootstrapping_ciphertext(params);
    bootsSymEncrypt(encrypt0, number1, key);
    int16_t number2 = 1;
    LweSample* encrypt1 = new_gate_bootstrapping_ciphertext(params);
    bootsSymEncrypt(encrypt1, number2, key);

    /* Save data to file */
    FILE* data_01 = fopen("/sdcard/Documents/data/data01.data","wb");
    if (data_01==nullptr) {
        LOGD("Error %d \n", errno);
        LOGD("It's null");
    }
    LOGD("Here----");
    export_gate_bootstrapping_ciphertext_toFile(data_01, encrypt0, params);
    export_gate_bootstrapping_ciphertext_toFile(data_01, encrypt1, params);
    fclose(data_01);


    return 0;
}