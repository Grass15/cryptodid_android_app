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

void compare_bit(LweSample* result, const LweSample* a, const LweSample* b, const LweSample* lsb_carry, LweSample* tmp, const TFheGateBootstrappingCloudKeySet* bk) {
    bootsXNOR(tmp, a, b, bk);
    bootsMUX(result, tmp, lsb_carry, a, bk);
}

// this function compares two multibit words, and puts the max in result
void minimum(LweSample* tmps,LweSample* result, const LweSample* a, const LweSample* b, const int nb_bits, const TFheGateBootstrappingCloudKeySet* bk) {
    //LweSample* tmps = new_gate_bootstrapping_ciphertext_array(2, bk->params);

    //initialize the carry to 0
    bootsCONSTANT(&tmps[0], 0, bk);
    //run the elementary comparator gate n times
    for (int i=0; i<nb_bits; i++) {
        compare_bit(&tmps[0], &a[i], &b[i], &tmps[0], &tmps[1], bk);
    }
    //tmps[0] is the result of the comparaison: 0 if a is larger, 1 if b is larger
    //select the max and copy it to the result
    for (int i=0; i<nb_bits; i++) {
        bootsMUX(&result[i], &tmps[0], &b[i], &a[i], bk);
    }

    //delete_gate_bootstrapping_ciphertext_array(2, tmps);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_proverapp_TFHECryptho_VerifyCircuit(JNIEnv *env, jobject thiz, jint n) {

    FILE* cloud_key = fopen("/sdcard/Documents/data/cloud.key","rb");
    TFheGateBootstrappingCloudKeySet* bk = new_tfheGateBootstrappingCloudKeySet_fromFile(cloud_key);
    fclose(cloud_key);

    //if necessary, the params are inside the key
    const TFheGateBootstrappingParameterSet* params = bk->params;
    LweSample* ciphertext1 = new_gate_bootstrapping_ciphertext_array(16, params);
    LweSample* ciphertext2 = new_gate_bootstrapping_ciphertext_array(16, params);

    FILE* cloud_data = fopen("/sdcard/Documents/data/cloud.data","rb");
    for (int i=0; i<16; i++)
        import_gate_bootstrapping_ciphertext_fromFile(cloud_data, &ciphertext1[i], params);
    for (int i=0; i<16; i++)
        import_gate_bootstrapping_ciphertext_fromFile(cloud_data, &ciphertext2[i], params);
    fclose(cloud_data);

    LweSample *tmps = new_gate_bootstrapping_ciphertext_array(2, params);
    LweSample *result = new_gate_bootstrapping_ciphertext_array(16, params);
    minimum(tmps, result, ciphertext1, ciphertext2, 16, bk);

    /*read the data (01) from the file*/
    LweSample* encrypt0 = new_gate_bootstrapping_ciphertext(params);
    LweSample* encrypt1 = new_gate_bootstrapping_ciphertext(params);


    FILE* data_01 = fopen("/sdcard/Documents/data/data01.data","rb");
    import_gate_bootstrapping_ciphertext_fromFile(data_01, encrypt0, params);
    import_gate_bootstrapping_ciphertext_fromFile(data_01, encrypt1, params);
    fclose(data_01);
    /*********** generate K***********/
    int k[16] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    LOGD("[+] k = %d\n", n);
    /*convert n to binary and store the bits in k*/
    for (int i = 0; n > 0; i++)
    {
        k[i] = n % 2;
        n = n / 2;
    }

    /* encrypt k */
    LweSample *k_enc = new_gate_bootstrapping_ciphertext_array(16, params);
    const LweParams *in_out_params = params->in_out_params;
    for (int i = 0; i < 16; i++)
    {
        if (k[i] == 0)
        {
            lweCopy(&k_enc[i], encrypt0, in_out_params);
        }
        else if (k[i] == 1)
        {
            lweCopy(&k_enc[i], encrypt1, in_out_params);
        }
        else
            printf("[-] error bits");
    }
    LweSample* result_for_L = new_gate_bootstrapping_ciphertext_array(16, params);

    for (int i = 0; i < 16; i++)
    {
        bootsAND(&result_for_L[i], &k_enc[i], &tmps[0], bk);
    }

    FILE* L_data = fopen("/sdcard/Documents/data/answer.data","wb");
    for (int i=0; i<16; i++)
        export_gate_bootstrapping_ciphertext_toFile(L_data, &result_for_L[i], params);
    fclose(L_data);

    delete_gate_bootstrapping_ciphertext_array(2, tmps);
    delete_gate_bootstrapping_ciphertext_array(16, k_enc);
    delete_gate_bootstrapping_ciphertext_array(16, result_for_L);
}