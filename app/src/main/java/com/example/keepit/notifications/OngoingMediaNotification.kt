package com.example.keepit.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.app.NotificationCompat.getColor
import com.example.keepit.MainActivity
import com.example.keepit.R
import com.example.keepit.broadcastReceivers.NotificationReceiver
import com.example.keepit.enums.NotificationAction


class OngoingMediaNotification {

    companion object {
        const val EXTRA_NAME = "action"
        const val NOTIFICATION_ID = 42

        private const val CHANNEL_ID = "flashcard_notifications"
        private const val CHANNEL_NAME = "Flashcard Notifications"
        private const val CHANNEL_DESCRIPTION = "Notification Channel for showing current Flash Card"

        fun setup(context: Context) {
            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                createChannel(context)
            }

            val notification = create(context, "firstTitle", "firstText")
            notificationManager.notify(NOTIFICATION_ID, notification)  //TODO initial notification (set to current flashcard)

//            broadcast intent instead?
//            val intent = Intent(context, NotificationReceiver::class.java)
//            intent.putExtra(EXTRA_NAME, NotificationAction.SHOW.toString())
//            context.sendBroadcast(intent)
        }

        fun createChannel(context: Context) {
            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW).apply {
                description = CHANNEL_DESCRIPTION
                importance = NotificationManager.IMPORTANCE_UNSPECIFIED
                notificationManager.createNotificationChannel(this)
            }
        }

        fun deleteChannel(context: Context) {
            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (notificationManager.getNotificationChannel(CHANNEL_ID) != null) {
                notificationManager.deleteNotificationChannel(CHANNEL_ID)
            }
        }

        fun cancelAll(context: Context) {
            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancelAll()
        }

        fun create(context: Context, title: String, text: String): Notification {

            val intent = Intent(context, MainActivity::class.java) //open main activity on click
            val openActivityPendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

            val showPendingIntent = createBroadcastIntend(context, NotificationAction.SHOW)
            val skipPendingIntent = createBroadcastIntend(context, NotificationAction.SKIP)
            val nextPendingIntent = createBroadcastIntend(context, NotificationAction.NEXT)
            val cancelPendingIntent = createBroadcastIntend(context, NotificationAction.CANCEL)

            //conditionally change action
            var action = Notification.Action(R.drawable.ic_baseline_screen_rotation_alt_32, "SHOW", showPendingIntent)
            if (NotificationReceiver.index % 2 == 1) {
                action = Notification.Action(R.drawable.ic_baseline_replay_5_32, "SKIP", skipPendingIntent)
            }

            return Notification.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setSubText("flash cards") //TODO i18n?

                .setSmallIcon(R.drawable.ic_baseline_filter_alt_24)
                .setColor(context.resources.getColor(R.color.notification_text, context.theme))

                .setContentIntent(openActivityPendingIntent)

                .setStyle(Notification.MediaStyle().setShowActionsInCompactView(0, 1, 2)) //media style // indices of actions (max 3) when compact
                .setVisibility(Notification.VISIBILITY_PUBLIC) //content shown on lockscreen
                .setOngoing(true) //can't be swiped away

                //TODO find good icons
                .addAction(R.drawable.ic_outline_done_32, "NEXT", nextPendingIntent)
                .addAction(action)
                .addAction(R.drawable.ic_baseline_skip_next_32, "SKIP", skipPendingIntent)
                .addAction(R.drawable.ic_cancel, "Cancel", cancelPendingIntent) //cancel notification

                .build()
        }

        private fun createBroadcastIntend(context: Context, action: NotificationAction): PendingIntent {
            val broadcastIntent = Intent(context, NotificationReceiver::class.java)
            broadcastIntent.putExtra(this.EXTRA_NAME, action.toString())
            return PendingIntent.getBroadcast(context, action.ordinal, broadcastIntent,
                PendingIntent.FLAG_UPDATE_CURRENT) //request id has to be unique for each action
        }
    }
}


// open fragment with notifiaction tap: https://stackoverflow.com/questions/26608627/how-to-open-fragment-page-when-pressed-a-notification-in-android#26608894
// requestCode: https://stackoverflow.com/questions/21526319/whats-requestcode-used-for-on-pendingintent