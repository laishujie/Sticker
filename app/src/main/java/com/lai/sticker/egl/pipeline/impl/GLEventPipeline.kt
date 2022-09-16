package com.lai.sticker.egl.pipeline.impl

import android.os.Handler
import com.lai.sticker.egl.pipeline.Pipeline


class GLEventPipeline private constructor() : Pipeline {

    private object Holder {
        val INSTANCE: Pipeline = GLEventPipeline()
    }

    companion object {
        val INSTANCE: Pipeline by lazy { Holder.INSTANCE }
        fun isMe(pipeline: Pipeline): Boolean {
            return INSTANCE == pipeline
        }
    }

    private var eventPipeline: EventPipeline? = null
    private fun check() {
        if (null == eventPipeline)
            eventPipeline = EventPipeline.create("GLEventPipeline")
    }

    override fun queueEvent(event: Runnable, front: Boolean) {
        check()
        eventPipeline?.queueEvent(event)
    }

    override fun queueEvent(event: Runnable, delayed: Long) {
        check()
        eventPipeline?.queueEvent(event, delayed)
    }

    override fun quit() {
        if (null != eventPipeline) {
            eventPipeline?.quit()
            eventPipeline = null
        }
    }

    override fun started(): Boolean {
        if (null == eventPipeline) return false
        return eventPipeline!!.started()
    }

    override fun getName(): String {
        if (null == eventPipeline) return "UNKNOWN"
        return eventPipeline!!.getName()
    }

    override fun getHandler(): Handler {
        return eventPipeline!!.getHandler()
    }

    override fun sleep() {
        eventPipeline?.sleep()
    }

    override fun wake() {
        eventPipeline?.wake()
    }
}