package com.lai.sticker.egl.pipeline

import android.os.Handler

interface Pipeline {
    fun queueEvent(event: Runnable, front: Boolean = false)
    fun queueEvent(event: Runnable, delayed: Long)
    fun quit()
    fun started(): Boolean
    fun getName(): String
    fun getHandler(): Handler
    fun sleep()
    fun wake()
}