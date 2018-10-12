package com.example.sp.applicationyoutube;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ServisNotification extends BroadcastReceiver {
    static YoutubeOverlay youtubeOverlay;
    static Intent YoutubeService;
    @Override
    public void onReceive(Context context, Intent intent) {

        switch (intent.getAction()){
            case "EkranBuyut" :
                youtubeOverlay.EkranBuyut();
                break;
            case "EkranKucult" :
                youtubeOverlay.EkranKucult();
                break;
            case "ServisSonlandir" :
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(1);
                context.stopService(YoutubeService);
                break;
        }
    }
}
