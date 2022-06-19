package com.example.keepit.broadcastReceivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.room.Room
import com.example.keepit.enums.NotificationAction
import com.example.keepit.notifications.OngoingMediaNotification
import com.example.keepit.room.AppDatabase

class NotificationReceiver : BroadcastReceiver() {


    companion object {
        var index = OngoingMediaNotification.NOTIFICATION_ID //just for debugging TODO

        var words = arrayOf("eins", "zwei", "drei")
        var translations = arrayOf("one", "two", "three")
        var word = words[0]

        //TODO implement functions

        fun skip() {
            //remotely modify companion
        }

        fun show() {

        }

        fun next() {

        }

        fun cancel() {

        }
    }

    //TODO proper access of companion object
    //probably put list of word there, fetched from the db in main activity and set here in the companion
    //need of an fragment to manage what is shown -> flashcards management


    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager //manager

        intent?.getStringExtra(OngoingMediaNotification.EXTRA_NAME)?.let {
            val action: NotificationAction = NotificationAction.valueOf(it)
            when (action) {
                NotificationAction.SKIP -> {
                    val db = Room.databaseBuilder(context, AppDatabase::class.java, "dictentries").build() //db
                    Toast.makeText(context, db.toString(), Toast.LENGTH_LONG).show()

                    val notification = OngoingMediaNotification.create(context, "word " + index++, "translation")
                    notificationManager.notify(OngoingMediaNotification.NOTIFICATION_ID, notification)
                }
                NotificationAction.NEXT -> {
                    //TODO
                    Toast.makeText(context, "next", Toast.LENGTH_LONG).show()
                }
                NotificationAction.SHOW -> {
                    Toast.makeText(context, "show", Toast.LENGTH_LONG).show()
                }
                NotificationAction.CANCEL -> {
                    Toast.makeText(context, "cancle", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}


// broadcast receiver and notifications: https://www.youtube.com/watch?v=CZ575BuLBo4
// notify: https://stackoverflow.com/questions/25821903/change-android-notification-text-dynamically