#include <cstdio>
#include <cstdlib>
#include <unistd.h>
#include <cstring>
#include <arpa/inet.h>
#include <jni.h>
#include "tfhe.h"
#include "tfhe_io.h"
#include "tfhe_core.h"
#include <sys/sendfile.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <cmath>
#include <ctime>
#include <android/log.h>
#include <cstdlib>
#define  LOG_TAG    "Tfhe:"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#include <iostream>
#include <iostream>
//using namespace std;


const char *APPNAME = "error";

char* concat(const char* s1, const char* s2)
{
    // Allocate memory for the concatenated string
    char* result = new char[strlen(s1) + strlen(s2) + 1];

    // Copy the first string to the result string
    strcpy(result, s1);

    // Append the second string to the result string
    strcat(result, s2);

    return result;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_loginid_cryptodid_claimFetcher_Fetcher_TFHE(JNIEnv *env, jobject thiz, jint sin_clear,
                                                       jstring folderPath, jstring attribute) {
    // TODO: implement TFHE
    const char *path = env->GetStringUTFChars(folderPath, NULL);
    const char *attr = env->GetStringUTFChars(attribute, NULL);
    LOGD(" file name is : %s",path);
    char* cloudkeyPath = concat(path,concat(concat("/", attr), "Cloud.key"));
    char* clouddataPath = concat(path,concat(concat("/", attr), "Cloud.data"));
    char* PK_Path = concat(path,concat(concat("/", attr), "PK.key"));
    char* answerPath = concat(path, "/answer.data");
    char* SecretKeyPath = concat(path,concat(concat("/", attr), "Keyset.key"));

    const TFheGateBootstrappingCloudKeySet* bk = NULL;
    LweSample *ONE;
    LweSample *ZERO;
    //generate a keyset
    const int minimum_lambda = 80;
    TFheGateBootstrappingParameterSet* params = new_default_gate_bootstrapping_parameters(minimum_lambda);
    //generate a random key
    uint32_t seed[] = { 314, 1592, 411 };
    tfhe_random_generator_setSeed(seed,3);
    TFheGateBootstrappingSecretKeySet* key = new_random_gate_bootstrapping_secret_keyset(params);

    //export the cloud key to a file (for the cloud)
    bk = &key->cloud;

    //Generate the public key
    FILE* PK_pt = fopen(PK_Path,"wb");
    LweSample *PK;
    PK = new_gate_bootstrapping_ciphertext( params);
    for (int i = 0; i<256; i++) {
        bootsSymEncrypt(PK, 0, key);
        export_gate_bootstrapping_ciphertext_toFile(PK_pt, PK, params);
    }
    bootsSymEncrypt(PK, 1, key);
    export_gate_bootstrapping_ciphertext_toFile(PK_pt, PK, params);
    fclose(PK_pt);
    ONE = new_gate_bootstrapping_ciphertext_array(1, params);
    ZERO = new_gate_bootstrapping_ciphertext_array(1, params);
    bootsSymEncrypt(ONE, 1, key);
    bootsSymEncrypt(ZERO, 0, key);

    //unsigned long sin_clear = n;
    LweSample* sin = new_gate_bootstrapping_ciphertext_array(32, params);
    for (int i=0; i<32; i++) {
        if ((sin_clear>>i)&0x1) {
            lweCopy(sin + i, ONE, params->in_out_params);
        } else
            lweCopy(sin + i, ZERO, params->in_out_params);
    }

    FILE* cloud_data = fopen(clouddataPath,"wb");
    for (int i=0; i<32; i++)
        export_gate_bootstrapping_ciphertext_toFile(cloud_data, &sin[i], params);
    fclose(cloud_data);

    FILE* cloud_key = fopen(cloudkeyPath,"wb");
    export_tfheGateBootstrappingCloudKeySet_toFile(cloud_key, &key->cloud);
    fclose(cloud_key);

    FILE* SK_pt = fopen(SecretKeyPath,"wb");
        export_tfheGateBootstrappingSecretKeySet_toFile(SK_pt,key);
        fclose(SK_pt);

    return 1;
}