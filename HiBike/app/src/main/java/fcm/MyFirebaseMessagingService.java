package fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.roundG0929.hibike.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.core.app.NotificationCompat;

//AAAAgdsrYfY:APA91bFPnAbWgVS2NITYanribOeuBkTbB715mTGQzLNjo9W9waNmEjqMYOzzjbwbJilmla-6oA09qnddeIWAUpT_EUte9KJ5vHsBl4tM-jA-OLB29KjoS7vyeaFKL6c0MGfk7wRb7ksQ

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage != null && remoteMessage.getData().size() > 0) {
            sendNotification(remoteMessage);
        }
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        System.out.println(FirebaseInstanceId.getInstance().getToken());
        int myInt;
        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");

        String notiGroup = "hibike";
        final String CHANNEL_ID = "ChannerID";
        NotificationManager mManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final String CHANNEL_NAME = "ChannerName";
            final String CHANNEL_DESCRIPTION = "ChannerDescription";
            final int importance = NotificationManager.IMPORTANCE_HIGH;

            // add in API level 26
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            mChannel.setDescription(CHANNEL_DESCRIPTION);

            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 200});
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            mManager.createNotificationChannel(mChannel);
        }
        //노티 설정
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.drawable.noti);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setGroup(notiGroup);

        //노티 그룹화 설정
        NotificationCompat.Builder summary = new NotificationCompat.Builder(this, CHANNEL_ID);
        summary.setSmallIcon(R.drawable.noti);
        summary.setContentTitle(title);
        summary.setGroup(notiGroup);
        summary.setGroupSummary(true);
        summary.setAutoCancel(true);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setContentTitle(title);
            builder.setVibrate(new long[]{500, 500});
        }
        //알림 갯수를 내부저장소에 파일을 만들어 저장함.
        SharedPreferences pref = getSharedPreferences("hibike_push", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        myInt = pref.getInt("myInt",0);
        mManager.notify(myInt, builder.build());
        mManager.notify(0, summary.build());
        myInt += 1;
        editor.putInt("myInt", myInt);
        editor.commit();
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }
}
