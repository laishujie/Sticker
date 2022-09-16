package com.lai.sticker.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.Nullable;

import com.lai.sticker.R;


/**
 * @Description: 修图拖动条 ,默认0到100，需要改变调用{@link #resetProgressRange}
 * 拖动条中心线位于控件中心
 * @Author: leory
 * @Time: 2021/2/4
 */
public class BeautySeekBar extends View {
    Context mContext;
    private int mProgress = 0;//当前进度
    private int mLastProgress = Integer.MAX_VALUE;//上一次记录的进度
    private int mStartProgress = 0;//最小值
    private int mEndProgress = 100;//最大值
    private int mWidth;//视图宽度
    private int mHeight;//视图高度
    private int mCircleRadius;//拖动圆半径
    private int mSeekBarHeight;//拖动条高度
    private int mPadding;//间距
    private int mSeekBarWidth;//拖动条宽度
    private Paint mCirclePaint;//圆画笔
    private Paint mSelectPaint;//选中画笔
    private Paint mUnSelectPaint;//未选中画笔
    private Paint mTextPaint;//文本画笔
    private ValueAnimator mTxtDisappearAnimator;
    private boolean bNeedDrawText = true;
    private boolean mUiEnable = true;

    private OnSeekChangeListener mListener;
    private int mTouchSlop;
    private float mDownX, mDownY;
    private boolean mHasMove;

    public BeautySeekBar(Context context) {
        this(context, null);
    }

    public BeautySeekBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BeautySeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mSeekBarWidth = mWidth - 2 * mPadding;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float progress = 1F * (mProgress - mStartProgress) / (mEndProgress - mStartProgress);
        float progressWidth = mSeekBarWidth * progress;
        float centerY = mHeight / 2f;
        float startX = mPadding;
        float endX = mPadding + mSeekBarWidth;
        float centerZero = mPadding + mSeekBarWidth * 1F * (0 - mStartProgress) / (mEndProgress - mStartProgress);
        float curX = progressWidth + mPadding;
        //画左线
        canvas.drawLine(startX, centerY, curX, centerY, mUnSelectPaint);
        //画右线
        canvas.drawLine(centerZero, centerY, endX, centerY, mUnSelectPaint);
        //画中间线
        canvas.drawLine(centerZero, centerY, curX, centerY, mSelectPaint);


        //画圆
        float circleY = mHeight / 2f;
        float circleX = mPadding + progressWidth;
        canvas.drawCircle(circleX, circleY, mCircleRadius, mCirclePaint);

        //画文本
        if (bNeedDrawText) {
            String text = String.valueOf(mProgress);
            float textY = mHeight / 2f - DpToPx(20);
            canvas.drawText(text, curX, textY, mTextPaint);
        }
    }
    /**
     * dp转为px
     */
    public int DpToPx(float dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }


    private void init() {
        mTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        mSeekBarHeight = DpToPx(1.5f);
        mCircleRadius = DpToPx(8);
        mPadding = DpToPx(16);
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(getResources().getColor(R.color.neutral100));
        mSelectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectPaint.setColor(getResources().getColor(R.color.neutral100));
        mSelectPaint.setStrokeWidth(mSeekBarHeight);
        mUnSelectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mUnSelectPaint.setColor(getResources().getColor(R.color.white_15));
        mUnSelectPaint.setStrokeWidth(mSeekBarHeight);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(DpToPx(13));
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setAlpha(0);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mUiEnable) {
            return super.onTouchEvent(event);
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mDownX = event.getX();
            mDownY = event.getY();
            mHasMove = false;
            processTouch(event.getX(), MotionEvent.ACTION_DOWN);
            cancelDisappearText();
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float dx = event.getX() - mDownX;
            float dy = event.getY() - mDownY;
            if (!mHasMove && Math.abs(dx) > mTouchSlop && Math.abs(dx) > Math.abs(dy)) {
                getParent().requestDisallowInterceptTouchEvent(true);
                mHasMove = true;
            }
            if (mHasMove) {
                processTouch(event.getX(), MotionEvent.ACTION_MOVE);
                return true;
            }

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            processTouch(event.getX(), MotionEvent.ACTION_UP);
            getParent().requestDisallowInterceptTouchEvent(false);
            return true;
        }
        return super.onTouchEvent(event);
    }


    private void processTouch(float x, int action) {
        float curWidth = x - mPadding;
        if (curWidth < 0) {
            curWidth = 0;
        }
        if (curWidth > mSeekBarWidth) {
            curWidth = mSeekBarWidth;
        }
        float progress = curWidth / mSeekBarWidth;
        mProgress = (int) (progress * (mEndProgress - mStartProgress) + mStartProgress);
        if (action == MotionEvent.ACTION_MOVE && mLastProgress == mProgress) {
            return;
        }
        mLastProgress = mProgress;
        if (mListener != null) {
            mListener.onProgress(mProgress, action);
        }
        disappearText(action);
        invalidate();


    }

    private void disappearText(int action) {
        if (action == MotionEvent.ACTION_UP) {
            cancelDisappearText();
            mTxtDisappearAnimator = ValueAnimator.ofInt(255, 0);
            mTxtDisappearAnimator.setDuration(200);
            mTxtDisappearAnimator.setStartDelay(1000);
            mTxtDisappearAnimator.addUpdateListener(animation -> {
                int alpha = (int) animation.getAnimatedValue();
                mTextPaint.setAlpha(alpha);
                invalidate();
            });
            mTxtDisappearAnimator.start();
        }
        mTextPaint.setAlpha(255);

    }

    private void cancelDisappearText() {
        if (mTxtDisappearAnimator != null) {
            mTxtDisappearAnimator.cancel();
        }
    }

    /**
     * 重置进度条数值范围
     *
     * @param startProgress
     * @param endProgress
     */
    public void resetProgressRange(int startProgress, int endProgress) {
        mStartProgress = startProgress;
        mEndProgress = endProgress;
    }

    public void setNeedDrawText(boolean bNeedDrawText) {
        this.bNeedDrawText = bNeedDrawText;
    }

    public int getProgress() {
        return mProgress;
    }

    /**
     * 设置当前进度
     *
     * @param progress mStartProgress到mEndProgress
     */
    public void setProgress(int progress) {
        mProgress = progress;
        if (mProgress < mStartProgress) {
            mProgress = mStartProgress;
        }
        if (mProgress > mEndProgress) {
            mProgress = mEndProgress;
        }
        invalidate();
    }

    public void setUiEnable(boolean uiEnable) {
        mUiEnable = uiEnable;
    }

    /**
     * 设置监听
     *
     * @param listener
     */
    public void setOnSeekChangeListener(OnSeekChangeListener listener) {
        mListener = listener;
    }

    public interface OnSeekChangeListener {
        /**
         * @param progress mStartProgress到mEndProgress
         * @param action
         */
        void onProgress(int progress, int action);
    }

}
