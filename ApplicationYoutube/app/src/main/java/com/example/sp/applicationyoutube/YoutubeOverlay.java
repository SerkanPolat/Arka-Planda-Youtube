package com.example.sp.applicationyoutube;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

public class YoutubeOverlay extends Service {
    Button buttonHareket;
    Button btnGizle;
    Button btnActivity;
    static String Adres;
    public YoutubeOverlay(){
        super();
    }

    private static final int LayoutParamFlags = WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;

    private LayoutInflater inflater;
    private Display mDisplay;
    private View layoutView;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    ConstraintLayout OverlayDisplayMain;
    WebView displayYoutubeVideo;
    @Override
    public void onCreate() {
        ServisNotification.youtubeOverlay = this;
        super.onCreate();
        Log.d("Mesaj","Overlay On Create Icindeyim "+Adres);
        params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT
                ,WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PRIORITY_PHONE,
                LayoutParamFlags,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        windowManager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        mDisplay = windowManager.getDefaultDisplay();
        inflater = LayoutInflater.from(this);
        layoutView = inflater.inflate(R.layout.youtubeservicelayout,null);

        /*String frameVideo = "<html><head> \n" +
                "<style type=\"text/css\">\n" +
                "body{\n" +
                "    width:auto;\n" +
                "    height:auto;\n" +
                "    background-color:#000000;\n" +
                "    opacity:1;\n" +
                "    filter:alpha(opacity=60);\n" +
                "\tmargin:0px;\n" +
                "}\n" +
                "</style>\n" +
                "\n" +
                "</head>\n" +
                "<body style=\"background-color:black\"><iframe width=\"250\" height=\"250\" src=\""+Adres+"\" frameborder=\"0\"></iframe></body></html>";*/

        btnGizle = layoutView.findViewById(R.id.button2);
        buttonHareket = layoutView.findViewById(R.id.button);
        displayYoutubeVideo = layoutView.findViewById(R.id.Web);
        OverlayDisplayMain = layoutView.findViewById(R.id.OverlayConst);
        displayYoutubeVideo.setWebViewClient(new Browser());
        displayYoutubeVideo.setWebChromeClient(new MyWebClient());
        btnActivity = layoutView.findViewById(R.id.button3);
        WebSettings webSettings = displayYoutubeVideo.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //displayYoutubeVideo.loadData(frameVideo, "text/html", "utf-8");
        displayYoutubeVideo.loadUrl(Adres);

        View.OnTouchListener touchListener = new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            private long downTime;
            DisplayMetrics metrics = calculateDisplayMetrics();
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downTime = SystemClock.elapsedRealtime();
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_MOVE:
                        if(params.y<0){
                            params.y = 0;
                        }
                        else if(params.x<0){
                            params.x = 0;
                        }
                        else if(metrics.heightPixels<params.y) {
                                params.y = metrics.heightPixels - 100;
                        }else if(metrics.widthPixels<params.x){
                                params.x = metrics.widthPixels - 100;
                            }else{
                            params.x = initialX + (int) (event.getRawX() - initialTouchX);
                            params.y = initialY + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(layoutView, params);
                        }
                }
                return false;
            }
        };

        buttonHareket.setOnTouchListener(touchListener);
        btnActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(1);
                Intent mainIntent = new Intent(YoutubeOverlay.this,MainActivity.class);
                mainIntent.putExtra("webURL",displayYoutubeVideo.getUrl());
                stopService(ServisNotification.YoutubeService);
                startActivity(mainIntent);
            }
        });
        btnGizle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OverlayDisplayMain.getLayoutParams().height = 0;
                OverlayDisplayMain.getLayoutParams().width = 0;
                displayYoutubeVideo.getLayoutParams().height = 0;
                displayYoutubeVideo.getLayoutParams().width = 0;
                windowManager.updateViewLayout(layoutView,params);
            }
        });
        windowManager.addView(layoutView,params);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        windowManager.removeViewImmediate(layoutView);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent ıntent) { return null; }
    public void EkranKucult(){

        if(OverlayDisplayMain.getLayoutParams().height==0){

            OverlayDisplayMain.getLayoutParams().height=460;
            OverlayDisplayMain.getLayoutParams().width=400;
            displayYoutubeVideo.getLayoutParams().height=400;
            displayYoutubeVideo.getLayoutParams().width=400;
        }else if(OverlayDisplayMain.getLayoutParams().height>400&&OverlayDisplayMain.getLayoutParams().width>340) {
            OverlayDisplayMain.getLayoutParams().height = OverlayDisplayMain.getLayoutParams().height - 10;
            OverlayDisplayMain.getLayoutParams().width = OverlayDisplayMain.getLayoutParams().width - 10;
            displayYoutubeVideo.getLayoutParams().height = displayYoutubeVideo.getLayoutParams().height - 10;
            displayYoutubeVideo.getLayoutParams().width = displayYoutubeVideo.getLayoutParams().width - 10;
            Log.d("Mesaj", "Kucultme : " + OverlayDisplayMain.getLayoutParams().height + "   " + displayYoutubeVideo.getLayoutParams().height);
        }
        windowManager.updateViewLayout(layoutView, params);
    }
    public void EkranBuyut(){
        if(OverlayDisplayMain.getLayoutParams().height==0){

            OverlayDisplayMain.getLayoutParams().height=460;
            OverlayDisplayMain.getLayoutParams().width=400;
            displayYoutubeVideo.getLayoutParams().height=400;
            displayYoutubeVideo.getLayoutParams().width=400;

        }else if(OverlayDisplayMain.getLayoutParams().height<560&&OverlayDisplayMain.getLayoutParams().width<500) {
            OverlayDisplayMain.getLayoutParams().height = OverlayDisplayMain.getLayoutParams().height + 10;
            OverlayDisplayMain.getLayoutParams().width = OverlayDisplayMain.getLayoutParams().width + 10;
            displayYoutubeVideo.getLayoutParams().height = displayYoutubeVideo.getLayoutParams().height + 10;
            displayYoutubeVideo.getLayoutParams().width = displayYoutubeVideo.getLayoutParams().width + 10;
        }
        windowManager.updateViewLayout(layoutView, params);
    }

    private DisplayMetrics calculateDisplayMetrics() {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        mDisplay.getMetrics(mDisplayMetrics);
        return mDisplayMetrics;
    }
}
