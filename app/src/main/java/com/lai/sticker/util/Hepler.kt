package com.lai.sticker.util

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.SparseArray
import android.util.TypedValue
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import android.view.ViewConfiguration
import androidx.activity.OnBackPressedCallback
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.lai.sticker.App
import com.lai.sticker.view.StickerGroup
import com.lai.sticker.view.bean.StickerTrickItem


object FragmentHelp {

    fun restoreFragment(activity: AppCompatActivity, tag: String?): Fragment? {
        val fm: FragmentManager = activity.supportFragmentManager
        return fm.findFragmentByTag(tag)
    }

    fun restoreFragment(fm: FragmentManager, tag: String?): Fragment? {
        return fm.findFragmentByTag(tag)
    }

    fun showOrHideFragment(activity: AppCompatActivity, fragment: Fragment) {
        val fm: FragmentManager = activity.supportFragmentManager
        if (fragment.isAdded) {
            if (fragment.isHidden) {
                fm.beginTransaction()
                    .show(fragment).commitAllowingStateLoss()
            } else {
                fm.beginTransaction()
                    .hide(fragment).commitAllowingStateLoss()
            }
            try {
                fm.executePendingTransactions()
            } catch (var4: IllegalStateException) {
            }
        }
    }

    fun showOrHideFragment(fm: FragmentManager, fragment: Fragment) {
        if (fragment.isAdded) {
            if (fragment.isHidden) {
                fm.beginTransaction()
                    .show(fragment).commitAllowingStateLoss()
            } else {
                fm.beginTransaction()
                    .hide(fragment).commitAllowingStateLoss()
            }
            try {
                fm.executePendingTransactions()
            } catch (var4: IllegalStateException) {
            }
        }
    }

    fun hideFragment(
        fm: FragmentManager, fragment: Fragment, @AnimatorRes @AnimRes enter: Int = 0,
        @AnimatorRes @AnimRes exit: Int = 0
    ) {
        if (fragment.isAdded) {
            if (!fragment.isHidden) {
                fm.beginTransaction()
                    .setCustomAnimations(enter, exit)
                    .hide(fragment).commitAllowingStateLoss()
            }
            try {
                fm.executePendingTransactions()
            } catch (var4: IllegalStateException) {
            }
        }
    }

    fun showFragment(
        fm: FragmentManager, fragment: Fragment, @AnimatorRes @AnimRes enter: Int = 0,
        @AnimatorRes @AnimRes exit: Int = 0
    ) {
        if (fragment.isAdded) {
            if (fragment.isHidden) {
                fm.beginTransaction()
                    .setCustomAnimations(enter, exit)
                    .show(fragment).commitAllowingStateLoss()
            }
            try {
                fm.executePendingTransactions()
            } catch (var4: IllegalStateException) {
            }
        }
    }

    fun initFragment(
        activity: AppCompatActivity,
        fragment: Fragment,
        containerId: Int,
        tag: String?,
        isShow: Boolean
    ) {
        val fm: FragmentManager = activity.supportFragmentManager
        if (isShow) {
            fm.beginTransaction()
                .add(containerId, fragment, tag).commitAllowingStateLoss()
        } else {
            fm.beginTransaction()
                .add(containerId, fragment, tag)
                .hide(fragment).commitAllowingStateLoss()
        }
        try {
            fm.executePendingTransactions()
        } catch (var9: java.lang.IllegalStateException) {
        }
    }

    fun initFragment(
        fm: FragmentManager,
        fragment: Fragment,
        containerId: Int,
        tag: String?,
        isShow: Boolean
    ) {
        if (isShow) {
            fm.beginTransaction()
                .add(containerId, fragment, tag).commitAllowingStateLoss()
        } else {
            fm.beginTransaction()
                .add(containerId, fragment, tag)
                .hide(fragment).commitAllowingStateLoss()
        }
        try {
            fm.executePendingTransactions()
        } catch (var9: java.lang.IllegalStateException) {
        }
    }

    fun isFragmentShowing(fragment: Fragment?): Boolean {
        return if (fragment == null) {
            false
        } else {
            fragment.isAdded && !fragment.isHidden
        }
    }

    fun addOnBackPressed(
        fragment: Fragment,
        owner: LifecycleOwner,
        onBackPressed: () -> Boolean
    ): OnBackPressedCallback {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!isFragmentShowing(fragment)) {
                    isEnabled = false
                    fragment.requireActivity().onBackPressedDispatcher.onBackPressed()
                    isEnabled = true
                } else if (!onBackPressed()) {
                    isEnabled = false
                    fragment.requireActivity().onBackPressedDispatcher.onBackPressed()
                    isEnabled = true
                }
            }
        }
        fragment.requireActivity().onBackPressedDispatcher.addCallback(owner, callback)
        return callback
    }
}

object TrackHelp {
    //宽度单位
    private val WIDTH_UNIT = dp2px(56f)

    //1秒
    private const val MILLISECONDS_1000 = 1000f

    //軌道高度
    val TRACK_HEIGHT = dp2px(34f)
    val ITEM_MARGIN = dp2px(2f)

    //1000毫秒 = 100宽度
    //500毫秒=？
    //500毫秒 / 1000毫秒* 100宽度 = 50宽度
    fun time2Width(time: Long, scale: Float): Float {
        return time / (MILLISECONDS_1000 / scale) * WIDTH_UNIT
    }

    fun LOGE(string: String) {
        Log.e("11111", string)
    }

    enum class ItemDrawState {
        NULL, UPDATE
    }

    fun width2Time(distance: Float, scale: Float): Float {
        return distance * (MILLISECONDS_1000 / scale / WIDTH_UNIT)
    }

    fun dp2px(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            App.getApp().resources.displayMetrics
        )
            .toInt()
    }

    fun sp2px(sp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            App.getApp().resources.displayMetrics
        )
            .toInt()
    }
}

/** Performs the given [action] for each key/value entry. */
public inline fun <T> SparseArray<T>.forEachCompat(action: (key: Int, value: T?) -> Unit) {
    for (index in 0 until size()) {
        action(keyAtCompat(index), valueAtCompat(index))
    }
}

/**
 * 兼容方式使用 SparseArray.keyAt 方法
 * @see SparseArray.keyAt
 */
fun <E> SparseArray<E>.keyAtCompat(index: Int): Int =
    if (index in 0 until size()) {
        keyAt(index)
    } else {
        0
    }

/**
 * 兼容方式使用 SparseArray.valueAt 方法
 * @see SparseArray.valueAt
 */
fun <E> SparseArray<E>.valueAtCompat(index: Int): E? =
    if (index in 0 until size()) {
        valueAt(index)
    } else {
        null
    }

class ItemRunnable(
    private val trackIndex: Int,
    private val stickerId: Int,
    private val dragMove: ((Int, Int, Float, Float) -> Unit),
    private val dragStart: ((Int, Int) -> Unit),
    private val dragEnd: ((Int, Int) -> Unit),
    private val selectBack: ((Int, Int) -> Unit)
) : Runnable {
    override fun run() {
        selectBack.invoke(trackIndex, stickerId)
    }

    fun dragMove(offsetX: Float, offsetY: Float) {
        dragMove.invoke(trackIndex, stickerId, offsetX, offsetY)
    }

    fun dragStart() {
        dragStart.invoke(trackIndex, stickerId)
    }

    fun dragEnd() {
        dragEnd.invoke(trackIndex, stickerId)
    }
}

data class TrackTouchHelper(private val callback: Callback) {

    private var currState: HorizontalState = HorizontalState.NULL

    enum class HorizontalState {
        NULL, LEFT, RIGHT
    }

    enum class ScrollState {
        STATIC, MOVE
    }

    interface Callback {
        fun selectBlockDown()
        fun selectBlockMove(state: HorizontalState, offsetTime: Float)
        fun allowIntercept(intercept: Boolean)
        fun selectBlockUp()
        fun emptyClick()
        fun refreshLayout()
    }


    private fun setState(horizontalState: HorizontalState) {
        currState = horizontalState
    }

    private var downX: Float = 0f
    fun onTouch(
        event: MotionEvent, scrollX: Int, scrollY: Int,
        stickerTrickItem: StickerTrickItem?,
        stickerGroup: StickerGroup
    ): Boolean {
        when (event.action) {
            ACTION_DOWN -> {
                setState(HorizontalState.NULL)
                stickerTrickItem?.apply {
                    val x = scrollX + event.x
                    val y = scrollY + event.y
                    downX = x
                    if (stickerGroup.controlSelectDecor.isSelectLeft(
                            stickerGroup.screenWidthHarf,
                            x,
                            y
                        )
                    ) {
                        callback.selectBlockDown()
                        setState(HorizontalState.LEFT)
                    } else if (stickerGroup.controlSelectDecor.isSelectRight(
                            stickerGroup.screenWidthHarf,
                            x,
                            y
                        )
                    ) {
                        callback.selectBlockDown()
                        setState(HorizontalState.RIGHT)
                    }
                }
                if (currState == HorizontalState.NULL) {
                    callback.allowIntercept(true)
                } else {
                    callback.allowIntercept(false)
                }
                TrackHelp.LOGE("TrackGroup DOWN")
            }
            ACTION_MOVE -> {
                TrackHelp.LOGE("TrackGroup MOVE")
                stickerTrickItem?.apply {
                    if (HorizontalState.NULL != currState) {
                        val tagOffsetX = event.x + scrollX - downX
                        val width2Time = TrackHelp.width2Time(tagOffsetX, stickerGroup.scaleFactor)
                        callback.selectBlockMove(currState, width2Time)
                        downX = event.x + scrollX
                    }
                }
            }
            ACTION_UP -> {
                if (currState == HorizontalState.NULL) {
                    callback.allowIntercept(false)
                    callback.emptyClick()
                } else {
                    callback.selectBlockUp()
                }
            }
        }
        return true
    }


}

class ItemTouchHelper(private val eventRunnable: ItemRunnable) : View.OnTouchListener {
    private var isMoving = false
    private val handler = Handler(Looper.getMainLooper())
    private var currentTimeMills = 0L
    private var isLongPressed = false
    private var mCurrRawX: Float = 0f
    private var mCurrRawY: Float = 0f

    private val longClickListener = Runnable {
        isLongPressed = true
        eventRunnable.dragStart()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        event?.apply {
            when (action) {
                ACTION_DOWN -> {
                    mCurrRawX = rawX
                    mCurrRawY = rawY
                    handler.postDelayed(
                        longClickListener,
                        ViewConfiguration.getLongPressTimeout().toLong()
                    ) // 长按
                    TrackHelp.LOGE("view DOWN")
                }
                ACTION_UP -> {
                    v?.performClick()
                    Log.e("11111", "view UP")
                    handler.removeCallbacks(longClickListener)

                    if (isLongPressed) {
                        eventRunnable.dragEnd()
                        isLongPressed = false
                    } else if (!isMoving) {
                        //                        mHandler.removeCallbacks(mLongPressRunnable)
                        if (System.currentTimeMillis() - currentTimeMills < 200) {
                            handler.removeCallbacks(eventRunnable)
                            handler.post(eventRunnable) // 双击
                        } else {
                            handler.postDelayed(eventRunnable, 200) // 单击
                        }
                        isMoving = false
                    }
                    currentTimeMills = System.currentTimeMillis()
                }
                ACTION_MOVE -> {
                    val moveRawX: Float = event.rawX - mCurrRawX
                    val moveRawY: Float = event.rawY - mCurrRawY
                    if (isLongPressed) {
                        eventRunnable.dragMove(moveRawX, moveRawY)
                    }

                    mCurrRawX = rawX
                    mCurrRawY = rawY
                }
            }
        }
        return true
    }
}





