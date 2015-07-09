package com.dygame.myasynctaskprogressbarsample;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Administrator on 2015/5/29.
 *  Custom Progressbar
 *  from Leaf_Loading
 */
public class CustomLoadingBar extends View
{
    /**
     * 畫筆對象的引用
     */
    private Paint paint;
    /**
     *  當前進度
     */
    private int startProgress;
    /**
     * 進度百分比的字符串的顏色
     */
    private int textColor;
    /**
     * 進度百分比的字符串的字體
     */
    private float textSize;
    /**
     * 總進度
     */
    private int max;
    // 淡白色
    private static final int WHITE_COLOR = 0xfffde399;
    // 橙色
    private static final int ORANGE_COLOR = 0xffffa800;
    // 所繪製的進度條部分的寬高
    private int mProgressWidth;
    private int mProgressHeight;
    // 當前所在的繪製的進度條的位置
    private int mCurrentProgressPosition;
    // 用於控制繪製的進度條距離左／上／下的距離
    private static final int LEFT_MARGIN = 9;
    // 用於控制繪製的進度條距離右的距離
    private static final int RIGHT_MARGIN = 25;
    private int mLeftMargin, mRightMargin;
    // 弧形的半徑
    private int mArcRadius;
    // arc的右上角的x坐標，也是矩形x坐標的起始點
    private int mArcRightLocation;
    //
    private int mTotalWidth, mTotalHeight;
    private Paint mWhitePaint, mOrangePaint;
    private RectF mWhiteRectF, mOrangeRectF, mArcRectF;
    //
    private static String TAG = "MyCrashHandler" ;
    //
    public CustomLoadingBar (Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mLeftMargin = dipToPx(context, LEFT_MARGIN);
        mRightMargin = dipToPx(context, RIGHT_MARGIN);
        paint = new Paint();
        mWhitePaint = new Paint();
        mWhitePaint.setAntiAlias(true);
        mWhitePaint.setColor(WHITE_COLOR);
        mOrangePaint = new Paint();
        mOrangePaint.setAntiAlias(true);
        mOrangePaint.setColor(ORANGE_COLOR);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);
        //文字的顏色
        textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, 0xffff5f5f);
        //文字的大小
        textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, 18);
        //最大進度
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
        //當前進度
        startProgress = mTypedArray.getInt(R.styleable.RoundProgressBar_startProgress, 0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalWidth = w;
        mTotalHeight = h;
        mProgressWidth = mTotalWidth - mLeftMargin - mRightMargin;
        mArcRadius = (mTotalHeight - 2 * mLeftMargin) ;
        //auto scale
        if (mArcRadius < 20) mArcRadius = 15 ;
        else if (mArcRadius < 50) mArcRadius /= 2 ;
        else mArcRadius = 25 ;
        //
        mProgressHeight = mArcRadius * 3 ;
        Log.i(TAG, "mProgressWidth = " + mProgressWidth + "  mTotalWidth= " + mTotalWidth + "  mTotalHeight=" + mTotalHeight + "  mArcRadius=" + mArcRadius);

        mWhiteRectF = new RectF(mLeftMargin + mCurrentProgressPosition, mLeftMargin, mTotalWidth - mRightMargin, mProgressHeight);
        mOrangeRectF = new RectF(mLeftMargin + mArcRadius, mLeftMargin , mCurrentProgressPosition , mProgressHeight);
        mArcRectF = new RectF(mLeftMargin, mLeftMargin, mLeftMargin + 2 * mArcRadius, mProgressHeight);
        mArcRightLocation = mLeftMargin + mArcRadius;

        Log.i(TAG, "mLeftMargin = " + mLeftMargin + "  mRightMargin= " + mRightMargin + "  mArcRightLocation=" + mArcRightLocation);
    }

    @Override
   protected void onDraw(Canvas canvas)
   {
        super.onDraw(canvas);
       // 繪製進度條
//       if (startProgress > 75)  mOrangePaint.setColor(0xffff0000);
//       else if (startProgress > 50) mOrangePaint.setColor(0xffffe800);
//       else if (startProgress > 25) mOrangePaint.setColor(0xffffc800);
       drawProgressAndLeafs(canvas);
       /**
           * 畫進度百分比
           */
       paint.setStrokeWidth(0);
       paint.setColor(textColor);
       paint.setTextSize(textSize);
       paint.setTypeface(Typeface.DEFAULT_BOLD); //設置字體
       int percent = (int)(((float)startProgress / (float)max) * 100); //中間的進度百分比，先轉換成float在進行除法運算，不然都為0
       float textWidth = paint.measureText(percent + "%");   //測量字體寬度，需要根據字體的寬度設置
       canvas.drawText(percent + "%", mArcRadius + textWidth / 2, mArcRadius + textSize / 2, paint); //畫出進度百分比
       //設置進度，此為線程安全控件，由於考慮多線的問題，需要同步刷新界面調用postInvalidate()能在非UI線程刷新
       postInvalidate();
    }

  private void drawProgressAndLeafs(Canvas canvas) 
  {
        if (startProgress > max) startProgress = max;
        // mProgressWidth為進度條的寬度，根據當前進度算出進度條的位置
        mCurrentProgressPosition = mProgressWidth * startProgress / max;
      // 即當前位置在圖中所示1範圍內
        if (mCurrentProgressPosition < mArcRadius)
        {
            // 1.繪製白色ARC，繪製orange ARC
            // 2.繪製白色矩形

            // 1.繪製白色ARC
            canvas.drawArc(mArcRectF, 90, 180, false, mWhitePaint);

            // 2.繪製白色矩形
            mWhiteRectF.left = mArcRightLocation;
            canvas.drawRect(mWhiteRectF, mWhitePaint);

            // 3.繪製棕色 ARC
            // 單邊角度
            int angle = (int) Math.toDegrees(Math.acos((mArcRadius - mCurrentProgressPosition) / (float) mArcRadius));
            // 起始的位置
            int startAngle = 180 - angle;
            // 掃過的角度
            int sweepAngle = 2 * angle;
            if (startProgress!= 100 && startProgress!=0) Log.i(TAG, "mProgress = " + startProgress + " startAngle= " + startAngle + "  mArcRadius=" + mArcRadius);
            canvas.drawArc(mArcRectF, startAngle, sweepAngle, false, mOrangePaint);
        }
        else
        {
            if (startProgress!= 100 && startProgress!=0) Log.i(TAG, "mProgress = " + startProgress + "mCurrentProgressPosition = " + mCurrentProgressPosition + "mArcRadius=" + mArcRadius);
            // 1.繪製white RECT
            // 2.繪製Orange ARC
            // 3.繪製orange RECT

            // 1.繪製white RECT
            mWhiteRectF.left = mCurrentProgressPosition;
            canvas.drawRect(mWhiteRectF, mWhitePaint);
            // 2.繪製Orange ARC
            canvas.drawArc(mArcRectF, 90, 180, false, mOrangePaint);
            // 3.繪製orange RECT
            mOrangeRectF.left = mArcRightLocation;
            mOrangeRectF.right = mCurrentProgressPosition;
            canvas.drawRect(mOrangeRectF, mOrangePaint);
        }
  }
  /**
     * 設置進度
     * 
     * @param progress
     */
    public void setProgress(int progress) {
        this.startProgress = progress;
        postInvalidate();
    }

    //
    public int dipToPx(Context context, int dip)
    {
        return (int) (dip * getScreenDensity(context) + 0.5f);
    }
    static public float getScreenDensity(Context context)
    {
        try
        {
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
            return dm.density;
        }
        catch (Exception e)
        {
            return DisplayMetrics.DENSITY_DEFAULT;
        }
    }
}
