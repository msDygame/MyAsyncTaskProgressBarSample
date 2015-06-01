package com.dygame.myasynctaskprogressbarsample;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.net.URL;


public class MainActivity extends ActionBarActivity
{
    protected Button testButton ;
    protected ProgressBar myProgressBar1;
    protected ProgressBar myProgressBar2;
    protected CustomProgressBar myProgressBar3 ;
    protected CustomLoadingBar myProgressBar4 ;
    protected ProgressDialog mDialog;
    protected ImageView ivFan ;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Uncaught Exception Handler(Crash Exception)
        MyCrashHandler pCrashHandler = MyCrashHandler.getInstance();
        pCrashHandler.init(getApplicationContext());
        //find resource
        testButton = (Button)findViewById(R.id.button1) ;
        testButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new DownloadFilesTask(MainActivity.this).execute();
            }
        });
        myProgressBar1 = (ProgressBar)findViewById(R.id.progressBar1) ;
        myProgressBar2 = (ProgressBar)findViewById(R.id.progressBar2) ;
        myProgressBar3 = (CustomProgressBar)findViewById(R.id.progressBar3) ;
        myProgressBar4 = (CustomLoadingBar)findViewById(R.id.progressBar4) ;
        ivFan = (ImageView) findViewById(R.id.ivFan_pic);
        //
        rotateAnimationFan();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * sample code from   http://developer.android.com/reference/android/os/AsyncTask.html
     *      The AsyncTask class must be loaded on the UI thread. This is done automatically as of JELLY_BEAN.
     *  The task instance must be created on the UI thread.
     *  execute(Params...) must be invoked on the UI thread.
     *  Do not call onPreExecute(), onPostExecute(Result), doInBackground(Params...), onProgressUpdate(Progress...) manually.
     *  The task can be executed only once (an exception will be thrown if a second execution is attempted.)
     */
    //分別是 參數（例子裡是URL），進度(publishProgress用到)，返回值 類型
    private class DownloadFilesTask extends AsyncTask<URL, Integer, String>
    {
        private Context pContext;
        public DownloadFilesTask(Context mContext)
        {
            pContext = mContext;
        }
        //初始化的工作
        @Override
        protected void onPreExecute()
        {
            mDialog = new ProgressDialog(pContext);
            mDialog.setMessage("Loading...");
            mDialog.setCancelable(false);
            mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mDialog.show();
            super.onPreExecute();
        }
        //只要呼叫execute這個方法， 就開始使用它了。
        protected String doInBackground(URL... urls)
        {
            int progress =0;
            while(progress<=100)
            {
                try
                {
                    Thread.sleep(100);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                publishProgress(Integer.valueOf(progress));
                // Escape early if cancel() is called
                if (isCancelled()) break;
                progress++ ;
            }
            return "Done";
        }
        //此函數在doInBackground調用publishProgress時觸發，雖然調用時只有一個參數，但是這裡取到的是一個數組，所以要用progesss[0]來取值
        protected void onProgressUpdate(Integer... progress)
        {
            mDialog.setProgress(progress[0]);
            myProgressBar1.setProgress(progress[0]);
            myProgressBar2.setProgress(progress[0]);
            myProgressBar3.setProgress(progress[0]);
            myProgressBar4.setProgress(progress[0]);
        }
        //此函數在doInBackground返回時觸發，result就是doInBackground執行後的返回值
        protected void onPostExecute(String result)
        {
            if (result.equals("Done"))
            {
                mDialog.dismiss();
            }
        }
    }
    /**
     *
     */
    public void rotateAnimationFan()
    {
        RotateAnimation rotateAnimation = initRotateAnimation(false, 1500, true, Animation.INFINITE);
        ivFan.startAnimation(rotateAnimation);
    }
    public static RotateAnimation initRotateAnimation(boolean isClockWise, long duration, boolean isFillAfter, int repeatCount)
    {
        int endAngle;
        if (isClockWise)
        {
            endAngle = 360;
        }
        else
        {
            endAngle = -360;
        }
        RotateAnimation mLoadingRotateAnimation = new RotateAnimation(0, endAngle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        LinearInterpolator lirInterpolator = new LinearInterpolator();
        mLoadingRotateAnimation.setInterpolator(lirInterpolator);
        mLoadingRotateAnimation.setDuration(duration);
        mLoadingRotateAnimation.setFillAfter(isFillAfter);
        mLoadingRotateAnimation.setRepeatCount(repeatCount);
        mLoadingRotateAnimation.setRepeatMode(Animation.RESTART);
        return mLoadingRotateAnimation;
    }
}
