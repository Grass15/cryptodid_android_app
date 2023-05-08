#include <jni.h>
#include <string>
#include <unistd.h>
#include "tfhe.h"
#include "tfhe_io.h"
#include "tfhe_core.h"
//#include <time.h>

void compare_bit(LweSample* result, const LweSample* a, const LweSample* b, const LweSample* lsb_carry, LweSample* tmp, const TFheGateBootstrappingCloudKeySet* bk) {
    bootsXNOR(tmp, a, b, bk);
    bootsMUX(result, tmp, lsb_carry, a, bk);
}

// this function compares two multibit words, and puts the max in result
void minimum(LweSample* result, const LweSample* a, const LweSample* b, const int nb_bits, const TFheGateBootstrappingCloudKeySet* bk) {
    LweSample* tmps = new_gate_bootstrapping_ciphertext_array(2, bk->params);

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

    delete_gate_bootstrapping_ciphertext_array(2, tmps);
}

extern "C" JNIEXPORT jint JNICALL
Java_com_example_tfhe_MainActivity_GetMinimum(
        JNIEnv* env,
        jobject,
        jint n1,
        jint n2
        ) {

////////////////////////////////////////////////////////////////////////////////////////////////////
    //clock_t begin = clock();

    //double time_spent = 0.0;

    const int minimum_lambda = 110;
    TFheGateBootstrappingParameterSet* params = new_default_gate_bootstrapping_parameters(minimum_lambda);

    //generate a random key
    uint32_t seed[] = { 314, 1592, 657 };
    tfhe_random_generator_setSeed(seed,3);
    TFheGateBootstrappingSecretKeySet* key = new_random_gate_bootstrapping_secret_keyset(params);

    //generate encrypt the 16 bits of 2017
    int16_t plaintext1 = n1;
    LweSample* ciphertext1 = new_gate_bootstrapping_ciphertext_array(16, params);

    for (int i=0; i<16; i++) {
        bootsSymEncrypt(&ciphertext1[i], (plaintext1>>i)&1, key);
    }

    //generate encrypt the 16 bits of 42
    int16_t plaintext2 = n2;
    LweSample* ciphertext2 = new_gate_bootstrapping_ciphertext_array(16, params);
    for (int i=0; i<16; i++) {
        bootsSymEncrypt(&ciphertext2[i], (plaintext2>>i)&1, key);
    }
    //clock_t end = clock();
    FILE* cloud_key = fopen("/data/data/com.example.tfhe/data/cloud.key","wb");
    export_tfheGateBootstrappingCloudKeySet_toFile(cloud_key, &key->cloud);
    fclose(cloud_key);

    FILE* cloud_key2 = fopen("/data/data/com.example.tfhe/data/cloud.key","rb");
    TFheGateBootstrappingCloudKeySet* bk = new_tfheGateBootstrappingCloudKeySet_fromFile(cloud_key);
    fclose(cloud_key2);

////////////////////////////////////////////////////////////////////////////////////////////////////
    //const TFheGateBootstrappingCloudKeySet* bk = &key->cloud;
    LweSample* result = new_gate_bootstrapping_ciphertext_array(16, params);
    minimum(result, ciphertext1, ciphertext2, 16, &key->cloud);
////////////////////////////////////////////////////////////////////////////////////////////////////
    //decrypt and rebuild the answer
    int16_t int_answer = 0;
    for (int i=0; i<16; i++) {
        int ai = bootsSymDecrypt(&result[i], key)>0;
        int_answer |= (ai<<i);
    }

    //clean up all pointers
    //delete_gate_bootstrapping_ciphertext_array(16, result);
    //delete_gate_bootstrapping_secret_keyset(key);
    //delete_gate_bootstrapping_ciphertext_array(16, ciphertext2);
    //delete_gate_bootstrapping_ciphertext_array(16, ciphertext1);
    //delete_gate_bootstrapping_parameters(params);

    //time_spent += (double)(end - begin) / CLOCKS_PER_SEC;
    // Encryptio took : 1.124 seconds
    // Computation took : 11.2 seconds
////////////////////////////////////////////////////////////////////////////////////////////////////
    return int_answer;
}