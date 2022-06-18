package com.example.keepit

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.room.Room
import com.example.keepit.broadcastReceiver.NotificationReceiver
import com.example.keepit.enums.Language
import com.example.keepit.enums.NotificationAction
import com.example.keepit.room.AppDatabase
import com.example.keepit.room.DictEntry
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.runBlocking
import java.util.*

var index = 0

class MainActivity : AppCompatActivity() {
//    private lateinit var button: ImageButton
//    private lateinit var editText: EditText

    private lateinit var navController: NavController
    private lateinit var drawerLayoutParent: DrawerLayout
    private lateinit var appBarConfig: AppBarConfiguration

    private lateinit var destinationChangeListener: NavController.OnDestinationChangedListener
    private lateinit var drawer: DrawerLayout

    private val bookmarks: MutableList<String> = mutableListOf()

    private var notificationManager: NotificationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById<DrawerLayout>(R.id.drawer_layout)

        //TODO hide toolbar on scroll down webview??
//        supportActionBar?.hide()
//        supportActionBar?.subtitle = "subtitle"

//        This makes back arrow open drawer instead of going back to home:
//        var toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
//        drawer.addDrawerListener(toggle)
//        toggle.syncState()


        setupDrawer()

        //TODO tab switcher main view

        //test db:
        runBlocking {
            databaseAccess()
        }


        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel("flashcard_notifications", "Flashcard Notifications", "Notification Channel for showing current Flash Card")

        createNotification(TextView(this))


        //https://stackoverflow.com/questions/33698122/android-change-actionbar-title-text-color
//        val s = SpannableString(title)
//        s.setSpan(
//            ForegroundColorSpan(resources.getColor(R.color.dark_red)),
//            0,
//            title.length,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        supportActionBar?.title = s

        NotificationReceiver.index = 100 //remotely modify companion TODO
    }

    fun createNotificationChannel(id: String, name: String, description: String) {
        if (notificationManager != null) {
            NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW).apply {
                this.description = description
                enableLights(true)
                lightColor = Color.RED
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                notificationManager!!.createNotificationChannel(this)
            }
        }
    }

    val notificationID = 42


    fun createNotification(v: View) {
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val broadcastIntent = Intent(this, NotificationReceiver::class.java)
        broadcastIntent.putExtra("action", NotificationAction.SKIP.toString())
        val pendingActionIntent: PendingIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        Notification.Builder(this, "flashcard_notifications")
            .setContentTitle("flashcard")
            .setContentText("word")
            .setSmallIcon(R.drawable.ic_baseline_filter_alt_24)
            .setColor(Color.BLUE)
            .setContentIntent(pendingIntent)

            .setStyle(Notification.MediaStyle().setShowActionsInCompactView(0,1,2)) //media style // indices of actions (max 3) when compact
            .setVisibility(Notification.VISIBILITY_PUBLIC) //content shown on lockscreen
            .setOngoing(true) //can't be swiped away

            .addAction(R.drawable.ic_baseline_arrow_forward_24, "SKIP", pendingActionIntent)
            .addAction(R.drawable.ic_baseline_arrow_forward_24, "SKIP", pendingActionIntent)
            .addAction(R.drawable.ic_baseline_arrow_forward_24, "SKIP", pendingActionIntent)
            .addAction(R.drawable.ic_baseline_arrow_forward_24, "Close", pendingActionIntent) //cancel notification

            .build().apply { notificationManager.notify(notificationID, this) }

        // open fragment with notifiaction tap: https://stackoverflow.com/questions/26608627/how-to-open-fragment-page-when-pressed-a-notification-in-android#26608894
    }

    //TODO hamburger menu animation too fast
    //TODO how to set fragment transition animations? (since menu is taking care of transitions)

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer((GravityCompat.START))
        } else {
            super.onBackPressed()
        }
    }

    private fun setupDrawer() {
        //TODO https://www.youtube.com/watch?v=zYVEMCiDcmY  make drawer nicer

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        drawerLayoutParent = findViewById(R.id.drawer_layout)
        NavigationUI.setupWithNavController(findViewById<NavigationView>(R.id.navigationView), navController)

        appBarConfig = AppBarConfiguration(navController.graph, drawerLayoutParent)
        setupActionBarWithNavController(navController, appBarConfig)

        destinationChangeListener = NavController.OnDestinationChangedListener { controller, destination, arguments ->
            //To be implemented
        }
    }

    private suspend fun databaseAccess() {
        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "dictentries").build()
        val dictEntryDao = db.dictEntryDao()

        dictEntryDao.deleteAll()
        dictEntryDao.insertAll(
            DictEntry("some.url", Date(), Language.AR, Language.DE, "Adam", "1st Street", null, null, null, arrayOf()),
        )

        val dictEntries: List<DictEntry> = dictEntryDao.getAll()
        for (e in dictEntries) {
            Log.i("Room",
                "${e.url} ${e.date} ${e.sourceLang} ${e.targetLang} ${e.sourceWord} ${e.targetWord} ${e.gram} ${e.phon} ${e.ind} ${
                    arrayToString(e.categories)
                }")
        }
    }

    fun arrayToString(array: Array<String>): String {
        var string = "["
        array.forEach { string += "$it," }
        return string.substringBeforeLast(',') + "]"
    }

    override fun onPause() {
        super.onPause()
        navController.removeOnDestinationChangedListener(destinationChangeListener)
    }

    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(destinationChangeListener)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.web_menu, menu)
        return true
    }

    //https://developer.android.com/training/appbar/actions
    //TODO back and forward navigation (own action bar for webviews?)
//    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
//        R.id.bookmarkButton -> {
////            val s = editText.text.toString()
//            val s = webView.url.toString()
//            if (!bookmarks.contains(s)) {
//                bookmarks.add(s)
//            }
//            true
//        }
//        R.id.backButton -> {
//            webView.goBack()
//            true
//        }
//        R.id.forwardButton -> {
//            webView.goForward()
//            true
//        }
//        R.id.bookarmsButton -> {
//            showDialog()
//            true
//        }
//        else -> {
//            // If we got here, the user's action was not recognized.
//            // Invoke the superclass to handle it.
//            super.onOptionsItemSelected(item)
//        }
//    }

    //https://protocoderspoint.com/android-alert-dialog-box-with-a-list-of-options/
//    private fun showDialog() {
//        val builder = AlertDialog.Builder(this)
//        builder.setTitle("Bookmarks")
//        builder.setItems(bookmarks.toTypedArray()) { dialog, which ->
//            editText.setText(bookmarks[which])
//            button.performClick()
//            dialog.dismiss()
//        }
//
//        builder.create().show()
//    }

}


// broadcast receiver and notifications: https://www.youtube.com/watch?v=CZ575BuLBo4

// save in bundle: https://medium.com/@evanbishop/saving-state-through-the-bundle-when-you-rotate-the-screen-d913422fe4f6