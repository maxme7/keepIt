package com.example.keepit.broadcastReceivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.keepit.enums.NotificationAction
import com.example.keepit.notifications.OngoingMediaNotification
import com.example.keepit.room.entities.DictEntry

class NotificationReceiver : BroadcastReceiver() {

    companion object {

        var list = emptyList<DictEntry>()
        var index = 0
        var showing = false

        fun incrementIndex() {
            if (index < list.size - 1) index++
            else index = 0

            showing = false
        }

//        fun skip(context: Context, notificationManager: NotificationManager) {
//            incrementIndex()
//            sdf(context, notificationManager, list[index].sourceWord + " | " + list[index].ind, "???")
//        }

        fun show(context: Context, notificationManager: NotificationManager) {
            showing = true //has to change before update (and thus calling Notification.create)
            updateNotification(context, notificationManager, getSrc(), getTarget())
        }

        fun next(context: Context, notificationManager: NotificationManager) {
            incrementIndex()
            updateNotification(context, notificationManager, getSrc(), "")
        }

        private fun getSrc(): String {
            return if (list[index].ind != "null")
                list[index].sourceWord + " " + list[index].ind
            else
                list[index].sourceWord
        }

        private fun getTarget(): String {
            return if (list[index].phon != "null")
                list[index].targetWord + " " + list[index].phon
            else
                list[index].targetWord
        }

        //remotely modify companion
        var cancel: () -> Unit = {} //TODO good practice?

        private fun updateNotification(context: Context, notificationManager: NotificationManager, title: String, text: String) {
            val notification = OngoingMediaNotification.create(context, title, text)
            notificationManager.notify(OngoingMediaNotification.NOTIFICATION_ID, notification)
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
                NotificationAction.SKIP -> next(context, notificationManager)  //TODO skip redundant?
                NotificationAction.NEXT -> next(context, notificationManager)
                NotificationAction.SHOW -> show(context, notificationManager)
                NotificationAction.CANCEL -> cancel()
            }
        }
    }
}


// broadcast receiver and notifications: https://www.youtube.com/watch?v=CZ575BuLBo4
// notify: https://stackoverflow.com/questions/25821903/change-android-notification-text-dynamically