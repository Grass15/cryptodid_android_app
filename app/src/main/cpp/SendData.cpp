#include <jni.h>
#include <cstdio>
#include <cstdlib>
#include <unistd.h>
#include <cstring>
#include <arpa/inet.h>
#include <strings.h>
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

using namespace std;


bool senddata(int sock, void *buf, int buflen)
{
    auto *pbuf = (unsigned char *) buf;

    while (buflen > 0)
    {
        int num = send(sock, pbuf, buflen, 0);
        if (num == -1)
        {
            return false;
        }
        pbuf += num;
        buflen -= num;
    }
    return true;
}
bool sendlong(int sock, long value)
{
    value = htonl(value);
    return senddata(sock, &value, sizeof(value));
}
int min(size_t a, size_t b)
{
    return  (a > b) ? b :a;
}
bool sendmyfile(int sock, FILE *f)
{
    fseek(f, 0, SEEK_END);
    long filesize = ftell(f);
    rewind(f);
    if (filesize == EOF)
        return false;
    if (!sendlong(sock, filesize))
        return false;
    if (filesize > 0)
    {
        char buffer[1024];
        do
        {
            size_t num = min(filesize, sizeof(buffer));
            num = fread(buffer, 1, num, f);
            if (num < 1)
                return false;
            if (!senddata(sock, buffer, num))
                return false;
            filesize -= num;
        }
        while (filesize > 0);
    }
    return true;
}




extern "C"
JNIEXPORT jint JNICALL
Java_com_example_proverapp_TFHECryptho_SendFile(JNIEnv *env, jobject thiz, jint file_option) {

    char const *ip = "192.168.1.7";
    int port = 7070;
    int e;

    int sockfd;
    struct sockaddr_in server_addr{};
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = port;
    server_addr.sin_addr.s_addr = inet_addr(ip);

    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if(sockfd < 0) {
        LOGE("[-]Error in socket");
        return -1;
    }
    printf("[+]Server socket created successfully.\n");


    e = connect(sockfd, (struct sockaddr*)&server_addr, sizeof(server_addr));
    if(e == -1) {
        LOGE("[-]Error in socket");
        return -1;
    }
    printf("[+]Connected to Server.\n");
    FILE* data;
    if (file_option == 1){
        data = fopen("/sdcard/Documents/data/cloud.data","rb");
        if (data==nullptr) {
            LOGD("Error %d \n", errno);
            LOGD("It's null");
        }
    }
    if (file_option == 2){
        data = fopen("/sdcard/Documents/data/cloud.key","rb");
        if (data==nullptr) {
            LOGD("Error %d \n", errno);
            LOGD("It's null");
        }
    }
    if (file_option == 3){
        data = fopen("/sdcard/Documents/data/data01.data","rb");
        if (data==nullptr) {
            LOGD("Error %d \n", errno);
            LOGD("It's null");
        }
    }
    sendmyfile(sockfd, data);
    fclose(data);

    LOGD("[+]File sent successfully.\n");
    close(sockfd);

    return 0;
}