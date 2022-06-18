package com.example.keepit.broadcastReceiver

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.Toast
import androidx.room.Room
import com.example.keepit.MainActivity
import com.example.keepit.R
import com.example.keepit.enums.NotificationAction
import com.example.keepit.room.AppDatabase

class NotificationReceiver : BroadcastReceiver() {

    val notificationID = 42

    companion object {
        var index = 42

        var words = arrayOf("eins", "zwei", "drei")
        var translations = arrayOf("one", "two", "three")
        var word = words[com.example.keepit.index]

        fun Context.skip() {

        }

        fun show() {

        }

        fun next() {

        }

    }
    //TODO proper acces of companion object
    //probably put list of word there, fetched from the db in main activity and set here in the companion
    //need of an fragment to manage what is shown -> flashcards management

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager //manager

        val i = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT)

        val broadcastIntent = Intent(context, NotificationReceiver::class.java)
        broadcastIntent.putExtra("action", NotificationAction.SKIP.toString())
        val pendingActionIntent: PendingIntent = PendingIntent.getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = Notification.Builder(context, "flashcard_notifications")
            .setContentTitle("word" + index++)
            .setContentText("tranlation")
            .setSmallIcon(R.drawable.ic_baseline_filter_alt_24)
            .setColor(Color.BLUE)
            .setSubText("flashcards")
            .setContentIntent(pendingIntent)

            .setStyle(Notification.MediaStyle().setShowActionsInCompactView(0, 1, 2)) //media style // indices of actions (max 3) when compact
            .setVisibility(Notification.VISIBILITY_PUBLIC) //content shown on lockscreen
            .setOngoing(true) //can't be swiped away

            .addAction(R.drawable.ic_baseline_arrow_forward_24, "SKIP", pendingActionIntent)
            .addAction(R.drawable.ic_baseline_arrow_forward_24, "SKIP", pendingActionIntent)
            .addAction(R.drawable.ic_baseline_arrow_forward_24, "SKIP", pendingActionIntent)
            .addAction(R.drawable.ic_baseline_arrow_forward_24, "Close", pendingActionIntent) //cancel notification

            .build()


        intent?.getStringExtra("action")?.let { //TODO make variable for name dont hardcode
            val action: NotificationAction = NotificationAction.valueOf(it)
            when (action) {
                NotificationAction.SKIP -> {
                    val db = Room.databaseBuilder(context, AppDatabase::class.java, "dictentries").build() //db
                    Toast.makeText(context, db.toString(), Toast.LENGTH_LONG).show()
                    notificationManager.notify(42, notification)
//                    context.startActivity()
                }
                NotificationAction.NEXT -> {
                    Toast.makeText(context, "as", Toast.LENGTH_LONG).show()
                }
                NotificationAction.SHOW -> {
                    Toast.makeText(context, "adff", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}


// broadcast receiver and notifications: https://www.youtube.com/watch?v=CZ575BuLBo4
// notify: https://stackoverflow.com/questions/25821903/change-android-notification-text-dynamically