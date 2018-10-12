package com.example.sp.applicationyoutube;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/*
    Written By Serkan Polat
 */

public class MainActivity extends AppCompatActivity{
    WebView web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyWebClient.main = this;
        super.onCreate(savedInstanceState);
        Intent IntentWebviewServis = getIntent();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        web = findViewById(R.id.mWebview);
        WebSettings WebAyarlar = web.getSettings();
        web.saveState(savedInstanceState);
        web.setSaveEnabled(true);
        WebAyarlar.setDomStorageEnabled(true);
        WebAyarlar.setSaveFormData(true);
        WebAyarlar.setJavaScriptEnabled(true);
        web.setWebViewClient(new Browser());
        web.setWebChromeClient(new MyWebClient());
        if(IntentWebviewServis.getStringExtra("webURL")==null){
            web.loadUrl("https://www.youtube.com");
        }else {
            web.loadUrl(IntentWebviewServis.getStringExtra("webURL"));
        }
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(web.getUrl().contains("m.youtube")){
                YoutubeOverlay.Adres = web.getUrl()/*.replace("watch?v=","embed/").replace("https://m","https://www")*/;
                Log.d("Mesaj",YoutubeOverlay.Adres);
                Intent overlay = new Intent();
                overlay.setClass(MainActivity.super.getBaseContext(), YoutubeOverlay.class);
                startService(overlay);
                ServisNotification.YoutubeService = overlay;
                    Intent Buyut = new Intent("EkranBuyut");
                    Intent Kucult = new Intent("EkranKucult");
                    Intent Kapat = new Intent("ServisSonlandir");
                    PendingIntent pendingIntentBuyut = PendingIntent.getBroadcast(getBaseContext(),1,Buyut,PendingIntent.FLAG_UPDATE_CURRENT);
                    PendingIntent pendingIntentKucult = PendingIntent.getBroadcast(getBaseContext(),2,Kucult,PendingIntent.FLAG_UPDATE_CURRENT);
                    PendingIntent pendingIntentKapat = PendingIntent.getBroadcast(getBaseContext(),0,Kapat,PendingIntent.FLAG_UPDATE_CURRENT);
                    RemoteViews notifiviews = new RemoteViews(getPackageName(),R.layout.notificationlayout);
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext()).setSmallIcon(R.drawable.ic_launcher_background).setAutoCancel(false).setOngoing(true).setContent(notifiviews);
                    notifiviews.setOnClickPendingIntent(R.id.buttonBuyut,pendingIntentBuyut);
                    notifiviews.setOnClickPendingIntent(R.id.buttonKucult,pendingIntentKucult);
                    notifiviews.setOnClickPendingIntent(R.id.buttonKapat,pendingIntentKapat);
                    notificationManager.notify(1,builder.build());
                finish();
                }else{
                    Toast.makeText(MainActivity.this, "Servis Yanlızca Youtube'de Calısmaktadır.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("Mesaj","   |      |     Donduruldu");
    }

    @Override
    public void onBackPressed() {
        web.goBack();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
