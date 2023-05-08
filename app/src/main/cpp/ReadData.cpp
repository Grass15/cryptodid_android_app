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
#include "jni.h"
#define min(a,b) ((a)<(b)?(a):(b))
using namespace std;

#define  LOG_TAG    "Tfhe:"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

bool readdata(int sock, void *buf, int buflen)
{
    auto *pbuf = (unsigned char *) buf;

    while (buflen > 0)
    {
        int num = recv(sock, pbuf, buflen, 0);
        if (num == -1)
        {
            return false;
        }
        else if (num == 0)
            return false;

        pbuf += num;
        buflen -= num;
    }
    return true;
}

bool readlong(int sock, long *value)
{
    if (!readdata(sock, value, sizeof(value)))
        return false;
    *value = ntohl(*value);
    return true;
}
bool comp(int a, int b)
{
    return (a < b);
}
bool readfile(int sock, FILE *f)
{
    long filesize;
    if (!readlong(sock, &filesize))
        return false;
    if (filesize > 0)
    {
        char buffer[1024];
        do
        {
            int num = min(filesize, sizeof(buffer));
            if (!readdata(sock, buffer, num))
                return false;
            int offset = 0;
            do
            {
                size_t written = fwrite(&buffer[offset], 1, num-offset, f);
                if (written < 1)
                    return false;
                offset += written;
            }
            while (offset < num);
            filesize -= num;
        }
        while (filesize > 0);
    }
    return true;
}


extern "C" JNIEXPORT jint JNICALL
Java_com_example_proverapp_TFHECryptho_ReadFile(JNIEnv *env, jobject) {

    char const *ip = "192.168.1.7";
    int port = 7070;
    int e;

    int sockfd;
    struct sockaddr_in server_addr{};

    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if(sockfd < 0) {
        LOGE("[-]Error in socket");
        return -1;
    }
    printf("[+]Server socket created successfully.\n");

    server_addr.sin_family = AF_INET;
    server_addr.sin_port = port;
    server_addr.sin_addr.s_addr = inet_addr(ip);

    e = connect(sockfd, (struct sockaddr*)&server_addr, sizeof(server_addr));
    if(e == -1) {
        LOGE("[-]Error in socket");
        return -1;
    }
    printf("[+]Connected to Server.\n");

    FILE *data = fopen("/sdcard/Documents/data/answer.data", "wb");
    if (data != nullptr)
    {
        bool ok = readfile(sockfd, data);
        fclose(data);

        if (ok)
        {
            LOGD("[+]Data written in the file successfully.\n");
        }
        else
            remove("/sdcard/Documents/data/answer.data");
    }
    fclose(data);
    close(sockfd);
    return 0;
}