package com.example.keepit

import android.Manifest.permission.*
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.example.keepit.broadcastReceivers.NotificationReceiver
import com.example.keepit.enums.Language
import com.example.keepit.notifications.OngoingMediaNotification
import com.example.keepit.room.AppDatabase
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {
//    private lateinit var button: ImageButton
//    private lateinit var editText: EditText

    private lateinit var navController: NavController
    private lateinit var drawerLayoutParent: DrawerLayout
    private lateinit var appBarConfig: AppBarConfiguration

    private lateinit var destinationChangeListener: NavController.OnDestinationChangedListener
    private lateinit var drawer: DrawerLayout

    private val bookmarks: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {

        setAppTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE), 1) //TODO update to current permissions

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



        runBlocking {
            val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "dictentries").build()
            val dictEntryDao = db.dictEntryDao()

            NotificationReceiver.list = dictEntryDao.getEntriesByLang(Language.DE, Language.AR)

        }

        NotificationReceiver.cancel = {
            OngoingMediaNotification.cancelAll(this)
        }

        setupPreferences()


        //https://stackoverflow.com/questions/33698122/android-change-actionbar-title-text-color
//        val s = SpannableString(title)
//        s.setSpan(
//            ForegroundColorSpan(resources.getColor(R.color.dark_red)),
//            0,
//            title.length,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        supportActionBar?.title = s
    }

    fun setAppTheme() {
        val themePreference = PreferenceManager.getDefaultSharedPreferences(this).getString("chooseTheme", "light_theme")
        if ("dark_theme" == themePreference) {
            setTheme(R.style.Theme_KeepIt_Dark)
        } else {
            setTheme(R.style.Theme_KeepIt)
        }
    }

    fun setupPreferences() {
//initial init of variables
        val preferenceManager = PreferenceManager.getDefaultSharedPreferences(this)

        for (preference in preferenceManager.all) {
            Log.i("PREF", preference.key.toString())
            Log.i("PREF", preference.value.toString())
        }
    }


    //TODO hamburger menu animation too fast
    //TODO how to set fragment transition animations? (since menu is taking care of transitions)

    override fun onBackPressed() {
//        can't get fragment reference //returns null
//        val webViewFragment = supportFragmentManager.findFragmentById(R.id.webViewFragment) as WebViewFragment

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer((GravityCompat.START))
//        } else if (webViewFragment.getWebView().canGoBack()) {
            //goBack() in webview
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

//        dictEntryDao.deleteAll() //TODO
//        dictEntryDao.insertAll(
//            DictEntry("some.url", Date(), Language.AR, Language.DE, "Adam", "1st Street", null, null, null, arrayOf()),
//        )
//
//        val dictEntries: List<DictEntry> = dictEntryDao.getAll()
//        for (e in dictEntries) {
//            Log.i("Room",
//                "${e.url} ${e.date} ${e.sourceLang} ${e.targetLang} ${e.sourceWord} ${e.targetWord} ${e.gram} ${e.phon} ${e.ind} ${
//                    arrayToString(e.categories)
//                }")
//        }
    }

    companion object {
        fun arrayToString(array: Array<String>): String { //TODO move
            var string = "["
            array.forEach { string += "$it," }
            return string.substringBeforeLast(',') + "]"
        }
    }

    override fun onPause() {
        super.onPause()
        navController.removeOnDestinationChangedListener(destinationChangeListener)

        // when app is not yet up completely, notifications (in onDestroy) don't get deleted; either pressing home button or task manager too early
        // put notificationSetup() somewhere where it is guaranteed that app will be up completely. (maybe where you select word set
        OngoingMediaNotification.setup(
            this) //TODO if statement; only setup, when setting flashcard notifications is true -> probably make a "start button" triggering notifications
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

    override fun onDestroy() {
//        OngoingMediaNotification.deleteChannel(this) //rather cancel notifications then delete whole channel
        OngoingMediaNotification.cancelAll(this)

        super.onDestroy()
    }


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