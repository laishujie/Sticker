package com.lai.sticker.view.bean

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.view.View
import com.lai.sticker.util.TrackHelp
import com.lai.sticker.view.StickerItemView

class StickerTrickItem(context: Context) : BaseTrackItem<StickerItemView>(context) {
    private val stickItemView: StickerItemView by lazy {
        StickerItemView(context)
    }

    override fun getItemView(): StickerItemView {
        return stickItemView
    }

    override fun draw(canvas: Canvas?, rectF: RectF) {
        canvas?.apply {
            stickItemView.drawRect(canvas, rectF)
        }
    }
}

abstract class BaseTrackItem<itemView : View>(var context: Context) : ITrackItemView {
    private var id: Int = 0
    val viewRect = RectF()
    override fun getItemViewRect(): RectF {
        return viewRect
    }

    fun data2ViewRect(
        trackIndex: Int,
        stickerTrickItem: StickerTrickItem,
        scaleFactor: Float
    ) {
        val duration: Long = stickerTrickItem.totalMillisecond()
        val time2Width = TrackHelp.time2Width(duration, scaleFactor)
        val left = TrackHelp.time2Width(stickerTrickItem.startMillisecond(), scaleFactor)
        val top = trackIndex * TrackHelp.TRACK_HEIGHT + TrackHelp.ITEM_MARGIN
        val right = left + time2Width
        val bottom = top + TrackHelp.TRACK_HEIGHT
        viewRect.set(left, top.toFloat(), right, bottom.toFloat())
    }

    init {
        id = View.generateViewId()
    }

    var itemDrawState: TrackHelp.ItemDrawState = TrackHelp.ItemDrawState.NULL
    override fun setDrawStatus(itemDrawState: TrackHelp.ItemDrawState) {
        this.itemDrawState = itemDrawState
    }

    var startMillisecond: Long = 0L
    var endMillisecond: Long = 0L
    override fun totalMillisecond(): Long {
        return endMillisecond - startMillisecond
    }

    override fun getItemId(): Int {
        return id
    }

    override fun endMillisecond(): Long {
        return endMillisecond
    }

    override fun startMillisecond(): Long {
        return startMillisecond
    }

    override fun getView(): View {
        return getItemView()
    }

    abstract fun getItemView(): itemView
}

interface ITrackItemView {
    fun getView(): View
    fun totalMillisecond(): Long
    fun startMillisecond(): Long
    fun endMillisecond(): Long
    fun getItemId(): Int
    fun draw(canvas: Canvas?, rectF: RectF)
    fun setDrawStatus(itemDrawState: TrackHelp.ItemDrawState)
    fun getItemViewRect(): RectF
}

data class TrackInfo(val rectF: RectF, val iTrackItemViews: ArrayList<ITrackItemView> = ArrayList())


