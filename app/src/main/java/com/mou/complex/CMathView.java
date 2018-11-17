package com.mou.complex;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import io.github.kexanie.library.MathView;

public class CMathView extends MathView {
    private static final String TAG = "CMathView";
    private Rect bounds;
    private Paint outlinePaint;
    private boolean loadingFinished;

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public void setHighlighted(boolean highlighted, int color) {
        isHighlighted = highlighted;
        if(highlighted)outlinePaint.setColor(color);
        invalidate();
    }

    private boolean isHighlighted = false;
    public CMathView(Context context, AttributeSet attrs) {
        super(context, attrs);
         bounds = new Rect();
        outlinePaint = new Paint();


        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setColor(Color.BLACK);
        outlinePaint.setStrokeWidth(20);
        outlinePaint.setStrokeCap(Paint.Cap.ROUND);

        setWebViewClient(new WebViewClient() {

            private boolean redirect;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                if (!loadingFinished) {
                    redirect = true;
                }
                loadingFinished = false;
                view.loadUrl(urlNewString);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                loadingFinished = false;
                Log.d(TAG, "onPageStarted: page started");
                //SHOW LOADING IF IT ISNT ALREADY VISIBLE
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(!redirect){
                    loadingFinished = true;
                    Log.d(TAG, "onPageStarted: page finished "+ loadingFinished);

                }

                if(loadingFinished && !redirect){
                    //HIDE LOADING IT HAS FINISHED
                } else{
                    redirect = false;
                }

            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(isHighlighted){
            canvas.getClipBounds(bounds);
            canvas.drawRect(bounds, outlinePaint);
        }
    }

    public boolean isLoadingFinished(){
        return loadingFinished;
    }
    @Override
    public void setText(String text) {
        text = "$$" +text+"$$";
        super.setText(text);
    }


}
