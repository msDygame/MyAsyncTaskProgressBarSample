package com.dygame.myasynctaskprogressbarsample;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2015/5/29.
 * Custom Progressbar
 * from : http://blog.csdn.net/xiaanming/article/details/10298163
 */
public class CustomProgressBar extends View
{
    /**
     * �e����H���ޥ�
     */
    private Paint paint;
    /**
     * �������C��
     */
    private int roundColor;
    /**
     * �����i�ת��C��
     */
    private int roundProgressColor;
    /**
     * �����i�צʤ��񪺦r�Ŧꪺ�C��
     */
    private int textColor;
    /**
     * �����i�צʤ��񪺦r�Ŧꪺ�r��
     */
    private float textSize;
    /**
     * �������e��
     */
    private float roundWidth;
    /**
     * �̤j�i��
     */
    private int max;
    /**
     * �O�_��ܤ������i��
     */
    private boolean textIsDisplayable;
    /**
     * �i�ת�����A��ߩΪ̪Ť�
     */
    /**
     * ��e�i��
     */
    private int startProgress;
    /**
     * �i�׶}�l�����׼�
     */
    private int startAngle;
    //������������R��
    private int centreColor ;
    //
    private int style;
    public static final int STROKE = 0;
    public static final int FILL = 1;
    //constructor
    public CustomProgressBar(Context context)
    {
        this(context, null);
    }

    public CustomProgressBar(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        paint = new Paint();
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);

        //����۩w�q�ݩʩM�q�{�ȡA�Ĥ@�ӰѼƬO�q�Τ��ݩʤ��o�쪺�]�m�A�p�G�Τ�S���]�m�A����N���q�{���ݩʡA�Y�G�ĤG�ӰѼ�
        //�������C��
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
        //�����i�ױ����C��
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
        //��r���C��
        textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, 0xffff5f5f);
        //��r���j�p
        textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, 25);
        //�������e��
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 10);
        //�̤j�i��
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
        //�O�_��ܤ������i��
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);
        //�i�ת�����A��ߩΪ̪Ť�
        style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);
        //�i�׶}�l�����׼�
        startAngle = mTypedArray.getInt(R.styleable.RoundProgressBar_startAngle, -90);
        //��ߪ��C��
        centreColor = mTypedArray.getColor(R.styleable.RoundProgressBar_centreColor,  0xff00ffff);
        //��e�i��
        startProgress = mTypedArray.getInt(R.styleable.RoundProgressBar_startProgress, 0);
        //�O�_��ܤ������i��
        //�^���귽
        mTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        int centre = getWidth()/2; //�����ߪ�x����
        int radius = (int) (centre - roundWidth/2); //�������b�|
        /**
             * ���� �e���ߪ��C��
             */
        if (centreColor != 0)
        {
            paint.setAntiAlias(true);
            paint.setColor(centreColor);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(centre, centre, radius, paint);
        }
        /**
             * �e�̥~�h���j����
             */
        paint.setColor(roundColor); //�]�m�������C��
        paint.setStyle(Paint.Style.STROKE); //�]�m�Ť�
        paint.setStrokeWidth(roundWidth); //�]�m�������e��
        paint.setAntiAlias(true);  //��������
        canvas.drawCircle(centre, centre, radius, paint); //�e�X����
        Log.e("log", centre + "");
        /**
             * �e�i�צʤ���
             */
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT_BOLD); //�]�m�r��
        int percent = (int)(((float)startProgress / (float)max) * 100);  //�������i�צʤ���A���ഫ��float�b�i�氣�k�B��A���M����0
        float textWidth = paint.measureText(percent + "%");   //���q�r��e�סA�ڭ̻ݭn�ھڦr�骺�e�׳]�m�b��������

        if(textIsDisplayable && percent != 0 && style == STROKE)
        {
            canvas.drawText(percent + "%", centre - textWidth / 2, centre + textSize/2, paint); //�e�X�i�צʤ���
        }

        /**
             * �e�꩷ �A�e�������i��
             */
        //�]�m�i�׬O����٬O�Ť�
        paint.setStrokeWidth(roundWidth); //�]�m�������e��
        paint.setColor(roundProgressColor);  //�]�m�i�ת��C��
        RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius);  //�Ω�w�q���꩷���Ϊ��M�j�p���ɭ�

        switch (style)
        {
            case STROKE:
            {
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, startAngle , 360 * startProgress / max, false, paint);  //�ھڶi�׵e�꩷
                /**
                        * �ĤG�ӰѼƬO�i�׶}�l�����סA-90��ܱq12�I��V�}�l���i�סA�p�G�O0��ܱq�T�I����V���i�סA�̦�����
                        * public void drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)
                        * oval :���w�꩷���~�����x�ΰϰ�C
                        * startAngle: �꩷�_�l���סA��쬰�סC
                        * sweepAngle: �꩷���L�����סA���ɰw��V�A��쬰�סC
                        * useCenter: �p�G��True�ɡA�bø�s�꩷�ɱN��ߥ]�A�b���A�q�`�Ψ�ø�s���ΡC
                        * paint: ø�s�꩷���e�O�ݩʡA�p�C��A�O�_��R��
                        */
                break;
            }
            case FILL:
            {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if(startProgress !=0)
                    canvas.drawArc(oval, startAngle , 360 * startProgress / max, true, paint);  //�ھڶi�׵e�꩷
                break;
            }
        }
    }

    public synchronized int getMax()
    {
        return max;
    }

    /**
     * �]�m�i�ת��̤j��
     * @param max
     */
    public synchronized void setMax(int max)
    {
        if(max < 0)
        {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * ����i��.�ݭn�P�B
     * @return
     */
    public synchronized int getProgress()
    {
        return startProgress;
    }

    /**
     * �]�m�i�סA�����u�{�w������A�ѩ�Ҽ{�h�u�����D�A�ݭn�P�B
     * ��s�ɭ��ե�postInvalidate()��b�DUI�u�{��s
     * @param progress
     */
    public synchronized void setProgress(int progress)
    {
        if(progress < 0)
        {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if(progress > max)
        {
            progress = max;
        }
        if(progress <= max)
        {
            this.startProgress = progress;
            postInvalidate();
        }
    }
    public int getCricleColor()
    {
        return roundColor;
    }

    public void setCricleColor(int cricleColor)
    {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor()
    {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor)
    {
        this.roundProgressColor = cricleProgressColor;
    }

    public int getTextColor()
    {
        return textColor;
    }

    public void setTextColor(int textColor)
    {
        this.textColor = textColor;
    }

    public float getTextSize()
    {
        return textSize;
    }

    public void setTextSize(float textSize)
    {
        this.textSize = textSize;
    }

    public float getRoundWidth()
    {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth)
    {
        this.roundWidth = roundWidth;
    }
}
