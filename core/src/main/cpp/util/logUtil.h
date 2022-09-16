//
// Created by Lai on 2020/11/7.
//
#pragma once
#ifndef OPENGLDEMO_LOGUTIL_H
#define OPENGLDEMO_LOGUTIL_H

#ifdef __ANDROID__

#include<android/log.h>
#include <sys/time.h>

#define  LOG_TAG "TextEngine"

#ifdef DEBUG
#define  LOGCATE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__);
#else
#define LOGCATE(...);
#endif

#ifdef DEBUG
#define  LOGCATI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__);
#else
#define LOGCATI(...);
#endif

#else
#define LOGCATI(...);
#define LOGCATE(...);
#endif


#endif //OPENGLDEMO_LOGUTIL_H
