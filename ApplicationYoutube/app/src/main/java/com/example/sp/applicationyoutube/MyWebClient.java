package com.example.sp.applicationyoutube;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;

public class MyWebClient extends WebChromeClient{
        static MainActivity main;
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;
        public Bitmap getDefaultVideoPoster()
        {
            if (main == null) {
                return null;
            }
            return BitmapFactory.decodeResource(main.getApplicationContext().getResources(), 2130837573);
        }
        public void onHideCustomView()
        {
            ((FrameLayout)main.getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            main.getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            main.setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
            Log.d("Mesaj","Ekrani Kuculttum");
        }
        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
        {
            if (this.mCustomView != null)
            {
                onHideCustomView();
                return;

            }

            Log.d("Mesaj","Ekrani Kapladim");
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = main.getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = main.getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)main.getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            main.getWindow().getDecorView().setSystemUiVisibility(3846);
        }
    }
