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

void verifier_circuit(int n,LweSample * result_for_L, const LweSample* ciphertext1, const LweSample* ciphertext2,
                      const LweSample* encrypt0,const LweSample *encrypt1,const TFheGateBootstrappingCloudKeySet * bk,
                      const TFheGateBootstrappingParameterSet* params){
    //do some operations on the ciphertexts: here, we will compute the
    //minimum of the two
    LweSample* tmps = new_gate_bootstrapping_ciphertext_array(2, params);
    LweSample* result = new_gate_bootstrapping_ciphertext_array(16, params);
    minimum(tmps,result, ciphertext1, ciphertext2, 16,bk);

    /*********** generate K***********/
    int k[16]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    LOGD("[+] k = %d\n",n);
    /*convert n to binary and store the bits in k*/
    for(int i=0;n>0;i++)
    {
        k[i]=n%2;
        n=n/2;
    }

    /* encrypt k*/
    LweSample* k_enc = new_gate_bootstrapping_ciphertext_array(16,params);
    //LweParams * in_out_params = bk->bk->in_out_params;
    const LweParams *in_out_params = params->in_out_params;
    for (int i=0;i<16;i++){
        if (k[i] == 0){
            lweCopy(&k_enc[i],encrypt0,in_out_params);
        }
        else if (k[i] == 1)
        {
            lweCopy(&k_enc[i],encrypt1,in_out_params);
        }
        else LOGD("[-] error bits");
    }
    for (int i=0; i<16; i++) {
        bootsAND(&result_for_L[i],&k_enc[i],&tmps[0],bk);
    }
    delete_gate_bootstrapping_ciphertext_array(2, tmps);
    delete_gate_bootstrapping_ciphertext_array(16, k_enc);
}

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
Java_com_loginid_cryptodid_claimFetcher_Fetcher_TFHE(JNIEnv *env, jobject thiz, jint n1,
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



    const int minimum_lambda = 50;
    TFheGateBootstrappingParameterSet* params = new_default_gate_bootstrapping_parameters(minimum_lambda);
    // generate the key
    uint32_t seed[] = { 314, 1592, 657 };
    tfhe_random_generator_setSeed(seed,3);
    //SetSeed();
    TFheGateBootstrappingSecretKeySet* key = new_random_gate_bootstrapping_secret_keyset(params);
    FILE* SK_pt = fopen(SecretKeyPath,"wb");
    export_tfheGateBootstrappingSecretKeySet_toFile(SK_pt,key);
    fclose(SK_pt);
    // Encrypt the number

    int16_t plaintext1 = n1;
    LweSample* claim = new_gate_bootstrapping_ciphertext_array(16, params);
    for (int i=0; i<16; i++) {
        bootsSymEncrypt(&claim[i], (plaintext1>>i)&1, key);
    }

    LweSample* ZERO = new_gate_bootstrapping_ciphertext(params);
    LweSample* ONE = new_gate_bootstrapping_ciphertext(params);

    FILE* PK_pt = fopen(PK_Path,"wb");
    bootsSymEncrypt(ZERO, 0, key);
    export_gate_bootstrapping_ciphertext_toFile(PK_pt, ZERO, params);
    bootsSymEncrypt(ONE, 1, key);
    export_gate_bootstrapping_ciphertext_toFile(PK_pt, ONE, params);
    fclose(PK_pt);

    FILE* cloud_data = fopen(clouddataPath,"wb");
    for (int i=0; i<16; i++)
        export_gate_bootstrapping_ciphertext_toFile(cloud_data, &claim[i], params);
    fclose(cloud_data);

    FILE* cloud_key = fopen(cloudkeyPath,"wb");
    export_tfheGateBootstrappingCloudKeySet_toFile(cloud_key, &key->cloud);
    fclose(cloud_key);

    return 1;
}