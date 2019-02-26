package com.sauce.inunion;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.media.RingtoneManager.TYPE_NOTIFICATION;

public class FireBaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    OkHttpClient client;
    Request request;

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        pref = getSharedPreferences("first", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("token", token);
        editor.apply();
        Log.d(TAG, "New token: " + pref.getString("token",null));

    }

    public void sendRegistrationToServer(Context context, String department) {
        pref = context.getSharedPreferences("first", context.MODE_PRIVATE);
        String thisToken = pref.getString("token",null);

        client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("department",department)
                .add("token",thisToken)
                .build();

//        List<MultipartBody.Part> body = new ArrayList<>();
//        body.add("token",token);

        request = new Request.Builder()
                .url("http://117.16.231.66:7001/tokenSave/")
                .post(body)
                .build();

        NetworkJob job =  new NetworkJob();
        job.execute();

        Log.d(TAG, "department: " + department + ", token: " + thisToken);
    }
    private class NetworkJob extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String[] params) {
            // do above Server call here
            try {
                client.newCall(request).execute();
            }catch (IOException e){
                e.printStackTrace();
            }
            return "some message";
        }

        @Override
        protected void onPostExecute(String message) {
            //process message
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (true) {
            } else {
                handleNow();
            }
        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//            sendNotification(remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("content"));

        }
    }
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

//    private void sendNotification(String messageBody) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        String channelId = getString(R.string.default_notification_channel_id);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this, channelId)
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setContentTitle("공지사항")
//                        .setContentText(messageBody)
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri)
//                        .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            String channelName = getString(R.string.default_notification_channel_name);
//            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
//            notificationManager.createNotificationChannel(channel);
//        }
//        notificationManager.notify(0, notificationBuilder.build());
//    }
    private void sendNotification(String title, String content) {
        if(title == null)
            title = "공지사항";

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(TYPE_NOTIFICATION);

        // 오레오(8.0) 이상일 경우 채널을 반드시 생성해야 한다.
        final String CHANNEL_ID = "채널ID";
        NotificationManager mManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final String CHANNEL_NAME = "채널 이름";
            final String CHANNEL_DESCRIPTION = "채널 Description";
            final int importance = NotificationManager.IMPORTANCE_HIGH;

            // add in API level 26
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            mChannel.setDescription(CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            mChannel.setSound(defaultSoundUri, null);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            mManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setWhen(System.currentTimeMillis());
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentText(content);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // 아래 설정은 오레오부터 deprecated 되면서 NotificationChannel에서 동일 기능을 하는 메소드를 사용.
            builder.setContentTitle(title);
            builder.setSound(defaultSoundUri);
            builder.setVibrate(new long[]{500, 500});
        }

        mManager.notify(0, builder.build());
    }

}