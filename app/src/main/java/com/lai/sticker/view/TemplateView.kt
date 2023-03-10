package com.lai.sticker.view

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.withSave
import com.lai.sticker.util.*
import com.lai.sticker.view.bean.StickerTrickItem
import com.lai.sticker.view.bean.TrackInfo
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class StickerItemView : View {
    private val bgPaint: Paint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        color = Color.parseColor("#f48d3e")
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val bgRect = RectF()

    private var isStopDraw = false

    private val myRound = TrackHelp.dp2px(5f).toFloat()

    fun stopDefaultDraw(isStop: Boolean, isUpdate: Boolean) {
        isStopDraw = isStop
        if (isUpdate)
            invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        if (bgRect.isEmpty) return
        if (!isStopDraw)
            drawRect(canvas, bgRect)
    }

    fun drawRect(canvas: Canvas, rectF: RectF) {
        canvas.drawRoundRect(rectF, myRound, myRound, bgPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val wSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val hSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        Log.e("11111", "view onMeasure")
        bgRect.set(0f, 0f, wSpecSize.toFloat(), hSpecSize.toFloat())
    }
}


class ControlSelectDecor {
    private val leftBlockRect = RectF()
    private val rightBlockRect = RectF()
    private val leftBlockGrayRect = RectF()
    private val rightBlockGrayRect = RectF()
    private val blockWidth = TrackHelp.dp2px(20f).toFloat()
    private val roud = TrackHelp.dp2px(5f).toFloat()
    private val pathRect = RectF()
    private val roundPath = Path()


    private val bgPaintStroke: Paint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.FILL
        strokeWidth = roud
    }

    fun isSelectLeft(canvasOffsetX: Int, x: Float, y: Float): Boolean {
        return leftBlockRect.contains(x - canvasOffsetX, y)
    }

    fun isSelectRight(canvasOffsetX: Int, x: Float, y: Float): Boolean {
        return rightBlockRect.contains(x - canvasOffsetX, y)
    }


    fun drawSelectDecor(
        canvas: Canvas,
        stickerTrickItem: StickerTrickItem,
    ) {
        val viewRect = stickerTrickItem.viewRect

        stickerTrickItem.getItemView().drawRect(canvas, viewRect)
//
//        canvas.translate(stickerItemView.left.toFloat(), stickerItemView.top.toFloat())
        leftBlockRect.right = viewRect.left
        leftBlockRect.left = leftBlockRect.right - blockWidth
        leftBlockRect.top = viewRect.top
        leftBlockRect.bottom = viewRect.bottom

        rightBlockRect.left = viewRect.right
        rightBlockRect.right = rightBlockRect.left + blockWidth
        rightBlockRect.top = viewRect.top
        rightBlockRect.bottom = viewRect.bottom

        pathRect.set(viewRect)
        pathRect.left = leftBlockRect.left
        pathRect.right = rightBlockRect.right
        roundPath.reset()
        roundPath.addRoundRect(pathRect, roud, roud, Path.Direction.CCW)

        leftBlockGrayRect.set(0f, 0f, leftBlockRect.width() * 0.1f, leftBlockRect.height() * 0.2f)
        rightBlockGrayRect.set(leftBlockGrayRect)
        leftBlockGrayRect.offset(
            leftBlockRect.centerX() - leftBlockGrayRect.centerX(),
            leftBlockRect.centerY() - leftBlockGrayRect.centerY()
        )
        rightBlockGrayRect.offset(
            rightBlockRect.centerX() - rightBlockGrayRect.centerX(),
            rightBlockRect.centerY() - rightBlockGrayRect.centerY()
        )

        bgPaintStroke.color = Color.parseColor("#eaeaeb")
        canvas.clipPath(roundPath)

        bgPaintStroke.style = Paint.Style.STROKE
        canvas.drawPath(roundPath, bgPaintStroke)
        bgPaintStroke.style = Paint.Style.FILL
        canvas.drawRect(leftBlockRect, bgPaintStroke)
        canvas.drawRect(rightBlockRect, bgPaintStroke)
        bgPaintStroke.color = Color.parseColor("#8c8c8d")
        canvas.drawRect(leftBlockGrayRect, bgPaintStroke)
        canvas.drawRect(rightBlockGrayRect, bgPaintStroke)
    }
}

class HFrame : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    var allowIntercept: Boolean = false
    var scrollState = TrackTouchHelper.ScrollState.STATIC

    val scrollList = ArrayList<IScrollLayout>()

    private var touchSlop: Int = 0
    private var downX = 0f

    init {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    //什么时候拦截呢？当没有选中view的时候，拦截滚动
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        ev?.apply {
            when (action) {
                MotionEvent.ACTION_UP -> {
                    Log.e("11111", "onInterceptTouchEvent UP")
                    scrollState = TrackTouchHelper.ScrollState.STATIC
                }
                MotionEvent.ACTION_DOWN -> {
                    downX = x
                    scrollState = TrackTouchHelper.ScrollState.STATIC
                    Log.e("11111", "onInterceptTouchEvent DoWN")
                }
                MotionEvent.ACTION_MOVE -> {
                    val moveX = abs(downX - x)
                    if (allowIntercept && moveX >= touchSlop) {
                        scrollState = TrackTouchHelper.ScrollState.MOVE
                        Log.e("11111", "onInterceptTouchEvent ACTION_MOVE true")
                        return true
                    }
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.apply {
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.e("11111", "HFrame DoWN")
                }
                MotionEvent.ACTION_MOVE -> {
                    if (scrollState == TrackTouchHelper.ScrollState.MOVE) {
                        //滚动屏幕
                        scrollList.forEach {
                            val offsetX = (downX - x).toInt()
                            Log.e("11111", "HFrame ACTION_MOVE offsetX $offsetX")
                            it.scrollBy(offsetX, 0)
                        }
                    }
                    downX = x
                }
                MotionEvent.ACTION_UP -> {
                    Log.e("11111", "HFrame UP")
                    performClick()
                }
            }
        }
        return true
    }
}

interface IScrollLayout {
    fun scrollBy(offsetX: Int, offsetY: Int)
}

abstract class BaseScrollLayout : IScrollLayout {
    abstract fun getScrollX(): Int
    abstract fun scrollTo(offsetX: Int, offsetY: Int)
    override fun scrollBy(offsetX: Int, offsetY: Int) {
        var toX = getScrollX() + offsetX
        if (toX < 0) {
            toX = 0
        }
        scrollTo(toX, offsetY)
    }
}


class StickerGroup : ViewGroup {
    var scaleFactor: Float = 1f
    private var selectStickerTrickItem: StickerTrickItem? = null
    private var selectTrackIndex = 0

    var iIScrollLayout = object : BaseScrollLayout() {
        override fun getScrollX(): Int {
            return scrollX
        }

        override fun scrollTo(offsetX: Int, offsetY: Int) {
            this@StickerGroup.scrollTo(offsetX, offsetY)
        }
    }

    var allowInterceptCallback: ((Boolean) -> Unit)? = null

    private var iSelectCallBack: ((Int, Int) -> Unit) = { arg1, arg2 ->
        setCurrStickerTrickItem(arg1, arg2)
        invalidate()
    }
    private val dragMove: ((Int, Int, Float, Float) -> Unit) = { arg1, arg2, offsetX, offsetY ->
        TrackHelp.LOGE("move x $offsetX y $offsetY")
    }

    private val dragStart: ((Int, Int) -> Unit) = { arg1, arg2 ->
        allowInterceptCallback?.invoke(false)
        TrackHelp.LOGE("drag start")
        setCurrStickerTrickItem(arg1, arg2)

        selectStickerTrickItem?.apply {
            getItemView().stopDefaultDraw(isStop = true, isUpdate = true)
        }
        invalidate()
    }

    private val dragEnd: ((Int, Int) -> Unit) = { arg1, arg2 ->
        TrackHelp.LOGE("drag end")
        allowInterceptCallback?.invoke(true)
        selectStickerTrickItem?.apply {
            getItemView().stopDefaultDraw(isStop = false, isUpdate = false)
        }
        selectStickerTrickItem = null

    }


    val screenWidthHarf by lazy {
        return@lazy resources.displayMetrics.widthPixels / 2
    }


    private val trackTouchHelper = TrackTouchHelper(object : TrackTouchHelper.Callback {
        override fun selectBlockDown() {
            selectStickerTrickItem?.apply {
                getItemView().stopDefaultDraw(isStop = true, isUpdate = true)
            }
        }

        override fun selectBlockMove(state: TrackTouchHelper.HorizontalState, offsetTime: Float) {
            selectStickerTrickItem?.apply {
                if (state == TrackTouchHelper.HorizontalState.RIGHT) {
                    val endOffsetTime = endMillisecond + offsetTime.toLong()
                    endMillisecond = max(startMillisecond, endOffsetTime)
                } else {
                    val startOffsetTime = startMillisecond + offsetTime.toLong()
                    startMillisecond = min(max(0, startOffsetTime), endMillisecond)
                }
                data2ViewRect(selectTrackIndex, this, scaleFactor)
                invalidate()
            }
        }

        override fun allowIntercept(intercept: Boolean) {
            allowInterceptCallback?.invoke(intercept)
        }

        override fun selectBlockUp() {
            selectStickerTrickItem?.apply {
                getItemView().stopDefaultDraw(isStop = false, isUpdate = false)
                requestLayout()
            }
        }

        override fun emptyClick() {
            val apply = selectStickerTrickItem?.let {
                return@let true
            }
            apply?.apply {
                selectStickerTrickItem = null
                invalidate()
            }
        }

        override fun refreshLayout() {
            requestLayout()
        }
    })

    val controlSelectDecor by lazy {
        ControlSelectDecor()
    }

    //轨道集合
    private val trackSparseArray: SparseArray<TrackInfo> = SparseArray()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        descendantFocusability = FOCUS_AFTER_DESCENDANTS
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Log.e("11111", "onLayout  trackIndex ${trackSparseArray.size()}")
        trackSparseArray.forEachCompat { track_index, value ->
            value?.iTrackItemViews?.forEach { view ->
                val trackItemView: View = view.getView()
                val left =
                    screenWidthHarf + TrackHelp.time2Width(view.startMillisecond(), scaleFactor)
                        .toInt()
                val top = track_index * TrackHelp.TRACK_HEIGHT + TrackHelp.ITEM_MARGIN
                view.setDrawStatus(TrackHelp.ItemDrawState.UPDATE)
                trackItemView.layout(
                    left,
                    top,
                    left + trackItemView.measuredWidth,
                    top + trackItemView.measuredHeight
                )
            }
        }
    }

    fun addSticker(trackIndex: Int, stickerTrickItem: StickerTrickItem) {
        val indexOfKey = trackSparseArray.indexOfKey(trackIndex)
        val view = stickerTrickItem.getView()
        stickerTrickItem.data2ViewRect(trackIndex, stickerTrickItem, scaleFactor)
        if (indexOfKey < 0) {
            val trackInfo = TrackInfo(RectF())
            trackInfo.iTrackItemViews.add(stickerTrickItem)
            trackSparseArray.put(trackIndex, trackInfo)
        } else {
            trackSparseArray.valueAt(trackIndex)?.iTrackItemViews?.add(stickerTrickItem)
        }
        view.setOnTouchListener(
            ItemTouchHelper(
                ItemRunnable(
                    trackIndex,
                    stickerTrickItem.getItemId(),
                    dragMove,
                    dragStart,
                    dragEnd,
                    iSelectCallBack
                )
            )
        )
        addView(view)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)

        selectStickerTrickItem?.apply {
            canvas?.let {
                it.withSave {
                    translate((screenWidthHarf).toFloat(), 0f)
                    controlSelectDecor.drawSelectDecor(canvas, this@apply)
                }
            }
        }
        Log.e("draw", "dispatchDraw")
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    //回滚上来的方法处理
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.apply {
            if (action == MotionEvent.ACTION_UP) {
                performClick()
            }
            return trackTouchHelper.onTouch(
                this,
                scrollX,
                scrollY,
                selectStickerTrickItem,
                this@StickerGroup
            )
        }
        return super.onTouchEvent(event)
    }

    private fun setCurrStickerTrickItem(trackIndex: Int, stickId: Int) {
        trackSparseArray.valueAtCompat(trackIndex)?.apply {
            iTrackItemViews.forEach {
                if (it.getItemId() == stickId) {
                    if (it is StickerTrickItem) {
                        selectStickerTrickItem = it
                        selectTrackIndex = trackIndex
                        return@apply
                    }
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.e("11111", "viewGroup onMeasure")
        trackSparseArray.forEachCompat { _, value ->
            value?.iTrackItemViews?.forEach { view ->
                val childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    view.getItemViewRect().width().toInt(), MeasureSpec.EXACTLY
                )
                val childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    view.getItemViewRect().height().toInt(),
                    MeasureSpec.EXACTLY
                )
                view.getView().measure(childWidthMeasureSpec, childHeightMeasureSpec)
            }
        }
    }
}

class TimeRulerView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = TrackHelp.sp2px(12f).toFloat()
        color = Color.WHITE
    }

    var iIScrollLayout = object : BaseScrollLayout() {
        override fun getScrollX(): Int {
            return scrollX
        }

        override fun scrollTo(offsetX: Int, offsetY: Int) {
            this@TimeRulerView.scrollTo(offsetX, offsetY)
        }
    }

    private val linePaint: Paint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        strokeWidth = TrackHelp.dp2px(2f).toFloat()
        color = Color.WHITE
    }

    //一秒代表的间距
    var totalDuration = 10000L
    private var scale = 1f
    private var viewWidth = 0
    private var viewHeight = 0
    private var textWidth = 0f
    private var startIndicator = 0f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        viewHeight = TrackHelp.dp2px(30f)
        textWidth = textPaint.measureText("00:00")
        startIndicator = textWidth * 0.5f
        viewWidth = TrackHelp.time2Width(totalDuration, scale).toInt()
        requestSpace(scale)
        setMeasuredDimension(viewWidth + textWidth.toInt(), viewHeight)
        bgRect.set(
            0f,
            0f,
            viewWidth.toFloat(),
            viewHeight.toFloat()
        )
    }

    val simpleDateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())

    private val bgRect = RectF()
    private val screenWidthHalf by lazy {
        return@lazy resources.displayMetrics.widthPixels / 2
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            it.withSave {
                translate((screenWidthHalf).toFloat(), 0f)
//                drawRect(bgRect, bgPaint)
                drawTime(it)
            }
        }
    }

    var space = 0f


    private fun requestSpace(scale: Float) {
        this.scale = scale
        val defaultTime = 500L
        space = TrackHelp.time2Width(defaultTime, scale)
    }

    var formatBuilder: StringBuilder = java.lang.StringBuilder()
    private val formatter = Formatter(formatBuilder, Locale.getDefault())

    private fun drawTime(canvas: Canvas) {
        var offsetX = 0f
        while (offsetX <= viewWidth) {
            val width2Time = TrackHelp.width2Time(offsetX, scale)
            if (width2Time.toLong() % 1000L == 0L) {
                val fontMetrics = textPaint.fontMetrics
                val top = fontMetrics.top //为基线到字体上边框的距离,即上图中的top
                val bottom = fontMetrics.bottom //为基线到字体下边框的距离,即上图中的bottom
                val baseLineY = (bgRect.centerY() - top / 2 - bottom / 2)
                val totalSeconds = width2Time / 1000
                val seconds = (totalSeconds % 60).toInt()
                val minutes = ((totalSeconds / 60) % 60).toInt()
                formatBuilder.setLength(0)
                canvas.drawText(
                    formatter.format("%02d:%02d", minutes, seconds).toString(),
                    offsetX - startIndicator,
                    baseLineY,
                    textPaint
                )
            } else {
                canvas.drawPoint(offsetX, bgRect.centerY(), linePaint)
            }
            offsetX += space
        }

        /*for (index in 0..sumSecond) {
            val startX = startIndicator + (index * totalDuration).toFloat() - textWidth * 0.5f
            val fontMetrics = textPaint.fontMetrics
            val top = fontMetrics.top //为基线到字体上边框的距离,即上图中的top
            val bottom = fontMetrics.bottom //为基线到字体下边框的距离,即上图中的bottom
            val baseLineY = (bgRect.centerY() - top / 2 - bottom / 2)

            canvas.drawText(
                formatTimeHHmm(index * 60),
                startX,
                baseLineY,
                textPaint
            )

            canvas.drawPoint(
                startX + (totalDuration / 2) + textWidth * 0.5f,
                bgRect.centerY(),
                linePaint
            )
        }*/
    }

}